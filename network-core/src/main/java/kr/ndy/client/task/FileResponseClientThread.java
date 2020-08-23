package kr.ndy.client.task;

import io.netty.channel.ChannelHandlerContext;
import kr.ndy.client.FileResponseBuffer;
import kr.ndy.server.task.FileTransferEvent;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class FileResponseClientThread extends Thread {

    private FileTransferEvent event;
    private FileResponseBuffer buffer;
    private String fileName;

    public FileResponseClientThread(FileTransferEvent event, String fileName, FileResponseBuffer buffer)
    {
        this.event = event;
        this.fileName = fileName;
        this.buffer = buffer;
    }

    public String getWritingFileName()
    {
        return fileName;
    }

    @Override
    public void run()
    {
        Iterator<Byte> buf = buffer.getBuffer().iterator();
        FileOutputStream fos;
        BufferedOutputStream bos;

        try
        {
            fos = new FileOutputStream("C://zeppy_client/{file_name}".replace("{file_name}", fileName));
            bos = new BufferedOutputStream(fos);

            while(buf.hasNext())
            {
                byte data = buf.next();
                buf.remove();

                bos.write(data);
            }

            bos.flush();
            bos.close();
            fos.close();

            event.onTransferFinish(this);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
