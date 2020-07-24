package kr.ndy.core.wallet;

import java.security.SecureRandom;

public class WalletGenerator {

    public static Wallet create()
    {
        return new Wallet(new WalletKeyGenerator(new SecureRandom()));
    }

}
