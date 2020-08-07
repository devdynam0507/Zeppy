package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.Message;

public interface IMessageHandler {
    void handle(ChannelHandlerContext channel, Message message);
}
