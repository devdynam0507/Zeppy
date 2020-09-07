package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageBuilder;
import kr.ndy.codec.MessageType;
import kr.ndy.p2p.P2P;
import kr.ndy.protocol.ICommProtocolConnection;
import kr.ndy.server.MessageServer;
import org.slf4j.Logger;

public class RequestPeers implements IMessageHandler {

    @Override
    public void handle(ChannelHandlerContext ctx, Message message, ICommProtocolConnection source, Logger logger)
    {
        P2P peers = ((MessageServer) source).getPeers();
        message = MessageBuilder.builder()
                .type(MessageType.RESPONSE_PEERS)
                .json(peers)
                .create();
        ctx.writeAndFlush(message);

        logger.info("Response peers!");
    }

}
