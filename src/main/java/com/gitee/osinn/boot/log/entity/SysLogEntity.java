package com.gitee.osinn.boot.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.osinn.boot.log.entity.base.SysLogSource;
import com.gitee.osinn.boot.log.enums.SysLogStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * sys_log 系统操作日志表
 *
 * @author wency_cai
 */
@Getter
@Setter
public class SysLogEntity extends SysLogSource {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * ip地址归属地
     */
    private String ipAddressAttr;

    /**
     * 请求uri
     */
    private String requestUri;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 操作方法
     */
    private String classMethod;

    /**
     * 状态 1.成功 2.失败
     */
    private SysLogStatusEnum status;

    /**
     * 执行时间（秒）
     */
    private BigDecimal executionTime;

    /**
     * 异常信息
     */
    private String exceptionMsg;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
