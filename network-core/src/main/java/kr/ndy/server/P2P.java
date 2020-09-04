package kr.ndy.server;

import java.nio.channels.Channel;
import java.util.HashSet;
import java.util.Set;

public class P2P {

    private Set<Channel> peers;

    public P2P()
    {
        this.peers = new HashSet<>();
    }

}
