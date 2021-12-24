package com.gitee.osinn.boot.log.service;

import cn.hutool.extra.spring.SpringUtil;
import com.gitee.osinn.boot.log.dto.*;
import com.gitee.osinn.boot.log.entity.SysLogEntity;
import com.gitee.osinn.boot.log.enums.SysLogStatusEnum;
import com.gitee.osinn.boot.log.enums.SysLogTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 日志服务接口，如果不自己实现，系统只处理日志入库接口，用户信息不保存
 *
 * @author wency_cai
 */
public interface ISysLogService {

    Logger log = LoggerFactory.getLogger(ISysLogService.class);

    HandleLogService handleLogService = SpringUtil.getBean(HandleLogService.class);


    /**
     * 业务接口从请求头获取用户信息
     * AOP日志记录用户信息
     * 此接口由自己实现方实现功能，返回用户基础信息
     *
     * @return
     */
    SysLogUserInfoDTO getSysLogUserInfoDTO();

    /**
     * 登录的时候只能通过token获取用户信息
     * 登录/退出日志记录用户信息
     * 此接口由自己实现方实现功能，返回用户基础信息
     * 此接口被 recordLogininfor(...)调用
     *
     * @return
     */
    SysLogUserInfoDTO getSysLogUserInfoByToken(String token);

    /**
     * 日志入库
     *
     * @param sysLogDTO
     * @return
     */
    default void saveSysLog(SysLogDTO sysLogDTO) {
        handleLogService.saveSysLog(sysLogDTO);
    }

    /**
     * 分页查询日志
     *
     * @param sysLogQueryDTO
     * @return
     */
    default PageDTO<SysLogEntity> fetchList(SysLogQueryDTO sysLogQueryDTO) {
        PageDTO<SysLogEntity> sysLogEntityPageDTO = handleLogService.fetchList(sysLogQueryDTO);
        return sysLogEntityPageDTO;
    }

    /**
     * 获取日志详情
     *
     * @param id
     * @return
     */
    default SysLogEntity fetchDetailsById(Long id) {
        return handleLogService.fetchDetailsById(id);
    }

    /**
     * 根据日志类型获取日志系统模块
     *
     * @param logType
     * @return
     */
    default List<ModuleNameDTO> fetchModuleNameAll(SysLogTypeEnum logType) {
        return handleLogService.fetchModuleNameAll(logType);
    }

    /**
     * 记录登录/退出登录信息
     *
     * @param moduleName    日志模块名称(label)
     * @param module        日志模块(value)
     * @param status
     * @param usAgent
     * @param ipAddress
     * @param executionTime 执行耗时，单位毫秒，可选
     * @param token
     */
    default void recordLogininfor(String moduleName,
                                  String module,
                                  SysLogStatusEnum status,
                                  String usAgent,
                                  String ipAddress,
                                  BigDecimal executionTime,
                                  String token) {
        handleLogService.recordLogininfor(moduleName,
                module,
                status,
                usAgent,
                ipAddress,
                executionTime,
                token);
    }
}
