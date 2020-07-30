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

    public synchronized void combine(BlockHeader header)
    {
        int size = size();

        header.setBlockHash(header.getPow().getPowBytes());

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

        chains.add(header);
        bfio.write(header);
    }

    private void recombine()
    {
        int chainSize = size();
        List<BlockHeader> recombinedChains = new ArrayList<>();
        recombinedChains.add(getGenesisBlockFromPrivsByte());

        for(int i = 0; i < chainSize; i++)
        {

        }
    }

    public void loadAllBlocks()
    {
        this.chains = bfio.read();
        recombine();

        logger.info("Successfully " + chains.size() + " loaded blocks");
    }

    public BlockFileIO getBlockFileIO() { return bfio; }

    public BlockHeader getGenesisBlock()
    {
        return chains.size() <= 0 ? null : chains.get(0);
    }

    public BlockHeader getGenesisBlockFromPrivsByte()
    {
        byte[] zero32 = ByteUtil.createZeroByte(32);

        return chains.stream().filter(i -> i.getPreviousHash().equals(zero32)).findFirst().orElse(null);
    }

    public int size()
    {
        return chains.size();
    }

}
