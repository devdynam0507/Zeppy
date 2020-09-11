package kr.ndy;

import io.netty.channel.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@ChannelHandler.Sharable
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
