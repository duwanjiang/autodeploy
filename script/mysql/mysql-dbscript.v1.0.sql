-- 自动部署系统配置表
DROP TABLE IF EXISTS `tbl_paramSystemConfig`;
CREATE TABLE `tbl_paramSystemConfig` (
 ip varchar(15) not null comment 'ip地址',
 stationCode varchar(14) not null comment '编码',
 stationName varchar(50) not null comment '名称',
 systemCode varchar(50) not null comment '系统编码',
 systemName varchar(100) not null comment '系统名称',
 configContext text not null comment 'json格式的配置内容',
 isActive smallint default 1 not null comment '是否激活 1-是 0-否',
 issueFlag smallint default 1 not null comment '是否下发 1-是 0-否',
 createTime datetime not null comment '入库时间',
 updateTime datetime not null comment '更新时间',
 backup1 varchar(50) null comment '备份1',
 backup2 varchar(50) null comment '备份2',
 backup3 varchar(50) null comment '备份3',
 backup4 varchar(50) null comment '备份4',
 backup5 varchar(50) null comment '备份5',
 primary key (ip,systemCode)
)ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=COMPACT;

-- =================================
--  初始化sql：
--  重要字段说明：
--  configContext.port 应用的端口
--  configContext.credentialsId Jenkins的凭据key
--  configContext.contextPath 服务部署后在tomcat中的应用名
--  configContext.war 需要部署应用的war名字
-- =================================

