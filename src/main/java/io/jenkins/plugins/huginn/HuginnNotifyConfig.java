package io.jenkins.plugins.huginn;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * Huginn通知配置
 */
public class HuginnNotifyConfig extends AbstractDescribableImpl<HuginnNotifyConfig> {
    
    private final String name;
    private final String webhookUrl;
    private final String apiKey;
    private final boolean enabled;
    
    @DataBoundConstructor
    public HuginnNotifyConfig(String name, String webhookUrl, String apiKey, boolean enabled) {
        this.name = name;
        this.webhookUrl = webhookUrl;
        this.apiKey = apiKey;
        this.enabled = enabled;
    }
    
    public String getName() {
        return name;
    }
    
    public String getWebhookUrl() {
        return webhookUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    @Extension
    public static class DescriptorImpl extends Descriptor<HuginnNotifyConfig> {
        
        @Override
        public String getDisplayName() {
            return Messages.HuginnNotifyConfig_DisplayName();
        }
        
        public FormValidation doCheckName(@QueryParameter String value) {
            if (value == null || value.trim().isEmpty()) {
                return FormValidation.error(Messages.validation_name_empty());
            }
            return FormValidation.ok();
        }
        
        public FormValidation doCheckWebhookUrl(@QueryParameter String value) {
            if (value == null || value.trim().isEmpty()) {
                return FormValidation.error(Messages.validation_webhookUrl_empty());
            }
            if (!value.startsWith("http://") && !value.startsWith("https://")) {
                return FormValidation.error(Messages.validation_webhookUrl_invalidFormat());
            }
            return FormValidation.ok();
        }
        
        public FormValidation doTestConnection(@QueryParameter String webhookUrl, 
                                             @QueryParameter String apiKey) {
            if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
                return FormValidation.error(Messages.validation_connection_needConfig());
            }
            
            try {
                HuginnNotifyService service = new HuginnNotifyService();
                if (service.testConnection(webhookUrl, apiKey)) {
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
