package kr.ndy.core.blockchain.generator;

import kr.ndy.core.blockchain.BlockHeader;

import java.util.LinkedList;
import java.util.Queue;

public class BlockMiningPool extends Thread {

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
        }

        System.out.println("mining pool");
    }

    public synchronized void addPool(BlockHeader header)
    {
        waitingHeaders.add(header);
    }
}
