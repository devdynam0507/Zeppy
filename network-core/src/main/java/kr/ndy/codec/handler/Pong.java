package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.IJsonSerializable;
import kr.ndy.codec.Message;
import org.json.simple.JSONObject;

public class Pong extends ReadWriteable implements IJsonSerializable, IMessageHandler {

    @Override
    public String toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pong", "OK");

        return jsonObject.toJSONString();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Message message)
    {

    }
}
