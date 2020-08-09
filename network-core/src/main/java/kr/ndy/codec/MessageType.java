package kr.ndy.codec;

public class MessageType {

    public static final byte NULL                  = 0xFFFFFFFF;
    public static final byte PING                  = 0x00000000;
    public static final byte REQUEST_FULL_CHAIN    = 0x00000002;
    public static final byte REQUEST_DNS_FULL_NODE = 0x00000003;
    public static final byte OK                    = 0x00000004;
    public static final byte RESPONSE_BLOCK_FILE   = 0x00000005;

}
