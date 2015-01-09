package com.github.kylefeng.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class Exec {
    private static final ExecutorService THE_EXEC = Executors
                                                      .newFixedThreadPool(
                                                          Runtime.getRuntime()
                                                              .availableProcessors() * 2 + 42,
                                                          countedThreadFactory("async-dispatch-%d",
                                                              true));

    public static final Executor         executor = threadPoolExecutor();

    public static void run(Runnable r) {
        executor.exec(r);
    }

    public static Executor threadPoolExecutor() {
        return new Executor() {
            @Override
            public void exec(Runnable runnable) {
                THE_EXEC.execute(runnable);
            }
        };
    }

    private static final AtomicLong counter = new AtomicLong(0);

    public static ThreadFactory countedThreadFactory(final String nameFormat, final boolean isDaemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread();
                t.setName(String.format(nameFormat, counter.incrementAndGet()));
                t.setDaemon(isDaemon);
                return t;
            }
        };
    }

}
