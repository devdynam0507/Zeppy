package kr.ndy.core.blockchain;

import kr.ndy.core.merkletree.MerkleTree;
import kr.ndy.core.transaction.TransactionBuilder;
import kr.ndy.core.wallet.Wallet;
import kr.ndy.core.wallet.WalletAddress;
import kr.ndy.core.wallet.WalletGenerator;
import kr.ndy.util.DateUtil;

import java.util.Date;

public class BlockHeader {

    private int version;
    private int difficulty;
    private byte[] previousHash;
    private MerkleTree merkleTree;
    private String timeStamp; //블럭 생성시 timestamp 생성
    private long nonce;

    private BlockBody body;
    private BlockPOW pow;

    public BlockHeader(byte[] previousHash)
    {
        this.version = BlockSchema.BLOCK_VERSION;
        this.difficulty = BlockSchema.BLOCK_DIFFICULTY;
        this.previousHash = previousHash;
        this.merkleTree = new MerkleTree();
        this.timeStamp = DateUtil.generateFormat(new Date());
        this.nonce = 0x00;

        this.body = new BlockBody();
        this.pow = new BlockPOW(this);
    }

    public BlockBody getBody() { return body; }
    public byte[] getPreviousHash() { return previousHash; }
    public int getDifficulty() { return difficulty; }
    public long getNonce() { return nonce; }
    public int getVersion() { return version; }
    public MerkleTree getMerkleTree() { return merkleTree; }
    public String getTimeStamp() { return timeStamp; }
    public synchronized BlockPOW getPow() { return pow; }

    public BlockInfo getBlockInfo() { return new BlockInfo(this); }

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
        Wallet wallet1 = WalletGenerator.create();
        WalletAddress address1 = wallet1.getAddress();

        Wallet wallet2 = WalletGenerator.create();
        WalletAddress address2 = wallet2.getAddress();

        for(int i = 0; i < 999; i++)
        {
            merkleTree.add(TransactionBuilder.builder().amount(0.5).receiver(address1.getWalletAddress()).sender(address2.getWalletAddress())
                                    .senderPrivateKey(address1.toHexString(address1.getPrivateKey().getEncoded()))
                .senderPublicKey(address1.toHexString(address1.getPublicKey().getEncoded())).build());
        }

        previousHash = new byte[32];

        for(int i = 0; i < 32; i++)
        {
            previousHash[i] = 0x00;
        }
    }

    public byte[] updateNonce()
    {
        ++nonce;
        System.out.println("nonce: " + nonce);
        return getBlockInfo().hash();
    }
}
