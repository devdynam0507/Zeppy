package kr.ndy.codec;

public class MessageBuilder {

    private byte messageType;
    private String json;

    private MessageBuilder() {}

    public static MessageBuilder builder()
    {
        return new MessageBuilder();
    }

    public MessageBuilder type(byte type)
    {
        this.messageType = type;
        return this;
    }

    public MessageBuilder json(String json)
    {
        this.json = json;
        return this;
    }

    public MessageBuilder json(IJsonSerializable json)
    {
        this.json = json.toJson();
        return this;
    }

    public Message create()
    {
        return new Message(messageType, json);
    }

}
