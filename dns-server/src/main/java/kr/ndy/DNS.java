package kr.ndy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kr.ndy.protocol.ICommProtocol;
import kr.ndy.server.MessageServer;
import kr.ndy.server.ServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class DNS extends SimpleChannelInboundHandler<String> implements ICommProtocol {

    private EventLoopGroup parent, child;
    private Logger logger;
    private DNSCache cache;

    public DNS(DNSCache cache)
    {
        this.logger = LoggerFactory.getLogger(DNS.class);
        this.parent = new NioEventLoopGroup(ServerOptions.SERVER_SOCK_THREAD);
        this.child = new NioEventLoopGroup(ServerOptions.CHANNEL_SOCK_THREAD);
        this.cache = cache;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String query) throws Exception
    {
        System.out.println(query);

        switch (query)
        {
            case DNSQuery.FULL_NODE_ADDR_GET:
                String address = cache.get(getAddress(ctx));
                String status  = address != null ? DNSQuery.FULL_NODE_ADDR_GET_SUCCESS : DNSQuery.FULL_NODE_ADDR_GET_FAILED;
                address        = address == null ? "NULL" : address;

                ctx.writeAndFlush(address + "&" + status);
                break;
            case DNSQuery.FULL_NODE_ADDR_PUSH:
                cache.push(getAddress(ctx));
                ctx.writeAndFlush(DNSQuery.FULL_NODE_ADDR_PUSH_SUCCESS);
        }
    }

    private String getAddress(ChannelHandlerContext ctx)
    {
        InetSocketAddress sockAddress = (InetSocketAddress) ctx.channel().remoteAddress();

        return sockAddress.getAddress().getHostAddress();
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
                    .handler(new LoggingHandler(MessageServer.class, LogLevel.INFO))
                    .childHandler(new DNSInitializer(this));

            bootstrap.bind(ServerOptions.DNS_SERVER_PORT).sync();
            logger.info("Initialized DNS bootstrap.. server enabled");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void disable()
    {

    }
}
