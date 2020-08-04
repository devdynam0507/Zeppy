package kr.ndy.core.blockchain.generator;

import kr.ndy.core.blockchain.BlockHeader;
import kr.ndy.core.blockchain.observer.IBlockObserver;

import java.util.LinkedList;
import java.util.Queue;

public class BlockMiningPool implements IBlockObserver {

    private Queue<BlockHeader> waitingHeaders;
    private BlockHeader currentPOWHeader; //작업증명중인 블럭
    private byte[] previousHash;
    private Thread testThread;

    public BlockMiningPool()
    {
        waitingHeaders = new LinkedList<>();
    }

    public synchronized BlockHeader getCurrentPOWHeader()
    {
        return currentPOWHeader;
    }

    public synchronized void complete(byte[] hash)
    {
        if(currentPOWHeader != null)
        {
            currentPOWHeader = null;
            previousHash = hash;
        }
    }

    public synchronized void addPool(BlockHeader header)
    {
        waitingHeaders.add(header);
    }

    private void createTestThread()
    {
        if(testThread == null)
        {
            testThread = new BlockMinerClient(0);
            testThread.start();
        }
    }

    @Override
    public void onGenerateBlock(BlockHeader header)
    {
        if(currentPOWHeader != null)
        {
            addPool(header);
        } else
        {
            if(waitingHeaders.size() > 0)
            {
                currentPOWHeader = waitingHeaders.remove();
            }else
            {
                currentPOWHeader = header;
            }
        }

        createTestThread();
        //TODO: 테스트용 코드 삭제, 클라이언트 마이너한테 broadcast
    }

    @Override
    public void onFinishedPOW(BlockHeader header)
    {

    }
}
