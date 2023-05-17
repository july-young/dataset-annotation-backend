

package org.dubhe.data.pool;

import org.dubhe.biz.base.constant.MagicNumConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * @description 定时任务线程池
 * @date 2020-05-29
 */
@Configuration
@EnableAsync
public class SchduledThreadPool {

    @Bean
    public Executor executor() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("async-scheduled-pool-");
        taskScheduler.setPoolSize(MagicNumConstant.FIVE);
        return taskScheduler;
    }

}
