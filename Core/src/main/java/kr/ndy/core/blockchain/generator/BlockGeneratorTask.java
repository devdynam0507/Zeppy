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
        int txLen;

        if(generator.getCurrentBlock() == null)
        {
            generator.createNewBlock();
            generator.getCurrentBlock().test();
        }

        txLen = generator.getCurrentBlock().getMerkleTree().toMerkleTree().length;

        if(txLen >= MAX_TX_LENGTH)
        {
            ZeppyModule.getInstance().getMiningPool().addPool(generator.getCurrentBlock());
        }
    }

}
