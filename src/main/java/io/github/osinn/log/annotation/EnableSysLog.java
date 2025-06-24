package io.github.osinn.log.annotation;

import cn.hutool.extra.spring.SpringUtil;
import io.github.osinn.log.starter.Ip2RegionOfAutoConfiguration;
import io.github.osinn.log.starter.LogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用系统日志
 *
 * @author wency_cai
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LogAutoConfiguration.class, SpringUtil.class, Ip2RegionOfAutoConfiguration.class})
public @interface EnableSysLog {

}
