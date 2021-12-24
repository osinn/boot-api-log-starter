package com.gitee.osinn.boot.log.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 异步线程池配置类
 *
 * @author wency_cai
 */
@EnableAsync
@Configuration
@EnableConfigurationProperties(AsyncTaskProperties.class)
public class AsyncTaskConfig {

    @Autowired
    private AsyncTaskProperties asyncTaskProperties;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncTaskProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncTaskProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncTaskProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asyncTaskProperties.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
