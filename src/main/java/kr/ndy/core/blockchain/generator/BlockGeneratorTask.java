package kr.ndy.core.blockchain.generator;

import java.util.TimerTask;

public class BlockGeneratorTask extends TimerTask {

    private BlockGenerator generator;
    private static final int MAX_TX_LENGTH = 999;

    public BlockGeneratorTask(BlockGenerator generator)
    {
        this.generator = generator;
    }

    @Override
    public void run()
    {
        int txLen = getGenerator().getCurrentBlock().getMerkleTree().toMerkleTree().length;

        if(txLen >= MAX_TX_LENGTH)
        {
            //TODO: Block Mining pool로 이동 (서버간)
        }
    }

    public synchronized BlockGenerator getGenerator() { return generator; }
}
