package kr.ndy.core.blockchain;

import kr.ndy.crypto.SHA256;

public class BlockPOW {

    private BlockHeader header;
    private byte[] pow;

    public BlockPOW(BlockHeader header)
    {
        this.header = header;
    }

    /**
     * @param pow POW Result
     *
     * */
    private void updatePow(byte[] pow)
    {
        this.pow = pow;
    }

    public byte[] getPowBytes()
    {
        return pow;
    }

    public synchronized boolean validation()
    {
        byte[] hash = header.updateNonce();
        int diff = header.getDifficulty();
        int nBits = 0;

        updatePow(hash);

        for(int i = 0; i < diff; i++)
        {
            if(hash[i] == 0X00000000)
            {
                ++nBits;
            } else
            {
                break;
            }
        }

        return nBits >= diff;
    }

    /**
     *
     * */
    public synchronized void pong(boolean bValidation)
    {

        pow = null;
    }

    @Override
    public String toString() {
        return new SHA256(null).toHexString(pow);
    }
}
