package com.gitee.osinn.boot.log.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.StringJoiner;

/**
 * 获取IP地址归属地
 *
 * @author wency_cai
 */
@Slf4j
public class AddressUtils {

    /**
     * 如果基于通过网络api接口查询，可以配置请求地址
     */
    public static String ipUrl;

    /**
     * 如果基于通过网络api接口查询，可以配置请求参数
     */
    public static Map<String, Object> params;

    private static String param;


    public static String getRealAddressByIp(String ip) {
        // 内网不查询
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip);
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = HttpUtil.createGet(ipUrl)
                    .body("ip=" + ip + "&" + getParams())
                    .execute()
                    .body();
            if (StringUtils.isEmpty(rspStr)) {
                log.error("获取地理位置异常 {}", ip);
                return null;
            }
            Map<String, String> obj = JSONUtil.toBean(rspStr, Map.class);
            String region = obj.get("pro");
            String city = obj.get("city");
            String addr = obj.get("addr");
            if (region.equals(city)) {
                addr = addr.replace(city, "").replace(" ", "");
                addr = String.format("%s %s", city, addr);
            } else {
                addr = addr.replace(region + city, "").replace(" ", "");
                addr = String.format("%s %s %s", region, city, addr);
            }
            return addr.replace("移通", "移动");
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
        return "未知";
    }

    private static String getParams() {
        if (StringUtils.isEmpty(param)) {
            StringJoiner stringJoiner = new StringJoiner("&");
            params.forEach((key, value) -> {
                stringJoiner.add(key + "=" + value);
            });
            param = stringJoiner.toString();
            return param;
        } else {
            return param;
        }
    }
}
