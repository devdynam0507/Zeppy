package kr.ndy.core.wallet;

import kr.ndy.crypto.ICryptoEncoder;
import kr.ndy.crypto.Base64Crypto;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;

public class WalletAddress {

    private Key privateKey, publicKey;
    private String walletAddress;

    public WalletAddress(WalletKeyGenerator generator)
    {
        KeyPair key = generator.generate();
        PublicKey pub = key.getPublic();

        this.privateKey = key.getPrivate();
        this.publicKey = pub;
        this.walletAddress = generator.getAddress(pub);
    }

    public Key getPrivateKey() { return privateKey; }
    public Key getPublicKey() { return publicKey; }
    public String getWalletAddress() { return walletAddress; }

    public String toHexString(byte[] key)
    {
        return new BigInteger(1, key).toString(16);
    }

    public String toBase64String()
    {
        ICryptoEncoder encoder = new Base64Crypto(privateKey.getEncoded());
        byte[] base64 = encoder.encode();

        return encoder.toHexString(base64);
    }

    public void print()
    {
        System.out.println(toHexString(privateKey.getEncoded()));
        System.out.println(toHexString(publicKey.getEncoded()));
        System.out.println(walletAddress);
    }

}
