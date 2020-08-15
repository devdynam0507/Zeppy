package kr.ndy.server.task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import kr.ndy.core.blockchain.BlockFileCache;

import java.util.List;

public class FileResponseThread extends Thread {

    private ChannelHandlerContext _ctx;
    private FileTransferEvent _callback;
    private BlockFileCache _fileCache;

    public FileResponseThread(ChannelHandlerContext ctx, BlockFileCache cache, FileTransferEvent transferEvent)
    {
        this._ctx = ctx;
        this._fileCache = cache;
        this._callback = transferEvent;
    }

    @Override
    public void run()
    {
        List<byte[]> binaryFiles = _fileCache.getBinaryFiles();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        try
        {
            for (byte[] binary : binaryFiles)
            {
                buf.writeBytes(binary);
                _ctx.writeAndFlush(buf).sync();
                buf.clear();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        _callback.onTransferFinish(this);
    }
}
