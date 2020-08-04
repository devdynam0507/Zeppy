package kr.ndy.core.blockchain.generator;

import kr.ndy.core.blockchain.observer.IBlockObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockGeneratorTask extends Thread {

    private BlockGenerator generator;
    private IBlockObserver observer;
    private static final int MAX_TX_LENGTH = 999;
    private static final Logger logger = LoggerFactory.getLogger(BlockGeneratorTask.class);

    public BlockGeneratorTask(BlockGenerator generator, IBlockObserver observer)
    {
        this.generator = generator;
        this.observer = observer;
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
            observer.onGenerateBlock(generator.getCurrentBlock());
            generator.setNull();
            logger.info("Block transaction is full, send mining pool ");
        }
    }

}
