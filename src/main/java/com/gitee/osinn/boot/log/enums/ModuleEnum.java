package com.gitee.osinn.boot.log.enums;

import lombok.Getter;

/**
 * @author wency_cai
 */
@Getter
public enum ModuleEnum {

    /**
     * 登录
     */
    SYS_LOGIN("登录系统"),

    /**
     * 注销
     */
    SYS_LOGOUT("退出登录");

    private final String label;

    ModuleEnum(final String label) {
        this.label = label;
    }

}
