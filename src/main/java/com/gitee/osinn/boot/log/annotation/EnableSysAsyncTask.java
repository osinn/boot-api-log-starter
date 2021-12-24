package com.gitee.osinn.boot.log.annotation;

import com.gitee.osinn.boot.log.config.AsyncTaskConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用线程池配置
 *
 * @author wency_cai
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AsyncTaskConfig.class)
public @interface EnableSysAsyncTask {
}
