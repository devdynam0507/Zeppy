package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.Message;

public class PingPongOK implements IMessageHandler {

    @Override
    public void handle(ChannelHandlerContext channel, Message message)
    {
        //TODO: 연결 확립
    }

}
