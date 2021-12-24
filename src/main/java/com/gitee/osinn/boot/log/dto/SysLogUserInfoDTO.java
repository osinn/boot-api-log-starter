package com.gitee.osinn.boot.log.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统日志保存用户基础信息DTO
 *
 * @author wency_cai
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysLogUserInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 操作人员
     */
    private String nickname;
}
