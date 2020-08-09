package kr.ndy.codec.handler;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.Message;
import kr.ndy.core.blockchain.BlockFileIO;
import kr.ndy.protocol.ICommProtocolConnection;
import org.slf4j.Logger;

import java.io.File;

public class FullChainLoader implements IMessageHandler {

    private File[] fullBlockFiles;
    private BlockFileIO fileIO;

    public FullChainLoader(BlockFileIO fileIO)
    {
        this.fileIO = fileIO;
        this.fullBlockFiles = fileIO.getFullBlockFiles();
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Message message, ICommProtocolConnection source, Logger logger)
    {

    }

}
