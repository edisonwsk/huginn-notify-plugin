# 多语言指南

本文档提供了在 Huginn 通知插件中实现和使用多语言功能的完整指南。

## 概述

Huginn 通知插件支持多语言国际化，允许用户以其首选语言使用插件。插件目前支持英文和中文，并且可以扩展以支持更多语言。

## 支持的语言

| 语言 | 代码 | 状态 | 覆盖率 |
|------|------|------|--------|
| English | en | ✅ 完整支持 | 100% |
| 简体中文 | zh_CN | ✅ 完整支持 | 100% |
| 日语 | ja | 🚧 计划中 | 0% |
| 韩语 | ko | 🚧 计划中 | 0% |

## 语言配置

### 全局语言设置

1. **访问全局配置**：
   - 进入 Jenkins → 系统管理 → 系统配置
   - 找到"Huginn 通知插件"部分

2. **选择语言**：
   ```
   消息语言: [下拉菜单]
   ├── 自动（跟随系统）
   ├── English
   └── 中文
   ```

3. **保存设置**：
   - 点击"保存"按钮
   - 重启 Jenkins 以确保设置生效

### 语言自动检测

当选择"自动（跟随系统）"时，插件会：

1. 检查 Jenkins 系统的 `Locale.getDefault()`
2. 根据系统语言自动选择：
   - `zh*` → 中文
   - 其他 → 英文

## 本地化内容

### 1. 用户界面

#### 配置页面
- 全局配置标题和描述
- 表单字段标签
- 帮助文本
- 验证消息

#### 构建日志
- 通知状态消息
- 错误和警告信息
- 调试输出

### 2. 消息内容

#### 预定义模板
插件提供多语言的默认消息模板：

**中文模板：**
```
构建通知
项目：${PROJECT_NAME}
构建：#${BUILD_NUMBER}
结果：${BUILD_STATUS}
持续时间：${BUILD_DURATION}
```

**英文模板：**
```
Build Notification
Project: ${PROJECT_NAME}
Build: #${BUILD_NUMBER}
Result: ${BUILD_STATUS}
Duration: ${BUILD_DURATION}
```

#### 构建状态本地化
| 状态 | 英文 | 中文 |
|------|------|------|
| SUCCESS | SUCCESS | 成功 |
| FAILURE | FAILURE | 失败 |
| UNSTABLE | UNSTABLE | 不稳定 |
| ABORTED | ABORTED | 已中止 |
| NOT_BUILT | NOT_BUILT | 未构建 |

## 开发者指南

### 添加新语言支持

#### 1. 创建资源文件

在 `src/main/resources/io/jenkins/plugins/huginn/` 目录下创建：

```
Messages_[语言代码].properties
```

例如，添加日语支持：
```
Messages_ja.properties
```

#### 2. 翻译消息键

复制 `Messages.properties` 并翻译所有值：

```properties
# 全局配置
HuginnGlobalConfiguration.DisplayName=Huginn通知プラグイン

# 通知发布器  
HuginnNotifyPublisher.DisplayName=Huginn通知

# 构建消息
build.title=ビルド通知
build.project=プロジェクト: {0}
build.number=ビルド番号: #{0}
build.result=ビルド結果: {0}
build.duration=期間: {0}
build.url=ビルドURL: {0}

# 构建结果
build.result.success=成功
build.result.failure=失敗
build.result.unstable=不安定
build.result.aborted=中止
build.result.not_built=未ビルド

# 验证消息
validation.webhookUrl.empty=Webhook URLは必須です
validation.connection.success=接続テスト成功！
validation.connection.failed=接続テストに失敗しました

# 日志消息
log.sendSuccess=Huginn通知: メッセージ送信成功
log.sendFailed=Huginn通知: メッセージ送信失敗
```

#### 3. 更新 MessageUtil 类

在 `MessageUtil.java` 中添加新语言的支持：

```java
public static String getLocalizedMessage(String key) {
    HuginnGlobalConfiguration config = HuginnGlobalConfiguration.get();
    String language = config != null ? config.getMessageLanguage() : "auto";
    
    if ("ja".equals(language)) {
        return getJapaneseMessage(key);
    } else if ("zh".equals(language)) {
        return getChineseMessage(key);
    } else if ("en".equals(language)) {
        return getEnglishMessage(key);
    } else {
        // auto - 根据系统语言
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("ja")) {
            return getJapaneseMessage(key);
        } else if (locale.getLanguage().equals("zh")) {
            return getChineseMessage(key);
        } else {
            return getEnglishMessage(key);
        }
    }
}

private static String getJapaneseMessage(String key) {
    switch (key) {
        case "build.title":
            return "ビルド通知";
        case "build.pipelineTitle":
            return "パイプライン通知";
        case "build.testMessage":
            return "これはHuginn通知プラグインからのテストメッセージです";
        default:
            return key;
    }
}
```

#### 4. 更新全局配置

在 `HuginnGlobalConfiguration/config.jelly` 中添加新语言选项：

```xml
<f:entry title="${%Message Language}" field="messageLanguage">
    <f:select>
        <f:option value="auto">${%messageLanguage.auto}</f:option>
        <f:option value="en">${%messageLanguage.en}</f:option>
        <f:option value="zh">${%messageLanguage.zh}</f:option>
        <f:option value="ja">${%messageLanguage.ja}</f:option>
    </f:select>
</f:entry>
```

并在 `Messages.properties` 中添加：
```properties
messageLanguage.ja=Japanese
```

### 使用最佳实践

#### 1. 消息键命名约定

```properties
# 格式：分类.子分类.具体描述
# 示例：
build.title=构建标题
build.result.success=构建成功
validation.webhookUrl.empty=Webhook URL为空
log.notification.sent=通知已发送
```

#### 2. 占位符使用

```properties
# 使用 {0}, {1}, {2} 等作为占位符
build.project=项目：{0}
build.info=项目 {0} 的构建 #{1} 已{2}
```

#### 3. 保持一致性

- 使用统一的术语翻译
- 保持消息格式的一致性
- 确保所有语言版本的完整性

## 用户使用指南

### 选择合适的语言设置

#### 1. 自动检测（推荐）
```
优点：
- 根据系统自动选择
- 无需手动配置
- 适合多用户环境

缺点：
- 可能不符合个人偏好
- 系统语言变更会影响显示
```

#### 2. 手动选择
```
优点：
- 精确控制显示语言
- 不受系统变更影响
- 团队统一标准

缺点：
- 需要手动配置
- 新用户需要额外设置
```

### 自定义消息模板

#### 多语言消息模板

在流水线中使用条件语句创建多语言消息：

```groovy
script {
    // 获取当前语言设置
    def globalConfig = jenkins.model.GlobalConfiguration.all().get(
        io.jenkins.plugins.huginn.HuginnGlobalConfiguration.class
    )
    def language = globalConfig?.messageLanguage ?: 'auto'
    
    // 根据语言选择消息
    def isChinese = (language == 'zh') || 
                   (language == 'auto' && Locale.getDefault().language == 'zh')
    
    def title = isChinese ? '部署通知' : 'Deployment Notification'
    def message = isChinese ? 
        """
        🚀 应用程序部署完成
        项目：${PROJECT_NAME}
        环境：${ENVIRONMENT}
        状态：${BUILD_RESULT == 'SUCCESS' ? '成功' : '失败'}
        """ :
        """
        🚀 Application deployment completed
        Project: ${PROJECT_NAME}  
        Environment: ${ENVIRONMENT}
        Status: ${BUILD_RESULT}
        """
    
    huginnNotify(
        title: title,
        message: message,
        severity: 'info'
    )
}
```

#### 使用变量的本地化

```groovy
// 使用本地化的构建状态
huginnNotify(
    title: 'Build Status',
    message: 'Build ${BUILD_STATUS}',  // 这会显示本地化的状态
    severity: 'info'
)

// 而不是使用原始结果
huginnNotify(
    title: 'Build Status', 
    message: 'Build ${BUILD_RESULT}',  // 这始终显示英文
    severity: 'info'
)
```

## 故障排除

### 常见问题

#### 1. 语言设置不生效

**问题**：更改语言设置后界面仍显示之前的语言

**解决方案**：
```bash
# 1. 检查全局配置是否正确保存
# 2. 重启 Jenkins
# 3. 清除浏览器缓存
# 4. 检查 Jenkins 脚本控制台中的语言设置
println Locale.getDefault()
```

#### 2. 部分内容未本地化

**问题**：某些消息仍显示英文

**可能原因**：
- 消息使用了未本地化的变量（如 `${BUILD_RESULT}`）
- 第三方插件的消息
- 自定义消息模板未更新

**解决方案**：
```groovy
// 错误：使用原始变量
message: 'Build ${BUILD_RESULT}'  

// 正确：使用本地化变量
message: 'Build ${BUILD_STATUS}'
```

#### 3. 新语言不显示

**问题**：添加的新语言在下拉菜单中不显示

**检查清单**：
- [ ] 创建了正确的 `.properties` 文件
- [ ] 文件名格式正确（`Messages_[语言代码].properties`）
- [ ] 更新了 `MessageUtil.java`
- [ ] 更新了全局配置的 Jelly 文件
- [ ] 重新编译并重启 Jenkins

### 调试技巧

#### 1. 检查当前语言设置

在 Jenkins 脚本控制台中运行：

```groovy
// 检查系统语言
println "系统语言: ${Locale.getDefault()}"
println "系统语言代码: ${Locale.getDefault().language}"

// 检查插件语言设置
def config = jenkins.model.GlobalConfiguration.all().get(
    io.jenkins.plugins.huginn.HuginnGlobalConfiguration.class
)
println "插件语言设置: ${config?.messageLanguage}"
```

#### 2. 测试消息本地化

```groovy
// 测试不同语言的消息
import io.jenkins.plugins.huginn.MessageUtil

println "英文消息: ${MessageUtil.getLocalizedMessage('build.title')}"

// 临时切换语言进行测试
def originalLocale = Locale.getDefault()
Locale.setDefault(Locale.SIMPLIFIED_CHINESE)
println "中文消息: ${MessageUtil.getLocalizedMessage('build.title')}"
Locale.setDefault(originalLocale)
```

## 贡献翻译

我们欢迎社区贡献新的语言翻译！

### 贡献流程

1. **Fork 项目**
2. **创建翻译分支**：`git checkout -b add-korean-translation`
3. **添加翻译文件**：按照上述开发者指南创建新语言文件
4. **测试翻译**：确保所有文本正确显示
5. **提交 Pull Request**：描述添加的语言和翻译质量

### 翻译质量标准

- ✅ 完整翻译所有消息键
- ✅ 使用准确的专业术语
- ✅ 保持消息格式一致
- ✅ 测试不同场景下的显示效果
- ✅ 提供本地化的文档示例

### 需要翻译的语言

目前我们欢迎以下语言的翻译贡献：

- 🇯🇵 日语 (Japanese)
- 🇰🇷 韩语 (Korean)  
- 🇩🇪 德语 (German)
- 🇫🇷 法语 (French)
- 🇪🇸 西班牙语 (Spanish)
- 🇷🇺 俄语 (Russian)

## 总结

多语言支持是 Huginn 通知插件的重要特性之一。通过正确的配置和使用，可以为不同语言背景的用户提供友好的使用体验。插件的多语言架构也为社区贡献新语言翻译提供了便利的框架。
