package com.reece.platform.accounts.config;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("${threadPool.corePoolSize:100}")
    private int corePoolSize;

    @Value("${threadPool.maxPoolSize:200}")
    private int maxPoolSize;

    @Value("${threadPool.queueCapacity:250}")
    private int queueCapacity;

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("TaskExecutorThread-");
        executor.initialize();
        return executor;
    }
}
