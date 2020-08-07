package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.Message;
import kr.ndy.protocol.ICommProtocolConnection;

public class PingPongOK implements IMessageHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Message message, ICommProtocolConnection connection)
    {
        connection.establish(ctx.channel()); //연결확립
        ctx.writeAndFlush(message); //클라이언트에게 OK response
    }

}
