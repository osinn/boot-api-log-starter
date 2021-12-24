package com.gitee.osinn.boot.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "log.async.task")
public class AsyncTaskProperties {
    /**
     * 线程池size
     */
    private int corePoolSize = 10;

    /**
     * 线程池最大数量
     */
    private int maxPoolSize = 20;

    /**
     * 队列最大长度
     */
    private int queueCapacity = 10;

    /**
     * 线程池名称
     */
    private String threadNamePrefix = "thread-com-gitee-osinn-boot-sys-log-";

}
