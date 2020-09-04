package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import kr.ndy.DNSInitializer;
import kr.ndy.DNSQuery;
import kr.ndy.protocol.ICommProtocol;
import kr.ndy.server.ServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DNSClient extends SimpleChannelInboundHandler<String> implements ICommProtocol {

    private Channel _server;
    private static final String NULL = "NULL";
    private Logger logger;

    public DNSClient()
    {
        this.logger = LoggerFactory.getLogger(DNSClient.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String query) throws Exception
    {
        String content;
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
                break;
            case DNSQuery.FULL_NODE_ADDR_GET_FAILED:
                logger.info("failed get full node addr");
        }
    }

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
            _server = bootstrap.connect("localhost", ServerOptions.DNS_SERVER_PORT).sync().channel();
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
