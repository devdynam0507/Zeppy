package kr.ndy.crypto;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.*;

public class PrivateKeyDecoder implements ICryptoDecoder<PrivateKey> {

    private String privateKeyStr;

    public PrivateKeyDecoder(String privateKeyStr)
    {
        this.privateKeyStr = privateKeyStr;
    }

    @Override
    public PrivateKey decode() {
        PrivateKey privateKey = null;

        try
        {
            byte[] privateKeyBytes = new BigInteger(privateKeyStr, 16).toByteArray();
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            e.printStackTrace();
        }

        return privateKey;
    }
}
