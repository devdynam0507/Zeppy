package kr.ndy.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 implements ICryptoEncoder {

    private byte[] bytes;

    public SHA256(byte[] bytes)
    {
        this.bytes = bytes;
    }

    @Override
    public byte[] encode()
    {
        MessageDigest digest;
        byte[] hash = new byte[512];

        try {
            digest = MessageDigest.getInstance("SHA-256");

            digest.update(bytes);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    @Override
    public String toHexString(byte[] src)
    {
        StringBuilder builder = new StringBuilder();

        for(byte b : src)
        {
            builder.append(String.format("%x", b & 0xFF));
        }

        return builder.toString();
    }
}
