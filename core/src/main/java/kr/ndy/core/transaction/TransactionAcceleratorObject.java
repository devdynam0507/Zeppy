package kr.ndy.core.transaction;

import kr.ndy.crypto.PublicKeyDecoder;
import org.json.simple.JSONObject;

import java.security.PublicKey;

public class TransactionAcceleratorObject extends Transaction {

    private final String acc;
    private final String signedStr;

    private final PublicKey pubKey;
    private final JSONObject decryptedTxBase64;

    protected TransactionAcceleratorObject(String acc, String signedStr, String pubKey, JSONObject decryptedTxBase64)
    {
        super((String) decryptedTxBase64.get("sender"),
              (String) decryptedTxBase64.get("receiver"),
              pubKey,
              (double) decryptedTxBase64.get("amount"),
              (String) decryptedTxBase64.get("createdAt")
        );

        this.acc = acc;
        this.signedStr = signedStr;
        this.pubKey = new PublicKeyDecoder(pubKey).decode();
        this.decryptedTxBase64 = decryptedTxBase64;
    }

    public String getAcc() { return acc; }
    public String getSignedStr() { return signedStr; }
    public JSONObject getTxJsonWithBase64() { return decryptedTxBase64; }
    public PublicKey getPublicKey() { return pubKey; }

}
