package kr.ndy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Message {

    private byte type;
    private JSONObject jsonObject;

    protected Message(byte type, String json)
    {
        this.type = type;

        try
        {
            this.jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public ByteBuf toPacket()
    {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        byte[] jsonBytes = jsonObject.toJSONString().getBytes();

        buf.writeByte(jsonBytes.length);

        buf.writeByte(type);
        buf.writeBytes(jsonBytes);

        return buf;
    }

    public static Message fromPacket(ByteBuf buf)
    {
        byte[] jsonBytes = new byte[buf.readByte()];
        byte message = buf.readByte();

        buf.readBytes(jsonBytes);

        return MessageBuilder.builder()
                .type(message)
                .json(new String(jsonBytes))
                .create();
    }

    public JSONObject getJson() { return jsonObject; }

    public byte getType() { return type; }

}
