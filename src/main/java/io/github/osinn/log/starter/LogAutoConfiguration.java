package io.github.osinn.log.starter;

import io.github.osinn.log.aop.SysLogAspect;
import io.github.osinn.log.mapper.SysLogMapper;
import io.github.osinn.log.service.HandleLogService;
import io.github.osinn.log.service.ISysLogService;
import io.github.osinn.log.service.SysLogServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 系统日志自动配置类
 *
 * @author wency_cai
 */
@MapperScan(basePackageClasses = SysLogMapper.class)
public class LogAutoConfiguration {

    @Bean
    public HandleLogService handleLogService() {
        return new HandleLogService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysLogService sysLogService() {
        return new SysLogServiceImpl();
    }

    @Bean
    @ConditionalOnBean(value = ISysLogService.class)
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }


}
