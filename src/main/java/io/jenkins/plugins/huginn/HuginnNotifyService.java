package io.jenkins.plugins.huginn;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Huginn通知服务类
 */
public class HuginnNotifyService {
    
    private static final Logger LOGGER = Logger.getLogger(HuginnNotifyService.class.getName());
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final int TIMEOUT_SECONDS = 30;
    
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    
    public HuginnNotifyService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 发送消息到Huginn Agent
     */
    public boolean sendMessage(String webhookUrl, String apiKey, String title, String content, String severity) {
        try {
            Map<String, Object> message = createMessage(title, content, severity);
            
            String jsonBody = objectMapper.writeValueAsString(message);
            
            Request.Builder requestBuilder = new Request.Builder()
                    .url(webhookUrl)
                    .post(RequestBody.create(jsonBody, JSON));
            
            // 如果有API密钥，添加到请求头
            if (apiKey != null && !apiKey.trim().isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + apiKey);
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    LOGGER.info(Messages.log_sendSuccess());
                    return true;
                } else {
                    LOGGER.warning(Messages.log_sendFailed() + " - 状态码: " + response.code() + 
                                 ", 响应: " + (response.body() != null ? response.body().string() : "无响应体"));
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, Messages.log_sendFailed(), e);
            return false;
        }
    }
    
    /**
     * 测试连接
     */
    public boolean testConnection(String webhookUrl, String apiKey) {
        return sendMessage(webhookUrl, apiKey, Messages.build_title(), Messages.build_testMessage(), "info");
    }
    
    /**
     * 创建消息体
     */
    private Map<String, Object> createMessage(String title, String content, String severity) {
        Map<String, Object> message = new HashMap<>();
        
        // Huginn Agent事件结构
        Map<String, Object> event = new HashMap<>();
        event.put("title", title);
        event.put("content", content);
        event.put("severity", severity);
        event.put("source", "Jenkins");
        event.put("timestamp", System.currentTimeMillis() / 1000);
        
        message.put("event", event);
        
        return message;
    }
}
