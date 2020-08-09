package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.IJsonSerializable;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageBuilder;
import kr.ndy.codec.MessageType;
import kr.ndy.protocol.ICommProtocolConnection;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

public class PingPongOK implements IJsonSerializable, IMessageHandler {

    @Override
    public String toJson()
    {
        JSONObject object = new JSONObject();
        object.put("pong", "OK");

        return object.toJSONString();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Message message, ICommProtocolConnection connection, Logger logger)
    {
        message = MessageBuilder.builder()
                .type(MessageType.OK)
                .json(this::toJson)
                .create();

        connection.establish(ctx.channel()); //연결확립
        ctx.writeAndFlush(message); //클라이언트에게 OK response
        logger.info("send ping message 'OK'");
    }

}
