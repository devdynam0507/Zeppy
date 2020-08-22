package kr.ndy.client;

import java.util.LinkedList;
import java.util.Queue;

public class FileResponseBuffer {

    private Queue<Byte> buffer;

    public FileResponseBuffer()
    {
        buffer = new LinkedList<>();
    }

    public void addBuffer(byte packet)
    {
        buffer.add(packet);
    }

    public Queue<Byte> getBuffer() { return buffer; }
    public void flush() { buffer.clear(); }
}
