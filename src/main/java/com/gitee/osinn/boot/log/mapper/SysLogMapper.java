package com.gitee.osinn.boot.log.mapper;

import com.gitee.osinn.boot.log.dto.SysLogQueryDTO;
import com.gitee.osinn.boot.log.entity.SysLogEntity;
import com.gitee.osinn.boot.log.enums.SysLogTypeEnum;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysLogMapper {
    @Insert("INSERT INTO sys_log ( id," +
            " ip_address," +
            " ip_address_attr," +
            " status," +
            " execution_time," +
            " browser," +
            " os," +
            " created_time," +
            " source," +
            " log_type," +
            " action_desc," +
            " module," +
            " module_name," +
            " business_type," +
            " user_id," +
            " account," +
            " request_uri," +
            " request_params," +
            " request_type," +
            " class_method," +
            " exception_msg," +
            " nickname ) " +
            "VALUES ( " +
            "#{id}," +
            " #{ipAddress}," +
            " #{ipAddressAttr}," +
            " #{status}," +
            " #{executionTime}," +
            " #{browser}," +
            " #{os}," +
            " #{createdTime}," +
            " #{source}," +
            " #{logType}," +
            " #{actionDesc}," +
            " #{module}," +
            " #{moduleName}," +
            " #{businessType}," +
            " #{userId}," +
            " #{account}," +
            " #{requestUri}," +
            " #{requestParams}," +
            " #{requestType}," +
            " #{classMethod}," +
            " #{exceptionMsg}," +
            " #{nickname} )")
    void saveSysLog(SysLogEntity sysLogEntity);

    @Select("select * from sys_log where id = #{id}")
    SysLogEntity selectById(@Param("id") Long id);

    @Select("select distinct `module`, module_name from sys_log where log_type = #{logType}")
    List<SysLogEntity> selectList(@Param("logType") SysLogTypeEnum logType);

    @Select("<script>" +
            "select * from sys_log \n" +
            "<where> " +
                "<if test='status != null'>" +
                " and status = #{status}" +
                "</if>" +
                "<if test='logType != null'>" +
                " and log_type = #{logType}" +
                "</if>" +
                "<if test='source != null'>" +
                " and source = #{source}" +
                "</if>" +
                "<if test='userId != null'>" +
                " and user_id = #{userId}" +
                "</if>" +
                "<if test='module != null'>" +
                " and module LIKE concat('%',#{module},'%')" +
                "</if>" +
                "<if test='account != null'>" +
                " and account LIKE concat('%',#{account},'%')" +
                "</if>" +
                "<if test='nickname != null'>" +
                " and nickname LIKE concat('%',#{nickname},'%')" +
                "</if>" +
                "<if test='actionDesc != null'>" +
                " and action_desc LIKE concat('%',#{actionDesc},'%')" +
                "</if>" +
            "</where>" +
            "order by created_time desc LIMIT #{pageNum},#{pageSize} " +
            "</script>")
    List<SysLogEntity> selectPage(SysLogQueryDTO sysLogQueryDTO);

    @Select("<script>" +
            "select count(*) from sys_log \n" +
            "<where> " +
            "<if test='status != null'>" +
            " and status = #{status}" +
            "</if>" +
            "<if test='logType != null'>" +
            " and log_type = #{logType}" +
            "</if>" +
            "<if test='source != null'>" +
            " and source = #{source}" +
            "</if>" +
            "<if test='userId != null'>" +
            " and user_id = #{userId}" +
            "</if>" +
            "<if test='module != null'>" +
            " and module LIKE concat('%',#{module},'%')" +
            "</if>" +
            "<if test='account != null'>" +
            " and account LIKE concat('%',#{account},'%')" +
            "</if>" +
            "<if test='nickname != null'>" +
            " and nickname LIKE concat('%',#{nickname},'%')" +
            "</if>" +
            "<if test='actionDesc != null'>" +
            " and action_desc LIKE concat('%',#{actionDesc},'%')" +
            "</if>" +
            "</where>" +
            "</script>")
    long selectPageCount(SysLogQueryDTO sysLogQueryDTO);
}
