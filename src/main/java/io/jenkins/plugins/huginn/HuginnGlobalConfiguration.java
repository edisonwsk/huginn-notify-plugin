package io.jenkins.plugins.huginn;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Huginn通知全局配置
 */
@Extension
@Symbol("huginnGlobalConfig")
public class HuginnGlobalConfiguration extends GlobalConfiguration {
    
    private String globalWebhookUrl;
    private String globalApiKey;
    private boolean globalEnabled;
    private String messageLanguage = "auto"; // 消息语言设置，默认为auto
    private List<HuginnNotifyConfig> configs;
    
    public HuginnGlobalConfiguration() {
        load();
        if (configs == null) {
            configs = new ArrayList<>();
        }
    }
    
    @Nonnull
    @Override
    public String getDisplayName() {
        return Messages.HuginnGlobalConfiguration_DisplayName();
    }
    
    public static HuginnGlobalConfiguration get() {
        return GlobalConfiguration.all().get(HuginnGlobalConfiguration.class);
    }
    
    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        save();
        return true;
    }
    
    public String getGlobalWebhookUrl() {
        return globalWebhookUrl;
    }
    
    public void setGlobalWebhookUrl(String globalWebhookUrl) {
        this.globalWebhookUrl = globalWebhookUrl;
    }
    
    public String getGlobalApiKey() {
        return globalApiKey;
    }
    
    public void setGlobalApiKey(String globalApiKey) {
        this.globalApiKey = globalApiKey;
    }
    
    public boolean isGlobalEnabled() {
        return globalEnabled;
    }
    
    public void setGlobalEnabled(boolean globalEnabled) {
        this.globalEnabled = globalEnabled;
    }
    
    public String getMessageLanguage() {
        return messageLanguage;
    }
    
    public void setMessageLanguage(String messageLanguage) {
        this.messageLanguage = messageLanguage;
    }
    
    public List<HuginnNotifyConfig> getConfigs() {
        return configs;
    }
    
    public void setConfigs(List<HuginnNotifyConfig> configs) {
        this.configs = configs;
    }
    
    public FormValidation doCheckGlobalWebhookUrl(@QueryParameter String value) {
        if (value == null || value.trim().isEmpty()) {
            return FormValidation.ok(Messages.validation_webhookUrl_useGlobal());
        }
        
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            return FormValidation.error(Messages.validation_webhookUrl_invalidFormat());
        }
        
        return FormValidation.ok();
    }
    
    public FormValidation doTestConnection(@QueryParameter String globalWebhookUrl, 
                                         @QueryParameter String globalApiKey) {
        if (globalWebhookUrl == null || globalWebhookUrl.trim().isEmpty()) {
            return FormValidation.error(Messages.validation_connection_needConfig());
        }
        
        try {
            HuginnNotifyService service = new HuginnNotifyService();
            if (service.testConnection(globalWebhookUrl, globalApiKey)) {
                return FormValidation.ok(Messages.validation_connection_success());
            } else {
                return FormValidation.error(Messages.validation_connection_failed());
            }
        } catch (Exception e) {
            return FormValidation.error(Messages.validation_connection_error(e.getMessage()));
        }
    }
}
