package kr.ndy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Message {

    private byte type;
    private String json;

    protected Message(byte type, String json)
    {
        this.type = type;
        this.json = json;
    }

    public ByteBuf toPacket()
    {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        byte[] jsonBytes = json.getBytes();

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

    public String getJson() { return json; }
    public byte getType() { return type; }

}
