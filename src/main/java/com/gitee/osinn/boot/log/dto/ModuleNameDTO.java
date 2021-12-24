package com.gitee.osinn.boot.log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wency_cai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleNameDTO implements Serializable {

    /**
     * 日志模块
     * module和moduleName属于value-label 关系
     */
    private String module;

    /**
     * 日志模块名称(label)
     */
    private String moduleName;
}
