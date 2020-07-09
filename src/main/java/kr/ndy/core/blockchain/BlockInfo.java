package kr.ndy.core.blockchain;

import kr.ndy.crypto.SHA256;
import kr.ndy.util.ByteUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockInfo {

    private BlockHeader block;

    public BlockInfo(BlockHeader block)
    {
        this.block = block;
    }

    public byte[] hash()
    {
        byte[] arr = new byte[24];

        copy(arr);
        arr = digest(arr);
        arr = new SHA256(arr).encode();
        arr = digest(arr);
        arr = new SHA256(arr).encode();

        return arr;
    }

    private byte[] digest(byte[] arr)
    {
        MessageDigest digest = null;

        try
        {
            digest = MessageDigest.getInstance("MD5");

            digest.update(arr);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return digest.digest();
    }

    private void copy(byte[] arr)
    {
        System.arraycopy(arr, 0, ByteUtil.toByte(block.getVersion()), 0, 4);
        System.arraycopy(arr, 4, ByteUtil.toByte(block.getNonce()), 0, 8);
        System.arraycopy(arr, 12, ByteUtil.toByte(block.getDifficulty()), 0, 4);
        System.arraycopy(arr, 16, ByteUtil.toByte(block.getTime()), 0, 8);
    }

}
