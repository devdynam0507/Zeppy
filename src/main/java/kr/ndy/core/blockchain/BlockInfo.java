package kr.ndy.core.blockchain;

import kr.ndy.crypto.SHA256;
import kr.ndy.util.ByteUtil;

import java.math.BigInteger;
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
        arr = new SHA256(arr).encode();
        arr = new SHA256(arr).encode();

        return arr;
    }

    private byte[] digest(byte[] arr)
    {
        MessageDigest digest = null;

        try
        {
            digest = MessageDigest.getInstance("SHA-256");

            digest.update(arr);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return digest.digest();
    }

    private void copy(byte[] arr)
    {
        System.arraycopy(ByteUtil.toByte(block.getVersion()), 0, arr, 0, 4);
        System.arraycopy(ByteUtil.toByte(block.getNonce()), 0, arr, 4, 8);
        System.arraycopy(ByteUtil.toByte(block.getDifficulty()), 0, arr, 12, 4);
        System.arraycopy(ByteUtil.toByte(block.getTime()), 0, arr, 16, 8);

        System.out.println(new BigInteger(arr).toString(16));
    }

}
