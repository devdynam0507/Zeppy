package kr.ndy.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import kr.ndy.protocol.ICommProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class FileTransferSever extends SimpleChannelInboundHandler<ByteBuf> implements ICommProtocol {

    private Logger logger;
    private EventLoopGroup parent, child;
    private int port;

    private Set<Channel> channels;

    public FileTransferSever(int port)
    {
        this.parent = new NioEventLoopGroup(ServerOptions.SERVER_SOCK_THREAD);
        this.child = new NioEventLoopGroup(ServerOptions.CHANNEL_SOCK_THREAD);
        this.port = port;
        this.logger = LoggerFactory.getLogger(FileTransferSever.class);
        this.channels = new HashSet<>();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {

    }

    @Override
    public void enable()
    {

    }

    @Override
    public void disable()
    {

    }

}
