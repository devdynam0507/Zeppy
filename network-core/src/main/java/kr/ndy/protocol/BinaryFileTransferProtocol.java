package kr.ndy.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import kr.ndy.codec.MessageType;

public class BinaryFileTransferProtocol {

    private static final int PROTOCOL_DATA_SIZE = 1021;

    private byte[] data;
    private byte[] fileNameBytes;
    private int offset;

    private ByteBuf buf;

    public BinaryFileTransferProtocol(String fileName, byte[] data)
    {
        this.data = data;
        this.fileNameBytes = fileName.getBytes();
        this.offset = 0;
        _init();
    }

    public boolean hasNext()
    {
        return offset < data.length;
    }

    private int _size()
    {
        int size = offset + PROTOCOL_DATA_SIZE;

        if(offset < data.length && size > data.length)
        {
            size = (offset + (data.length - offset)) + 1;
        }

        return size;
    }

    private void _init()
    {
        buf = ByteBufAllocator.DEFAULT.buffer(data.length + 1);

        for(int i = 0; i < data.length; i++)
        {
            buf.writeByte(data[i]);
        }

        buf.writeByte(-1);
    }

    private void _eof(ChannelHandlerContext _ctx) throws InterruptedException
    {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte(MessageType.EOF);
        buf.writeByte(fileNameBytes.length);
        buf.writeBytes(fileNameBytes);

        ChannelFuture future = _ctx.writeAndFlush(buf).sync();

        if(!future.isSuccess())
        {
//            System.out.println(future.cause());
        }
    }

    private void _sof(ChannelHandlerContext _ctx) throws InterruptedException
    {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte(MessageType.FILE_NAME_PACKET);
        buf.writeByte(fileNameBytes.length);
        buf.writeBytes(fileNameBytes);

        _ctx.writeAndFlush(buf).sync();
    }

    public void handle(ChannelHandlerContext context) throws InterruptedException
    {
        _sof(context);

        while(hasNext())
        {
            int loop = _size();

            for(; offset < loop; offset++)
            {
                ByteBuf bufWrap = ByteBufAllocator.DEFAULT.buffer();
                bufWrap.writeByte(MessageType.TRANSFER_BIT_PACKET);
                bufWrap.writeByte(buf.readByte());
                bufWrap.writeByte(fileNameBytes.length);
                bufWrap.writeBytes(fileNameBytes);

                context.writeAndFlush(bufWrap).sync();
            }
        }

        _eof(context);
    }

}
