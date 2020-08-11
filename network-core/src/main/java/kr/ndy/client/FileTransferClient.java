package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import kr.ndy.protocol.ICommProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileTransferClient extends SimpleChannelInboundHandler<ByteBuf> implements ICommProtocol {

    private int port;
    private EventLoopGroup group;
    private Logger logger;

    public FileTransferClient(int port)
    {
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.logger = LoggerFactory.getLogger(FileTransferClient.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {
        
    }

    @Override
    public void enable()
    {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .handler(this);

        try
        {
            bootstrap.connect("localhost", port).sync();
            logger.info("connected file transfer server");
        } catch (InterruptedException e)
        {
            logger.warn("occured interrupt exception");
            disable();
        }
    }

    @Override
    public void disable()
    {
        try
        {
            group.shutdownGracefully().sync();
            logger.info("disabled transfer client");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
