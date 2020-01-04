-- 系统参数配置表
CREATE TABLE tbl_paramSystemConfig (
 ip varchar2(15) not null,
 stationCode varchar2(14) not null ,
 stationName varchar2(50) not null ,
 systemCode varchar2(50) not null ,
 systemName varchar2(100) not null ,
 configContext clob not null ,
 isActive number default 1 not null,
 issueFlag number default 1 not null,
 createTime date not null,
 updateTime date not null,
 backup1 varchar2(50) null,
 backup2 varchar2(50) null,
 backup3 varchar2(50) null,
 backup4 varchar2(50) null,
 backup5 varchar2(50) null,
 primary key (ip,systemCode)
);

comment on column tbl_paramSystemConfig.ip is '收费站ip地址';
comment on column tbl_paramSystemConfig.stationCode is '收费站编码';
comment on column tbl_paramSystemConfig.stationName is '收费站名称';
comment on column tbl_paramSystemConfig.systemCode is '系统编码';
comment on column tbl_paramSystemConfig.systemName is '系统名称';
comment on column tbl_paramSystemConfig.configContext is 'json格式的配置内容';
comment on column tbl_paramSystemConfig.isActive is '是否激活 1-是 0-否';
comment on column tbl_paramSystemConfig.issueFlag is '是否下发 1-是 0-否';
comment on column tbl_paramSystemConfig.createTime is '入库时间';
comment on column tbl_paramSystemConfig.updateTime is '更新时间';
comment on column tbl_paramSystemConfig.backup1 is '备份1';
comment on column tbl_paramSystemConfig.backup2 is '备份2';
comment on column tbl_paramSystemConfig.backup3 is '备份3';
comment on column tbl_paramSystemConfig.backup4 is '备份4';
comment on column tbl_paramSystemConfig.backup5 is '备份5';

-- =================================
--  初始化sql：
--  重要字段说明：
--  configContext.port 应用的端口
--  configContext.credentialsId Jenkins的凭据key
--  configContext.contextPath 服务部署后在tomcat中的应用名
--  configContext.war 需要部署应用的war名字
-- =================================
INSERT INTO tbl_paramsystemconfig (ip, stationCode, stationName, systemCode, systemName, configContext, isActive, issueFlag, createTime, updateTime, backup1, backup2, backup3, backup4, backup5)
VALUES ('10.63.1.125', '6300104', '乐都站', 'smServer', '部站服务端', '{"port":35000,"credentialsId":"tomcatUser","contextPath":"/smServer","war":"smServer.war"}', '1', '1', TO_DATE('2020-01-04 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), TO_DATE('2020-01-04 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), NULL, NULL, NULL, NULL, '1');
INSERT INTO tbl_paramsystemconfig (ip, stationCode, stationName, systemCode, systemName, configContext, isActive, issueFlag, createTime, updateTime, backup1, backup2, backup3, backup4, backup5)
VALUES ('10.63.1.28', '6300101', '马场垣主线站', 'smServer', '部站服务端', '{"port":35000,"credentialsId":"tomcatUser","contextPath":"/smServer","war":"smServer.war"}', '0', '1', TO_DATE('2020-01-04 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), TO_DATE('2020-01-04 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), NULL, NULL, NULL, NULL, '1');
