package kr.ndy.core.blockchain;

import kr.ndy.core.merkletree.MerkleTree;
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

    public BlockHeader(byte[] previousHash)
    {
        this.version = BlockSchema.BLOCK_VERSION;
        this.difficulty = BlockSchema.BLOCK_DIFFICULTY;
        this.previousHash = previousHash;
        this.merkleTree = new MerkleTree();
        this.timeStamp = DateUtil.generateFormat(new Date());
        this.nonce = 0x00;

        this.body = new BlockBody();
    }

    public BlockBody getBody() { return body; }
    public byte[] getPreviousHash() { return previousHash; }
    public int getDifficulty() { return difficulty; }
    public long getNonce() { return nonce; }
    public int getVersion() { return version; }
    public MerkleTree getMerkleTree() { return merkleTree; }
    public String getTimeStamp() { return timeStamp; }
    public BlockPOW getPow() { return new BlockPOW(this); }

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

    public void updateNonce()
    {
        ++nonce;
    }
}
