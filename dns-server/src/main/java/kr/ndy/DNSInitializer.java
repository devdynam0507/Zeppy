package kr.ndy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class DNSInitializer extends ChannelInitializer {

    private SimpleChannelInboundHandler<String> handler;

    public DNSInitializer(SimpleChannelInboundHandler<String> handler)
    {
        this.handler = handler;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("handler", handler);
    }
}
