package com.gitee.osinn.boot.log.dto;

import com.gitee.osinn.boot.log.entity.base.SysLogSource;
import com.gitee.osinn.boot.log.enums.SysLogStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 查询日志DTO
 *
 * @author wency_cai
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysLogQueryDTO extends SysLogSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 每页条数
     */
    private long pageSize;

    /**
     * 当前页，从0开始
     */
    private long pageNum;

    /**
     * 日志状态 0成功；1失败
     */
    private SysLogStatusEnum status;

}