package kr.ndy.crypto;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeyDecoder implements ICryptoDecoder<PublicKey> {

    private String publicKeyStr;

    public PublicKeyDecoder(String publicKeyStr)
    {
        this.publicKeyStr = publicKeyStr;
    }

    @Override
    public PublicKey decode()
    {
        PublicKey publicKey = null;

        try
        {
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(new BigInteger(publicKeyStr, 16).toByteArray());
            KeyFactory factory = KeyFactory.getInstance("EC");
            publicKey = factory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            e.printStackTrace();
        }

        return publicKey;
    }
}
