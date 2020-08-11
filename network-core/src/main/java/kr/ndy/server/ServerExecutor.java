package kr.ndy.server;

import jdk.internal.jline.internal.Nullable;
import kr.ndy.protocol.ICommProtocol;

public class ServerExecutor {

    private ICommProtocol[] protocols;

    public ServerExecutor(ICommProtocol... protocol)
    {
        this.protocols = protocol;
    }

    @Nullable
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
