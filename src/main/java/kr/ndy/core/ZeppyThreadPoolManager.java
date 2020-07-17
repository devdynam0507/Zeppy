package kr.ndy.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ZeppyThreadPoolManager {

    private static ZeppyThreadPoolManager instance;

    private ScheduledExecutorService service;

    private ZeppyThreadPoolManager()
    {
        service = Executors.newScheduledThreadPool(2);
    }

    public static synchronized ZeppyThreadPoolManager getInstance()
    {
        if(instance == null)
        {
            instance = new ZeppyThreadPoolManager();
        }

        return instance;
    }

    public void service(Thread thread, long delay, long period, TimeUnit timeUnit)
    {
        service.scheduleAtFixedRate(thread, delay, period, timeUnit);
    }

}
