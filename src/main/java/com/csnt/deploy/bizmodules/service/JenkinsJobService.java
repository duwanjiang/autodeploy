package com.csnt.deploy.bizmodules.service;

import cn.hutool.http.HttpRequest;
import com.csnt.deploy.bean.JobConfigContextBean;
import com.csnt.deploy.bean.JobInfoBean;
import com.csnt.deploy.bean.VersionInfoBean;
import com.csnt.deploy.helper.JenkinsHelper;
import com.csnt.deploy.prop.ConfigProp;
import com.csnt.deploy.util.DbUtil;
import com.csnt.deploy.util.StringUtil;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName JenkinsJobService
 * @Description Jenkins job 服务类
 * @Author duwanjiang
 * @Date 2019/12/30 2:14
 * Version 1.0
 **/
public class JenkinsJobService {
    private static Logger logger = LoggerFactory.getLogger(JenkinsJobService.class);

    private static final String serviceName = "[Jenkins的任务服务]";


    /**
     * 查询所有的系统信息
     *
     * @return
     */
    public List<Record> queryAllSystems() {
        return Db.find(DbUtil.getSql("queryAllSystem"));
    }

    /**
     * 查询所有的系统信息
     *
     * @return
     */
    public Record querySystemByCode(String systemCode) {
        return Db.findFirst(DbUtil.getSqlPara("querySystemByCode", Kv.by("systemCode", systemCode)));
    }

    /**
     * 更加系统查询第一个收费站
     *
     * @return
     */
    public Record queryFirstStationByCode(String systemCode) {
        return Db.findFirst(DbUtil.getSqlPara("queryFirstStationByCode", Kv.by("systemCode", systemCode)));
    }

    /**
     * 根据系统编码查询收费站
     *
     * @param systemCode
     * @return
     */
    public List<Record> listStationBySystemCode(String systemCode) {
        return Db.find(DbUtil.getSqlPara("listStationBySystemCode", Kv.by("systemCode", systemCode)));
    }

    /**
     * 开始创建任务
     *
     * @param station
     * @param jobConfig
     * @return
     */
    public String createJob(Record station, JobConfigContextBean jobConfig) {
        String stationName = station.getStr("stationName");
        String stationCode = station.getStr("stationCode");
        String systemCode = station.getStr("systemCode");
        String systemName = station.getStr("systemName");
        String customWorkspace = Paths.get(ConfigProp.DIR_ROOT_UPLOAD, systemCode).toString();
        String ip = station.getStr("ip");
        String jobName = new String(String.format("%s-%s", systemCode, stationCode).getBytes(), Charset.forName("utf-8"));
        logger.info("{}开始创建[{}]收费站的[{}]任务", serviceName, stationName, jobName);

        StringBuilder xml = new StringBuilder(
                "<project>\n" +
                        "<actions/>\n" +
                        "<description>")
                .append(systemName + "_" + stationName).append("</description>\n" +
                        "<keepDependencies>false</keepDependencies>\n" +
                        "<properties/>\n" +
                        "<scm class=\"hudson.scm.NullSCM\"/>\n" +
                        "<canRoam>true</canRoam>\n" +
                        "<disabled>false</disabled>\n" +
                        "<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n" +
                        "<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n" +
                        "<triggers/>\n" +
                        "<concurrentBuild>false</concurrentBuild>\n")
                .append("<customWorkspace>").append(customWorkspace).append("</customWorkspace>")
                .append("<builders/>\n" +
                        "<publishers>\n" +
                        "<hudson.plugins.deploy.DeployPublisher plugin=\"deploy@1.15\">\n" +
                        "<adapters>\n" +
                        "<hudson.plugins.deploy.tomcat.Tomcat9xAdapter>\n")
                .append("<credentialsId>").append(jobConfig.getCredentialsId()).append("</credentialsId>\n")
                .append("<url>http://").append(ip).append(":").append(jobConfig.getPort()).append("</url>\n")
                .append("<path/>\n" +
                        "</hudson.plugins.deploy.tomcat.Tomcat9xAdapter>\n" +
                        "</adapters>\n")
                .append("<contextPath>").append(jobConfig.getContextPath()).append("</contextPath>\n")
                .append("<war>").append(jobConfig.getWar()).append("</war>\n")
                .append("<onFailure>false</onFailure>\n" +
                        "</hudson.plugins.deploy.DeployPublisher>\n" +
                        "</publishers>\n" +
                        "<buildWrappers/>\n" +
                        "</project>");
        if (JenkinsHelper.createJob(jobName, xml.toString())) {
            logger.info("{}创建任务[{}]成功", serviceName, jobName);
            return jobName;
        } else {
            //删除该任务再重新创建
            if (JenkinsHelper.deleteJob(jobName) && JenkinsHelper.createJob(jobName, xml.toString())) {
                logger.info("{}创建任务[{}]成功", serviceName, jobName);
                return jobName;
            }
        }
        logger.error("{}创建任务[{}]失败", serviceName, jobName);
        return null;
    }

    /**
     * 创建一个试图
     *
     * @param viewName
     * @return
     */
    public boolean createView(String viewName, List<String> jobNames, String description) {
        StringBuilder xml = new StringBuilder(
                String.format("<hudson.model.ListView>\n" +
                        "<name>%s</name>\n" +
                        "<description>%s</description>\n" +
                        "<filterExecutors>false</filterExecutors>\n" +
                        "<filterQueue>false</filterQueue>\n" +
                        "<properties class=\"hudson.model.View$PropertyList\"/>\n" +
                        "<jobNames>\n" +
                        "<comparator class=\"hudson.util.CaseInsensitiveComparator\"/>\n", viewName, description));
        for (String jobName : jobNames) {
            xml.append("<string>").append(jobName).append("</string>\n");
        }
        xml.append("</jobNames>\n" +
                "<jobFilters/>\n" +
                "<columns>\n" +
                "<hudson.views.StatusColumn/>\n" +
                "<hudson.views.WeatherColumn/>\n" +
                "<hudson.views.JobColumn/>\n" +
                "<hudson.views.LastSuccessColumn/>\n" +
                "<hudson.views.LastFailureColumn/>\n" +
                "<hudson.views.LastDurationColumn/>\n" +
                "<hudson.views.BuildButtonColumn/>\n" +
                "</columns>\n" +
                "<recurse>true</recurse>\n" +
                "</hudson.model.ListView>");

        if (JenkinsHelper.createView(viewName, xml.toString())) {
            logger.info("{}创建试图[{}]成功", serviceName, viewName);
            return true;
        } else {
            //删除该任务再重新创建
            if (JenkinsHelper.deleteView(viewName) && JenkinsHelper.createView(viewName, xml.toString())) {
                logger.info("{}创建试图[{}]成功", serviceName, viewName);
                return true;
            }
        }
        logger.error("{}创建试图[{}]失败", serviceName, viewName);
        return false;
    }

    /**
     * 部署任务列表
     *
     * @param systemCode
     * @return
     */
    public boolean deployJobsBySystem(String systemCode) {
        logger.info("{}开始部署[{}]系统的任务", serviceName, systemCode);
        //查询需要下发的收费站列表
        Map<String, Job> jobs = JenkinsHelper.getJobList();
        logger.info("{}[{}]系统需要部署的任务有[{}]个", serviceName, systemCode, jobs.size());
        //创建系统名的试图
        if (jobs.size() > 0) {
            for (Job job : jobs.values()) {
                String jobName = job.getName();
                if (StringUtil.isNotEmpty(systemCode) && !jobName.startsWith(systemCode + "-")) {
                    continue;
                }
                logger.info("{}开始部署[{}]任务", serviceName, job.getName());
                if (!JenkinsHelper.buildJob(jobName)) {
                    logger.error("{}部署[{}]任务失败", serviceName, job.getName());
                    return false;
                }
            }
        }
        logger.info("{}完成部署[{}]系统的任务", serviceName, systemCode);
        return true;
    }

    /**
     * 部署任务列表
     *
     * @param systemCode
     * @return
     */
    public List<JobInfoBean> listJobsBySystem(String systemCode) {
        logger.info("{}查询[{}]系统的任务", serviceName, systemCode);
        List<JobInfoBean> list = new ArrayList<>();
        Map<String, Job> jobs = JenkinsHelper.getJobList();
        if (jobs == null) {
            logger.info("{}查询到[{}]系统有[{}]个任务", serviceName, systemCode, 0);
            return list;
        }
        logger.info("{}查询到所以系统[{}]个任务", serviceName, systemCode, jobs.size());
        //创建系统名的试图
        if (jobs.size() > 0) {
            for (Job job : jobs.values()) {
                String jobName = job.getName();
                if (StringUtil.isNotEmpty(systemCode) && !jobName.startsWith(systemCode + "-")) {
                    continue;
                }
                JobInfoBean jobStatus = new JobInfoBean();
                jobStatus.setJobName(job.getName());
                BuildWithDetails buildWithDetails = JenkinsHelper.getJobBuildDetailInfo(job.getName());
                jobStatus.setJobStatus(buildWithDetails.getResult());
                String config = JenkinsHelper.getJobConfig(job.getName());
                String url = getUrlByConfig(config);
                String war = getWarByConfig(config);
                //查询服务版本号
                VersionInfoBean versionInfoBean = getAppVersionInfo(job.getName(), url, war);
                jobStatus.setVersionInfoBean(versionInfoBean);
                //获取版本信息
                list.add(jobStatus);
            }
        }
        logger.info("{}完成查询[{}]系统的任务", serviceName, systemCode);
        return list;
    }

    /**
     * 从配置中获取url
     *
     * @param config
     * @return
     */
    protected String getUrlByConfig(String config) {
        String regex = "<url>(.*)</url>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(config);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 从配置中获取war包
     *
     * @param config
     * @return
     */
    protected String getWarByConfig(String config) {
        String regex = "<war>(.*)</war>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(config);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 获取指定收费站版本信息
     *
     * @return
     */
    public VersionInfoBean getAppVersionInfo(String jobName, String url, String war) {
        logger.info("{}开始获取[{}]的版本信息", serviceName, jobName);
        VersionInfoBean versionInfoBean = null;
        if (!ConfigProp.VERSION_INFO_ENABLE) {
            return versionInfoBean;
        }
        try {
            url = String.format("%s/%s%s", url,
                    war.replace(".war", ""), ConfigProp.VERSION_URL);
            //链式构建请求
            String result = HttpRequest.post(url)
                    //超时，毫秒
                    .timeout(ConfigProp.VERSION_REQ_TIMEOUT)
                    .execute().body();
            versionInfoBean = FastJson.getJson().parse(result, VersionInfoBean.class);
            logger.info("{}获取到[{}]的版本信息[{}]", serviceName, jobName, result);
        } catch (Exception e) {
            logger.error("{}获取[{}]的版本信息异常:{}", serviceName, jobName, e.toString(), e);
        }
        return versionInfoBean;
    }
}
