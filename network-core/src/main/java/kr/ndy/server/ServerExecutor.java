package kr.ndy.server;

import kr.ndy.protocol.ICommProtocol;

public class ServerExecutor {

    private ICommProtocol[] protocols;

    public ServerExecutor(ICommProtocol... protocol)
    {
        this.protocols = protocol;
    }

    public void executeService()
    {
        for(ICommProtocol protocol : protocols)
        {
            protocol.enable();
        }
    }

}
