package com.gitee.osinn.boot.log.starter;

import com.gitee.osinn.boot.log.service.Ip2RegionOfSearcher;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Ip2RegionProperties
 *
 * @author wency_cai
 */
@Data
@ConfigurationProperties(prefix = Ip2RegionProperties.PREFIX)
public class Ip2RegionProperties {
    public static final String PREFIX = "ip2region";

    /**
     * 是否开启自动配置
     */
    private boolean enabled = true;

    /**
     * db数据文件位置
     */
    private String dbFile = "ip2region/ip2region.db";

    /**
     * 查询算法
     */
    private Ip2RegionOfSearcher.ALGORITHM algorithm = Ip2RegionOfSearcher.ALGORITHM.BTREE_SEARCH;

    /**
     * 如果基于通过网络api接口查询，可以配置请求地址
     */
    private String ipUrl = "http://whois.pconline.com.cn/ipJson.jsp";

    /**
     * 如果基于通过网络api接口查询，可以配置get请求参数
     */
    private Map<String, Object> params = initParam();

    public Map<String, Object> initParam() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("json", true);
        return params;
    }
}
