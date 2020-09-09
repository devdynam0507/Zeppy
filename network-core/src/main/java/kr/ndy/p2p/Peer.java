package kr.ndy.p2p;

import io.netty.channel.Channel;
import kr.ndy.codec.Message;

public class Peer {

    private String address;
    private boolean isAlive; //피어의 인터넷이 연결되었는지
    private Channel channel;

    private Peer(String address, Channel channel, boolean isAlive)
    {
        this.address       = address;
        this.channel       = channel;
        this.isAlive       = isAlive;
    }

    public static Peer create(String address, Channel channel, boolean isAlive)
    {
        return new Peer(address, channel, isAlive);
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    public boolean sendMessage(Message message)
    {
        return channel.writeAndFlush(message).isSuccess();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return address.equals(((Peer) o).address);
    }

}
