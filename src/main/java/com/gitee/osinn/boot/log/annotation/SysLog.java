package com.gitee.osinn.boot.log.annotation;

import com.gitee.osinn.boot.log.enums.BusinessType;
import com.gitee.osinn.boot.log.enums.SysLogSourceEnum;
import com.gitee.osinn.boot.log.enums.SysLogTypeEnum;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author wency_cai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 描述
     */
    String actionDesc();

    /**
     * 模块
     *
     * @return
     */
    String module();

    /**
     * 日志模块名称(label)
     */
    String moduleName();

    /**
     * 日志来源
     *
     * @return
     */
    SysLogSourceEnum source() default SysLogSourceEnum.WEB;

    /**
     * 日志类型
     *
     * @return
     */
    SysLogTypeEnum logType() default SysLogTypeEnum.REQUEST;

    /**
     * 业务操作
     */
    BusinessType businessType() default BusinessType.OTHER;
}