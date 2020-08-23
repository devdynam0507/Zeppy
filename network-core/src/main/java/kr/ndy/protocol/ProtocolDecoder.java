package kr.ndy.protocol;

import io.netty.buffer.ByteBuf;

public interface ProtocolDecoder<T> {

    void onReceivePacket(ByteBuf buf);
    void flush(T flushTarget);

}
