package kr.ndy.codec;

public enum MessageType {

    PING((byte) 0x00000000),
    PONG((byte) 0x00000001),
    REQUEST_FULL_CHAIN((byte) 0x00000002),
    REQUEST_DNS_FULL_NODE((byte) 0x00000003);

    private byte packet;

    MessageType(byte packet)
    {
        this.packet = packet;
    }

}
