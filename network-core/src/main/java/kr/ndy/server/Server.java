package kr.ndy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageType;
import kr.ndy.codec.handler.IMessageHandler;
import kr.ndy.codec.handler.MessageHandlerFactory;
import kr.ndy.protocol.ICommProtocolConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class Server extends SimpleChannelInboundHandler<Message> implements ICommProtocolConnection {

    private Logger logger;
    private EventLoopGroup parent, child;
    private int port;

    private Set<Channel> channels;

    public Server(int port)
    {
        this.logger = LoggerFactory.getLogger(Server.class);
        this.parent = new NioEventLoopGroup(ServerSchema.SERVER_SOCK_THREAD);
        this.child = new NioEventLoopGroup(ServerSchema.CHANNEL_SOCK_THREAD);
        this.port = port;
        this.channels = new HashSet<>();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception
    {
        IMessageHandler handler = null;

        switch (message.getType())
        {
            case MessageType.PING:
                handler = MessageHandlerFactory.getMessageHandlerFactory(MessageType.OK);
                break;
        }

        handler.handle(ctx, message, this);
    }

    @Override
    public void enable()
    {
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(Server.class, LogLevel.INFO))
                    .childHandler(new ServerInitializer(this));

            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("Initialized server bootstrap.. server enabled");
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
        channels.add(channel);
        logger.info("Establish connection client: {id}".replace("{id}", channel.id().asLongText()));
    }
}
