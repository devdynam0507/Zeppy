package kr.ndy.core.wallet;

public class Wallet {

    private WalletAddress address;

    protected Wallet(WalletKeyGenerator generator)
    {
        this.address = new WalletAddress(generator);
    }

    public WalletAddress getAddress() { return address; }
}