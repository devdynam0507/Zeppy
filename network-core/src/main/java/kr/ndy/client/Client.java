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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class Client extends SimpleChannelInboundHandler<Message> implements ICommProtocolConnection {

    //TODO: add dns seeds member variable
    private int port;
    private EventLoopGroup group;
    private ClientInitializer initializer;
    private Set<Channel> channels;
    private Logger logger;

    public Client(/* Todo: param  dns  seeds */int port)
    {
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.initializer = new ClientInitializer(this);
        this.channels = new HashSet<>();
        this.logger = LoggerFactory.getLogger(Client.class);
    }

    /**
     * channel active시 Ping message를 전송
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        IMessageHandler message = MessageHandlerFactory.getMessageHandlerFactory(MessageType.PING);
        message.handle(ctx, null, null);
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
