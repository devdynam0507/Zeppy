package kr.ndy.server;

import kr.ndy.protocol.ICommProtocol;

public class ProtocolExecutor {

    private ICommProtocol[] protocols;

    public ProtocolExecutor(ICommProtocol... protocol)
    {
        this.protocols = protocol;
    }

    public ICommProtocol getProtocol(ICommProtocol comm)
    {
        ICommProtocol protocol = null;

        for(ICommProtocol value : protocols)
        {
            if(value.getClass().isInstance(comm))
            {
                protocol = value;
                break;
            }
        }

        return protocol;
    }

    public void executeService()
    {
        for(ICommProtocol protocol : protocols)
        {
            protocol.enable();
        }
    }

}
