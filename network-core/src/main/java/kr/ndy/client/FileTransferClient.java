package kr.ndy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import kr.ndy.client.task.FileResponseClientThread;
import kr.ndy.codec.MessageType;
import kr.ndy.protocol.ICommProtocol;
import kr.ndy.protocol.ProtocolDecoder;
import kr.ndy.server.task.FileTransferEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileTransferClient extends SimpleChannelInboundHandler<ByteBuf> implements ICommProtocol, FileTransferEvent, ProtocolDecoder<String> {

    private int port;
    private EventLoopGroup group;
    private Logger logger;
    private Channel _server;
    private Map<String, FileResponseBuffer> buffers;
    private static final String CLIENT_BLOCK_FILE_PATH = "C://zeppy_client"; //test path

    /**
     * 1. 메세지 서버랑 연결시 파일 전송 서버랑 같이 연결
     *
     * 2. 지속적인 update 패킷을 주고받아 블록 생성 시 transfer  file
     *
     * 3. file별로 Map을 만들어서 Buffer 가  겹치지  않도록  해야함 1024바이트씩  처리하다보니 버퍼가  생성되고 중간에 버퍼에 데이터가  추가됨
     * */

    public FileTransferClient(int port)
    {
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.logger = LoggerFactory.getLogger(FileTransferClient.class);
        this.buffers = new HashMap<>();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        createClientBlockFileDirectory();

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte(MessageType.REQUEST_FULL_BLOCKS);

        ctx.writeAndFlush(buf);
        ctx.fireChannelActive();

        logger.info("Client activate channel, request full blocks");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {
        int messageType = buf.readByte();

        switch (messageType)
        {
            case MessageType.FILE_NAME_PACKET:
                createBuffer(_readFileName(buf));
                break;
            case MessageType.TRANSFER_BIT_PACKET:
                onReceivePacket(buf);
                break;
            case MessageType.EOF:
                String fileName = _readFileName(buf);
                Thread thread = new FileResponseClientThread(this::onTransferFinish, fileName, buffers.get(fileName));
                thread.start();
                logger.info("Received EOF packet, start write files");

        }
    }

    private String _readFileName(ByteBuf buf)
    {
        byte[] fileNameBytes = new byte[buf.readByte()];
        buf.readBytes(fileNameBytes);

        return new String(fileNameBytes);
    }

    public synchronized void createBuffer(String fileName)
    {
        FileResponseBuffer buffer = new FileResponseBuffer();

        buffers.put(fileName, buffer);
    }

    public boolean createClientBlockFileDirectory()
    {
        return new File(CLIENT_BLOCK_FILE_PATH).mkdir();
    }

    @Override
    public void flush(String flushTarget)
    {
        FileResponseBuffer buffer = buffers.get(flushTarget);
        buffer.flush();

        buffers.remove(flushTarget);
    }

    @Override
    public void onTransferFinish(Thread thread)
    {
        FileResponseClientThread clientThread = (FileResponseClientThread) thread;

        flush(clientThread.getWritingFileName());
        logger.info("Call transfer finish client");
    }

    @Override
    public void onReceivePacket(ByteBuf buf)
    {
        byte data = buf.readByte();
        String fileName = _readFileName(buf);
        FileResponseBuffer buffer = buffers.get(fileName);

        buffer.addBuffer(data);
    }

    @Override
    public void enable()
    {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .option(ChannelOption.SO_KEEPALIVE, true)
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
