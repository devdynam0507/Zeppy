package kr.ndy.core.transaction;

import kr.ndy.crypto.PublicKeyDecoder;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class TransactionAcceleratorObject {

    private final String acc;
    private final String signedStr;

    private final PublicKey pubKey;
    private final String txJsonWithBase64;

    protected TransactionAcceleratorObject(String acc, String signedStr, String pubKey, String txJsonWithBase64)
    {
        this.acc = acc;
        this.signedStr = signedStr;
        this.pubKey = new PublicKeyDecoder(pubKey).decode();
        this.txJsonWithBase64 = txJsonWithBase64;
    }

    public String getAcc() { return acc; }
    public String getSignedStr() { return signedStr; }
    public String getTxJsonWithBase64() { return txJsonWithBase64; }
    public PublicKey getPublicKey() { return pubKey; }

}
