package kr.ndy.crypto;

import kr.ndy.core.wallet.WalletKeyGenerator;

import java.math.BigInteger;
import java.security.*;

public class DigitalSignatureSign implements ICryptoEncoder {

    private String str;
    private Key privateKey, publicKey;

    public DigitalSignatureSign(String test, Key privateKey, Key publicKey)
    {
        this.str = test;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public byte[] encode()
    {
        try
        {
            /** 전자 서명 */

            Signature signature= Signature.getInstance("SHA1withECDSA");
            signature.initSign((PrivateKey) privateKey);

            byte[] arr = str.getBytes("UTF-8");
            byte[] arr2;
            signature.update(arr);
            arr2 = signature.sign();

            System.out.println("서명: 0x" + new BigInteger(1, arr2).toString(16));

            Signature v = Signature.getInstance("SHA1withECDSA");
            v.initVerify((PublicKey) publicKey);
            v.update(arr);

            boolean result = v.verify(arr2);
            System.out.println("퍼블릭 서명: " + result);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public String toHexString(byte[] src) {
        return null;
    }
}
