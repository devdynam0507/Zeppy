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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class DNS extends SimpleChannelInboundHandler<String> {

    private EventLoopGroup parent, child;
    private Logger logger;
    private DNSCache cache;

    public DNS(DNSCache cache)
    {
        this.logger = LoggerFactory.getLogger(DNS.class);
        this.parent = new NioEventLoopGroup(4);
        this.child  = new NioEventLoopGroup(1);
        this.cache  = cache;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String query) throws Exception
    {
        logger.info("request query: " + query);

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

    public void enable()
    {
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(DNS.class, LogLevel.INFO))
                    .childHandler(new DNSInitializer(this));

            bootstrap.bind(23556).sync();
            logger.info("Initialized DNS bootstrap.. server enabled");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void disable()
    {

    }
}
