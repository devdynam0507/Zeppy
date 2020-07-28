package kr.ndy.core;

import kr.ndy.core.blockchain.BlockChain;
import kr.ndy.core.blockchain.BlockFileIO;
import kr.ndy.core.blockchain.generator.BlockGenerator;
import kr.ndy.core.blockchain.generator.BlockGeneratorTask;
import kr.ndy.core.blockchain.generator.BlockMiningPool;
import kr.ndy.core.blockchain.observer.BlockObserverManager;

import java.util.concurrent.TimeUnit;

public class ZeppyModule {

    private static ZeppyModule instance;

    private BlockGenerator generator;
    private BlockGeneratorTask generatorTask;
    private BlockMiningPool miningPool;
    private BlockObserverManager blockObserverManager;
    private BlockChain blockChain;
    private BlockFileIO blockIO;

    private ZeppyModule()
    {}

    public synchronized static ZeppyModule getInstance()
    {
        if(instance == null)
        {
            instance = new ZeppyModule();
            instance.initialize();
            instance.registerObservers();
        }

        return instance;
    }

    private void initialize()
    {
        this.generator = new BlockGenerator();
        this.generatorTask = new BlockGeneratorTask(generator);
        this.miningPool = new BlockMiningPool();
        this.blockObserverManager = new BlockObserverManager();
        this.blockIO = new BlockFileIO();
    }

    public void executeModuleTasks()
    {
        ZeppyThreadPoolManager.getInstance().service(generatorTask, 0, 1, TimeUnit.SECONDS);
        ZeppyThreadPoolManager.getInstance().service(miningPool, 0, 1, TimeUnit.SECONDS);
    }

    public void registerObservers()
    {
        blockObserverManager.addObserver(miningPool);
    }

    public BlockGenerator getBlockGenerator() { return generator; }
    public BlockObserverManager getBlockObserverManager() { return blockObserverManager; }

    ////////////////////////////////////////////// Threads //////////////////////////////////////////////
    public BlockMiningPool getMiningPool() { return miningPool; }
    public BlockGeneratorTask getGeneratorTask() { return generatorTask; }

}
