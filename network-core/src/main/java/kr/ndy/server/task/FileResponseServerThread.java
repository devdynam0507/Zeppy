package kr.ndy.server.task;

import io.netty.channel.ChannelHandlerContext;

import kr.ndy.core.blockchain.BlockFileCache;
import kr.ndy.protocol.BinaryFileTransferProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class FileResponseServerThread extends Thread {

    private ChannelHandlerContext _ctx;
    private FileTransferEvent _callback;
    private BlockFileCache _fileCache;
    private Logger logger = LoggerFactory.getLogger(FileResponseServerThread.class);

    public FileResponseServerThread(ChannelHandlerContext ctx, BlockFileCache cache, FileTransferEvent transferEvent)
    {
        this._ctx = ctx;
        this._fileCache = cache;
        this._callback = transferEvent;
    }

    @Override
    public void run()
    {
        try
        {
            Map<String, byte[]> binaryFiles = _fileCache.getBinaryFiles();
            Set<String> keys = binaryFiles.keySet();

            for (String key : keys)
            {
                BinaryFileTransferProtocol protocol = new BinaryFileTransferProtocol(key, binaryFiles.get(key));
                protocol.handle(_ctx);
                logger.info("eof");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        _callback.onTransferFinish(this);
    }
}
