package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import kr.ndy.codec.Message;
import kr.ndy.protocol.ICommProtocol;

public class Client extends SimpleChannelInboundHandler<Message> implements ICommProtocol {

    //TODO: add dns seeds member variable
    private int port;
    private EventLoopGroup group;
    private ClientInitializer initializer;

    public Client(/* Todo: param  dns  seeds */int port)
    {
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.initializer = new ClientInitializer(this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception
    {


    }

    @Override
    public void enable()
    {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class)
                .handler(initializer);

        try
        {
            ChannelFuture future = bootstrap.connect("localhost", port).sync();
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
