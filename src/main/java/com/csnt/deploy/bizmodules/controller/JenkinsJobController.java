package com.csnt.deploy.bizmodules.controller;

import com.csnt.deploy.bean.JobConfigContextBean;
import com.csnt.deploy.bean.JobInfoBean;
import com.csnt.deploy.bean.ResultApi;
import com.csnt.deploy.bizmodules.service.JenkinsJobService;
import com.csnt.deploy.helper.JenkinsHelper;
import com.csnt.deploy.util.FilesUtil;
import com.csnt.deploy.util.StringUtil;
import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JenkinsJobController
 * @Description TODO
 * @Author duwanjiang
 * @Date 2019/12/30 2:14
 * Version 1.0
 **/
public class JenkinsJobController extends Controller {
    private static Logger logger = LoggerFactory.getLogger(JenkinsJobService.class);

    private static final String serviceName = "[Jenkins的任务控制器]";

    private JenkinsJobService jenkinsJobService = new JenkinsJobService();

    /**
     * 查询所有的系统信息
     */
    public void queryAllSystems() {
        renderJson(jenkinsJobService.queryAllSystems());
    }

    /**
     * 初始化所以的收费站定时任务
     */
    public void initJobs() {
        String appCode = getPara("appCode");
        ResultApi resultApi = new ResultApi();
        if (StringUtil.isEmpty(appCode)) {
            logger.error("{}appCode不能为空", serviceName);
            resultApi.setCode(-1);
            resultApi.setMsg("appCode不能为空");
            renderJson(resultApi);
            return;
        }
        logger.info("{}当前需要初始化收费站部署job的项目是[{}]", serviceName, appCode);
        //查询需要下发的收费站列表
        List<Record> stations = jenkinsJobService.listStationBySystemCode(appCode);
        List<String> jobNames = new ArrayList<>();
        //创建系统名的试图
        if (stations.size() > 0) {
            String systemName = stations.get(0).getStr("systemName");
            //开始创建job
            for (Record station : stations) {
                JobConfigContextBean jobConfig = FastJson.getJson().parse(station.getStr("configContext"), JobConfigContextBean.class);
                if (StringUtil.isEmpty(jobConfig.getWar()) || StringUtil.isEmpty(jobConfig.getCredentialsId())) {
                    resultApi.setCode(-1);
                    resultApi.setMsg("当前系统不能创建任务");
                    renderJson(resultApi);
                    logger.error("{}当前系统[{}]不能创建任务", serviceName, appCode);
                    return;
                }
                String jobName = jenkinsJobService.createJob(station, jobConfig);
                if (jobName == null) {
                    continue;
                }
                jobNames.add(jobName);
            }

            resultApi.setMsg("初始化任务列表[成功]");
            renderJson(resultApi);
        }
    }

    /**
     * 删除所有任务
     */
    public void deleteJobs() {
        String appCode = getPara("appCode");
        ResultApi resultApi = new ResultApi();
        Map<String, Job> jobs = new HashMap<>();
        jobs = JenkinsHelper.getJobList();

        //创建系统名的试图
        if (jobs != null && jobs.size() > 0) {
            for (Job job : jobs.values()) {
                String jobName = job.getName();
                if (StringUtil.isNotEmpty(appCode) && !jobName.startsWith(appCode + "-")) {
                    continue;
                }
                if (JenkinsHelper.deleteJob(jobName)) {
                    logger.info("{}删除[{}]任务成功", serviceName, job.getName());
                }
            }
        }
        resultApi.setMsg("删除任务完成");
        renderJson(resultApi);
    }

    /**
     * 部署指定系统的所有jobs
     */
    public void deployJobs() {
        String appCode = getPara("appCode");
        ResultApi result = new ResultApi();
        if (StringUtil.isEmpty(appCode)) {
            logger.error("{}appCode不能为空", serviceName);
            result.setCode(-1);
            result.setMsg("appCode不能为空");
            renderJson(result);
            return;
        }
        logger.info("{}当前需要部署收费站的项目是[{}]", serviceName, appCode);
        //查询系统信息
        if (!jenkinsJobService.deployJobsBySystem(appCode)) {
            result.setCode(-1);
            result.setMsg("调用Jenkins部署任务[失败]");
            renderJson(result);
            logger.error("{}调用Jenkins部署任务[失败]", serviceName);
            return;
        }
        result.setMsg("调用Jenkins部署任务[成功]");
        renderJson(result);
        logger.info("{}调用Jenkins部署任务[成功]", serviceName);
    }

    /**
     * 部署指定job
     */
    public void deployJob() {
        String jobName = getPara("jobName");
        ResultApi result = new ResultApi();
        if (StringUtil.isEmpty(jobName)) {
            logger.error("{}jobName不能为空", serviceName);
            result.setCode(-1);
            result.setMsg("jobName不能为空");
            renderJson(result);
            return;
        }
        logger.info("{}当前需要部署的任务是[{}]", serviceName, jobName);
        if (!JenkinsHelper.buildJob(jobName)) {
            result.setCode(-1);
            result.setMsg("部署项目[" + jobName + "]失败");
            renderJson(result);
            return;
        }
        result.setMsg("部署项目[" + jobName + "]完成");
        renderJson(result);
    }

    /**
     * 查询指定系统的任务
     */
    public void listJobs() {
        String appCode = getPara("appCode");
        if (StringUtil.isEmpty(appCode)) {
            logger.error("{}appCode不能为空", serviceName);
            renderJson("appCode不能为空");
            return;
        }
        logger.info("{}查询[{}]系统的所有任务", serviceName, appCode);
        //查询系统信息
        //查询需要下发的收费站列表
        List<JobInfoBean> jobs = jenkinsJobService.listJobsBySystem(appCode);
        renderJson(jobs);
        logger.info("{}查询到[{}]系统的任务[{}]个", serviceName, appCode, jobs.size());
    }

    /**
     * 获取指定任务的日志
     */
    public void getJobLog() {
        String jobName = getPara("jobName");
        if (StringUtil.isEmpty(jobName)) {
            logger.error("{}jobName不能为空", serviceName);
            renderJson("jobName不能为空");
            return;
        }
        logger.info("{}当前需要部署的任务是[{}]", serviceName, jobName);
        BuildWithDetails buildWithDetails = JenkinsHelper.getJobBuildDetailInfo(jobName);
        try {
            renderText(buildWithDetails.getConsoleOutputHtml());
            return;
        } catch (IOException e) {
            logger.error("{}获取[{}]任务的日志异常:{}", serviceName, jobName, e.toString(), e);
            renderJson("部署项目[" + jobName + "]异常:" + e.toString());
        }
    }

    /**
     * 接收上传的war包
     */
    public void uploadWar() {
        String appCode = getHeader("appCode");
        ResultApi result = new ResultApi();
        if (StringUtil.isEmpty(appCode)) {
            logger.error("{}appCode不能为空", serviceName);
            result.setMsg("appCode不能为空");
            result.setCode(-1);
            renderJson(result);
            return;
        }
        logger.info("{}开始上传[{}]系统的war包", serviceName, appCode);
        UploadFile fileWar = null;
        try {
            JobConfigContextBean jobConfig = null;
            Record station = jenkinsJobService.queryFirstStationByCode(appCode);
            if (station != null) {
                jobConfig = FastJson.getJson().parse(station.getStr("configContext"), JobConfigContextBean.class);
            }
            //接收到war包
            fileWar = getFile("file", appCode);
            if (fileWar != null) {
                String fileName = fileWar.getFileName();
                String originalFileName = fileWar.getOriginalFileName();
                //判断war文件是否和配置的相同
                if (jobConfig != null && !jobConfig.getWar().equals(originalFileName)) {
                    logger.error("{}上传war文件与配置文件名不相同,上传war包为:{},应该war包:{}", serviceName, jobConfig.getWar(), originalFileName);
                    result.setCode(-1);
                    result.setMsg("上传war文件与配置文件名[" + jobConfig.getWar() + "]不相同,请重新上传");
                    renderJson(result);

                    //删除历史文件
                    FilesUtil.delDir(fileWar.getUploadPath());
                    return;
                }
                if (!fileName.equals(originalFileName)) {
                    //将文件名修改为上传文件名
                    Files.move(Paths.get(fileWar.getUploadPath(), fileName),
                            Paths.get(fileWar.getUploadPath(), originalFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                result.setMsg("上传文件成功");
                renderJson(result);
                logger.info("{}上传[{}]文件成功", serviceName, fileWar.getOriginalFileName());
                return;
            } else {
                result.setCode(-1);
                result.setMsg("上传文件失败");
                renderJson(result);
                logger.error("{}上传文件成功", serviceName);
                return;
            }
        } catch (Exception e) {
            logger.error("{}接收文件异常:{}", serviceName, e.toString(), e);
            result.setCode(-1);
            result.setMsg("上传文件异常:" + e.toString());
            renderJson(result);
        }
    }
}
