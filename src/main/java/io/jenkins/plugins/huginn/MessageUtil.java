package io.jenkins.plugins.huginn;

import hudson.model.Run;
import hudson.model.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 消息工具类
 */
public class MessageUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 获取本地化消息
     */
    public static String getLocalizedMessage(String key) {
        // 获取全局语言配置
        HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
        String language = config != null ? config.getMessageLanguage() : "auto";
        
        if ("zh".equals(language)) {
            return getChineseMessage(key);
        } else if ("en".equals(language)) {
            return getEnglishMessage(key);
        } else {
            // auto - 根据系统默认语言
            Locale locale = Locale.getDefault();
            if (locale.getLanguage().equals("zh")) {
                return getChineseMessage(key);
            } else {
                return getEnglishMessage(key);
            }
        }
    }
    
    private static String getChineseMessage(String key) {
        switch (key) {
            case "build.title":
                return "构建通知";
            case "build.pipelineTitle":
                return "流水线通知";
            case "build.testMessage":
                return "这是来自Huginn通知插件的测试消息";
            default:
                return key;
        }
    }
    
    private static String getEnglishMessage(String key) {
        switch (key) {
            case "build.title":
                return "Build Notification";
            case "build.pipelineTitle":
                return "Pipeline Notification";
            case "build.testMessage":
                return "This is a test message from Huginn Notify Plugin";
            default:
                return key;
        }
    }
    
    /**
     * 展开消息中的变量
     */
    public static String expandMessage(String message, Run<?, ?> run) {
        if (message == null || run == null) {
            return message;
        }
        
        String expanded = message;
        
        // 基本信息
        expanded = expanded.replace("${BUILD_NUMBER}", String.valueOf(run.getNumber()));
        expanded = expanded.replace("${BUILD_ID}", run.getId());
        expanded = expanded.replace("${BUILD_DISPLAY_NAME}", run.getDisplayName());
        expanded = expanded.replace("${BUILD_URL}", run.getAbsoluteUrl());
        expanded = expanded.replace("${BUILD_DURATION}", run.getDurationString());
        expanded = expanded.replace("${BUILD_TIMESTAMP}", DATE_FORMAT.format(new Date(run.getTimeInMillis())));
        
        // 项目信息
        expanded = expanded.replace("${PROJECT_NAME}", run.getParent().getName());
        expanded = expanded.replace("${PROJECT_DISPLAY_NAME}", run.getParent().getDisplayName());
        expanded = expanded.replace("${PROJECT_URL}", run.getParent().getAbsoluteUrl());
        
        // 结果信息
        Result result = run.getResult();
        if (result != null) {
            expanded = expanded.replace("${BUILD_RESULT}", result.toString());
            expanded = expanded.replace("${BUILD_STATUS}", getResultDisplayName(result));
        }
        
        // Jenkins信息
        expanded = expanded.replace("${JENKINS_URL}", jenkins.model.Jenkins.get().getRootUrl());
        
        return expanded;
    }
    
    private static String getResultDisplayName(Result result) {
        if (result == Result.SUCCESS) {
            return "SUCCESS";
        } else if (result == Result.FAILURE) {
            return "FAILURE";
        } else if (result == Result.UNSTABLE) {
            return "UNSTABLE";
        } else if (result == Result.ABORTED) {
            return "ABORTED";
        } else if (result == Result.NOT_BUILT) {
            return "NOT_BUILT";
        }
        return result.toString();
    }
}
