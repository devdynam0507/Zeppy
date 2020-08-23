package kr.ndy.core.blockchain;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockFileCache {

    private Map<String, byte[]> binaryFiles;
    private List<File> fileList;

    public BlockFileCache(File directory)
    {
        this.binaryFiles = new HashMap<>();
        this.fileList = new ArrayList<>();

        if(directory.isDirectory())
        {
            load(directory);
        }
    }

    public BlockFileCache(String directoryPath)
    {
        this(new File(directoryPath));
    }

    public void update(File file)
    {
        fileList.add(file);
        binaryFiles.put(file.getName(), toByte(file));
    }

    public Map<String, byte[]> getBinaryFiles()
    {
        return binaryFiles;
    }

    private void load(File directory)
    {
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".zblk"));
        for(File file : files)
        {
            fileList.add(file);
        }

        this.binaryFiles = fileToBytesArrays();
    }

    private byte[] toByte(File file)
    {
        FileInputStream _fis;
        BufferedInputStream _in;
        byte[] _buf = new byte[(int) file.length()];

        try
        {
            _fis = new FileInputStream(file);
            _in = new BufferedInputStream(_fis);

            _in.read(_buf);
            _fis.close();
            _in.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return _buf;
    }

    private Map<String, byte[]> fileToBytesArrays()
    {
        if(binaryFiles.size() == 0)
        {
            for(File file : fileList)
            {
                binaryFiles.put(file.getName(), toByte(file));
            }
        }

        return binaryFiles;
    }

}
