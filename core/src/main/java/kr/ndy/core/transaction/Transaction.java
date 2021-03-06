package kr.ndy.core.transaction;

import kr.ndy.crypto.PrivateKeyDecoder;
import kr.ndy.crypto.PublicKeyDecoder;
import kr.ndy.crypto.SHA256;
import kr.ndy.util.ByteUtil;
import kr.ndy.util.DateUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class Transaction {

    private String txDate; // transaction 생성일자 yyyy-MM-dd-hh:mm:ss
    private String txId;

    private String sender; // 송금자 address
    private String receiver; // 받는사람 address

    private PrivateKey senderPrivateKey;
    private PublicKey senderPublicKey;

    private double amount;

    public Transaction(String sender,
                       String receiver,
                       String senderPrivateKey,
                       String senderPublicKey,
                       double amount,
                       String createdAt)
    {
        try
        {
            this.sender = sender;
            this.receiver = receiver;
            this.senderPublicKey = new PublicKeyDecoder(senderPublicKey).decode();
            this.amount = amount;
            this.txDate = createdAt;
            this.senderPrivateKey = new PrivateKeyDecoder(senderPrivateKey).decode();

            this.txId = ByteUtil.toHex(new SHA256(ByteUtil.sumByteArrays(
                    sender.getBytes(),
                    receiver.getBytes(),
                    ByteUtil.toByte(amount),
                    txDate.getBytes()
            )).encode());
        } catch (NullPointerException e)
        {
        }
    }

    public Transaction(String sender,
                       String receiver,
                       String senderPublicKey,
                       double amount,
                       String createdAt)
    {
        this(sender, receiver, null, senderPublicKey, amount, createdAt);
    }

    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public PrivateKey getSenderPrivateKey() { return senderPrivateKey; }
    public PublicKey getSenderPublicKey() { return senderPublicKey; }
    public double getAmount() { return amount; }
    public String getTxDate() { return txDate; }
    public Date getTxDateWithInstance() { return DateUtil.conversion(txDate); }
    public TransactionInfo getTxInfo() { return new TransactionInfo(this); }
    public String getTxId() { return txId; }
}
