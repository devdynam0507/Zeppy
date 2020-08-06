package kr.ndy.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import kr.ndy.codec.Message;

public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * 1. Client 연결 시 ping-pong을  주고받아  연결할  수  있는  상태인지  대화
     * 2. 연결확립
     * */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception
    {

    }

}
