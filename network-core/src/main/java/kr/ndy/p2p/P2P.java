package kr.ndy.p2p;

import java.util.HashSet;
import java.util.Set;

public class P2P {

    private Set<Peer> peers;

    public P2P()
    {
        this.peers = new HashSet<>();
    }

    public void addPeers(Peer peer)
    {
        peers.add(peer);
    }

}
