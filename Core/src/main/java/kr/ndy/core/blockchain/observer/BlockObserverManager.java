package kr.ndy.core.blockchain.observer;

import kr.ndy.core.blockchain.BlockHeader;

import java.util.ArrayList;
import java.util.List;

public class BlockObserverManager {

    private List<IBlockObserver> observers;

    public BlockObserverManager()
    {
        this.observers = new ArrayList<>();
    }

    public void addObserver(IBlockObserver observer)
    {
        observers.add(observer);
    }

    public void call(BlockHeader header)
    {
        observers.forEach(observer -> observer.onGenerateBlock(header));
    }

    public void finish(BlockHeader header)
    {
        observers.forEach(observer -> observer.onFinishedPOW(header));
    }

}
