package com.gitee.osinn.boot.log.entity.base;

import com.gitee.osinn.boot.log.dto.SysLogUserInfoDTO;
import com.gitee.osinn.boot.log.enums.BusinessType;
import com.gitee.osinn.boot.log.enums.SysLogSourceEnum;
import com.gitee.osinn.boot.log.enums.SysLogTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wency_cai
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysLogSource extends SysLogUserInfoDTO {

    /**
     * 日志来源
     */
    private SysLogSourceEnum source;

    /**
     * 日志类型
     */
    private SysLogTypeEnum logType;

    /**
     * 动作描述
     */
    private String actionDesc;

    /**
     * 日志模块(value)
     * module和moduleName属于value-label 关系
     */
    private String module;

    /**
     * 日志模块名称(label)
     */
    private String moduleName;

    /**
     * 业务操作
     */
    private BusinessType businessType;
}