package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import kr.ndy.DNSInitializer;
import kr.ndy.DNSQuery;
import kr.ndy.client.callback.DNSEventHandlerRegister;
import kr.ndy.protocol.ICommProtocol;
import kr.ndy.p2p.P2P;
import kr.ndy.server.ServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class DNSClient extends SimpleChannelInboundHandler<String> implements ICommProtocol {

    private Channel _server;
    private static final String NULL = "NULL";
    private Logger logger;
    private P2P peers;
    private List<String> fullNodeCaches;
    private DNSEventHandlerRegister handlerRegister;

    public DNSClient(P2P peers, DNSEventHandlerRegister register)
    {
        this.logger          = LoggerFactory.getLogger(DNSClient.class);
        this.peers           = peers;
        this.fullNodeCaches  = new ArrayList<>();
        this.handlerRegister = register;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        push();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String query) throws Exception
    {
        String content = null;
        if(isGetQuery(query))
        {
            String[] parsed = parseQuery(query);

            if(parsed.length == 2)
            {
                content = parsed[0];
                query = parsed[1];
            }
        }

        switch (query)
        {
            case DNSQuery.FULL_NODE_ADDR_PUSH_SUCCESS:
                logger.info("pushed full node addr");
                break;
            case DNSQuery.FULL_NODE_ADDR_GET_SUCCESS:
                if(!fullNodeCaches.contains(content))
                {
                    fullNodeCaches.add(content);
                    handlerRegister.callOnGetNodeAddress(content);
                    logger.info("add full node address: " + content);
                }

                break;
            case DNSQuery.FULL_NODE_ADDR_GET_FAILED:
                logger.info("failed get full node addr");
        }
    }

    public void push()
    {
        _server.writeAndFlush(DNSQuery.FULL_NODE_ADDR_PUSH);
    }

    public void requestAddress() throws InterruptedException
    {
        _server.writeAndFlush(DNSQuery.FULL_NODE_ADDR_GET).sync();
    }

    public void inactive(String hostAddress)
    {
        fullNodeCaches.remove(hostAddress);
    }

    public List<String> getFullNodeCaches() { return fullNodeCaches; }

    @Override
    public void enable()
    {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new DNSInitializer(this));

        try
        {
            //TODO: DNS서버 주소로 변경
            _server = bootstrap.connect("158.247.203.223", ServerOptions.DNS_SERVER_PORT).sync().channel();
        } catch (InterruptedException e)
        {
        }
    }

    private String[] parseQuery(String query)
    {
        return query.split("&");
    }

    private boolean isGetQuery(String query)
    {
        return query.contains(DNSQuery.FULL_NODE_ADDR_GET_SUCCESS) || query.contains(DNSQuery.FULL_NODE_ADDR_GET_FAILED);
    }

    private boolean isNull(String content)
    {
        return content.equals(NULL);
    }

    @Override
    public void disable()
    {

    }
}
