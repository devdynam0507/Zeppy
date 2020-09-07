package kr.ndy.p2p;

import kr.ndy.codec.IJsonSerializable;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class P2P implements IJsonSerializable {

    private List<Peer> peers;

    public P2P()
    {
        this.peers = new ArrayList<>();
    }

    public void addPeers(Peer peer)
    {
        peers.add(peer);
    }

    //TODO: to  json serialize
    @Override
    public String toJson()
    {
        JSONObject jsonObject = new JSONObject();
        int size = peers.size() >= 5 ? 5 : peers.size();
        int count = 0;

        for(int i = 0; i < size; i++)
        {
            Peer peer = peers.get(i);

            if(peer.isAlive())
            {
                jsonObject.put(i, peer.getAddress());
                ++count;
            }
        }
        jsonObject.put("size", count);

        return jsonObject.toJSONString();
    }
}
