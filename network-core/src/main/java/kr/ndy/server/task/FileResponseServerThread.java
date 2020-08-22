package kr.ndy.server.task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.MessageType;
import kr.ndy.core.blockchain.BlockFileCache;
import kr.ndy.protocol.BinaryFileTransferProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private List<byte[]> testCache() throws Exception
    {
        List<byte[]> l = new ArrayList<>();
        File file = new File("C://Zeppy/a.txt");

        FileInputStream _fis;
        BufferedInputStream _in;
        byte[] _buf = new byte[(int) file.length()];

        _fis = new FileInputStream(file);
        _in = new BufferedInputStream(_fis);

        _in.read(_buf);
        l.add(_buf);
        _fis.close();
        _in.close();

        return l;
    }

    @Override
    public void run()
    {
        try
        {
            List<byte[]> binaryFiles = _fileCache.getBinaryFiles();

            for (byte[] binary : binaryFiles)
            {
                BinaryFileTransferProtocol protocol = new BinaryFileTransferProtocol(binary);
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
