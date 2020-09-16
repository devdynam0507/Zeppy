package kr.ndy.core.blockchain;

import kr.ndy.Main;
import kr.ndy.core.merkletree.MerkleTree;
import kr.ndy.core.transaction.TransactionBuilder;
import kr.ndy.core.wallet.Wallet;
import kr.ndy.core.wallet.WalletAddress;
import kr.ndy.core.wallet.WalletGenerator;
import kr.ndy.core.transaction.Transaction;
import kr.ndy.util.ByteUtil;
import kr.ndy.util.DateUtil;

import java.util.Date;

public class BlockHeader {

    private int version;
    private int difficulty;
    private byte[] previousHash, blockHash;
    private MerkleTree merkleTree;
    private String timeStamp; //블럭 생성시 timestamp 생성
    private long nonce;

    private BlockBody body;
    private BlockPOW pow;
    private BlockInfo blockInfo;

    public BlockHeader()
    {
        this.version = BlockSchema.BLOCK_VERSION;
        this.difficulty = BlockSchema.BLOCK_DIFFICULTY;
        this.merkleTree = new MerkleTree();
        this.timeStamp = DateUtil.generateFormat(new Date());
        this.nonce = 0x00;

        this.body = new BlockBody();
        this.pow = new BlockPOW(this);
        this.blockInfo = new BlockInfo(this);
    }

    public BlockBody getBody() { return body; }
    public byte[] getPreviousHash() { return previousHash != null ? previousHash : ByteUtil.createZeroByte(32); }
    public byte[] getBlockHash() { return blockHash; }
    public int getDifficulty() { return difficulty; }
    public long getNonce() { return nonce; }
    public int getVersion() { return version; }
    public MerkleTree getMerkleTree() { return merkleTree; }
    public String getTimeStamp() { return timeStamp; }
    public synchronized BlockPOW getPow() { return pow; }
    public BlockInfo getBlockInfo() { return blockInfo; }

    public long getTime()
    {
        return DateUtil.conversion(getTimeStamp()).getTime();
    }

    public byte[] getMerkleRoot()
    {
        byte[][] merkleTree = getMerkleTree().toMerkleTree();

        return merkleTree[merkleTree.length - 1];
    }

    public void test()
    {
        WalletAddress address1 = Main.wallet1.getAddress();
        WalletAddress address2 = Main.wallet2.getAddress();

        for(int i = 0; i < 999; i++)
        {
            Transaction t = TransactionBuilder.builder()
                    .amount(10)
                    .receiver(address1.getWalletAddress())
                    .sender(address2.getWalletAddress())
                    .senderPrivateKey(address2.toHexString(address2.getPrivateKey().getEncoded()))
                    .senderPublicKey(address2.toHexString(address2.getPublicKey().getEncoded())).build();

            merkleTree.add(t);
            getBody().getTransactions().add(t);
        }

        previousHash = new byte[32];

        for(int i = 0; i < 32; i++)
        {
            previousHash[i] = 0x00;
        }
    }

    public void setPreviousHash(byte[] previousHash) { this.previousHash = previousHash; }
    public void setBlockHash(byte[] blockHash) { this.blockHash = blockHash; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }
    public void updateNonce(long nonce) { this.nonce = nonce; }

    public boolean isGenesis()
    {
        return previousHash.length == 1 && previousHash[0] == 0x00000000;
    }

    public byte[] updateNonce()
    {
        ++nonce;

        return blockInfo.hash();
    }
}
