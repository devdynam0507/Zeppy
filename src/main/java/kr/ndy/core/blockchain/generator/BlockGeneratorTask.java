package kr.ndy.core.blockchain.generator;

import kr.ndy.core.ZeppyModule;

public class BlockGeneratorTask extends Thread {

    private BlockGenerator generator;
    private static final int MAX_TX_LENGTH = 999;

    public BlockGeneratorTask(BlockGenerator generator)
    {
        this.generator = generator;
    }

    @Override
    public void run()
    {
        System.out.println("tx");
        int txLen = 0;

        if(generator.getCurrentBlock() == null)
        {
            generator.createNewBlock();
        }

        txLen = generator.getCurrentBlock().getMerkleTree().toMerkleTree().length;

        if(txLen >= MAX_TX_LENGTH)
        {
            ZeppyModule.getInstance().getMiningPool().addPool(generator.getCurrentBlock());
        }
    }

}
