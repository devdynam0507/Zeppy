package kr.ndy.core.blockchain;

import kr.ndy.crypto.SHA256;

import java.math.BigInteger;

public class BlockPOW extends Thread {

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
    public synchronized void updatePow(byte[] pow)
    {
        //TODO: Update pow
    }

    public synchronized boolean validation()
    {
        header.updateNonce();
        BlockInfo blockInfo = new BlockInfo(header);
        System.out.println(blockInfo.hash());

        return false;
    }

    /**
     *
     * */
    public synchronized void pong(boolean bValidation)
    {

        pow = null;
    }

    @Override
    public void run()
    {
        pong(validation());
        //TODO: Validation POW
    }
}
