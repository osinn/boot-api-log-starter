package com.gitee.osinn.boot.log.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.osinn.boot.log.annotation.SysLog;
import com.gitee.osinn.boot.log.dto.IpInfo;
import com.gitee.osinn.boot.log.dto.SysLogDTO;
import com.gitee.osinn.boot.log.entity.base.SysLogSource;
import com.gitee.osinn.boot.log.starter.Ip2RegionProperties;
import com.gitee.osinn.boot.log.utils.AddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class HandleLogService extends AbstractHandleLogService {


    @Autowired(required = false)
    private Ip2RegionOfSearcher ip2RegionOfSearcher;

    @Autowired
    private Ip2RegionProperties ip2RegionProperties;

    @Async
    public void handleLog(JoinPoint joinPoint, SysLogDTO sysLog, String ipAddress, String requestUri, String requestType, String headerUserAgent) {

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

        String address;

        if (!ip2RegionProperties.isEnabled()) {
            address = StringUtils.isEmpty(ip2RegionProperties.getIpUrl()) ? "未知" : AddressUtils.getRealAddressByIp(ipAddress);
        } else {
            IpInfo ipInfo = null;
            try {
                ipInfo = ip2RegionOfSearcher.search(ipAddress);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            address = ipInfo == null ? "未知" : ipInfo.getAddressAndIsp().replace("中国", "");
        }

        ipAddress = LOCAL_HOST_IPs.contains(ipAddress) ? "127.0.0.1" : ipAddress;
        sysLog.setIpAddress(ipAddress);
        sysLog.setIpAddressAttr(address);


        sysLog.setRequestUri(URLUtil.getPath(requestUri));
        sysLog.setRequestType(requestType);

        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName()
                + "." + joinPoint.getSignature().getName() + "()");

        SysLogSource sysLogSource = getMethodSysLogsAnnotationValue(joinPoint);
        sysLog.setActionDesc(sysLogSource.getActionDesc());
        sysLog.setSource(sysLogSource.getSource());
        sysLog.setLogType(sysLogSource.getLogType());
        sysLog.setModule(sysLogSource.getModule());
        sysLog.setModuleName(sysLogSource.getModuleName());
        sysLog.setBusinessType(sysLogSource.getBusinessType());

        // 过滤请求参数
        List<Object> params = Arrays.stream(joinPoint.getArgs()).filter(item -> Objects.nonNull(item) && !isFilterObject(item)).collect(Collectors.toList());
        sysLog.setRequestParams(this.paramFilter(JSONUtil.toJsonStr(params)));

        // String retValue = this.paramFilter(JSONUtil.toJsonStr(joinPoint.getArgs()));

        UserAgent userAgent = UserAgentUtil.parse(headerUserAgent);
        sysLog.setBrowser(userAgent.getBrowser().getName());
        sysLog.setOs(userAgent.getPlatform().getName() + " " + userAgent.getOs().getName());
        sysLogService.saveSysLog(sysLog);
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult || o instanceof Page;
    }

    /**
     * 请求参数过滤
     */
    private String paramFilter(String jsonParams) {
        if (StrUtil.isNotBlank(jsonParams)) {
            JSONArray result = JSONUtil.createArray();
            JSONArray params = JSONUtil.parseArray(jsonParams);
            params.forEach(param -> {
                if (param != null && JSONUtil.isJsonObj(param.toString())) {
                    JSONObject jsonParam = JSONUtil.parseObj(param);
                    if (jsonParam.size() > 0) {
                        jsonParam.remove("password");
                        result.add(jsonParam);
                    }
                } else {
                    result.add(param);
                }
            });
            return JSONUtil.toJsonStr(result);
        }
        return "";
    }


    /**
     * 获取注解上面的值
     */
    private SysLogSource getMethodSysLogsAnnotationValue(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(SysLog.class)) {
            //获取方法上注解中表明的权限
            SysLog sysLog = method.getAnnotation(SysLog.class);
            return new SysLogSource(sysLog.source(), sysLog.logType(), sysLog.actionDesc(), sysLog.module(), sysLog.moduleName(), sysLog.businessType());
        }
        throw new RuntimeException("找不到 SysLog 日志注解");
    }

}
