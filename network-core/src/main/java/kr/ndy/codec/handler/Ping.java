package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import jdk.internal.jshell.tool.MessageHandler;
import kr.ndy.codec.IJsonSerializable;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageType;
import org.json.simple.JSONObject;

public class Ping extends ReadWriteable implements IJsonSerializable, IMessageHandler {

    @Override
    public String toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ping", "OK");

        return jsonObject.toJSONString();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Message message)
    {
        JSONObject json = message.getJson();
        String status = (String) json.get("pong");

        if(status != null && status.equals("OK"))
        {
            ctx.writeAndFlush(MessageHandlerFactory.getMessageHandlerFactory(MessageType.PONG));
        }
    }
}
