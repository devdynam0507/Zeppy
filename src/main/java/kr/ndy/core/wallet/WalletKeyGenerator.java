package kr.ndy.core.wallet;

import io.leonard.Base58;
import kr.ndy.crypto.RIPEMD160;
import kr.ndy.crypto.SHA256;

import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

public class WalletKeyGenerator {

    private SecureRandom secureRandom;

    public WalletKeyGenerator(SecureRandom secureRandom)
    {
        this.secureRandom = secureRandom;
    }

    public String getAddress(PublicKey publicKey)
    {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        ECPoint ecPoint = ecPublicKey.getW();
        byte[] pubHash = new SHA256(ecPoint.getAffineX().toByteArray()).encode();
        byte[] ripemd = new RIPEMD160(pubHash).encode();
        byte[] checksum = new SHA256(new SHA256(pubHash).encode()).encode();
        byte[] arr = new byte[ripemd.length + checksum.length + 1];

        arr[0] = 0x00;
        System.arraycopy(ripemd, 0, arr, 1, ripemd.length);
        System.arraycopy(checksum, 0, arr, ripemd.length + 1, checksum.length);

        return Base58.encode(arr);
    }

    public KeyPair generate()
    {
        KeyPair key = null;

        try
        {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec spec = new ECGenParameterSpec("secp256k1");

            generator.initialize(spec, secureRandom);
            key = generator.generateKeyPair();

        }catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return key;
    }

}
