package kr.ndy.core.blockchain;

import kr.ndy.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BlockFileIO {

    protected static final String BLOCK_FILE_EXTENSION = ".zblk";
    public static final String BLOCK_FILE_DIRECTORY = System.getProperty("user.dir") + "/blocks/";

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
        File file = new File(BLOCK_FILE_DIRECTORY);
        file.mkdir();

        return file;
    }

    public File[] getFullBlockFiles()
    {
        return mkdir().listFiles((dir, name) -> name.endsWith(BLOCK_FILE_EXTENSION));
    }

    public Vector<BlockHeader> read()
    {
        Vector<BlockHeader> blocks = new Vector<>();
        File[] files = getFullBlockFiles();

        if(files != null && files.length > 0)
        {
            for(File file : files)
            {
                BlockHeader header = read(file);

                if(header != null)
                {
                    blocks.add(header);
                }
            }

            logger.info("Loads " + blocks.size() + " blocks from disk and loads them");
        }else
        {
            logger.info("Block file's null");
        }

        return blocks;
    }

    public BlockHeader read(File file)
    {
        FileInputStream fis;
        BufferedInputStream bis;
        BlockHeader header = null;

        try
        {
           fis = new FileInputStream(file);
           bis = new BufferedInputStream(fis);
           StringBuilder builder = new StringBuilder();
           int buf;

           while((buf = bis.read()) != -1)
           {
                builder.append((char) buf);
           }

           header = BlockInfo.fromJson(builder.toString());
           logger.info("Successfully parsed block json");
        } catch (FileNotFoundException e)
        {
            logger.warn("Not founded file!", e);
        } catch (IOException e)
        {
            logger.warn("IOException", e);
        }

        return header;
    }

    public void write(BlockHeader header)
    {
        String fileName = ByteUtil.toHex(header.getBlockHash()) + BLOCK_FILE_EXTENSION;
        String json = header.getBlockInfo().toJson();
        FileOutputStream fos;
        BufferedOutputStream bos;

        try
        {
            fos = new FileOutputStream(BLOCK_FILE_DIRECTORY + fileName);
            bos = new BufferedOutputStream(fos);

            writeLock.lock();
            bos.write(json.getBytes());
            bos.close();
            fos.close();

            logger.info("successfully write file " + fileName);
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
