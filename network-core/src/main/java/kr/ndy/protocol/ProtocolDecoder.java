package kr.ndy.protocol;

import io.netty.buffer.ByteBuf;

public interface ProtocolDecoder {

    void onReceivePacket(ByteBuf buf);
    void flush();

}
