package kr.ndy.crypto;

import kr.ndy.core.transaction.TransactionAcceleratorObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;

public class DigitalSignatureVerify implements ICryptoDecoder<Boolean> {

    private TransactionAcceleratorObject accInstance;

    public DigitalSignatureVerify(TransactionAcceleratorObject accInstance)
    {
        this.accInstance =  accInstance;
    }

    @Override
    public Boolean decode() {
        PublicKey publicKey = accInstance.getPublicKey();
        boolean bVerify = false;

        try
        {
            byte[] signed = new BigInteger(accInstance.getSignedStr(), 16).toByteArray();
            byte[] data = accInstance.getAcc().getBytes("UTF-8");

            Signature v = Signature.getInstance("SHA1withECDSA");
            v.initVerify(publicKey);
            v.update(data);

            bVerify = v.verify(signed);
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                UnsupportedEncodingException | SignatureException e)
        {
            e.printStackTrace();
        }

        return bVerify;
    }
}
