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

    public FileResponseClientThread(FileTransferEvent event, FileResponseBuffer buffer)
    {
        this.event = event;
        this.buffer = buffer;
    }

    @Override
    public void run()
    {
        Iterator<Byte> buf = buffer.getBuffer().iterator();
        FileOutputStream fos;
        BufferedOutputStream bos;

        try
        {
            //File name 받는거 필요함.
            fos = new FileOutputStream("C://zeppy_client/a.txt");
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
