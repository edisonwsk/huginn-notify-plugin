# Huginn 通知插件配置示例

## 1. 流水线作业示例

### 基本流水线通知

```groovy
pipeline {
    agent any
    stages {
        stage('构建') {
            steps {
                echo '正在构建应用程序...'
                // 您的构建步骤
            }
        }
        stage('测试') {
            steps {
                echo '正在运行测试...'
                // 您的测试步骤
            }
        }
    }
    post {
        always {
            huginnNotify(
                title: '构建完成',
                message: '项目 ${PROJECT_NAME} #${BUILD_NUMBER} 构建${BUILD_RESULT}',
                severity: 'info'
            )
        }
        success {
            huginnNotify(
                title: '构建成功',
                message: '构建成功完成！\n项目：${PROJECT_NAME}\n构建：#${BUILD_NUMBER}\n持续时间：${BUILD_DURATION}',
                severity: 'success'
            )
        }
        failure {
            huginnNotify(
                title: '构建失败',
                message: '项目 ${PROJECT_NAME} #${BUILD_NUMBER} 构建失败\n请检查构建日志：${BUILD_URL}',
                severity: 'error'
            )
        }
        unstable {
            huginnNotify(
                title: '构建不稳定',
                message: '项目 ${PROJECT_NAME} #${BUILD_NUMBER} 构建不稳定\n某些测试可能失败了。',
                severity: 'warning'
            )
        }
    }
}
```

### 带自定义 Webhook 的流水线

```groovy
pipeline {
    agent any
    environment {
        HUGINN_WEBHOOK = 'https://your-huginn.com/users/1/web_requests/1/webhook'
        HUGINN_API_KEY = credentials('huginn-api-key')
    }
    stages {
        stage('部署') {
            steps {
                echo '正在部署应用程序...'
                // 您的部署步骤
            }
            post {
                success {
                    huginnNotify(
                        webhookUrl: "${HUGINN_WEBHOOK}",
                        apiKey: "${HUGINN_API_KEY}",
                        title: '部署成功',
                        message: '应用程序已成功部署到生产环境！\n项目：${PROJECT_NAME}\n版本：${BUILD_NUMBER}',
                        severity: 'success'
                    )
                }
                failure {
                    huginnNotify(
                        webhookUrl: "${HUGINN_WEBHOOK}",
                        apiKey: "${HUGINN_API_KEY}",
                        title: '部署失败',
                        message: '部署失败！\n项目：${PROJECT_NAME}\n构建：#${BUILD_NUMBER}\n错误详情：${BUILD_URL}console',
                        severity: 'error'
                    )
                }
            }
        }
    }
}
```

### 多阶段流水线与条件通知

```groovy
pipeline {
    agent any
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: '部署环境')
        booleanParam(name: 'NOTIFY_TEAM', defaultValue: true, description: '发送团队通知')
    }
    stages {
        stage('构建') {
            steps {
                echo "正在为 ${params.ENVIRONMENT} 环境构建..."
                // 构建步骤
            }
        }
        stage('测试') {
            steps {
                echo '正在运行测试...'
                // 测试步骤
            }
        }
        stage('部署') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo "正在部署到 ${params.ENVIRONMENT}..."
                // 部署步骤
            }
        }
    }
    post {
        success {
            script {
                if (params.NOTIFY_TEAM) {
                    huginnNotify(
                        title: "部署成功 - ${params.ENVIRONMENT.toUpperCase()}",
                        message: """
                        ✅ 部署成功完成！
                        
                        项目：${PROJECT_NAME}
                        环境：${params.ENVIRONMENT}
                        构建：#${BUILD_NUMBER}
                        持续时间：${BUILD_DURATION}
                        
                        变更：${BUILD_URL}changes
                        """,
                        severity: 'success'
                    )
                }
            }
        }
        failure {
            huginnNotify(
                title: "部署失败 - ${params.ENVIRONMENT.toUpperCase()}",
                message: """
                ❌ 部署失败！
                
                项目：${PROJECT_NAME}
                环境：${params.ENVIRONMENT}
                构建：#${BUILD_NUMBER}
                
                控制台：${BUILD_URL}console
                """,
                severity: 'error'
            )
        }
    }
}
```

## 2. 自由风格作业配置

### 基本配置
- **构建后操作**：添加"Huginn 通知"
- **Webhook URL**：留空使用全局配置或指定作业特定的 URL
- **API 密钥**：留空使用全局配置或指定作业特定的密钥
- **通知条件**：
  - ✅ 成功时通知
  - ✅ 失败时通知
  - ✅ 不稳定时通知
  - ✅ 中止时通知

### 自定义消息模板
```
🏗️ 构建通知

项目：${PROJECT_NAME}
构建编号：#${BUILD_NUMBER}
状态：${BUILD_STATUS}
持续时间：${BUILD_DURATION}

📊 详情：${BUILD_URL}
📝 变更：${BUILD_URL}changes
```

## 3. Huginn Webhook 代理配置

### 基本 Webhook 代理

```json
{
  "secret": "your-webhook-secret",
  "expected_receive_period_in_days": "1",
  "payload_path": "event",
  "verbs": "post",
  "code": "
    event = JSON.parse(request.body)['event']
    
    if event
      create_event(payload: {
        title: event['title'],
        content: event['content'],
        severity: event['severity'],
        source: event['source'],
        timestamp: event['timestamp'],
        jenkins_data: event
      })
    end
  "
}
```

### 高级处理代理

```json
{
  "secret": "your-webhook-secret",
  "expected_receive_period_in_days": "1",
  "payload_path": "event",
  "verbs": "post",
  "code": "
    event = JSON.parse(request.body)['event']
    
    if event
      # 根据严重性处理事件
      case event['severity']
      when 'error'
        # 发送到错误通知频道
        create_event(payload: {
          type: 'alert',
          priority: 'high',
          title: event['title'],
          message: event['content'],
          source: 'Jenkins',
          timestamp: event['timestamp']
        })
      when 'success'
        # 记录成功
        create_event(payload: {
          type: 'info',
          title: event['title'],
          message: event['content'],
          source: 'Jenkins'
        })
      else
        # 标准处理
        create_event(payload: event)
      end
    end
  "
}
```

## 4. 消息模板

### 基本模板
```
项目 ${PROJECT_NAME} #${BUILD_NUMBER} 构建${BUILD_RESULT}
持续时间：${BUILD_DURATION}
链接：${BUILD_URL}
```

### 详细模板
```
🏗️ Jenkins 构建通知

📋 项目：${PROJECT_DISPLAY_NAME}
🔢 构建：#${BUILD_NUMBER}
📊 状态：${BUILD_STATUS}
⏱️ 持续时间：${BUILD_DURATION}
📅 开始时间：${BUILD_TIMESTAMP}

🔗 链接：
• 构建详情：${BUILD_URL}
• 控制台输出：${BUILD_URL}console
• 变更记录：${BUILD_URL}changes

Jenkins：${JENKINS_URL}
```

### 条件模板（流水线）
```groovy
script {
    def message = """
    ${currentBuild.result == 'SUCCESS' ? '✅' : '❌'} 构建 ${currentBuild.result}
    
    项目：${env.JOB_NAME}
    构建：#${env.BUILD_NUMBER}
    分支：${env.BRANCH_NAME ?: '不适用'}
    """
    
    if (currentBuild.result != 'SUCCESS') {
        message += "\n🔍 检查日志：${env.BUILD_URL}console"
    }
    
    huginnNotify(
        title: "构建 ${currentBuild.result}",
        message: message,
        severity: currentBuild.result == 'SUCCESS' ? 'success' : 'error'
    )
}
```

## 5. 环境特定配置

### 开发环境
```groovy
// Jenkinsfile
when {
    branch 'develop'
}
post {
    always {
        huginnNotify(
            title: '[开发] 构建通知',
            message: '开发环境构建完成',
            severity: 'info'
        )
    }
}
```

### 生产环境
```groovy
// Jenkinsfile
when {
    branch 'main'
}
post {
    success {
        huginnNotify(
            title: '[生产] 部署成功',
            message: '生产环境部署成功完成',
            severity: 'success'
        )
    }
    failure {
        huginnNotify(
            title: '[生产] 部署失败',
            message: '紧急：生产环境部署失败！',
            severity: 'error'
        )
    }
}
```

## 6. 全局配置示例

### Jenkins 全局配置
```
启用全局通知：✅
全局 Webhook URL：https://huginn.example.com/users/1/web_requests/1/webhook
全局 API 密钥：your-api-key-here
消息语言：自动（跟随系统）
```

### 多个 Huginn 代理
```
代理配置：
1. 名称：开发环境
   Webhook URL：https://huginn.example.com/users/1/web_requests/dev/webhook
   API 密钥：dev-api-key
   启用：✅

2. 名称：生产环境
   Webhook URL：https://huginn.example.com/users/1/web_requests/prod/webhook
   API 密钥：prod-api-key
   启用：✅
```

## 7. 高级用法示例

### 条件通知
```groovy
pipeline {
    agent any
    stages {
        stage('构建') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        huginnNotify(
                            title: '生产构建开始',
                            message: '正在开始生产环境构建...',
                            severity: 'info'
                        )
                    }
                }
                // 构建步骤
            }
        }
    }
}
```

### 带重试的通知
```groovy
post {
    failure {
        script {
            retry(3) {
                huginnNotify(
                    title: '构建失败',
                    message: '构建失败，正在重试通知...',
                    severity: 'error'
                )
            }
        }
    }
}
```

### 动态消息内容
```groovy
post {
    always {
        script {
            def buildTime = new Date(currentBuild.startTimeInMillis).format('yyyy-MM-dd HH:mm:ss')
            def testResults = currentBuild.rawBuild.getAction(hudson.tasks.test.AbstractTestResultAction.class)
            def testInfo = testResults ? 
                "测试：${testResults.totalCount} 个，失败：${testResults.failCount} 个" : 
                "无测试结果"
            
            huginnNotify(
                title: '构建报告',
                message: """
                构建时间：${buildTime}
                项目：${PROJECT_NAME}
                结果：${BUILD_RESULT}
                ${testInfo}
                """,
                severity: 'info'
            )
        }
    }
}
```

## 8. 国际化消息示例

### 中文消息
```groovy
huginnNotify(
    title: '部署通知',
    message: '''
    🚀 应用程序部署完成
    
    项目：${PROJECT_NAME}
    环境：生产
    版本：v${BUILD_NUMBER}
    状态：${BUILD_RESULT == 'SUCCESS' ? '成功' : '失败'}
    
    部署时间：${BUILD_TIMESTAMP}
    ''',
    severity: 'success'
)
```

### 英文消息
```groovy
huginnNotify(
    title: 'Deployment Notification',
    message: '''
    🚀 Application deployment completed
    
    Project: ${PROJECT_NAME}
    Environment: Production
    Version: v${BUILD_NUMBER}
    Status: ${BUILD_RESULT}
    
    Deployment time: ${BUILD_TIMESTAMP}
    ''',
    severity: 'success'
)
```
