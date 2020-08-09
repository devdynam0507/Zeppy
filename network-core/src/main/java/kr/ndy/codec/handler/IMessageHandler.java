package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.Message;
import kr.ndy.protocol.ICommProtocolConnection;
import org.slf4j.Logger;

public interface IMessageHandler {

    /**
     * @param ctx Channel context
     * @param message Packet message
     * @param source protocol instance
     * */
    void handle(ChannelHandlerContext ctx, Message message, ICommProtocolConnection source, Logger logger);

}
