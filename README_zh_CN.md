[English Documentation](README.md)

# Huginn 通知插件

一个用于通过 webhook 向 Huginn 代理发送构建通知的 Jenkins 插件。

## 功能特性

- **多平台支持**：支持传统的自由风格作业和流水线作业
- **灵活配置**：全局和每个作业的配置选项
- **可定制通知**：配置何时发送通知（成功、失败、不稳定、已中止）
- **变量替换**：在自定义消息中支持 Jenkins 构建变量
- **多语言支持**：英文和中文语言支持
- **安全通信**：支持 API 密钥认证
- **连接测试**：内置连接测试功能

## 安装

1. 构建插件：
   ```bash
   mvn clean package
   ```
   或者在 Windows 上运行：
   ```cmd
   build.bat
   ```

2. 在 Jenkins 中安装生成的 `.hpi` 文件：
   - 进入 Jenkins → 系统管理 → 插件管理
   - 点击"高级设置"选项卡
   - 从 `target/huginn-notify.hpi` 上传 `.hpi` 文件

## 配置

### 全局配置

1. 进入 Jenkins → 系统管理 → 系统配置
2. 找到"Huginn 通知插件"部分
3. 配置以下设置：
   - **启用全局通知**：启用/禁用全局通知
   - **全局 Webhook URL**：所有作业的默认 webhook URL
   - **全局 API 密钥**：用于身份验证的默认 API 密钥
   - **消息语言**：选择自动、英文或中文

### 作业配置

#### 自由风格作业

1. 在作业配置中，进入"构建后操作"
2. 添加"Huginn 通知"
3. 配置通知设置：
   - **Webhook URL**：覆盖全局 webhook URL（可选）
   - **API 密钥**：覆盖全局 API 密钥（可选）
   - **通知条件**：选择何时发送通知
   - **自定义消息**：定义自定义消息模板（可选）

#### 流水线作业

在流水线中使用 `huginnNotify` 步骤：

```groovy
pipeline {
    agent any
    stages {
        stage('构建') {
            steps {
                echo '正在构建...'
                // 您的构建步骤
            }
        }
    }
    post {
        always {
            huginnNotify(
                webhookUrl: 'https://your-huginn-instance.com/users/1/web_requests/1/webhook',
                apiKey: 'your-api-key',
                title: '构建通知',
                message: '项目 ${PROJECT_NAME} #${BUILD_NUMBER} 构建${BUILD_RESULT}',
                severity: 'info'
            )
        }
        success {
            huginnNotify(
                title: '构建成功',
                message: '构建成功完成！',
                severity: 'success'
            )
        }
        failure {
            huginnNotify(
                title: '构建失败',
                message: '构建失败，请检查日志。',
                severity: 'error'
            )
        }
    }
}
```

## 消息变量

您可以在自定义消息中使用以下变量：

- `${BUILD_NUMBER}` - 构建编号
- `${BUILD_ID}` - 构建 ID
- `${BUILD_DISPLAY_NAME}` - 构建显示名称
- `${BUILD_URL}` - 构建 URL
- `${BUILD_DURATION}` - 构建持续时间
- `${BUILD_TIMESTAMP}` - 构建时间戳
- `${BUILD_RESULT}` - 构建结果（SUCCESS、FAILURE 等）
- `${BUILD_STATUS}` - 本地化构建状态
- `${PROJECT_NAME}` - 项目名称
- `${PROJECT_DISPLAY_NAME}` - 项目显示名称
- `${PROJECT_URL}` - 项目 URL
- `${JENKINS_URL}` - Jenkins 根 URL

## Huginn 代理配置

要在 Huginn 中接收通知，您需要设置一个 Webhook 代理：

1. 在 Huginn 中创建新的 Webhook 代理
2. 配置代理接收 POST 请求
3. 在 Jenkins 插件配置中设置 webhook URL
4. 可选择使用 API 密钥配置身份验证

Huginn Webhook 代理配置示例：
```json
{
  "secret": "your-secret-key",
  "expected_receive_period_in_days": "1",
  "payload_path": "event"
}
```

## 支持的 Huginn 事件格式

插件以以下格式发送事件：

```json
{
  "event": {
    "title": "构建通知",
    "content": "构建详情和状态",
    "severity": "info|success|warning|error",
    "source": "Jenkins",
    "timestamp": 1234567890
  }
}
```

## 从源码构建

要求：
- Java 11 或更高版本
- Maven 3.6 或更高版本

```bash
git clone https://github.com/example/huginn-notify-plugin.git
cd huginn-notify-plugin
mvn clean package
```

构建的插件将在 `target/huginn-notify.hpi` 中可用。

### Windows 用户

在 Windows 上，您可以直接运行：
```cmd
build.bat
```

此脚本会自动设置 Java 环境并构建插件。

## 开发

以开发模式运行插件：

```bash
mvn hpi:run
```

这将启动一个安装了插件的 Jenkins 实例，地址为 `http://localhost:8080/jenkins`。

## 使用示例

### 基本流水线示例

```groovy
pipeline {
    agent any
    stages {
        stage('构建') {
            steps {
                echo '正在构建应用程序...'
                // 您的构建命令
            }
        }
        stage('测试') {
            steps {
                echo '正在运行测试...'
                // 您的测试命令
            }
        }
    }
    post {
        success {
            huginnNotify(
                title: '🎉 构建成功',
                message: '''
                构建成功完成！
                
                项目：${PROJECT_NAME}
                构建：#${BUILD_NUMBER}
                持续时间：${BUILD_DURATION}
                
                详情：${BUILD_URL}
                ''',
                severity: 'success'
            )
        }
        failure {
            huginnNotify(
                title: '❌ 构建失败',
                message: '''
                构建失败！
                
                项目：${PROJECT_NAME}
                构建：#${BUILD_NUMBER}
                
                控制台输出：${BUILD_URL}console
                ''',
                severity: 'error'
            )
        }
    }
}
```

### 环境特定配置

```groovy
pipeline {
    agent any
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: '部署环境')
    }
    stages {
        stage('部署') {
            steps {
                echo "正在部署到 ${params.ENVIRONMENT} 环境..."
                // 部署步骤
            }
        }
    }
    post {
        success {
            huginnNotify(
                title: "${params.ENVIRONMENT.toUpperCase()} 部署成功",
                message: "应用程序已成功部署到 ${params.ENVIRONMENT} 环境！",
                severity: 'success'
            )
        }
        failure {
            huginnNotify(
                title: "${params.ENVIRONMENT.toUpperCase()} 部署失败",
                message: "部署到 ${params.ENVIRONMENT} 环境失败！请检查日志。",
                severity: 'error'
            )
        }
    }
}
```

## 故障排除

### 常见问题

1. **连接测试失败**：
   - 验证 webhook URL 是否正确且可访问
   - 如果需要身份验证，请检查 API 密钥
   - 确保 Huginn 实例正在运行且可达

2. **未发送通知**：
   - 检查通知条件是否与构建结果匹配
   - 验证是否配置了 webhook URL（全局或作业级别）
   - 检查 Jenkins 系统日志中的错误消息

3. **变量替换不工作**：
   - 确保正确的变量语法：`${VARIABLE_NAME}`
   - 检查变量在构建上下文中是否可用

### 获取帮助

- 检查插件文档
- 查看 Jenkins 系统日志
- 验证 Huginn webhook 代理配置
- 首先使用简单消息进行测试

## 贡献

1. Fork 仓库
2. 创建功能分支
3. 进行更改
4. 为您的更改添加测试
5. 提交拉取请求

## 许可证

此项目根据 MIT 许可证授权 - 有关详细信息，请参阅 LICENSE 文件。

## 支持

如需支持和问题：
- 在 GitHub 上创建问题
- 检查现有问题以查找类似问题
- 提供有关您的环境和配置的详细信息

## 版本历史

### v1.0.0
- 初始版本
- 基本的 Huginn 通知功能
- 支持自由风格和流水线作业
- 全局和作业级别配置
- 多语言支持（英文和中文）
- 变量替换支持
- 连接测试功能
