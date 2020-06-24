package kr.ndy.crypto;

import io.leonard.Base58;

import java.math.BigInteger;

public class Base58Crypto implements ICryptoEncoder {

    private byte[] ripemd160;

    public Base58Crypto(byte[] ripemd160)
    {
        this.ripemd160 = ripemd160;
    }

    @Override
    public byte[] encode() {
        return new BigInteger(Base58.encode(ripemd160), 16).toByteArray();
    }

    @Override
    public String toHexString(byte[] src) {
        return null;
    }
}
