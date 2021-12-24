package com.gitee.osinn.boot.log.dto;

import com.gitee.osinn.boot.log.enums.SysLogStatusEnum;
import com.gitee.osinn.boot.log.entity.base.SysLogSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 系统日志保存DTO
 *
 * @author wency_cai
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLogDTO extends SysLogSource implements Serializable {

    private static final long serialVersionUID = 1L;

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
}
