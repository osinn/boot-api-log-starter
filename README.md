# boot-api-log-starter
> Spring boot starter 日志服务自动配置

# 添加依赖
```
<dependency>
    <groupId>com.gitee.osinn.framework</groupId>
    <artifactId>boot-api-log-starter</artifactId>
    <version>1.0</version>
</dependency>
```
# application.yml配置
```
ip2region:
  # 是否使用ip2region查询
  enabled: true
  # ip2region文件位置
  dbfile: ip2region/ip2region.db
  # b-tree算法查询（ b-tree算法、binary算法、memory算法）
  # 如果不是memory(内存)算法查询，会自动在项目的根目录下创建ip2region文件夹来保存ip2region/ip2region.db文件
  # 保存ip2region/ip2region.db文件到本地是为了避免非内存查询的时候有些IP查询不到归属信息
  algorithm: BTREE_SEARCH
  # 如果不使用ip2region查询，会通过网络以get请求方式查询IP地址信息
  params: # 请求参数json格式
    json: true
  # ip信息查询地址
  ip-url: http://whois.pconline.com.cn/ipJson.jsp
```
# 使用姿势
- @EnableSysAsyncTask 启动异步并配置线程池
- @EnableSysLog 在启动类上添加此注解来启用日志服务自动配置
- @SysLog 在方法上添加此注解拦截请求以便获取请求接口相关数据

## 更新日志
[CHANGELOG](./CHANGELOG.md)

# 特别说明
- 默认使用`mybatis`作为 `ORM`,所以需要引入mybatis`相关依赖
- 如果不使用`mybatis` 必须实现 `ISysLogService`接口并且必须实现`getSysLogUserInfoDTO`、`getSysLogUserInfoByToken`以及`saveSysLog`方法，根据需求编写接口逻辑代码
- 如果使用`mybatis`, 又想记录操作人那么需要实现 `ISysLogService`接口并实现`getSysLogUserInfoDTO`和`getSysLogUserInfoByToken`接口返回登录用户信息即可，否则只需要引入依赖，启动类上添加`@EnableSysLog`注解即可启用日志插件，在想要记录日志的方法上添加`@SysLog`注解并填写对应参数即可记录日志
- `getSysLogUserInfoDTO`是根据请求头获取token再根据token获取登录用户信息（场景：业务接口请求时使用）
- `getSysLogUserInfoByToken`是根据token来获取登录用户信息（场景：登录成功时调用）与 `recordLogininfor`接口配合使用，先调用`recordLogininfor`，内部实现`recordLogininfor`方法会调用`getSysLogUserInfoByToken`并传递token
- 登录成功时需要手动调用`recordLogininfor`
- 内置使用`mybatis`实现数据入库，分页查询、查询详情接口
- fetchModuleNameAll 根据日志类型获取日志系统模块
- fetchDetailsById 查询详情
- fetchList 列表分页查询
- saveSysLog 入库


# 创建表
```
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '当前登录用户id',
  `account` varchar(20) DEFAULT NULL COMMENT '当前登录用户账号',
  `nickname` varchar(255) DEFAULT NULL COMMENT '当前登录用户昵称',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `ip_address_attr` varchar(100) DEFAULT NULL COMMENT 'IP地址归属地',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `request_params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `request_type` varchar(30) DEFAULT NULL COMMENT '请求类型',
  `class_method` varchar(255) DEFAULT NULL COMMENT '请求方法',
  `module` varchar(50) NOT NULL COMMENT '日志模块',
  `module_name` varchar(255) NOT NULL COMMENT '日志模块名称',
  `source` varchar(50) NOT NULL COMMENT '日志来源',
  `log_type` varchar(50) NOT NULL COMMENT '日志类型',
  `action_desc` varchar(255) DEFAULT NULL COMMENT '动作描述',
  `status` varchar(50) NOT NULL COMMENT '状态',
  `business_type` varchar(30) NOT NULL DEFAULT 'OTHER' COMMENT '业务操作',
  `execution_time` decimal(10,0) DEFAULT NULL COMMENT '执行耗时(毫秒单位)',
  `exception_msg` text COMMENT '异常信息',
  `browser` varchar(100) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(100) DEFAULT NULL COMMENT '操作系统',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_log` (`user_id`,`account`,`nickname`,`module`,`source`,`log_type`,`action_desc`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';
```
# ISysLogService接口
#### 接口方法
```

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
void saveSysLog(SysLogDTO sysLogDTO);

/**
 * 分页查询日志
 *
 * @param sysLogQueryDTO
 * @return
 */
PageDTO<SysLogEntity> fetchList(SysLogQueryDTO sysLogQueryDTO);

/**
 * 获取日志详情
 *
 * @param id
 * @return
 */
SysLogEntity fetchDetailsById(Long id);

/**
 * 根据日志类型获取日志系统模块
 *
 * @param logType
 * @return
 */
List<ModuleNameDTO> fetchModuleNameAll(SysLogTypeEnum logType);

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
void recordLogininfor(String moduleName,
                      String module,
                      SysLogStatusEnum status,
                      String usAgent,
                      String ipAddress,
                      BigDecimal executionTime,
                      String token);
```
- 如果不实现`ISysLogService`接口,不会记录操作人
- 如果想自己实现入库逻辑，可实现`saveSysLog`方法，此方法带回日志相关数据

# `@SysLog` 注解,参数如下
```
/**
 * 描述
 */
String actionDesc();

/**
 * 模块
 *
 * @return
 */
String module();

/**
 * 日志模块名称(label)
 */
String moduleName();

/**
 * 日志来源
 *
 * @return
 */
SysLogSourceEnum source() default SysLogSourceEnum.WEB;

/**
 * 日志类型
 *
 * @return
 */
SysLogTypeEnum logType() default SysLogTypeEnum.REQUEST;

/**
 * 业务操作
 */
BusinessType businessType() default BusinessType.OTHER;
```
# 关于ip2region.db查询
```
全部的查询客户端单次查询都在0.x毫秒级别，内置了三种查询算法

memory算法：整个数据库全部载入内存，单次查询都在0.1x毫秒内，C语言的客户端单次查询在0.00x毫秒级别。
binary算法：基于二分查找，基于ip2region.db文件，不需要载入内存，单次查询在0.x毫秒级别。
b-tree算法：基于btree算法，基于ip2region.db文件，不需要载入内存，单词查询在0.x毫秒级别，比binary算法更快。
任何客户端b-tree都比binary算法快，当然memory算法固然是最快的！
```