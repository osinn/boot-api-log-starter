package io.github.osinn.log.starter;

import io.github.osinn.log.service.Ip2RegionOfSearcher;
import io.github.osinn.log.utils.AddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;


/**
 * @author wency_cai
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(Ip2RegionProperties.class)
@ConditionalOnClass(DbSearcher.class)
public class Ip2RegionOfAutoConfiguration {

//    @Bean
//    @ConditionalOnProperty(prefix = Ip2RegionProperties.PREFIX, name = "enabled", havingValue = "true")
//    public Ip2RegionOfSearcher ip2RegionOfnSearcher(Ip2RegionProperties properties) {
//        AddressUtils.ipUrl = properties.getIpUrl();
//        AddressUtils.params = properties.getParams();
//        try (InputStream inputStream = ClassLoaderUtil.getClassLoader().getResourceAsStream(properties.getDbFile())) {
//            DbSearcher dbSearcher = null;
//            if (!Ip2RegionOfSearcher.ALGORITHM.MEMORY_SEARCH.equals(properties.getAlgorithm())) {
//                String rootPath;
//                File targetFile;
//                rootPath = System.getProperty("user.dir");
//                targetFile = new File(rootPath + "/ip2region/ip2region.db");
//                if (!targetFile.isFile()) {
//                    targetFile.getParentFile().mkdirs();
//                    Files.copy(inputStream, Paths.get(targetFile.getAbsolutePath()));
//                }
//                dbSearcher = new DbSearcher(new DbConfig(), targetFile.getAbsolutePath());
//            } else {
//                byte[] bytes = IoUtil.readBytes(inputStream);
//                dbSearcher = new DbSearcher(new DbConfig(), bytes);
//            }
//            return new Ip2RegionOfSearcher(dbSearcher, properties.getAlgorithm());
//        } catch (DbMakerConfigException e) {
//            log.warn(e.getMessage());
//        } catch (Exception e) {
//            log.warn("Ip2RegionOfSearcher can not find db file in the path [" + properties.getDbFile() + "]");
//        }
//        return null;
//    }

    @Bean
    @ConditionalOnProperty(prefix = Ip2RegionProperties.PREFIX, name = "enabled", havingValue = "true")
    public Ip2RegionOfSearcher ip2RegionOfnSearcher(Ip2RegionProperties properties) {
        AddressUtils.ipUrl = properties.getIpUrl();
        AddressUtils.params = properties.getParams();
        try {
            return new Ip2RegionOfSearcher(properties.getAlgorithm());
        } catch (FileNotFoundException e) {
            log.warn("Ip2RegionOfSearcher can not find db file in the path [" + properties.getDbFile() + "]");
        } catch (DbMakerConfigException e) {
            log.warn(e.getMessage());
        }

        return null;
    }
}
