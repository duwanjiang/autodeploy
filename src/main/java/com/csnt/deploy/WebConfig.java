package com.csnt.deploy;

import com.csnt.deploy.bizmodules.controller.JenkinsJobController;
import com.csnt.deploy.prop.ConfigProp;
import com.csnt.deploy.util.EncryptUtil;
import com.csnt.deploy.util.UtilDate;
import com.jfinal.config.*;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Date: Created in 17:24 2018/5/21
 */
public class WebConfig extends JFinalConfig {

    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);

    /**
     * 常量配置
     *
     * @param me
     */
    @Override
    public void configConstant(Constants me) {
        me.setEncoding("UTF-8");
        me.setViewType(ViewType.JSP);
        //设置文件的大小为500M
        me.setMaxPostSize(1024 * 1024 * 500);
        me.setJsonDatePattern(UtilDate.yyyy_MM_ddTHHmmss);
        //默认上传路径
        me.setBaseUploadPath(Paths.get(ConfigProp.DIR_ROOT_UPLOAD).toString());
    }

    /**
     * 访问路由配置
     *
     * @param me
     */
    @Override
    public void configRoute(Routes me) {
        me.add("/job", JenkinsJobController.class, "/");
    }

    /**
     * 模板引擎配置
     *
     * @param me
     */
    @Override
    public void configEngine(Engine me) {

    }

    /**
     * 插件配置
     *
     * @param me
     */
    @Override
    public void configPlugin(Plugins me) {
        //初始化数据源
        initDataSource(me);
    }

    /**
     * 全局拦截器
     *
     * @param me
     */
    @Override
    public void configInterceptor(Interceptors me) {
    }

    @Override
    public void configHandler(Handlers me) {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 初始化数据源
     */
    private void initDataSource(Plugins me) {
        Path configPath = Paths.get(System.getProperty("catalina.home"), "appConfig",
                "smServer", "database.properties");
        logger.debug("configPath:{}", configPath.toString());
        if (Files.isRegularFile(configPath) && Files.exists(configPath)) {
            logger.debug("使用新配置");
            loadPropertyFile(configPath.toFile(), "utf-8");
        } else {
            logger.debug("新配置未找到，使用老配置");
            loadPropertyFile("database.properties", "utf-8");
        }
        String dbConfig = getProperty("database.register", "");
        String[] dataBases = dbConfig.split(",");
        for (String dbKey : dataBases) {
            if (StrKit.isBlank(dbKey)) {
                continue;
            }
            DruidPlugin dp = new DruidPlugin(getProperty(dbKey + ".jdbcUrl"),
                    getProperty(dbKey + ".user"),
                    EncryptUtil.decrypt(getProperty(dbKey + ".password")),
                    getProperty(dbKey + ".driver"));
            //设置连接池数量
            if (StrKit.notNull(getPropertyToInt(dbKey + ".initialSize"), getPropertyToInt(dbKey + ".minIdle"), getPropertyToInt(dbKey + ".maxActive"))) {
                dp.set(getPropertyToInt(dbKey + ".initialSize"), getPropertyToInt(dbKey + ".minIdle"), getPropertyToInt(dbKey + ".maxActive"));
            }
            ActiveRecordPlugin arp;
            if ("main".equals(dbKey)) {
                arp = new ActiveRecordPlugin(dp);
                //添加sql模板文件
                arp.addSqlTemplate("sqltemplate/main.sql");
            } else {
                //按需配置
                arp = new ActiveRecordPlugin(dbKey, dp);
            }
            arp.setDialect(getDbDialectByDriver(getProperty(dbKey + ".driver")));
            arp.setContainerFactory(new CaseInsensitiveContainerFactory());
            arp.setShowSql(true);
            me.add(dp);
            me.add(arp);
        }
    }

    /**
     * 通过jdbc驱动名来获取数据库方言
     */
    private static Dialect getDbDialectByDriver(String driverUrl) {
        Dialect dialect = null;
        if (StrKit.isBlank(driverUrl)) {
            return dialect;
        }
        if (driverUrl.toLowerCase().contains("mysql")) {
            dialect = new MysqlDialect();
        } else if (driverUrl.toLowerCase().contains("sqlserver")) {
            dialect = new SqlServerDialect();
        } else if (driverUrl.toLowerCase().contains("oracle")) {
            dialect = new OracleDialect();
        }
        return dialect;
    }

}
