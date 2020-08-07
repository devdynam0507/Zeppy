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
            case MessageType.PONG:
                handler = new Pong();
                break;
            case MessageType.OK:
                handler = new PingPongOK();
                break;
            case MessageType.REQUEST_DNS_FULL_NODE:
            case MessageType.REQUEST_FULL_CHAIN:
                break;
        }

        return handler;
    }

}
