package kr.ndy.core.blockchain;

import kr.ndy.core.merkletree.MerkleTree;

public class Block {

    private int version;
    private int difficulty;
    private byte[] previousHash;
    private MerkleTree merkleTree;
    private String timeStamp; //블럭 생성시 timestamp 생성
    private int nonce;

    public Block(byte[] previousHash)
    {
        this.version = BlockSchema.BLOCK_VERSION;
        this.difficulty = BlockSchema.BLOCK_DIFFICULTY;
        this.previousHash = previousHash;
        this.merkleTree = new MerkleTree();
        this.timeStamp = null;
        this.nonce = 0x00;
    }

}
