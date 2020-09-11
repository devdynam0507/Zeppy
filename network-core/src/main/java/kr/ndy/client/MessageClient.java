package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageBuilder;
import kr.ndy.codec.MessageType;
import kr.ndy.codec.handler.IMessageHandler;
import kr.ndy.codec.handler.MessageHandlerFactory;
import kr.ndy.codec.handler.Ping;
import kr.ndy.p2p.P2P;
import kr.ndy.p2p.Peer;
import kr.ndy.protocol.ICommProtocolConnection;

import kr.ndy.server.ServerOptions;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 모든 소켓 관련 처리는 이 클래스에서 합니다.
 * 피어간 통신을 담당하는 클래스입니다.
 * */
public class MessageClient extends SimpleChannelInboundHandler<Message> implements ICommProtocolConnection {

    //TODO: add dns seeds member variable
    private int port;
    private EventLoopGroup group;
    private ClientInitializer initializer;
    private Set<Channel> channels;
    private FileTransferClient _ftClient;
    private P2P peers;
    private DNSClient dnsClient;
    private ChannelFuture _server; //Full node channel

    //static members
    private static Logger logger = LoggerFactory.getLogger(MessageClient.class);

    public MessageClient(int port, P2P peers, DNSClient dnsClient)
    {
        this.port        = port;
        this.group       = new NioEventLoopGroup();
        this.initializer = new ClientInitializer(this);
        this.channels    = new HashSet<>();
        this.peers       = peers;
        this.dnsClient   = dnsClient;

        //connectFileTransferSever(); //ftp 서버랑 연결
    }

    /**
     * channel active시 Ping message를 전송
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        IMessageHandler message = MessageHandlerFactory.getMessageHandlerFactory(MessageType.PING); //핑메세지 날림.
        message.handle(ctx, null, null, logger);

        dnsClient.push();
        ctx.writeAndFlush(MessageBuilder.builder().type(MessageType.REQUEST_PEERS));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String hostAddress        = address.getAddress().getHostAddress();

        _server = null;
        peers.removePeer(peers.getPeer(hostAddress));
        dnsClient.inactive(hostAddress);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception
    {
        switch (message.getType())
        {
            case MessageType.OK:
                establish(ctx.channel());
                break;
            case MessageType.RESPONSE_PEERS:
                JSONObject jsonObject = message.getJson();
                int size              = (int) jsonObject.get("size");

                logger.info("Client response peer size: " + size);
                logger.info("If isn't size is an 0, Add new peers to node");
                if(size > 0)
                {
                    Bootstrap bootstrap = getBootstrap();

                    for(int i = 0; i < size; i++)
                    {
                        String hostAddress          = (String) jsonObject.get(i);
                        Channel channel = bootstrap.connect(hostAddress, port).sync().channel();
                        group.register(channel);
                        peers.addPeers(Peer.create(hostAddress, channel, true));
                        logger.info("Connected to " + hostAddress);
                    }
                }
        }
    }

    private void releaseFileTransferServer()
    {
        if(_ftClient != null)
        {
            _ftClient.disable();
            _ftClient = null;
        }
    }

    private void connectFileTransferSever()
    {
        if(_ftClient == null)
        {
            _ftClient = new FileTransferClient(ServerOptions.TEST_FILE_TRANSFER_SERVER_PORT);
            _ftClient.enable();
        }
    }

    public void sendPingToPeers()
    {
        List<Peer> activePeers = peers.getPeers();

        for(Peer peer : activePeers)
        {
            boolean isSuccess = sendMessage(peer, MessageBuilder.builder()
                    .type(MessageType.PING)
                    .json(new Ping())
                    .create());

            if(!isSuccess)
            {
                logger.info(peer.getAddress() + " is died");
            }
        }
    }

    public boolean sendMessage(Peer peer, Message message)
    {
        return peer.sendMessage(message);
    }

    public void refresh()
    {
        if(_server == null)
        {
            enable();
        }
    }

    @Override
    public void establish(Channel channel)
    {
        channels.add(channel);
        logger.info("Success established connection server: {id}".replace("{id}", channel.id().asLongText()));
    }

    public Bootstrap getBootstrap()
    {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .handler(initializer);

        return bootstrap;
    }

    @Override
    public void enable()
    {
        Bootstrap bootstrap = getBootstrap();

        try
        {
            List<String> fullNodeAddress = dnsClient.getFullNodeCaches();
            if(fullNodeAddress.size() > 0)
            {
                _server = bootstrap.connect(fullNodeAddress.get(0), port).sync();
            }
        } catch (InterruptedException e)
        {
            disable();
        }
    }

    @Override
    public void disable()
    {
        releaseFileTransferServer();
        group.shutdownGracefully();
    }
}
