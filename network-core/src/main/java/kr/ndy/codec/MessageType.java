package kr.ndy.codec;

public class MessageType {

    public static final byte NULL                       = 0xFFFFFFFF;
    public static final byte PING                       = 0x00000000;
    public static final byte REQUEST_DNS_FULL_NODE      = 0x00000001;
    public static final byte OK                         = 0x00000002;
    public static final byte RESPONSE_UPDATE_BLOCK      = 0x00000003;
    public static final byte RESPONSE_FULL_BLOCKS       = 0x00000004;
    public static final byte REQUEST_UPDATE_BLOCK       = 0x00000005;
    public static final byte REQUEST_FULL_BLOCKS        = 0x00000006;
    public static final byte CONNECTION_FULL            = 0x00000007;

}
