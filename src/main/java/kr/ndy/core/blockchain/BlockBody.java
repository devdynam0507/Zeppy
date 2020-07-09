package kr.ndy.core.blockchain;

import kr.ndy.core.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BlockBody {

    private float fee;
    private int txCount;
    private int blockIndex;
    private List<Transaction> transactions;

    protected BlockBody()
    {
        this.fee = BlockSchema.MINER_FEE;
        this.transactions = new ArrayList<>();
        this.txCount = transactions.size();
        this.blockIndex = -1;
    }

    public float getFee() { return fee; }

    public int getBlockIndex() { return blockIndex; }

    public int getTxCount() { return txCount; }

    public List<Transaction> getTransactions() { return transactions; }
}
