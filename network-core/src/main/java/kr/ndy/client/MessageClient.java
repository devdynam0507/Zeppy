package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageType;
import kr.ndy.codec.handler.IMessageHandler;
import kr.ndy.codec.handler.MessageHandlerFactory;
import kr.ndy.protocol.ICommProtocolConnection;

import kr.ndy.server.ServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class MessageClient extends SimpleChannelInboundHandler<Message> implements ICommProtocolConnection {

    //TODO: add dns seeds member variable
    private int port;
    private EventLoopGroup group;
    private ClientInitializer initializer;
    private Set<Channel> channels;
    private Logger logger;
    private FileTransferClient _ftClient;

    public MessageClient(/* Todo: param  dns  seeds */int port)
    {
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.initializer = new ClientInitializer(this);
        this.channels = new HashSet<>();
        this.logger = LoggerFactory.getLogger(MessageClient.class);

        connectFileTransferSever(); //ftp 서버랑 연결
    }

    /**
     * channel active시 Ping message를 전송
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        IMessageHandler message = MessageHandlerFactory.getMessageHandlerFactory(MessageType.PING);
        message.handle(ctx, null, null, logger);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception
    {
        switch (message.getType())
        {
            case MessageType.OK:
                establish(ctx.channel());
                break;

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

    @Override
    public void establish(Channel channel)
    {
        channels.add(channel);
        logger.info("Success established connection server: {id}".replace("{id}", channel.id().asLongText()));
    }

    @Override
    public void enable()
    {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .handler(initializer);

        try
        {
            bootstrap.connect("localhost", port).sync();
        } catch (InterruptedException e)
        {
            disable();
        }
    }

    @Override
    public void disable()
    {
        group.shutdownGracefully();
    }
}
