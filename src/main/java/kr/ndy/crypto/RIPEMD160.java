package kr.ndy.crypto;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

public class RIPEMD160 implements ICryptoEncoder {

    private byte[] publicKey;

    public RIPEMD160(byte[] publicKey)
    {
        this.publicKey = publicKey;
    }

    @Override
    public byte[] encode()
    {
        RIPEMD160Digest digest = new RIPEMD160Digest();
        SHA256 sha256 = new SHA256(publicKey);
        byte[] hashPubKey = sha256.encode();

        digest.update(hashPubKey, 0, hashPubKey.length);
        byte[] d = new byte[digest.getDigestSize()];
        digest.doFinal(d, 0);

        return d;
    }

    @Override
    public String toHexString(byte[] src)
    {
        return null;
    }
}
