package kr.ndy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kr.ndy.client.DNSClient;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageType;
import kr.ndy.codec.handler.IMessageHandler;
import kr.ndy.codec.handler.MessageHandlerFactory;
import kr.ndy.p2p.P2P;
import kr.ndy.p2p.Peer;
import kr.ndy.protocol.ICommProtocolConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class MessageServer extends SimpleChannelInboundHandler<Message> implements ICommProtocolConnection {

    private Logger logger;
    private EventLoopGroup parent, child;
    private int port;
    private P2P peers;
    private DNSClient dnsClient;

    private Set<Channel> channels;

    public MessageServer(int port, P2P peers, DNSClient dnsClient)
    {
        this.logger    = LoggerFactory.getLogger(MessageServer.class);
        this.parent    = new NioEventLoopGroup(ServerOptions.SERVER_SOCK_THREAD);
        this.child     = new NioEventLoopGroup(ServerOptions.CHANNEL_SOCK_THREAD);
        this.port      = port;
        this.channels  = new HashSet<>();
        this.peers     = peers;
        this.dnsClient = dnsClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception
    {
        IMessageHandler handler = null;

        //TODO: Full chain download 요청 시 FileTransfer server로 리시브
        switch (message.getType())
        {
            case MessageType.PING:
                handler = MessageHandlerFactory.getMessageHandlerFactory(MessageType.OK);
                break;
            case MessageType.REQUEST_PEERS:
                handler = MessageHandlerFactory.getMessageHandlerFactory(MessageType.REQUEST_PEERS);
                break;
        }

        handler.handle(ctx, message, this, logger);
    }

    public P2P getPeers() { return peers; }

    @Override
    public void enable()
    {
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(MessageServer.class, LogLevel.INFO))
                    .childHandler(new MessageServerInitializer(this));

            bootstrap.bind(port).sync();
            logger.info("Initialized message server bootstrap.. server enabled");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void disable()
    {
        try
        {
            parent.shutdownGracefully().sync();
            child.shutdownGracefully().sync();
        }catch (InterruptedException e)
        {
            logger.warn("Failure close channel: " + e);
        }
    }

    @Override
    public void establish(Channel channel)
    {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        peers.addPeers(Peer.create(hostAddress,true)); //피어 연결 확립.

        logger.info("establish connection client: {ip}".replace("{ip}", hostAddress));
    }
}
