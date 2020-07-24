package kr.ndy.core.transaction;

import kr.ndy.crypto.PublicKeyDecoder;

import java.security.PublicKey;

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
