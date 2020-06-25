package kr.ndy.core.transaction;

import kr.ndy.util.DateUtil;

import java.security.PublicKey;
import java.util.Date;

public class TransactionBuilder {

    private String sender, receiver, createdAt;
    private String privateKey;
    private String publicKey;
    private double amount;

    private TransactionBuilder() {}

    public static TransactionBuilder builder()
    {
        return new TransactionBuilder();
    }

    public TransactionBuilder sender(String sender)
    {
        this.sender = sender;
        return this;
    }

    public TransactionBuilder receiver(String receiver)
    {
        this.receiver = receiver;
        return this;
    }

    public TransactionBuilder senderPrivateKey(String privateKey)
    {
        this.privateKey = privateKey;
        return this;
    }

    public TransactionBuilder senderPublicKey(String publicKey)
    {
        this.publicKey = publicKey;
        return this;
    }

    public TransactionBuilder amount(double amount)
    {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder createdAt(String createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    public Transaction build()
    {
        return new Transaction(
                sender,
                receiver,
                privateKey,
                publicKey,
                amount,
                createdAt == null ? DateUtil.generateFormat(new Date()) : createdAt
        );
    }

}
