package com.gitee.osinn.boot.log.service;

import cn.hutool.core.io.IoUtil;
import com.gitee.osinn.boot.log.dto.IpInfo;
import com.gitee.osinn.boot.log.utils.IpInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * @author wency_cai
 */
@Slf4j
public class Ip2RegionOfSearcher {

    private DbSearcher dbSearcher;

    private final ALGORITHM algorithm;

    public Ip2RegionOfSearcher(DbSearcher dbSearcher, ALGORITHM algorithm) {
        this.dbSearcher = dbSearcher;
        this.algorithm = algorithm;
    }

    public Ip2RegionOfSearcher(ALGORITHM algorithm) throws FileNotFoundException, DbMakerConfigException {
        this.algorithm = algorithm;
        this.existIp2regionDb();
    }

    public IpInfo search(String ip) {
        return search(ip, algorithm);
    }

    public IpInfo search(String ip, ALGORITHM algorithm) {

        DataBlock dataBlock = null;
        try {
            this.existIp2regionDb();
            switch (algorithm) {
                case BTREE_SEARCH:
                    dataBlock = this.dbSearcher.btreeSearch(ip);
                    break;
                case BINARY_SEARCH:
                    dataBlock = this.dbSearcher.binarySearch(ip);
                    break;
                case MEMORY_SEARCH:
                    dataBlock = this.dbSearcher.memorySearch(ip);
                    break;
                default:
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return IpInfoUtil.toIpInfo(dataBlock);
    }

    private void existIp2regionDb() throws DbMakerConfigException, FileNotFoundException {
        String dbPath = Ip2RegionOfSearcher.class.getResource("/ip2region/ip2region.db").getPath();
        File file = new File(dbPath);

        if (!file.exists()) {
            log.warn("资源目录ip地址库文件不存在");
            String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
            dbPath = tmpDir + File.separator + "ip2region.db";
            log.warn("加载临时路径下的ip地址库文件:{}", dbPath);
            file = new File(dbPath);
            if (!file.exists() || (System.currentTimeMillis() - file.lastModified() > 86400000L)) {
                log.warn("临时路径ip地址库文件不存在或者ip地址库文件存在时间超过1天，重新创建临时文件...");
                try {
                    InputStream inputStream = new ClassPathResource("ip2region/ip2region.db").getInputStream();
                    IoUtil.copy(inputStream, new FileOutputStream(file));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        if (this.dbSearcher == null) {
            DbConfig config = new DbConfig();
            this.dbSearcher = new DbSearcher(config, dbPath);
        }
    }

    public ALGORITHM getAlgorithm() {
        return algorithm;
    }

    /**
     * 查询算法
     */
    public enum ALGORITHM {

        /**
         * b-tree算法
         */
        BTREE_SEARCH,

        /**
         * binary算法
         */
        BINARY_SEARCH,

        /**
         * memory算法
         */
        MEMORY_SEARCH,

    }
}
