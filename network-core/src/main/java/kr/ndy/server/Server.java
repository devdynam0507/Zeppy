package kr.ndy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private Logger logger;
    private EventLoopGroup parent, child;
    private int port;

    public Server(int port)
    {
        this.logger = LoggerFactory.getLogger(Server.class);
        this.parent = new NioEventLoopGroup(ServerSchema.SERVER_SOCK_THREAD);
        this.child = new NioEventLoopGroup(ServerSchema.CHANNEL_SOCK_THREAD);
        this.port = port;
    }

    public void enable()
    {
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(Server.class, LogLevel.INFO))
                    //TODO: fix  to  handler  param
                    .childHandler(new ServerInitializer(null));

            bootstrap.bind(port).sync();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

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

}
