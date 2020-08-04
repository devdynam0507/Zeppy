package kr.ndy.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtil {

    public static byte[] toByte(int a)
    {
        byte[] arr = new byte[4];

        arr[3] = (byte) ((byte) ((a & 0xFF000000)) >> 24);
        arr[2] = (byte) ((byte) ((a & 0x00FF0000)) >> 16);
        arr[1] = (byte) ((byte) ((a & 0x0000FF00)) >> 8);
        arr[0] = (byte) ((a & 0x000000FF));

        return arr;
    }

    public static byte[] toByte(long a)
    {
        byte[] arr = new byte[8];

        arr[7] = (byte) (a >> 56);
        arr[6] = (byte) (a >> 48);
        arr[5] = (byte) (a >> 40);
        arr[4] = (byte) (a >> 32);
        arr[3] = (byte) (a >> 24);
        arr[2] = (byte) (a >> 16);
        arr[1] = (byte) (a >> 8);
        arr[0] = (byte) a;

        return arr;
    }

    public static byte[] toLittleEndian(byte[] arr)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap(arr);
        ByteBuffer destBuffer = ByteBuffer.allocate(arr.length);
        destBuffer.order(ByteOrder.LITTLE_ENDIAN);

        while(byteBuffer.hasRemaining())
        {
            destBuffer.put(byteBuffer.get());
        }

        return destBuffer.array();
    }

    public static String toHex(byte[] b)
    {
        return String.format("%064x", new BigInteger(1, b));
    }

    public static byte[] createZeroByte(final int size)
    {
        byte[] bytes = new byte[size];

        for(int i = 0; i < size; i++)
        {
            bytes[i] = 0x00000000;
        }

        return bytes;
    }
}
