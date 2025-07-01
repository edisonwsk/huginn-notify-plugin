package io.jenkins.plugins.huginn;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.*;

/**
 * 基本功能测试
 */
public class HuginnNotifyPublisherTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testConfigurationRoundTrip() throws Exception {
        // 创建一个自由风格项目
        FreeStyleProject project = jenkins.createFreeStyleProject();
        
        // 创建 Huginn 通知配置
        HuginnNotifyPublisher publisher = new HuginnNotifyPublisher();
        publisher.setWebhookUrl("https://example.com/webhook");
        publisher.setApiKey("test-api-key");
        publisher.setNotifyOnSuccess(true);
        publisher.setNotifyOnFailure(true);
        publisher.setCustomMessage("Test message: ${BUILD_NUMBER}");
        
        // 添加到项目
        project.getPublishersList().add(publisher);
        
        // 保存并重新加载配置
        project = jenkins.configRoundtrip(project);
        
        // 验证配置
        HuginnNotifyPublisher roundTripPublisher = project.getPublishersList().get(HuginnNotifyPublisher.class);
        assertNotNull(roundTripPublisher);
        assertEquals("https://example.com/webhook", roundTripPublisher.getWebhookUrl());
        assertEquals("test-api-key", roundTripPublisher.getApiKey());
        assertTrue(roundTripPublisher.isNotifyOnSuccess());
        assertTrue(roundTripPublisher.isNotifyOnFailure());
        assertEquals("Test message: ${BUILD_NUMBER}", roundTripPublisher.getCustomMessage());
    }

    @Test
    public void testGlobalConfiguration() throws Exception {
        // 获取全局配置
        HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
        assertNotNull(config);
        
        // 设置全局配置
        config.setGlobalEnabled(true);
        config.setGlobalWebhookUrl("https://global.example.com/webhook");
        config.setGlobalApiKey("global-api-key");
        config.setMessageLanguage("en");
        config.save();
        
        // 验证全局配置
        config = HuginnGlobalConfiguration.get();
        assertTrue(config.isGlobalEnabled());
        assertEquals("https://global.example.com/webhook", config.getGlobalWebhookUrl());
        assertEquals("global-api-key", config.getGlobalApiKey());
        assertEquals("en", config.getMessageLanguage());
    }

    @Test
    public void testMessageUtilExpansion() {
        // 创建一个模拟的构建
        FreeStyleProject project = null;
        try {
            project = jenkins.createFreeStyleProject("test-project");
            FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
            
            // 测试消息展开
            String template = "Project: ${PROJECT_NAME}, Build: ${BUILD_NUMBER}, Result: ${BUILD_RESULT}";
            String expanded = MessageUtil.expandMessage(template, build);
            
            assertNotNull(expanded);
            assertTrue(expanded.contains("test-project"));
            assertTrue(expanded.contains("1"));
            
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}
