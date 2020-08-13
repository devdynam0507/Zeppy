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
import kr.ndy.core.blockchain.BlockHeader;
import kr.ndy.core.blockchain.observer.IBlockObserver;
import kr.ndy.protocol.ICommProtocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FileTransferSever extends SimpleChannelInboundHandler<ByteBuf> implements ICommProtocol, IBlockObserver {

    private Logger logger;
    private EventLoopGroup parent, child;
    private int port;

    private Map<ChannelId, Channel> channels;

    public FileTransferSever(int port)
    {
        this.parent = new NioEventLoopGroup(ServerOptions.SERVER_SOCK_THREAD);
        this.child = new NioEventLoopGroup(ServerOptions.CHANNEL_SOCK_THREAD);
        this.port = port;
        this.logger = LoggerFactory.getLogger(FileTransferSever.class);
        this.channels = new HashMap<>();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception
    {
        Channel channel = ctx.channel();
        channels.put(channel.id(), channel);
        logger.info("channel registered id: " + ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception
    {
        if(buf.readableBytes() >= 4)
        {
            int messageType = buf.readByte();

            if(messageType == MessageType.REQUEST_FULL_BLOCKS)
            {
                //response block files
            }
        }
    }


    public void broadcast(ByteBuf buf)
    {
        Collection<Channel> clients = channels.values();
        clients.forEach(channel -> channel.writeAndFlush(buf));
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
