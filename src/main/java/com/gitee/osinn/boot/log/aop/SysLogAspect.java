package com.gitee.osinn.boot.log.aop;

import cn.hutool.extra.servlet.ServletUtil;
import com.gitee.osinn.boot.log.dto.SysLogUserInfoDTO;
import com.gitee.osinn.boot.log.service.HandleLogService;
import com.gitee.osinn.boot.log.service.ISysLogService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.gitee.osinn.boot.log.dto.SysLogDTO;
import com.gitee.osinn.boot.log.enums.SysLogStatusEnum;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 系统日志AOP
 *
 * @author wency_cai
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private ISysLogService sysLogService;

    @Autowired
    private HandleLogService handleLogService;

    @Pointcut("@annotation(com.gitee.osinn.boot.log.annotation.SysLog)")
    public void log() {
    }

    @SneakyThrows
    @Around("log()")
    public Object handleLog(ProceedingJoinPoint joinPoint) {
        // 执行目标方法，并且计算运行时间
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        BigDecimal time = new BigDecimal(endTime).subtract(new BigDecimal(startTime));

        setLog(joinPoint, SysLogStatusEnum.SUCCESS, null, time);

        // 返回结果
        return result;
    }

    @AfterThrowing(throwing = "ex", pointcut = "log()")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        setLog(joinPoint, SysLogStatusEnum.FAIL, ex.getMessage(), null);
    }

    private void setLog(JoinPoint joinPoint, SysLogStatusEnum sysLogStatusEnum, String exceptionMsg, BigDecimal executionTime) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取IP地址
        String ipAddress = ServletUtil.getClientIP(request);
        SysLogDTO sysLog = new SysLogDTO();
        sysLog.setStatus(sysLogStatusEnum);
        sysLog.setExceptionMsg(exceptionMsg);
        sysLog.setExecutionTime(executionTime);

        // 设置用户信息
        SysLogUserInfoDTO sysLogUserInfoDTO = sysLogService.getSysLogUserInfoDTO();
        if (sysLogUserInfoDTO != null) {
            sysLog.setUserId(sysLogUserInfoDTO.getUserId());
            sysLog.setAccount(sysLogUserInfoDTO.getAccount());
            sysLog.setNickname(sysLogUserInfoDTO.getNickname());
        }

        String requestUri = request.getRequestURI();
        String headerUserAgent = request.getHeader("User-Agent");
        // 异步保存系统日志
        handleLogService.handleLog(joinPoint, sysLog, ipAddress, requestUri, request.getMethod(), headerUserAgent);
    }

}
