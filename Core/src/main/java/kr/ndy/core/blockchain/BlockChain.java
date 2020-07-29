package kr.ndy.core.blockchain;

import kr.ndy.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {

    private Logger logger;
    private List<BlockHeader> chains;
    private BlockFileIO bfio;

    public BlockChain(BlockFileIO bfio)
    {
        this.chains = new ArrayList<>();
        this.logger = LoggerFactory.getLogger(BlockChain.class);
        this.bfio = bfio;
    }

    public void combine(BlockHeader header)
    {
        int size = size();

        if(size > 0)
        {
            BlockHeader previousBlock = chains.get(size - 1);

            header.setPreviousHash(previousBlock.getBlockHash());
            //TODO: send updated blockchain packet
            logger.info("Combined block hash: " + ByteUtil.toHex(header.getBlockHash()));
        } else
        {
            header.setPreviousHash(ByteUtil.createZeroByte(32));
            logger.info("Created genesis block");
        }

        header.setBlockHash(header.getPow().getPowBytes());
        chains.add(header);
        bfio.write(header);
        System.exit(0);
    }

    public void loadAllBlocks()
    {
        this.chains = bfio.read();
        logger.info("Successfully " + chains.size() + " loaded blocks");
    }

    public BlockFileIO getBlockFileIO() { return bfio; }

    public BlockHeader getGenesisBlock()
    {
        return chains.size() <= 0 ? null : chains.get(0);
    }

    public int size()
    {
        return chains.size();
    }

}
