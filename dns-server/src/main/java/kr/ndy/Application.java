package kr.ndy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
public class Application {

    public static ChannelFuture future;

    public static void main(String[] args)
    {
        DNSCache cache = new DNSCache();
        DNS dns = new DNS(cache);
        dns.enable();
    }

    public static void testEnable()
    {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(getHandler());
                    }
                });

        try
        {
            future = bootstrap.connect("localhost", 23556).sync();

            while(true)
            {
                future.channel().writeAndFlush(DNSQuery.FULL_NODE_ADDR_GET);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e)
        {
        }
    }

    public static SimpleChannelInboundHandler<String> getHandler()
    {
        return new SimpleChannelInboundHandler<String>()
        {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception
            {
                System.out.println("dns client activated");
            }

            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception
            {
                System.out.println("client:"+ s);
            }
        };
    }

}
