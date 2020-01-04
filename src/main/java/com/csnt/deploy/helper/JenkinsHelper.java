package com.csnt.deploy.helper;

import com.csnt.deploy.prop.ConfigProp;
import com.csnt.deploy.util.StringUtil;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.util.EncodingUtils;
import com.offbytwo.jenkins.helper.Range;
import com.offbytwo.jenkins.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JenkinsHelper
 * @Description jenkins 帮助类
 * @Author duwanjiang
 * @Date 2019/12/29 23:58
 * Version 1.0
 **/
public class JenkinsHelper {
    private static Logger logger = LoggerFactory.getLogger(JenkinsHelper.class);

    private static final String serviceName = "[Jenkins帮助类]";

    /**
     * Jenkins 对象
     */
    private static JenkinsServer jenkinsServer = connection();
    /**
     * http 客户端对象
     */
    private static JenkinsHttpClient jenkinsHttpClient = getClient();

    /**
     * Http 客户端工具
     * <p>
     * 如果有些 API 该Jar工具包未提供，可以用此Http客户端操作远程接口，执行命令
     *
     * @return
     */
    public synchronized static JenkinsHttpClient getClient() {
        try {
            if (jenkinsHttpClient == null) {
                jenkinsHttpClient = new JenkinsHttpClient(new URI(ConfigProp.JENKINS_URL), ConfigProp.JENKINS_USERNAME, ConfigProp.JENKINS_PASSWORD);
            }
        } catch (URISyntaxException e) {
            logger.error("{}获取到Jenkins客户端异常:{}", serviceName, e.toString(), e);
        }
        return jenkinsHttpClient;
    }

    /**
     * 连接 Jenkins
     */
    public synchronized static JenkinsServer connection() {
        try {
            if (jenkinsServer == null) {
                jenkinsServer = new JenkinsServer(new URI(ConfigProp.JENKINS_URL), ConfigProp.JENKINS_USERNAME, ConfigProp.JENKINS_PASSWORD);
            }
        } catch (URISyntaxException e) {
            logger.error("{}连接Jenkins服务端异常:{}", serviceName, e.toString(), e);
        }
        return jenkinsServer;
    }


    /**
     * 重启 Jenkins
     */
    public static boolean restart() {
        try {
            jenkinsServer.restart(true);
        } catch (IOException e) {
            logger.error("{}重启Jenkins异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 安全重启 Jenkins
     */
    public static boolean safeRestart() {
        try {
            jenkinsServer.safeRestart(true);
        } catch (IOException e) {
            logger.error("{}安全重启Jenkins异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 安全结束 Jenkins
     */
    public static boolean safeExit() {
        try {
            jenkinsServer.safeExit(true);
        } catch (IOException e) {
            logger.error("{}安全结束Jenkins异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 关闭 Jenkins 连接
     */
    public static void close() {
        jenkinsServer.close();
    }

    /**
     * 判断 Jenkins 是否运行
     */
    public static boolean isRunning() {
        return jenkinsServer.isRunning();
    }


    /**
     * 创建视图
     */
    public static boolean createView(String viewName, String xml) {
        try {
            // 创建一个 xml 字符串，里面设置一个 view 描述信息
            /*String xml = "<listView _class=\"hudson.model.ListView\">\n" +
                    "<description>%s</description>\n" +
                    "</listView>";
            xml = String.format(xml, description);*/
            // 创建 view
            jenkinsServer.createView(viewName, xml);
        } catch (IOException e) {
            logger.error("{}创建视图异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }


    /**
     * 获取视图基本信息
     */
    public static View getView(String viewName) {
        // 获取视图基本信息
        try {
            return jenkinsServer.getView(viewName);
        } catch (IOException e) {
            logger.error("{}获取视图异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 获取视图基本信息
     */
    public static String getViewXml(String viewName) {
        try {
            // 获取视图xml信息
            return jenkinsHttpClient.get("/view/" + EncodingUtils.encode(viewName) + "/api/xml");
        } catch (IOException e) {
            logger.error("{}获取视图xml基本信息异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 获取视图配置 XML 信息
     */
    public static String getViewConfig(String viewName) {
        try {
            // 获取视图配置xml信息
            return jenkinsHttpClient.get("/view/" + EncodingUtils.encode(viewName) + "/config.xml");
        } catch (IOException e) {
            logger.error("{}获取视图配置 XML 信息异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 更新视图信息
     */
    public static boolean updateView(String viewName, String xml) {
        try {
            // 创建一个 xml 字符串，里面设置一个要修改的某些字段,具体xml可以到jenkins查看
            // 例如，下面xml文件是从地址：https://Jenkins-IP/jenkins/view/test-view/config.xml 获取的
         /*   String xml = "<hudson.model.ListView>\n" +
                    "<name>test-view</name>\n" +
                    "<description>用于测试的视图1111</description>\n" +
                    "<filterExecutors>false</filterExecutors>\n" +
                    "<filterQueue>false</filterQueue>\n" +
                    "<properties class=\"hudson.model.View$PropertyList\"/>\n" +
                    "<jobNames>\n" +
                    "<comparator class=\"hudson.util.CaseInsensitiveComparator\"/>\n" +
                    "</jobNames>\n" +
                    "<jobFilters/>\n" +
                    "<columns>\n" +
                    "<hudson.views.StatusColumn/>\n" +
                    "<hudson.views.WeatherColumn/>\n" +
                    "<hudson.views.JobColumn/>\n" +
                    "<hudson.views.LastSuccessColumn/>\n" +
                    "<hudson.views.LastFailureColumn/>\n" +
                    "<hudson.views.LastDurationColumn/>\n" +
                    "<hudson.views.BuildButtonColumn/>\n" +
                    "<hudson.plugins.favorite.column.FavoriteColumn plugin=\"favorite@2.3.2\"/>\n" +
                    "</columns>\n" +
                    "<recurse>false</recurse>\n" +
                    "</hudson.model.ListView>";*/
            jenkinsServer.updateView(viewName, xml);
        } catch (IOException e) {
            logger.error("{}更新视图信息异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 删除视图
     */
    public static boolean deleteView(String viewName) {
        try {
            jenkinsHttpClient.post("/view/" + EncodingUtils.encode(viewName) + "/doDelete");
        } catch (IOException e) {
            logger.error("{}删除视图异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }


    /**
     * 获取 Job 对象
     */
    public static JobWithDetails getJob(String jobName) {
        try {
            // 获取 Job 信息
            return jenkinsServer.getJob(jobName);
        } catch (IOException e) {
            logger.error("{}获取Job对象异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 获取全部 Job Build列表
     */
    public static List<Build> getJobBuildListAll(String jobName) {
        try {
            // 获取 Job 信息
            JobWithDetails job = jenkinsServer.getJob(jobName);
            // 获取全部 Build 信息
            return job.getAllBuilds();
        } catch (IOException e) {
            logger.error("{}获取全部 Job Build列表异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 根据 Job Build 编号获取编译信息
     */
    public static Build getJobByNumber(String jobName, int buildNum) {
        try {
            // 获取 Job 信息
            JobWithDetails job = jenkinsServer.getJob(jobName);
            // 根据
            return job.getBuildByNumber(buildNum);
        } catch (IOException e) {
            logger.error("{}根据Job Build 编号获取编译信息异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }


    /**
     * 获取 Job 一定范围的 Build 列表
     */
    public static List<Build> getJobBuildListRange(String jobName, int fromBuildNum, int toBuildNum) {
        try {
            // 获取 Job 信息
            JobWithDetails job = jenkinsServer.getJob(jobName);
            // 设定范围
            Range range = Range.build().from(fromBuildNum).to(toBuildNum);
            // 获取一定范围的 Build 信息
            return job.getAllBuilds(range);
        } catch (IOException e) {
            logger.error("{} 获取Job一定范围的 Build 列表异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }


    /**
     * 获取 Build 详细信息
     */
    public static BuildWithDetails getJobBuildDetailInfo(String jobName) {
        try {
            // 获取 Job 信息
            JobWithDetails job = jenkinsServer.getJob(jobName);
            // 这里用最后一次编译来示例
            return job.getLastBuild().details();
        } catch (IOException e) {
            logger.error("{}获取 Build 详细信息异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 获取 Build Log 日志信息
     */
    public static ConsoleLog getJobBuildLog(String jobName) {
        try {
            // 获取 Job 信息
            JobWithDetails job = jenkinsServer.getJob(jobName);
            // 这里用最后一次编译来示例
            BuildWithDetails build = job.getLastBuild().details();
            // 获取构建的日志，如果正在执行构建，则会只获取已经执行的过程日志

            // 获取部分日志,一般用于正在执行构建的任务
            return build.getConsoleOutputText(0);
        } catch (IOException e) {
            logger.error("{}获取 Build Log 日志信息异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }


    /**
     * 创建 Job
     */
    public static boolean createJob(String jobName, String xml) {
        try {
            /**创建一个流水线任务，且设置一个简单的脚本**/
           /* // 创建 Pipeline 脚本
            String script = "node(){ \n" +
                    "echo 'hello world!' \n" +
                    "}";
            // xml配置文件，且将脚本加入到配置中
            String xml = "<flow-definition plugin=\"workflow-job@2.32\">\n" +
                    "<description>测试项目</description>\n" +
                    "<definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.66\">\n" +
                    "<script>" + script + "</script>\n" +
                    "<sandbox>true</sandbox>\n" +
                    "</definition>\n" +
                    "</flow-definition>";*/
            // 创建 Job
            jenkinsServer.createJob(jobName, xml);
        } catch (IOException e) {
            logger.error("{}创建 Job[{}]异常:{}", serviceName, jobName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 更新 Job
     * <p>
     * 更改之前创建的无参数Job，更改其为参数Job
     */
    public static boolean updateJob(String jobName, String xml) {
        try {
            /**
             * 更改一个流水线任务，让一个无参数的任务变成带参数任务
             */
            // 创建 Pipeline 脚本，用一个key变量
           /* String script = "node(){ \n" +
                    "echo \"${key}\" \n" +
                    "}";
            // xml配置文件，且将脚本加入到配置中
            String xml = "<flow-definition plugin=\"workflow-job@2.32\">\n" +
                    "<actions/>\n" +
                    "<description>测试项目</description>\n" +
                    "<keepDependencies>false</keepDependencies>\n" +
                    "<properties>\n" +
                    "<hudson.model.ParametersDefinitionProperty>\n" +
                    "<parameterDefinitions>\n" +
                    "<hudson.model.StringParameterDefinition>\n" +
                    "<name>key</name>\n" +
                    "<description>用于测试的字符变量</description>\n" +
                    "<defaultValue>hello</defaultValue>\n" +
                    "<trim>false</trim>\n" +
                    "</hudson.model.StringParameterDefinition>\n" +
                    "</parameterDefinitions>\n" +
                    "</hudson.model.ParametersDefinitionProperty>\n" +
                    "</properties>\n" +
                    "<definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@2.66\">\n" +
                    "<script>" + script + "</script>\n" +
                    "<sandbox>true</sandbox>\n" +
                    "</definition>\n" +
                    "<disabled>false</disabled>\n" +
                    "</flow-definition>";*/
            // 创建 Job
            jenkinsServer.updateJob(jobName, xml);
        } catch (IOException e) {
            logger.error("{}更新 Job异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 获取 Job 列表
     */
    public static Map<String, Job> getJobList() {
        try {
            // 获取 Job 列表
            return jenkinsServer.getJobs();
        } catch (IOException e) {
            logger.error("{}获取 Job 列表异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 获取 View 名称获取 Job 列表
     */
    public static Map<String, Job> getJobListByView(String viewName) {
        try {
            if (StringUtil.isEmpty(viewName)) {
                viewName = "all";
            }
            // 获取 Job 列表
            return jenkinsServer.getJobs(viewName);
        } catch (IOException e) {
            logger.error("{}获取 View 名称获取 Job 列表异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 查看 Job XML 信息
     */
    public static String getJobConfig(String jobName) {
        try {
            return jenkinsServer.getJobXml(jobName);
        } catch (IOException e) {
            logger.error("{}查看 Job XML 信息异常:{}", serviceName, e.toString(), e);
            return null;
        }
    }

    /**
     * 执行无参数 Job build
     */
    public static boolean buildJob(String jobName) {
        try {
            jenkinsServer.getJob(jobName).build();
        } catch (IOException e) {
            logger.error("{} 执行无参数 Job build异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 执行带参数 Job build
     */
    public static boolean buildParamJob(String jobName, Map<String, String> param) {
        try {
            /**
             * 例如，现有一个job，拥有一个字符参数"key"
             * 现在对这个值进行设置，然后执行一个输出这个值的脚本
             */
            // 设置参数值
           /* Map<String, String> param = new HashMap<>();
            param.put("key", "hello world!");*/
            // 执行 build 任务
            jenkinsServer.getJob(jobName).build(param);
        } catch (IOException e) {
            logger.error("{}执行带参数 Job build异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 停止最后构建的 Job Build
     */
    public static boolean stopLastJobBuild(String jobName) {
        try {
            // 获取最后的 build 信息
            Build build = jenkinsServer.getJob(jobName).getLastBuild();
            // 停止最后的 build
            build.Stop();
        } catch (IOException e) {
            logger.error("{}停止最后构建的 Job Build异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 删除 Job
     */
    public static boolean deleteJob(String jobName) {
        try {
            jenkinsServer.deleteJob(jobName);
        } catch (IOException e) {
            logger.error("{}删除 Job异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 禁用 Job
     */
    public static boolean disableJob(String jobName) {
        try {
            jenkinsServer.disableJob(jobName);
        } catch (IOException e) {
            logger.error("{}禁用 Job异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 启用 Job
     */
    public static boolean enableJob(String jobName) {
        try {
            jenkinsServer.enableJob(jobName);
        } catch (IOException e) {
            logger.error("{}启用 Job异常:{}", serviceName, e.toString(), e);
            return false;
        }
        return true;
    }

}
