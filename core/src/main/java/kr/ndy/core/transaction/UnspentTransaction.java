package kr.ndy.core.transaction;

import kr.ndy.core.blockchain.BlockChain;
import kr.ndy.core.blockchain.BlockHeader;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UnspentTransaction extends Transaction {

    private String txId;

    public UnspentTransaction(String sender,
                              String receiver,
                              PrivateKey senderPrivateKey,
                              PublicKey senderPublicKey,
                              double amount,
                              String createdAt,
                              String txId)
    {
        super(
                sender,
                receiver,
                new BigInteger(1, senderPrivateKey.getEncoded()).toString(16),
                new BigInteger(1, senderPublicKey.getEncoded()).toString(16),
                amount,
                createdAt
        );

        this.txId = txId;
    }

    public static double getAmount(List<Transaction> utxo)
    {
        double amount = 0.0D;

        for(Transaction t : utxo)
        {
            System.out.println(t.getAmount());
            amount += t.getAmount();
        }

        return amount;
    }

    public static List<Transaction> collectUnspentTransactions(String walletAddress, BlockChain chain)
    {
        final Vector<BlockHeader> chains = chain.getChains();
        final List<Transaction> collectedUnspentTransaction = new ArrayList<>();

        for(BlockHeader header : chains)
        {
            final List<Transaction> transactions = header.getBody().getTransactions();

            for(Transaction transaction : transactions)
            {
                if(transaction.getReceiver().equals(walletAddress))
                {
                    Transaction u = new UnspentTransaction(
                            transaction.getSender(),
                            transaction.getReceiver(),
                            transaction.getSenderPrivateKey(),
                            transaction.getSenderPublicKey(),
                            transaction.getAmount(),
                            transaction.getTxDate(),
                            transaction.getTxId()
                    );
                    collectedUnspentTransaction.add(u);
                }
            }
        }

        return collectedUnspentTransaction;
    }


    public static String toString(List<UnspentTransaction> list)
    {
        return list.toString();
    }
}
