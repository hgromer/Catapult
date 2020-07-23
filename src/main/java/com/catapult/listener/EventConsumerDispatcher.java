package com.catapult.listener;

import java.util.concurrent.*;

public class EventConsumerDispatcher extends ThreadPoolExecutor {
    public EventConsumerDispatcher() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Catapult Event Consumer Dispatch Thread");
                thread.setDaemon(true);
                return thread;
            }
        });
    }
}
