package com.gitee.osinn.boot.log.enums;


import lombok.Getter;

/**
 * 系统日志来源
 *
 * @author wency_cai
 */
@Getter
public enum SysLogSourceEnum {

    /***/
    PC(1, "PC端"),
    WEB(1, "后台管理系统"),
    MP(2, "小程序平台"),
    H5(3, "H5移动端平台"),
    APP(4, "应用平台"),
    IOS(4, "苹果平台"),
    UNKNOWN(3, "未知");

    private final Integer value;
    private final String text;

    SysLogSourceEnum(final Integer value, final String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String toString() {
        return this.name() + ":" + this.text;
    }
}