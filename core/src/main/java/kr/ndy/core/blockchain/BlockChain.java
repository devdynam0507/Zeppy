package kr.ndy.core.blockchain;

import kr.ndy.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BlockChain {

    private Logger logger;
    private Vector<BlockHeader> chains;
    private BlockFileIO bfio;

    public BlockChain(BlockFileIO bfio)
    {
        this.chains = new Vector<>();
        this.logger = LoggerFactory.getLogger(BlockChain.class);
        this.bfio = bfio;
    }

    public Vector<BlockHeader> getChains() { return chains; }

    public synchronized void combine(BlockHeader header)
    {
        int size = size();

        if(size > 0)
        {
            BlockHeader previousBlock = chains.lastElement();

            header.setPreviousHash(previousBlock.getBlockHash());
            //TODO: send updated blockchain packet
            logger.info("Combined block hash: " + ByteUtil.toHex(header.getBlockHash()));
            logger.info("Combined block prevhash: " + ByteUtil.toHex(header.getPreviousHash()));
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
        Vector<BlockHeader> recombined = new Vector<>();
        BlockHeader block = getGenesisBlockFromPrivsByte();

        //TODO: 최적화 필요
        if(block != null)
        {
            recombined.add(block);
            chains.remove(block);

            int size = size();
            byte[] blockHash = block.getBlockHash();

            while(size > 0)
            {
                for(BlockHeader find : chains)
                {
                    if(Arrays.equals(find.getPreviousHash(), blockHash))
                    {
                        --size;
                        recombined.add(find);
                        blockHash = find.getBlockHash();
                    }
                }
            }

            chains = recombined;
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

        return chains.stream().filter(i -> i.isGenesis()).findFirst().orElse(null);
    }

    public int size()
    {
        return chains.size();
    }

}
