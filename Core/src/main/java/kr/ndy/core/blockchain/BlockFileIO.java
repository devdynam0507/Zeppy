package kr.ndy.core.blockchain;

import kr.ndy.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BlockFileIO {

    private static final String BLOCK_FILE_EXTENSION = ".zblk";

    private Logger logger;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    public BlockFileIO()
    {
        this.logger = LoggerFactory.getLogger(BlockFileIO.class);
        this.lock = new ReentrantReadWriteLock(false);
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    private File mkdir()
    {
        File file = new File("C://Zeppy");
        file.mkdir();

        return file;
    }

    public List<BlockHeader> read()
    {
        List<BlockHeader> blocks = new ArrayList<>();
        File[] files = mkdir().listFiles((dir, name) -> name.endsWith(BLOCK_FILE_EXTENSION));

        for(File file : files)
        {
            blocks.add(read(file));
        }
        
        return blocks;
    }

    public BlockHeader read(File file)
    {
        return null;
    }

    public void write(BlockHeader header)
    {
        String fileName = ByteUtil.toHex(header.getBlockHash()) + BLOCK_FILE_EXTENSION;
        String json = header.getBlockInfo().toJson();
        FileOutputStream fos;
        BufferedOutputStream bos;

        try
        {
            fos = new FileOutputStream("C://Zeppy/" + fileName);
            bos = new BufferedOutputStream(fos);

            writeLock.lock();
            bos.write(json.getBytes());
            bos.close();
            fos.close();
        }catch (FileNotFoundException e)
        {
            logger.warn("FileNotFoundException! - " + fileName, e.getCause());
        }catch (IOException e)
        {
            logger.warn("IOException - ", e.getCause());
        }finally
        {
            writeLock.unlock();
        }
    }

}
