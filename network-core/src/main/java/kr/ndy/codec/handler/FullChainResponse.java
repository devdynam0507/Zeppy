package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.Message;
import kr.ndy.protocol.ICommProtocolConnection;
import org.slf4j.Logger;

public class FullChainResponse implements IMessageHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Message message, ICommProtocolConnection source, Logger logger)
    {

    }

}
