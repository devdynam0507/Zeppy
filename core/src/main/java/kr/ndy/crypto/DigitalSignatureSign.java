package kr.ndy.crypto;

import java.security.Key;
import java.security.PrivateKey;
import java.security.Signature;

public class DigitalSignatureSign implements ICryptoEncoder {

    private String data;
    private Key privateKey;

    public DigitalSignatureSign(String data, Key privateKey)
    {
        this.data = data;
        this.privateKey = privateKey;
    }

    @Override
    public byte[] encode()
    {
        byte[] signed = null;

        try
        {
            Signature signature= Signature.getInstance("SHA1withECDSA");
            signature.initSign((PrivateKey) privateKey);

            byte[] arr = data.getBytes("UTF-8");

            signature.update(arr);
            signed = signature.sign();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return signed;
    }

    @Override
    public String toHexString(byte[] src) {
        return null;
    }
}
