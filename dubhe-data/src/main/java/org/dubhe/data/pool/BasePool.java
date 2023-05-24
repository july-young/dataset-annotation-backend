

package org.dubhe.data.pool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @description 线程池

 */
@Component
public class BasePool {

    private ThreadPoolExecutor executorService;
    @Value("${basepool.corePoolSize:40}")
    private Integer corePoolSize;
    @Value("${basepool.maximumPoolSize:60}")
    private Integer maximumPoolSize;
    @Value("${basepool.keepAliveTime:120}")
    private Integer keepAliveTime;
    @Value("${basepool.blockQueueSize:10}")
    private Integer blockQueueSize;

    @PostConstruct
    public void init() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(blockQueueSize);
        ThreadFactory threadFactory = new DefaultThreadFactoryImpl();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                workQueue, threadFactory, handler);
    }

    public ThreadPoolExecutor getExecutor() {
        return executorService;
    }

}
