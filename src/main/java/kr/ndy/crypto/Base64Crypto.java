package kr.ndy.crypto;

import java.util.Base64;

public class Base64Crypto implements ICryptoEncoder, ICryptoDecoder {

    private byte[] encodeBytes;

    public Base64Crypto(byte[] encodeBytes)
    {
        this.encodeBytes = encodeBytes;
    }

    @Override
    public byte[] encode()
    {
        return Base64.getEncoder().encode(encodeBytes);
    }

    @Override
    public byte[] decode()
    {
        return Base64.getDecoder().decode(encodeBytes);
    }

    @Override
    public String toHexString(byte[] src)
    {
        return Base64.getEncoder().encodeToString(src);
    }
}
