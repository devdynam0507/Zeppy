package kr.ndy.core.blockchain.generator;

import kr.ndy.core.ZeppyModule;
import kr.ndy.core.blockchain.BlockChain;
import kr.ndy.core.blockchain.BlockHeader;
import kr.ndy.core.blockchain.BlockPOW;

public class BlockMinerClient extends Thread {

    private int id;

    public BlockMinerClient(int id)
    {
        this.id = id;
    }

    @Override
    public void run()
    {
        ZeppyModule module = ZeppyModule.getInstance();
        BlockMiningPool pool = module.getMiningPool();
        BlockChain chain = module.getBlockChain();

        while(true)
        {
            BlockHeader header = pool.getCurrentPOWHeader();

            if(header != null)
            {
                BlockPOW pow = header.getPow();
                boolean bValid = pow.validation();

                if(bValid)
                {
                    //TODO: pow 검증 시 수수료 지급 Transaction, 블럭 체인에 추가
                    header.setBlockHash(pow.getPowBytes());
                    chain.combine(header);
                    pool.complete(pow.getPowBytes());
                }
            }
        }
    }

}
