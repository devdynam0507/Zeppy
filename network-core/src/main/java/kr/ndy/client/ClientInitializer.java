package kr.ndy.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageDecoder;
import kr.ndy.codec.MessageEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private SimpleChannelInboundHandler<Message> _handler;

    public ClientInitializer(SimpleChannelInboundHandler<Message> _handler)
    {
        this._handler = _handler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("handler", _handler);
    }
}
