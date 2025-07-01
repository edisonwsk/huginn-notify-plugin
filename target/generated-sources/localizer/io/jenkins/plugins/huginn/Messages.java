// CHECKSTYLE:OFF

package io.jenkins.plugins.huginn;

import org.jvnet.localizer.Localizable;
import org.jvnet.localizer.ResourceBundleHolder;
import org.kohsuke.accmod.Restricted;


/**
 * Generated localization support class.
 * 
 */
@SuppressWarnings({
    "",
    "PMD",
    "all"
})
@Restricted(org.kohsuke.accmod.restrictions.NoExternalUse.class)
public class Messages {

    /**
     * The resource bundle reference
     * 
     */
    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(Messages.class);

    /**
     * Key {@code build.result.not_built}: {@code NOT_BUILT}.
     * 
     * @return
     *     {@code NOT_BUILT}
     */
    public static String build_result_not_built() {
        return holder.format("build.result.not_built");
    }

    /**
     * Key {@code build.result.not_built}: {@code NOT_BUILT}.
     * 
     * @return
     *     {@code NOT_BUILT}
     */
    public static Localizable _build_result_not_built() {
        return new Localizable(holder, "build.result.not_built");
    }

    /**
     * Key {@code validation.name.empty}: {@code Configuration name cannot be
     * empty}.
     * 
     * @return
     *     {@code Configuration name cannot be empty}
     */
    public static String validation_name_empty() {
        return holder.format("validation.name.empty");
    }

    /**
     * Key {@code validation.name.empty}: {@code Configuration name cannot be
     * empty}.
     * 
     * @return
     *     {@code Configuration name cannot be empty}
     */
    public static Localizable _validation_name_empty() {
        return new Localizable(holder, "validation.name.empty");
    }

    /**
     * Key {@code build.result.aborted}: {@code ABORTED}.
     * 
     * @return
     *     {@code ABORTED}
     */
    public static String build_result_aborted() {
        return holder.format("build.result.aborted");
    }

    /**
     * Key {@code build.result.aborted}: {@code ABORTED}.
     * 
     * @return
     *     {@code ABORTED}
     */
    public static Localizable _build_result_aborted() {
        return new Localizable(holder, "build.result.aborted");
    }

    /**
     * Key {@code validation.connection.success}: {@code Connection test
     * successful!}.
     * 
     * @return
     *     {@code Connection test successful!}
     */
    public static String validation_connection_success() {
        return holder.format("validation.connection.success");
    }

    /**
     * Key {@code validation.connection.success}: {@code Connection test
     * successful!}.
     * 
     * @return
     *     {@code Connection test successful!}
     */
    public static Localizable _validation_connection_success() {
        return new Localizable(holder, "validation.connection.success");
    }

    /**
     * Key {@code build.url}: {@code Build URL: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Build URL: {0}}
     */
    public static String build_url(Object arg0) {
        return holder.format("build.url", arg0);
    }

    /**
     * Key {@code build.url}: {@code Build URL: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Build URL: {0}}
     */
    public static Localizable _build_url(Object arg0) {
        return new Localizable(holder, "build.url", arg0);
    }

    /**
     * Key {@code HuginnNotifyPublisher.DisplayName}: {@code Huginn
     * Notification}.
     * 
     * @return
     *     {@code Huginn Notification}
     */
    public static String HuginnNotifyPublisher_DisplayName() {
        return holder.format("HuginnNotifyPublisher.DisplayName");
    }

    /**
     * Key {@code HuginnNotifyPublisher.DisplayName}: {@code Huginn
     * Notification}.
     * 
     * @return
     *     {@code Huginn Notification}
     */
    public static Localizable _HuginnNotifyPublisher_DisplayName() {
        return new Localizable(holder, "HuginnNotifyPublisher.DisplayName");
    }

    /**
     * Key {@code validation.webhookUrl.invalidFormat}: {@code Webhook URL
     * must start with http:// or https://}.
     * 
     * @return
     *     {@code Webhook URL must start with http:// or https://}
     */
    public static String validation_webhookUrl_invalidFormat() {
        return holder.format("validation.webhookUrl.invalidFormat");
    }

    /**
     * Key {@code validation.webhookUrl.invalidFormat}: {@code Webhook URL
     * must start with http:// or https://}.
     * 
     * @return
     *     {@code Webhook URL must start with http:// or https://}
     */
    public static Localizable _validation_webhookUrl_invalidFormat() {
        return new Localizable(holder, "validation.webhookUrl.invalidFormat");
    }

    /**
     * Key {@code build.testMessage}: {@code This is a test message from
     * Huginn Notify Plugin}.
     * 
     * @return
     *     {@code This is a test message from Huginn Notify Plugin}
     */
    public static String build_testMessage() {
        return holder.format("build.testMessage");
    }

    /**
     * Key {@code build.testMessage}: {@code This is a test message from
     * Huginn Notify Plugin}.
     * 
     * @return
     *     {@code This is a test message from Huginn Notify Plugin}
     */
    public static Localizable _build_testMessage() {
        return new Localizable(holder, "build.testMessage");
    }

    /**
     * Key {@code build.result.failure}: {@code FAILURE}.
     * 
     * @return
     *     {@code FAILURE}
     */
    public static String build_result_failure() {
        return holder.format("build.result.failure");
    }

    /**
     * Key {@code build.result.failure}: {@code FAILURE}.
     * 
     * @return
     *     {@code FAILURE}
     */
    public static Localizable _build_result_failure() {
        return new Localizable(holder, "build.result.failure");
    }

    /**
     * Key {@code validation.webhookUrl.useGlobal}: {@code Using global
     * webhook URL}.
     * 
     * @return
     *     {@code Using global webhook URL}
     */
    public static String validation_webhookUrl_useGlobal() {
        return holder.format("validation.webhookUrl.useGlobal");
    }

    /**
     * Key {@code validation.webhookUrl.useGlobal}: {@code Using global
     * webhook URL}.
     * 
     * @return
     *     {@code Using global webhook URL}
     */
    public static Localizable _validation_webhookUrl_useGlobal() {
        return new Localizable(holder, "validation.webhookUrl.useGlobal");
    }

    /**
     * Key {@code HuginnNotifyStep.DisplayName}: {@code Send Huginn
     * Notification}.
     * 
     * @return
     *     {@code Send Huginn Notification}
     */
    public static String HuginnNotifyStep_DisplayName() {
        return holder.format("HuginnNotifyStep.DisplayName");
    }

    /**
     * Key {@code HuginnNotifyStep.DisplayName}: {@code Send Huginn
     * Notification}.
     * 
     * @return
     *     {@code Send Huginn Notification}
     */
    public static Localizable _HuginnNotifyStep_DisplayName() {
        return new Localizable(holder, "HuginnNotifyStep.DisplayName");
    }

    /**
     * Key {@code log.sendFailed}: {@code Huginn Notification: Message send
     * failed}.
     * 
     * @return
     *     {@code Huginn Notification: Message send failed}
     */
    public static String log_sendFailed() {
        return holder.format("log.sendFailed");
    }

    /**
     * Key {@code log.sendFailed}: {@code Huginn Notification: Message send
     * failed}.
     * 
     * @return
     *     {@code Huginn Notification: Message send failed}
     */
    public static Localizable _log_sendFailed() {
        return new Localizable(holder, "log.sendFailed");
    }

    /**
     * Key {@code build.result.success}: {@code SUCCESS}.
     * 
     * @return
     *     {@code SUCCESS}
     */
    public static String build_result_success() {
        return holder.format("build.result.success");
    }

    /**
     * Key {@code build.result.success}: {@code SUCCESS}.
     * 
     * @return
     *     {@code SUCCESS}
     */
    public static Localizable _build_result_success() {
        return new Localizable(holder, "build.result.success");
    }

    /**
     * Key {@code messageLanguage.en}: {@code English}.
     * 
     * @return
     *     {@code English}
     */
    public static String messageLanguage_en() {
        return holder.format("messageLanguage.en");
    }

    /**
     * Key {@code messageLanguage.en}: {@code English}.
     * 
     * @return
     *     {@code English}
     */
    public static Localizable _messageLanguage_en() {
        return new Localizable(holder, "messageLanguage.en");
    }

    /**
     * Key {@code HuginnGlobalConfiguration.DisplayName}: {@code Huginn
     * Notification Plugin}.
     * 
     * @return
     *     {@code Huginn Notification Plugin}
     */
    public static String HuginnGlobalConfiguration_DisplayName() {
        return holder.format("HuginnGlobalConfiguration.DisplayName");
    }

    /**
     * Key {@code HuginnGlobalConfiguration.DisplayName}: {@code Huginn
     * Notification Plugin}.
     * 
     * @return
     *     {@code Huginn Notification Plugin}
     */
    public static Localizable _HuginnGlobalConfiguration_DisplayName() {
        return new Localizable(holder, "HuginnGlobalConfiguration.DisplayName");
    }

    /**
     * Key {@code messageLanguage.zh}: {@code Chinese}.
     * 
     * @return
     *     {@code Chinese}
     */
    public static String messageLanguage_zh() {
        return holder.format("messageLanguage.zh");
    }

    /**
     * Key {@code messageLanguage.zh}: {@code Chinese}.
     * 
     * @return
     *     {@code Chinese}
     */
    public static Localizable _messageLanguage_zh() {
        return new Localizable(holder, "messageLanguage.zh");
    }

    /**
     * Key {@code log.skipNotification}: {@code Huginn Notification: Skipping
     * notification for build result: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Huginn Notification: Skipping notification for build result:
     *     {0}}
     */
    public static String log_skipNotification(Object arg0) {
        return holder.format("log.skipNotification", arg0);
    }

    /**
     * Key {@code log.skipNotification}: {@code Huginn Notification: Skipping
     * notification for build result: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Huginn Notification: Skipping notification for build result:
     *     {0}}
     */
    public static Localizable _log_skipNotification(Object arg0) {
        return new Localizable(holder, "log.skipNotification", arg0);
    }

    /**
     * Key {@code validation.message.empty}: {@code Message content cannot be
     * empty}.
     * 
     * @return
     *     {@code Message content cannot be empty}
     */
    public static String validation_message_empty() {
        return holder.format("validation.message.empty");
    }

    /**
     * Key {@code validation.message.empty}: {@code Message content cannot be
     * empty}.
     * 
     * @return
     *     {@code Message content cannot be empty}
     */
    public static Localizable _validation_message_empty() {
        return new Localizable(holder, "validation.message.empty");
    }

    /**
     * Key {@code build.pipelineTitle}: {@code Pipeline Notification}.
     * 
     * @return
     *     {@code Pipeline Notification}
     */
    public static String build_pipelineTitle() {
        return holder.format("build.pipelineTitle");
    }

    /**
     * Key {@code build.pipelineTitle}: {@code Pipeline Notification}.
     * 
     * @return
     *     {@code Pipeline Notification}
     */
    public static Localizable _build_pipelineTitle() {
        return new Localizable(holder, "build.pipelineTitle");
    }

    /**
     * Key {@code messageLanguage.auto}: {@code Auto (Follow System)}.
     * 
     * @return
     *     {@code Auto (Follow System)}
     */
    public static String messageLanguage_auto() {
        return holder.format("messageLanguage.auto");
    }

    /**
     * Key {@code messageLanguage.auto}: {@code Auto (Follow System)}.
     * 
     * @return
     *     {@code Auto (Follow System)}
     */
    public static Localizable _messageLanguage_auto() {
        return new Localizable(holder, "messageLanguage.auto");
    }

    /**
     * Key {@code validation.connection.failed}: {@code Connection test
     * failed}.
     * 
     * @return
     *     {@code Connection test failed}
     */
    public static String validation_connection_failed() {
        return holder.format("validation.connection.failed");
    }

    /**
     * Key {@code validation.connection.failed}: {@code Connection test
     * failed}.
     * 
     * @return
     *     {@code Connection test failed}
     */
    public static Localizable _validation_connection_failed() {
        return new Localizable(holder, "validation.connection.failed");
    }

    /**
     * Key {@code validation.connection.error}: {@code Connection test error:
     * {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Connection test error: {0}}
     */
    public static String validation_connection_error(Object arg0) {
        return holder.format("validation.connection.error", arg0);
    }

    /**
     * Key {@code validation.connection.error}: {@code Connection test error:
     * {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Connection test error: {0}}
     */
    public static Localizable _validation_connection_error(Object arg0) {
        return new Localizable(holder, "validation.connection.error", arg0);
    }

    /**
     * Key {@code validation.webhookUrl.needGlobal}: {@code Webhook URL is
     * required. Please configure in job settings or global configuration.}.
     * 
     * @return
     *     {@code Webhook URL is required. Please configure in job settings or
     *     global configuration.}
     */
    public static String validation_webhookUrl_needGlobal() {
        return holder.format("validation.webhookUrl.needGlobal");
    }

    /**
     * Key {@code validation.webhookUrl.needGlobal}: {@code Webhook URL is
     * required. Please configure in job settings or global configuration.}.
     * 
     * @return
     *     {@code Webhook URL is required. Please configure in job settings or
     *     global configuration.}
     */
    public static Localizable _validation_webhookUrl_needGlobal() {
        return new Localizable(holder, "validation.webhookUrl.needGlobal");
    }

    /**
     * Key {@code validation.connection.needConfig}: {@code Connection test
     * requires webhook URL configuration}.
     * 
     * @return
     *     {@code Connection test requires webhook URL configuration}
     */
    public static String validation_connection_needConfig() {
        return holder.format("validation.connection.needConfig");
    }

    /**
     * Key {@code validation.connection.needConfig}: {@code Connection test
     * requires webhook URL configuration}.
     * 
     * @return
     *     {@code Connection test requires webhook URL configuration}
     */
    public static Localizable _validation_connection_needConfig() {
        return new Localizable(holder, "validation.connection.needConfig");
    }

    /**
     * Key {@code log.sendSuccess}: {@code Huginn Notification: Message sent
     * successfully}.
     * 
     * @return
     *     {@code Huginn Notification: Message sent successfully}
     */
    public static String log_sendSuccess() {
        return holder.format("log.sendSuccess");
    }

    /**
     * Key {@code log.sendSuccess}: {@code Huginn Notification: Message sent
     * successfully}.
     * 
     * @return
     *     {@code Huginn Notification: Message sent successfully}
     */
    public static Localizable _log_sendSuccess() {
        return new Localizable(holder, "log.sendSuccess");
    }

    /**
     * Key {@code build.duration}: {@code Duration: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Duration: {0}}
     */
    public static String build_duration(Object arg0) {
        return holder.format("build.duration", arg0);
    }

    /**
     * Key {@code build.duration}: {@code Duration: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Duration: {0}}
     */
    public static Localizable _build_duration(Object arg0) {
        return new Localizable(holder, "build.duration", arg0);
    }

    /**
     * Key {@code build.title}: {@code Build Notification}.
     * 
     * @return
     *     {@code Build Notification}
     */
    public static String build_title() {
        return holder.format("build.title");
    }

    /**
     * Key {@code build.title}: {@code Build Notification}.
     * 
     * @return
     *     {@code Build Notification}
     */
    public static Localizable _build_title() {
        return new Localizable(holder, "build.title");
    }

    /**
     * Key {@code HuginnNotifyConfig.DisplayName}: {@code Huginn Notification
     * Configuration}.
     * 
     * @return
     *     {@code Huginn Notification Configuration}
     */
    public static String HuginnNotifyConfig_DisplayName() {
        return holder.format("HuginnNotifyConfig.DisplayName");
    }

    /**
     * Key {@code HuginnNotifyConfig.DisplayName}: {@code Huginn Notification
     * Configuration}.
     * 
     * @return
     *     {@code Huginn Notification Configuration}
     */
    public static Localizable _HuginnNotifyConfig_DisplayName() {
        return new Localizable(holder, "HuginnNotifyConfig.DisplayName");
    }

    /**
     * Key {@code build.project}: {@code Project: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Project: {0}}
     */
    public static String build_project(Object arg0) {
        return holder.format("build.project", arg0);
    }

    /**
     * Key {@code build.project}: {@code Project: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Project: {0}}
     */
    public static Localizable _build_project(Object arg0) {
        return new Localizable(holder, "build.project", arg0);
    }

    /**
     * Key {@code build.result}: {@code Build Result: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Build Result: {0}}
     */
    public static String build_result(Object arg0) {
        return holder.format("build.result", arg0);
    }

    /**
     * Key {@code build.result}: {@code Build Result: {0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Build Result: {0}}
     */
    public static Localizable _build_result(Object arg0) {
        return new Localizable(holder, "build.result", arg0);
    }

    /**
     * Key {@code build.number}: {@code Build Number: #{0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Build Number: #{0}}
     */
    public static String build_number(Object arg0) {
        return holder.format("build.number", arg0);
    }

    /**
     * Key {@code build.number}: {@code Build Number: #{0}}.
     * 
     * @param arg0
     *      1st format parameter, {@code {0}}, as {@link String#valueOf(Object)}.
     * @return
     *     {@code Build Number: #{0}}
     */
    public static Localizable _build_number(Object arg0) {
        return new Localizable(holder, "build.number", arg0);
    }

    /**
     * Key {@code validation.webhookUrl.empty}: {@code Webhook URL cannot be
     * empty}.
     * 
     * @return
     *     {@code Webhook URL cannot be empty}
     */
    public static String validation_webhookUrl_empty() {
        return holder.format("validation.webhookUrl.empty");
    }

    /**
     * Key {@code validation.webhookUrl.empty}: {@code Webhook URL cannot be
     * empty}.
     * 
     * @return
     *     {@code Webhook URL cannot be empty}
     */
    public static Localizable _validation_webhookUrl_empty() {
        return new Localizable(holder, "validation.webhookUrl.empty");
    }

    /**
     * Key {@code build.result.unstable}: {@code UNSTABLE}.
     * 
     * @return
     *     {@code UNSTABLE}
     */
    public static String build_result_unstable() {
        return holder.format("build.result.unstable");
    }

    /**
     * Key {@code build.result.unstable}: {@code UNSTABLE}.
     * 
     * @return
     *     {@code UNSTABLE}
     */
    public static Localizable _build_result_unstable() {
        return new Localizable(holder, "build.result.unstable");
    }

    /**
     * Key {@code log.webhookNotConfigured}: {@code Huginn Notification:
     * Webhook URL not configured, skipping notification}.
     * 
     * @return
     *     {@code Huginn Notification: Webhook URL not configured, skipping
     *     notification}
     */
    public static String log_webhookNotConfigured() {
        return holder.format("log.webhookNotConfigured");
    }

    /**
     * Key {@code log.webhookNotConfigured}: {@code Huginn Notification:
     * Webhook URL not configured, skipping notification}.
     * 
     * @return
     *     {@code Huginn Notification: Webhook URL not configured, skipping
     *     notification}
     */
    public static Localizable _log_webhookNotConfigured() {
        return new Localizable(holder, "log.webhookNotConfigured");
    }

}
