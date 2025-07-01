package io.jenkins.plugins.huginn;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Huginn通知发布器 - 用于传统Job
 */
public class HuginnNotifyPublisher extends Notifier implements SimpleBuildStep {
    
    private String webhookUrl;
    private String apiKey;
    private boolean notifyOnStart;
    private boolean notifyOnSuccess;
    private boolean notifyOnFailure;
    private boolean notifyOnUnstable;
    private boolean notifyOnAborted;
    private String customMessage;
    
    @DataBoundConstructor
    public HuginnNotifyPublisher() {
        // 默认配置
        this.notifyOnSuccess = true;
        this.notifyOnFailure = true;
        this.notifyOnUnstable = true;
        this.notifyOnAborted = true;
    }
    
    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, 
                       @Nonnull Launcher launcher, @Nonnull TaskListener listener) 
                       throws InterruptedException, IOException {
        
        Result result = run.getResult();
        if (result == null) {
            return;
        }
        
        // 检查是否需要发送通知
        if (!shouldNotify(result)) {
            listener.getLogger().println(Messages.log_skipNotification(result));
            return;
        }
        
        // 获取配置
        String effectiveWebhookUrl = getEffectiveWebhookUrl();
        String effectiveApiKey = getEffectiveApiKey();
        
        if (effectiveWebhookUrl == null || effectiveWebhookUrl.trim().isEmpty()) {
            listener.getLogger().println(Messages.log_webhookNotConfigured());
            return;
        }
        
        // 构建消息
        String title = MessageUtil.getLocalizedMessage("build.title");
        String content = buildMessage(run);
        String severity = getResultSeverity(result);
        
        // 发送通知
        HuginnNotifyService service = new HuginnNotifyService();
        boolean success = service.sendMessage(effectiveWebhookUrl, effectiveApiKey, title, content, severity);
        
        if (success) {
            listener.getLogger().println(Messages.log_sendSuccess());
        } else {
            listener.getLogger().println(Messages.log_sendFailed());
        }
    }
    
    private boolean shouldNotify(Result result) {
        if (result == Result.SUCCESS) {
            return notifyOnSuccess;
        } else if (result == Result.FAILURE) {
            return notifyOnFailure;
        } else if (result == Result.UNSTABLE) {
            return notifyOnUnstable;
        } else if (result == Result.ABORTED) {
            return notifyOnAborted;
        }
        return false;
    }
    
    private String buildMessage(Run<?, ?> run) {
        if (customMessage != null && !customMessage.trim().isEmpty()) {
            return MessageUtil.expandMessage(customMessage, run);
        }
        
        StringBuilder message = new StringBuilder();
        message.append(Messages.build_project(run.getParent().getDisplayName())).append("\n");
        message.append(Messages.build_number(run.getNumber())).append("\n");
        message.append(Messages.build_result(getResultDisplayName(run.getResult()))).append("\n");
        message.append(Messages.build_duration(run.getDurationString())).append("\n");
        message.append(Messages.build_url(run.getAbsoluteUrl()));
        
        return message.toString();
    }
    
    private String getResultDisplayName(Result result) {
        if (result == Result.SUCCESS) {
            return Messages.build_result_success();
        } else if (result == Result.FAILURE) {
            return Messages.build_result_failure();
        } else if (result == Result.UNSTABLE) {
            return Messages.build_result_unstable();
        } else if (result == Result.ABORTED) {
            return Messages.build_result_aborted();
        } else if (result == Result.NOT_BUILT) {
            return Messages.build_result_not_built();
        }
        return result.toString();
    }
    
    private String getResultSeverity(Result result) {
        if (result == Result.SUCCESS) {
            return "success";
        } else if (result == Result.FAILURE) {
            return "error";
        } else if (result == Result.UNSTABLE) {
            return "warning";
        } else if (result == Result.ABORTED) {
            return "info";
        }
        return "info";
    }
    
    private String getEffectiveWebhookUrl() {
        if (webhookUrl != null && !webhookUrl.trim().isEmpty()) {
            return webhookUrl;
        }
        
        HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
        return config != null ? config.getGlobalWebhookUrl() : null;
    }
    
    private String getEffectiveApiKey() {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return apiKey;
        }
        
        HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
        return config != null ? config.getGlobalApiKey() : null;
    }
    
    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
    
    // Getters and Setters
    public String getWebhookUrl() {
        return webhookUrl;
    }
    
    @DataBoundSetter
    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    @DataBoundSetter
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public boolean isNotifyOnStart() {
        return notifyOnStart;
    }
    
    @DataBoundSetter
    public void setNotifyOnStart(boolean notifyOnStart) {
        this.notifyOnStart = notifyOnStart;
    }
    
    public boolean isNotifyOnSuccess() {
        return notifyOnSuccess;
    }
    
    @DataBoundSetter
    public void setNotifyOnSuccess(boolean notifyOnSuccess) {
        this.notifyOnSuccess = notifyOnSuccess;
    }
    
    public boolean isNotifyOnFailure() {
        return notifyOnFailure;
    }
    
    @DataBoundSetter
    public void setNotifyOnFailure(boolean notifyOnFailure) {
        this.notifyOnFailure = notifyOnFailure;
    }
    
    public boolean isNotifyOnUnstable() {
        return notifyOnUnstable;
    }
    
    @DataBoundSetter
    public void setNotifyOnUnstable(boolean notifyOnUnstable) {
        this.notifyOnUnstable = notifyOnUnstable;
    }
    
    public boolean isNotifyOnAborted() {
        return notifyOnAborted;
    }
    
    @DataBoundSetter
    public void setNotifyOnAborted(boolean notifyOnAborted) {
        this.notifyOnAborted = notifyOnAborted;
    }
    
    public String getCustomMessage() {
        return customMessage;
    }
    
    @DataBoundSetter
    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
    
    @Extension
    @Symbol("huginnNotify")
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
        
        @Override
        public String getDisplayName() {
            return Messages.HuginnNotifyPublisher_DisplayName();
        }
        
        public FormValidation doCheckWebhookUrl(@QueryParameter String value) {
            if (value == null || value.trim().isEmpty()) {
                return FormValidation.ok(Messages.validation_webhookUrl_useGlobal());
            }
            if (!value.startsWith("http://") && !value.startsWith("https://")) {
                return FormValidation.error(Messages.validation_webhookUrl_invalidFormat());
            }
            return FormValidation.ok();
        }
        
        public FormValidation doTestConnection(@QueryParameter String webhookUrl, 
                                             @QueryParameter String apiKey) {
            String effectiveUrl = webhookUrl;
            String effectiveApiKey = apiKey;
            
            if (effectiveUrl == null || effectiveUrl.trim().isEmpty()) {
                HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
                if (config != null) {
                    effectiveUrl = config.getGlobalWebhookUrl();
                    if (effectiveApiKey == null || effectiveApiKey.trim().isEmpty()) {
                        effectiveApiKey = config.getGlobalApiKey();
                    }
                }
            }
            
            if (effectiveUrl == null || effectiveUrl.trim().isEmpty()) {
                return FormValidation.error(Messages.validation_connection_needConfig());
            }
            
            try {
                HuginnNotifyService service = new HuginnNotifyService();
                if (service.testConnection(effectiveUrl, effectiveApiKey)) {
                    return FormValidation.ok(Messages.validation_connection_success());
                } else {
                    return FormValidation.error(Messages.validation_connection_failed());
                }
            } catch (Exception e) {
                return FormValidation.error(Messages.validation_connection_error(e.getMessage()));
            }
        }
    }
}
