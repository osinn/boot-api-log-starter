package com.gitee.osinn.boot.log.annotation;

import cn.hutool.extra.spring.SpringUtil;
import com.gitee.osinn.boot.log.starter.Ip2RegionOfAutoConfiguration;
import com.gitee.osinn.boot.log.starter.LogAutoConfiguration;
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
