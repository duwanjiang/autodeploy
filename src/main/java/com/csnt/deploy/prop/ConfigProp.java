package com.csnt.deploy.prop;

import com.csnt.deploy.util.EncryptUtil;
import com.jfinal.kit.Prop;

import java.nio.file.Paths;

/**
 * Description:
 * Copyright: © 2019 CSNT. All rights reserved.
 * Company:CSNT
 *
 * @author magic_z
 * @date 2019/8/6/006 10:22
 */
public class ConfigProp {

    public static final Prop PROP = new Prop("config.properties");

    /*根目录*/
    public static final String DIR_ROOT = PROP.get("dir.root");


    /**
     * 上传文件的接收根目录
     */
    public static final String DIR_ROOT_UPLOAD = Paths.get(DIR_ROOT, "upload").toString();

    /**
     * 是否启动版本查询
     */
    public static final boolean VERSION_INFO_ENABLE = PROP.getBoolean("version.info.enable",true);

    /**
     * 版本查询url
     */
    public static final String VERSION_URL = PROP.get("version.url","version.url");

    /**
     * 版本查询超时时间
     */
    public static final int VERSION_REQ_TIMEOUT = PROP.getInt("version.req.timeout",2000);

    /**
     * jenkins 配置
     */
    public static final String JENKINS_URL = PROP.get("jenkins.url");
    public static final String JENKINS_USERNAME = PROP.get("jenkins.username");
    public static final String JENKINS_PASSWORD = EncryptUtil.decrypt(PROP.get("jenkins.password"));


    private ConfigProp() {
    }
}
