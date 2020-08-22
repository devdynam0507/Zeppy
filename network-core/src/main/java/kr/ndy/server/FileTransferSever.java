package kr.ndy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import kr.ndy.codec.MessageType;
import kr.ndy.core.ZeppyModule;
import kr.ndy.core.blockchain.BlockFileCache;
import kr.ndy.core.blockchain.BlockHeader;
import kr.ndy.core.blockchain.observer.IBlockObserver;
import kr.ndy.protocol.ICommProtocol;

import kr.ndy.server.task.FileResponseServerThread;
import kr.ndy.server.task.FileTransferEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FileTransferSever extends SimpleChannelInboundHandler<ByteBuf> implements ICommProtocol,
        IBlockObserver, FileTransferEvent {

    private Logger logger;
    private EventLoopGroup parent, child;
    private int port;
    private BlockFileCache cache;

    private Set<Thread> connections;
    private Map<ChannelId, Channel> channels;

    public FileTransferSever(int port)
    {
        this.parent = new NioEventLoopGroup(ServerOptions.SERVER_SOCK_THREAD);
        this.child = new NioEventLoopGroup(ServerOptions.CHANNEL_SOCK_THREAD);
        this.port = port;
        this.logger = LoggerFactory.getLogger(FileTransferSever.class);
        this.channels = new HashMap<>();
        this.connections = new HashSet<>();
        this.cache = ZeppyModule.getInstance().getFileCache();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        channels.put(channel.id(), channel);
        logger.info("channel registered id: " + ctx.channel().id().asLongText());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {
        if(buf.readableBytes() >= 1)
        {
            int messageType = buf.readByte();
            logger.warn("received  packet transfer server id: " + messageType);

            if(messageType == MessageType.REQUEST_FULL_BLOCKS)
            {
                if(connections.size() < ServerOptions.FILE_TP_SERVER_CONNECTION_LIMIT)
                {
                    new FileResponseServerThread(ctx, cache, this::onTransferFinish).start();
                    logger.info("start response server thread");
                } else
                {
                    ctx.writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeByte(MessageType.CONNECTION_FULL));
                }
            }
        }
    }

    public void broadcast(ByteBuf buf)
    {
        Collection<Channel> clients = channels.values();
        clients.forEach(channel -> channel.writeAndFlush(buf));
    }

    @Override
    public void onTransferFinish(Thread thread)
    {
        connections.remove(thread);
    }

    @Override
    public void onGenerateBlock(BlockHeader header)
    {
        //broadcast miner pool
    }

    @Override
    public void onFinishedPOW(BlockHeader header)
    {
        String blockJson = header.getBlockInfo().toJson();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        buf.writeByte(MessageType.RESPONSE_UPDATE_BLOCK);
        buf.writeBytes(blockJson.getBytes());
        broadcast(buf);
    }

    @Override
    public void enable()
    {
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(MessageServer.class, LogLevel.INFO))
                    .childHandler(this);

            bootstrap.bind(port).sync();
            logger.info("Initialized ftp server bootstrap.. server enabled");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void disable()
    {
        try
        {
            parent.shutdownGracefully().sync();
            child.shutdownGracefully().sync();
        }catch (InterruptedException e)
        {
            logger.warn("Failure close channel: " + e);
        }
    }

}
