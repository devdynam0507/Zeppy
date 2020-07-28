package kr.ndy.core.blockchain;

import kr.ndy.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {

    private Logger logger;
    private List<BlockHeader> chains;

    public BlockChain()
    {
        chains = new ArrayList<>();
        logger = LoggerFactory.getLogger(BlockChain.class);
    }

    public void combine(BlockHeader header)
    {
        int size = size();

        if(size > 0)
        {
            BlockHeader previousBlock = chains.get(size - 1);

            header.setPreviousHash(previousBlock.getBlockHash());
            header.setBlockHash(header.getPow().getPowBytes());

            //TODO: send updated blockchain packet

            logger.info("Combined block hash: " + ByteUtil.toHex(header.getBlockHash()));
        } else
        {
            chains.add(header);
            logger.info("Created genesis block");
        }
    }

    public BlockHeader getGenesisBlock()
    {
        return chains.size() <= 0 ? null : chains.get(0);
    }

    public int size()
    {
        return chains.size();
    }

}
