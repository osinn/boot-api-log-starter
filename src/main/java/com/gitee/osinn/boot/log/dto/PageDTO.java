package com.gitee.osinn.boot.log.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 描述
 *
 * @author wency_cai
 */
@Getter
@Setter
@ToString
public class PageDTO<T> {

    private long total;

    private long size;

    private long current;

    private List<T> data;

}
