package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import kr.ndy.codec.MessageType;
import kr.ndy.protocol.ICommProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileTransferClient extends SimpleChannelInboundHandler<ByteBuf> implements ICommProtocol {

    private int port;
    private EventLoopGroup group;
    private Logger logger;
    private Channel _server;
    private static final String CLIENT_BLOCK_FILE_PATH = "C://zeppy_client"; //test path

    /**
     * 1. 메세지 서버랑 연결시 파일 전송 서버랑 같이 연결
     *
     * 2. 지속적인 update 패킷을 주고받아 블록 생성 시 transfer  file
     *
     * */

    public FileTransferClient(int port)
    {
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.logger = LoggerFactory.getLogger(FileTransferClient.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        // directory가 없으면 전체 블록 파일을 서버에 요청
        if(createClientBlockFileDirectory())
        {
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
            buf.writeByte(MessageType.RESPONSE_FULL_BLOCKS);

            _server.writeAndFlush(buf);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {

    }

    public boolean createClientBlockFileDirectory()
    {
        return new File(CLIENT_BLOCK_FILE_PATH).mkdir();
    }

    @Override
    public void enable()
    {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .handler(this);

        try
        {
            _server = bootstrap.connect("localhost", port).sync().channel();
            logger.info("connected file transfer server");
        } catch (InterruptedException e)
        {
            logger.warn("occured interrupt exception");
            disable();
        }
    }

    @Override
    public void disable()
    {
        try
        {
            group.shutdownGracefully().sync();
            logger.info("disabled transfer client");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
