package kr.ndy.core.blockchain;

import kr.ndy.core.transaction.Transaction;

import java.util.List;

public class BlockBuilder {

    private byte[] previousHash, blockHash;
    private long nonce;
    private String timeStamp;
    private List<Transaction> transactions;

    private BlockBuilder() { }

    public static BlockBuilder builder() { return new BlockBuilder(); }

    public BlockBuilder previous(byte[] previousHash)
    {
        this.previousHash = previousHash;
        return this;
    }

    public BlockBuilder hash(byte[] blockHash)
    {
        this.blockHash = blockHash;
        return this;
    }

    public BlockBuilder nonce(long nonce)
    {
        this.nonce = nonce;
        return this;
    }

    public BlockBuilder time(String timeStamp)
    {
        this.timeStamp = timeStamp;
        return this;
    }

    public BlockBuilder transactions(List<Transaction> transactions)
    {
        this.transactions = transactions;
        return this;
    }

    public BlockHeader build()
    {
        BlockHeader block = new BlockHeader();
        List<Transaction> bodyTransactions = block.getBody().getTransactions();

        block.setPreviousHash(previousHash);
        block.setBlockHash(blockHash);
        block.setTimeStamp(timeStamp);
        block.updateNonce(nonce);

        for(Transaction tx : transactions)
        {
            bodyTransactions.add(tx);
        }

        return block;
    }

}
