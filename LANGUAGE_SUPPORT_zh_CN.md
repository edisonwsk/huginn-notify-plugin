# 语言支持

Huginn 通知插件提供多语言支持，目前支持英文和中文。

## 支持的语言

- **英文 (English)** - 默认语言
- **中文 (简体中文)** - 完整本地化支持

## 语言配置

### 全局语言设置

在 Jenkins 全局配置中，您可以设置插件的语言偏好：

1. 进入"系统管理"→"系统配置"
2. 找到"Huginn 通知插件"部分
3. 在"消息语言"下拉菜单中选择：
   - **自动（跟随系统）**：根据 Jenkins 系统语言自动选择
   - **English**：强制使用英文
   - **中文**：强制使用中文

### 语言选择逻辑

插件使用以下逻辑来确定显示语言：

1. 如果全局配置设置为"English"，则始终使用英文
2. 如果全局配置设置为"中文"，则始终使用中文
3. 如果全局配置设置为"自动"：
   - 检查 Jenkins 系统的默认 Locale
   - 如果系统语言是中文（zh），则使用中文
   - 否则使用英文作为默认语言

## 本地化内容

### 界面文本

插件的所有界面元素都已本地化：

#### 配置页面
- 全局配置标题和描述
- 作业配置选项
- 表单验证消息
- 帮助文本

#### 构建日志消息
- 通知发送状态
- 错误和警告消息
- 调试信息

### 预定义消息模板

插件提供本地化的默认消息模板：

#### 中文模板
```
构建通知
项目：${PROJECT_NAME}
构建编号：#${BUILD_NUMBER}
构建结果：${BUILD_STATUS}
持续时间：${BUILD_DURATION}
链接：${BUILD_URL}
```

#### 英文模板
```
Build Notification
Project: ${PROJECT_NAME}
Build Number: #${BUILD_NUMBER}
Build Result: ${BUILD_STATUS}
Duration: ${BUILD_DURATION}
URL: ${BUILD_URL}
```

## 自定义消息本地化

### 在流水线中使用本地化消息

您可以在流水线脚本中根据语言设置动态选择消息：

```groovy
script {
    def isChineseLocale = java.util.Locale.getDefault().language == 'zh'
    
    def title = isChineseLocale ? '构建通知' : 'Build Notification'
    def message = isChineseLocale ? 
        "项目 ${PROJECT_NAME} 构建${BUILD_RESULT}" :
        "Project ${PROJECT_NAME} build ${BUILD_RESULT}"
    
    huginnNotify(
        title: title,
        message: message,
        severity: 'info'
    )
}
```

### 使用 MessageUtil 获取本地化消息

在自定义 Groovy 脚本中，您可以使用插件的 MessageUtil 类：

```groovy
// 这个功能在插件内部使用，展示了如何实现本地化
import io.jenkins.plugins.huginn.MessageUtil

script {
    def localizedTitle = MessageUtil.getLocalizedMessage('build.title')
    def localizedTest = MessageUtil.getLocalizedMessage('build.testMessage')
    
    huginnNotify(
        title: localizedTitle,
        message: localizedTest,
        severity: 'info'
    )
}
```

## 构建结果本地化

构建结果状态会根据语言设置进行本地化：

### 中文
- SUCCESS → 成功
- FAILURE → 失败
- UNSTABLE → 不稳定
- ABORTED → 已中止
- NOT_BUILT → 未构建

### 英文
- SUCCESS → SUCCESS
- FAILURE → FAILURE
- UNSTABLE → UNSTABLE
- ABORTED → ABORTED
- NOT_BUILT → NOT_BUILT

## 时间格式本地化

时间戳格式会根据语言设置进行调整：

### 中文格式
```
${BUILD_TIMESTAMP} → 2025-06-30 13:45:30
```

### 英文格式
```
${BUILD_TIMESTAMP} → 2025-06-30 13:45:30
```

## 贡献翻译

如果您想为插件添加新的语言支持，请按照以下步骤：

### 1. 创建资源文件

在 `src/main/resources/io/jenkins/plugins/huginn/` 目录下创建新的属性文件：

- `Messages_[language_code].properties` - 新语言的消息
- 例如：`Messages_ja.properties` 用于日语

### 2. 翻译内容

复制 `Messages.properties` 的内容并翻译所有键值：

```properties
# 全局配置
HuginnGlobalConfiguration.DisplayName=Huginn通知プラグイン

# 通知发布器
HuginnNotifyPublisher.DisplayName=Huginn通知

# 构建消息
build.title=ビルド通知
build.project=プロジェクト: {0}
```

### 3. 更新 MessageUtil

在 `MessageUtil.java` 中添加新语言的支持：

```java
private static String getLocalizedMessage(String key, String language) {
    if ("ja".equals(language)) {
        return getJapaneseMessage(key);
    }
    // 其他语言...
}

private static String getJapaneseMessage(String key) {
    switch (key) {
        case "build.title":
            return "ビルド通知";
        // 其他键...
    }
}
```

### 4. 更新全局配置

在全局配置的下拉菜单中添加新语言选项。

## 故障排除

### 语言显示不正确

1. **检查全局配置**：
   - 确认在全局配置中选择了正确的语言
   - 保存配置后重启 Jenkins

2. **检查系统 Locale**：
   - 如果使用"自动"设置，检查 Jenkins 的系统 Locale
   - 在 Jenkins 脚本控制台中运行：`println Locale.getDefault()`

3. **浏览器语言**：
   - 某些情况下，浏览器的语言设置可能影响显示
   - 尝试清除浏览器缓存

### 自定义消息不本地化

1. **使用正确的变量**：
   - 确保使用 `${BUILD_STATUS}` 而不是 `${BUILD_RESULT}` 来获取本地化的状态
   - `${BUILD_RESULT}` 始终返回英文值

2. **检查语言设置**：
   - 验证全局语言配置是否正确应用
   - 重新启动 Jenkins 以确保设置生效

## 最佳实践

### 1. 一致的语言使用

- 在全局配置中设置统一的语言偏好
- 避免在同一个 Jenkins 实例中混合使用多种语言

### 2. 自定义消息模板

- 为不同语言创建单独的消息模板
- 使用条件逻辑动态选择合适的模板

### 3. 团队协作

- 确保团队成员了解当前的语言设置
- 在文档中明确说明使用的语言偏好

### 4. 测试多语言支持

- 在不同语言设置下测试插件功能
- 验证所有消息都正确本地化

## 技术实现细节

### 资源文件结构

```
src/main/resources/io/jenkins/plugins/huginn/
├── Messages.properties                 # 英文（默认）
├── Messages_zh_CN.properties         # 简体中文
└── [其他语言文件]
```

### 消息键命名约定

```properties
# 分类.功能.描述
build.title=构建标题
build.result.success=成功结果
validation.webhookUrl.empty=验证错误
log.sendSuccess=日志消息
```

### Jenkins 本地化机制

插件使用 Jenkins 的标准本地化机制：
- Properties 文件用于静态文本
- Jelly 文件中的 `${%key}` 语法
- Java 代码中的 `Messages.key()` 调用
