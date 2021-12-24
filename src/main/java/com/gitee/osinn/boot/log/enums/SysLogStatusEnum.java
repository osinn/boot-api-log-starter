package com.gitee.osinn.boot.log.enums;


/**
 * 系统日志状态枚举
 *
 * @author wency_cai
 */
public enum SysLogStatusEnum {

    SUCCESS(1, "成功"),
    FAIL(2, "失败");

    private final Integer value;
    private final String text;

    SysLogStatusEnum(final Integer value, final String text) {
        this.value = value;
        this.text = text;
    }

}