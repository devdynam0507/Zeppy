package kr.ndy;

import kr.ndy.core.wallet.Wallet;
import kr.ndy.core.wallet.WalletAddress;
import kr.ndy.core.wallet.WalletGenerator;
import kr.ndy.crypto.DigitalSignatureSign;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {

    public static void main(String... args)
    {
        Security.addProvider(new BouncyCastleProvider());
        Wallet wallet = WalletGenerator.create();
        WalletAddress address = wallet.getAddress();
        DigitalSignatureSign dss = new DigitalSignatureSign("plaintext", address.getPrivateKey(), address.getPublicKey());
        dss.encode();
    }

}
