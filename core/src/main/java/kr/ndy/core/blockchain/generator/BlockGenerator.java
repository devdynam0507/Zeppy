package kr.ndy.core.blockchain.generator;

import kr.ndy.core.blockchain.BlockHeader;
import kr.ndy.core.transaction.Transaction;

public class BlockGenerator {

    private BlockHeader currentBlock;
    private BlockHeader previousBlock;

    public synchronized void createNewBlock()
    {
        this.currentBlock = new BlockHeader();
    }

    public synchronized void addTransaction(Transaction transaction)
    {
        currentBlock.getMerkleTree().add(transaction);
    }
    public synchronized void setNull() { this.currentBlock = null; }

    public BlockHeader getCurrentBlock() { return currentBlock; }
    public BlockHeader getPreviousBlock() { return previousBlock; }

}
