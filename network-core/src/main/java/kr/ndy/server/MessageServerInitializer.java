package kr.ndy.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageDecoder;
import kr.ndy.codec.MessageEncoder;

public class MessageServerInitializer extends ChannelInitializer<SocketChannel> {

    private SimpleChannelInboundHandler<Message> handler;

    public MessageServerInitializer(SimpleChannelInboundHandler<Message> handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("handler", handler);
    }

}
