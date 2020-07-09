package kr.ndy.util;

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

}
