package kr.ndy.core.blockchain.generator;

import kr.ndy.core.ZeppyModule;
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
        BlockHeader header = pool.getCurrentPOWHeader();

        while(true)
        {
            if(header != null)
            {
                BlockPOW pow = header.getPow();
                boolean bValid = pow.validation();

                if(bValid)
                {
                    //TODO: pow 검증 시 수수료 지급 Transaction, 블럭 체인에 추가
                    module.getBlockChain().combine(header);
                    header = null;
                    System.out.println("Valid");
                }
            } else
            {
                header = pool.getCurrentPOWHeader();
            }
        }
    }

}
