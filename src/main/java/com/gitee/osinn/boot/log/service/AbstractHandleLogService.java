package com.gitee.osinn.boot.log.service;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.gitee.osinn.boot.log.dto.*;
import com.gitee.osinn.boot.log.entity.SysLogEntity;
import com.gitee.osinn.boot.log.enums.BusinessType;
import com.gitee.osinn.boot.log.enums.SysLogSourceEnum;
import com.gitee.osinn.boot.log.enums.SysLogStatusEnum;
import com.gitee.osinn.boot.log.enums.SysLogTypeEnum;
import com.gitee.osinn.boot.log.mapper.SysLogMapper;
import com.gitee.osinn.boot.log.utils.SnowflakeIdWorker;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author wency_cai
 */
public abstract class AbstractHandleLogService {

    Logger log = LoggerFactory.getLogger(AbstractHandleLogService.class);

    @Autowired(required = false)
    protected SysLogMapper sysLogMapper;

    @Autowired
    protected Ip2regionSearcher regionSearcher;

    @Autowired
    @Lazy
    protected ISysLogService sysLogService;

    public void saveSysLog(SysLogDTO sysLogDTO) {
        SysLogEntity sysLogEntity = new SysLogEntity();
        BeanUtils.copyProperties(sysLogDTO, sysLogEntity);
        sysLogEntity.setCreatedTime(LocalDateTime.now());

        if (sysLogEntity.getId() == null) {
            synchronized (this) {
                SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
                sysLogEntity.setId(idWorker.nextId());
            }
        }

        sysLogMapper.saveSysLog(sysLogEntity);
    }

    public PageDTO<SysLogEntity> fetchList(SysLogQueryDTO sysLogQueryDTO) {
        List<SysLogEntity> sysLogEntities = sysLogMapper.selectPage(sysLogQueryDTO);
        PageDTO<SysLogEntity> pageDTO = new PageDTO<>();
        pageDTO.setData(sysLogEntities);
        pageDTO.setSize(sysLogQueryDTO.getPageSize());
        pageDTO.setCurrent(sysLogQueryDTO.getPageNum());
        pageDTO.setTotal(sysLogMapper.selectPageCount(sysLogQueryDTO));
        return pageDTO;
    }

    public SysLogEntity fetchDetailsById(Long id) {
        return sysLogMapper.selectById(id);
    }

    public List<ModuleNameDTO> fetchModuleNameAll(SysLogTypeEnum logType) {
        if (logType == null) {
            return new ArrayList<>();
        }
        List<SysLogEntity> sysLogEntities = sysLogMapper.selectList(logType);
        List<ModuleNameDTO> moduleNameDTOList = new ArrayList<>();
        sysLogEntities.forEach(sysLogEntity -> {
            moduleNameDTOList.add(new ModuleNameDTO(sysLogEntity.getModule(), sysLogEntity.getModuleName()));
        });
        return moduleNameDTOList;
    }

    @Async
    public void recordLogininfor(String moduleName,
                                 String module,
                                 SysLogStatusEnum status,
                                 String usAgent,
                                 String ipAddress,
                                 BigDecimal executionTime,
                                 String token) {
        log.debug("当前线程：{}", Thread.currentThread().getName());
        UserAgent userAgent = UserAgentUtil.parse(usAgent);
//
//        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
//        // 获取IP地址
//        String ipAddress = ServletUtil.getClientIP(request);

        // 封装对象
        SysLogDTO sysLog = new SysLogDTO();
        sysLog.setBrowser(userAgent.getBrowser().getName());
        sysLog.setOs(userAgent.getPlatform().getName() + " " + userAgent.getOs().getName());
        // 设置用户信息
        SysLogUserInfoDTO sysLogUserInfoDTO = sysLogService.getSysLogUserInfoByToken(token);
        if (sysLogUserInfoDTO != null) {
            sysLog.setUserId(sysLogUserInfoDTO.getUserId());
            sysLog.setAccount(sysLogUserInfoDTO.getAccount());
            sysLog.setNickname(sysLogUserInfoDTO.getNickname());
        }

        sysLog.setStatus(status);
        sysLog.setIpAddress(ipAddress);

        /**
         * 本地IP数组
         */
        List<String> LOCAL_HOST_IPs = new ArrayList<String>() {
            {
                add("0:0:0:0:0:0:0:1");
                add("localhost");
                add("127.0.0.1");
            }
        };
        IpInfo ipInfo = null;
        try {
            ipInfo = regionSearcher.btreeSearch(ipAddress);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String address = ipInfo == null ? "未知" : ipInfo.getAddressAndIsp().replace("中国", "");
        ipAddress = LOCAL_HOST_IPs.contains(ipAddress) ? "127.0.0.1" : ipAddress;
        sysLog.setIpAddress(ipAddress);
        sysLog.setIpAddressAttr(address);
        sysLog.setActionDesc("登录系统");
        sysLog.setSource(SysLogSourceEnum.WEB);
        sysLog.setLogType(SysLogTypeEnum.LOGIN);
        sysLog.setModule(module);
        sysLog.setModuleName(moduleName);
        sysLog.setBusinessType(BusinessType.GRANT);
        sysLog.setExecutionTime(executionTime);
        // 保存日志
        this.saveSysLog(sysLog);
    }
}
