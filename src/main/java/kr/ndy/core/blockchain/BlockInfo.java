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
        byte[] prevHash = ByteUtil.toLittleEndian(block.getPreviousHash());
        byte[] merkleRoot = ByteUtil.toLittleEndian(block.getMerkleRoot());
        byte[] arr = new byte[24 + prevHash.length + merkleRoot.length];

        copy(arr, prevHash, merkleRoot);
        arr = new BigInteger(arr).toByteArray();
        arr = digest(digest(arr));
        System.out.println(new BigInteger(arr).toString(16));

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

    private void copy(byte[] arr, byte[] prevHash, byte[] merkleRoot)
    {
        int sum = prevHash.length + merkleRoot.length;

        System.arraycopy(merkleRoot, 0, arr, 0, merkleRoot.length);
        System.arraycopy(prevHash, 0, arr,  merkleRoot.length, prevHash.length);
        System.arraycopy(ByteUtil.toByte(block.getVersion()), 0, arr, sum, 4);
        System.arraycopy(ByteUtil.toByte(block.getNonce()), 0, arr, sum + 4, 8);
        System.arraycopy(ByteUtil.toByte(block.getDifficulty()), 0, arr, sum + 4 + 8, 4);
        System.arraycopy(ByteUtil.toByte(block.getTime()), 0, arr, sum + 4 + 8 + 4, 8);

    }

}
