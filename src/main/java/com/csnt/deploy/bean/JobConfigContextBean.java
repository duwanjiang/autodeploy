package com.csnt.deploy.bean;

import java.io.Serializable;

/**
 * @ClassName JobConfigBean
 * @Description TODO
 * @Author duwanjiang
 * @Date 2019/12/31 2:52
 * Version 1.0
 **/
public class JobConfigContextBean implements Serializable {

    private int port;
    private String credentialsId;
    private String contextPath;
    private String war;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getWar() {
        return war;
    }

    public void setWar(String war) {
        this.war = war;
    }
}
