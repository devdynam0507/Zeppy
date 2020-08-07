package kr.ndy.protocol;

import io.netty.channel.Channel;

public interface ICommProtocolConnection extends ICommProtocol {

    void establish(Channel channel);

}
