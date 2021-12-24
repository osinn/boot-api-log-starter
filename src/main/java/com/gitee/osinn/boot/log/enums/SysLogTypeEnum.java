package com.gitee.osinn.boot.log.enums;


/**
 * 日志类型
 *
 * @author wency_cai
 */
public enum SysLogTypeEnum {

    LOGIN(1, "登录"),

    REQUEST(2, "请求接口"),

    UNKNOWN(3, "未知");

    private final Integer value;
    private final String text;

    SysLogTypeEnum(final Integer value, final String text) {
        this.value = value;
        this.text = text;
    }

}