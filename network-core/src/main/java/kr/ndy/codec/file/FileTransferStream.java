package kr.ndy.codec.file;

import kr.ndy.codec.handler.ReadWriteable;
import kr.ndy.core.blockchain.BlockFileIO;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class FileTransferStream extends ReadWriteable {

    private byte[] buf;
    private String fileName;
    private final int fileSize;
    private int pos;

    public FileTransferStream(String fileName, int fileSize)
    {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.buf = new byte[fileSize];
        this.pos = 0;
    }

    public void write(byte[] _buf, int bufLength)
    {
        System.arraycopy(_buf, 0, buf, pos, bufLength);
        pos += bufLength;
    }

    public int getPos()
    {
        return pos;
    }

    public void flushAndWriteFile() throws IOException
    {
        Lock lock = getWriteLock();
        FileOutputStream fos = new FileOutputStream("C://Zeppy/" + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        lock.lock();

        bos.write(buf);
        fos.close();
        bos.close();

        lock.unlock();
    }

}
