package kr.ndy.core.blockchain.generator;

import kr.ndy.core.ZeppyModule;
import kr.ndy.core.blockchain.BlockHeader;
import kr.ndy.core.blockchain.observer.IBlockObserver;

import java.util.LinkedList;
import java.util.Queue;

public class BlockMiningPool extends Thread implements IBlockObserver {

    private Queue<BlockHeader> waitingHeaders;
    private BlockHeader currentPOWHeader; //작업증명중인 블럭

    public BlockMiningPool()
    {
        waitingHeaders = new LinkedList<>();
    }

    @Override
    public void run()
    {
        if(currentPOWHeader == null && waitingHeaders.size() > 0)
        {
            currentPOWHeader = waitingHeaders.remove();
            ZeppyModule.getInstance().getBlockObserverManager().call(currentPOWHeader);
        }
    }

    public synchronized BlockHeader getCurrentPOWHeader()
    {
        return currentPOWHeader;
    }

    public synchronized void addPool(BlockHeader header)
    {
        waitingHeaders.add(header);
    }

    @Override
    public void onGenerateBlock(BlockHeader header)
    {
        //TODO: 테스트용 코드 삭제, 클라이언트 마이너 풀한테 broadcast
        Thread cl1 = new BlockMinerClient(0);
        Thread cl2 = new BlockMinerClient(1);

        cl1.start();
        cl2.start();
    }

    @Override
    public void onFinishedPOW(BlockHeader header)
    {

    }
}
