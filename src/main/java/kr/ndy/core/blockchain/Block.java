package kr.ndy.core.blockchain;

public class Block {

    private int version;
    private byte[] previousHash;
    //TODO: MerkleRoot
    private String timeStamp;
    private int difficulty;
    private int nonce;

    public Block()
    {
        this.version = BlockSchema.BLOCK_VERSION;
        this.difficulty = BlockSchema.BLOCK_DIFFICULTY;


    }

}
