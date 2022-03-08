package com.gitee.osinn.boot.log.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * ip 信息详情
 *
 * @author L.cm
 */
@Data
public class IpInfo implements Serializable {

    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 国家
     */
    private String country;
    /**
     * 区域
     */
    private String region;
    /**
     * 省
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 运营商
     */
    private String isp;
    /**
     * region ptr in the db file
     */
    private int dataPtr;

    /**
     * 拼接完整的地址
     *
     * @return address
     */
    public String getAddress() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(region);
        regionSet.add(province);
        regionSet.add(city);
        regionSet.removeIf(Objects::isNull);
        StringJoiner stringJoiner = new StringJoiner(" ");
        regionSet.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    /**
     * 拼接完整的地址
     *
     * @return address
     */
    public String getAddressAndIsp() {
        Set<String> regionSet = new LinkedHashSet<>();
        regionSet.add(country);
        regionSet.add(region);
        regionSet.add(province);
        regionSet.add(city);
        regionSet.add(isp);
        regionSet.removeIf(Objects::isNull);

        StringJoiner stringJoiner = new StringJoiner(" ");
        regionSet.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}