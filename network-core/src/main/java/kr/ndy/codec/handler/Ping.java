package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.IJsonSerializable;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageBuilder;
import kr.ndy.codec.MessageType;
import kr.ndy.protocol.ICommProtocolConnection;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

public class Ping extends ReadWriteable implements IJsonSerializable, IMessageHandler {

    @Override
    public String toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ping", "OK");

        return jsonObject.toJSONString();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Message message, ICommProtocolConnection connection, Logger logger)
    {
        message = MessageBuilder.builder()
                .json(this::toJson)
                .type(MessageType.PING)
                .create();

        ctx.writeAndFlush(message);
        logger.info("send ping message 'request'");
    }

}
