package io.jenkins.plugins.huginn;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Huginn通知Pipeline步骤
 */
public class HuginnNotifyStep extends Step implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String webhookUrl;
    private String apiKey;
    private String title;
    private String message;
    private String severity;
    
    @DataBoundConstructor
    public HuginnNotifyStep() {
    }
    
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
    
    public String getTitle() {
        return title;
    }
    
    @DataBoundSetter
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    @DataBoundSetter
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    @DataBoundSetter
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new HuginnNotifyStepExecution(context, this);
    }
    
    @Extension
    public static class DescriptorImpl extends StepDescriptor {
        
        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.singleton(TaskListener.class);
        }
        
        @Override
        public String getFunctionName() {
            return "huginnNotify";
        }
        
        @Override
        public String getDisplayName() {
            return Messages.HuginnNotifyStep_DisplayName();
        }
        
        public FormValidation doCheckMessage(@QueryParameter String value) {
            if (value == null || value.trim().isEmpty()) {
                return FormValidation.error(Messages.validation_message_empty());
            }
            return FormValidation.ok();
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
    
    /**
     * Pipeline步骤执行器
     */
    public static class HuginnNotifyStepExecution extends SynchronousNonBlockingStepExecution<Boolean> {
        
        private static final long serialVersionUID = 1L;
        
        private final HuginnNotifyStep step;
        
        public HuginnNotifyStepExecution(StepContext context, HuginnNotifyStep step) {
            super(context);
            this.step = step;
        }
        
        @Override
        protected Boolean run() throws Exception {
            TaskListener listener = getContext().get(TaskListener.class);
            Run<?, ?> run = getContext().get(Run.class);
            
            // 获取有效的配置
            String effectiveWebhookUrl = getEffectiveWebhookUrl();
            String effectiveApiKey = getEffectiveApiKey();
            
            if (effectiveWebhookUrl == null || effectiveWebhookUrl.trim().isEmpty()) {
                listener.getLogger().println(Messages.log_webhookNotConfigured());
                return false;
            }
            
            // 构建消息
            String title = step.getTitle();
            if (title == null || title.trim().isEmpty()) {
                title = Messages.build_pipelineTitle();
            }
            
            String message = step.getMessage();
            if (message == null || message.trim().isEmpty()) {
                message = buildDefaultMessage(run);
            } else {
                message = MessageUtil.expandMessage(message, run);
            }
            
            String severity = step.getSeverity();
            if (severity == null || severity.trim().isEmpty()) {
                severity = "info";
            }
            
            // 发送通知
            HuginnNotifyService service = new HuginnNotifyService();
            boolean success = service.sendMessage(effectiveWebhookUrl, effectiveApiKey, title, message, severity);
            
            if (success) {
                listener.getLogger().println(Messages.log_sendSuccess());
            } else {
                listener.getLogger().println(Messages.log_sendFailed());
            }
            
            return success;
        }
        
        private String getEffectiveWebhookUrl() {
            if (step.getWebhookUrl() != null && !step.getWebhookUrl().trim().isEmpty()) {
                return step.getWebhookUrl();
            }
            
            HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
            return config != null ? config.getGlobalWebhookUrl() : null;
        }
        
        private String getEffectiveApiKey() {
            if (step.getApiKey() != null && !step.getApiKey().trim().isEmpty()) {
                return step.getApiKey();
            }
            
            HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
            return config != null ? config.getGlobalApiKey() : null;
        }
        
        private String buildDefaultMessage(Run<?, ?> run) {
            StringBuilder message = new StringBuilder();
            message.append(Messages.build_project(run.getParent().getDisplayName())).append("\n");
            message.append(Messages.build_number(run.getNumber())).append("\n");
            message.append(Messages.build_url(run.getAbsoluteUrl()));
            
            return message.toString();
        }
    }
}
