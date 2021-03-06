package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.IJsonSerializable;
import kr.ndy.codec.Message;
import kr.ndy.codec.MessageBuilder;
import kr.ndy.codec.MessageType;
import kr.ndy.p2p.P2P;
import kr.ndy.protocol.ICommProtocolConnection;
import kr.ndy.server.MessageServer;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

public class RequestPeers implements IMessageHandler, IJsonSerializable {

    @Override
    public String toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("peers", "OK");

        return jsonObject.toJSONString();
    }

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
