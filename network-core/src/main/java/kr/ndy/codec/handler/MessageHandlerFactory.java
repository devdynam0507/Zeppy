package kr.ndy.codec.handler;

import kr.ndy.codec.MessageType;

public class MessageHandlerFactory {

    public static IMessageHandler getMessageHandlerFactory(byte id)
    {
        IMessageHandler handler = null;

        switch (id)
        {
            case MessageType.PING:
                handler = new Ping();
                break;
            case MessageType.OK:
                handler = new PingPongOK();
                break;
            case MessageType.REQUEST_PEERS:
                handler = new RequestPeers();
                break;
        }

        return handler;
    }

}
