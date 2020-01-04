package com.csnt.deploy.bean;

import com.offbytwo.jenkins.model.BuildResult;

import java.io.Serializable;

/**
 * @ClassName JobStatus
 * @Description TODO
 * @Author duwanjiang
 * @Date 2019/12/31 15:56
 * Version 1.0
 **/
public class JobInfoBean implements Serializable {

    private String jobName;
    private BuildResult jobStatus;
    private VersionInfoBean versionInfoBean;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public BuildResult getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(BuildResult jobStatus) {
        this.jobStatus = jobStatus;
    }

    public VersionInfoBean getVersionInfoBean() {
        return versionInfoBean;
    }

    public void setVersionInfoBean(VersionInfoBean versionInfoBean) {
        this.versionInfoBean = versionInfoBean;
    }

    @Override
    public String toString() {
        return "JobInfoBean{" +
                "jobName='" + jobName + '\'' +
                ", jobStatus=" + jobStatus +
                ", versionInfoBean=" + versionInfoBean +
                '}';
    }
}
