package kr.ndy.core;

import kr.ndy.core.blockchain.generator.BlockGenerator;
import kr.ndy.core.blockchain.generator.BlockGeneratorTask;
import kr.ndy.core.blockchain.generator.BlockMiningPool;

import java.util.concurrent.TimeUnit;

public class ZeppyModule {

    private static ZeppyModule instance;

    private BlockGenerator generator;
    private BlockGeneratorTask generatorTask;
    private BlockMiningPool miningPool;

    private ZeppyModule()
    {}

    public synchronized static ZeppyModule getInstance()
    {
        if(instance == null)
        {
            instance = new ZeppyModule();
            instance.initialize();
        }

        return instance;
    }

    private void initialize()
    {
        this.generator = new BlockGenerator();
        this.generatorTask = new BlockGeneratorTask(generator);
        this.miningPool = new BlockMiningPool();
    }

    public void executeModuleTasks()
    {
        ZeppyThreadPoolManager.getInstance().service(generatorTask, 0, 1, TimeUnit.SECONDS);
        ZeppyThreadPoolManager.getInstance().service(miningPool, 0, 1, TimeUnit.SECONDS);
    }

    public BlockGenerator getBlockGenerator() { return generator; }

    ////////////////////////////////////////////// Threads //////////////////////////////////////////////
    public BlockMiningPool getMiningPool() { return miningPool; }
    public BlockGeneratorTask getGeneratorTask() { return generatorTask; }

}
