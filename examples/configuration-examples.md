# Huginn Notify Plugin Configuration Examples

## 1. Pipeline Job Examples

### Basic Pipeline Notification

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building application...'
                // Your build steps here
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Your test steps here
            }
        }
    }
    post {
        always {
            huginnNotify(
                title: 'Build Complete',
                message: 'Build ${BUILD_RESULT} for ${PROJECT_NAME} #${BUILD_NUMBER}',
                severity: 'info'
            )
        }
        success {
            huginnNotify(
                title: 'Build Success',
                message: 'Build completed successfully!\nProject: ${PROJECT_NAME}\nBuild: #${BUILD_NUMBER}\nDuration: ${BUILD_DURATION}',
                severity: 'success'
            )
        }
        failure {
            huginnNotify(
                title: 'Build Failed',
                message: 'Build failed for ${PROJECT_NAME} #${BUILD_NUMBER}\nPlease check the build logs: ${BUILD_URL}',
                severity: 'error'
            )
        }
        unstable {
            huginnNotify(
                title: 'Build Unstable',
                message: 'Build unstable for ${PROJECT_NAME} #${BUILD_NUMBER}\nSome tests may have failed.',
                severity: 'warning'
            )
        }
    }
}
```

### Pipeline with Custom Webhook

```groovy
pipeline {
    agent any
    environment {
        HUGINN_WEBHOOK = 'https://your-huginn.com/users/1/web_requests/1/webhook'
        HUGINN_API_KEY = credentials('huginn-api-key')
    }
    stages {
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                // Your deployment steps here
            }
            post {
                success {
                    huginnNotify(
                        webhookUrl: "${HUGINN_WEBHOOK}",
                        apiKey: "${HUGINN_API_KEY}",
                        title: 'Deployment Success',
                        message: 'Application deployed successfully to production!\nProject: ${PROJECT_NAME}\nVersion: ${BUILD_NUMBER}',
                        severity: 'success'
                    )
                }
                failure {
                    huginnNotify(
                        webhookUrl: "${HUGINN_WEBHOOK}",
                        apiKey: "${HUGINN_API_KEY}",
                        title: 'Deployment Failed',
                        message: 'Deployment failed!\nProject: ${PROJECT_NAME}\nBuild: #${BUILD_NUMBER}\nError details: ${BUILD_URL}console',
                        severity: 'error'
                    )
                }
            }
        }
    }
}
```

### Multi-Stage Pipeline with Conditional Notifications

```groovy
pipeline {
    agent any
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
        booleanParam(name: 'NOTIFY_TEAM', defaultValue: true, description: 'Send notifications to team')
    }
    stages {
        stage('Build') {
            steps {
                echo "Building for ${params.ENVIRONMENT}..."
                // Build steps
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Test steps
            }
        }
        stage('Deploy') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo "Deploying to ${params.ENVIRONMENT}..."
                // Deploy steps
            }
        }
    }
    post {
        success {
            script {
                if (params.NOTIFY_TEAM) {
                    huginnNotify(
                        title: "Deployment Success - ${params.ENVIRONMENT.toUpperCase()}",
                        message: """
                        ✅ Deployment completed successfully!
                        
                        Project: ${PROJECT_NAME}
                        Environment: ${params.ENVIRONMENT}
                        Build: #${BUILD_NUMBER}
                        Duration: ${BUILD_DURATION}
                        
                        Changes: ${BUILD_URL}changes
                        """,
                        severity: 'success'
                    )
                }
            }
        }
        failure {
            huginnNotify(
                title: "Deployment Failed - ${params.ENVIRONMENT.toUpperCase()}",
                message: """
                ❌ Deployment failed!
                
                Project: ${PROJECT_NAME}
                Environment: ${params.ENVIRONMENT}
                Build: #${BUILD_NUMBER}
                
                Console: ${BUILD_URL}console
                """,
                severity: 'error'
            )
        }
    }
}
```

## 2. Freestyle Job Configuration

### Basic Configuration
- **Post-build Actions**: Add "Huginn Notification"
- **Webhook URL**: Leave empty to use global config or specify job-specific URL
- **API Key**: Leave empty to use global config or specify job-specific key
- **Notification Conditions**: 
  - ✅ Notify on Success
  - ✅ Notify on Failure
  - ✅ Notify on Unstable
  - ✅ Notify on Aborted

### Custom Message Template
```
🏗️ Build Notification

Project: ${PROJECT_NAME}
Build Number: #${BUILD_NUMBER}
Status: ${BUILD_STATUS}
Duration: ${BUILD_DURATION}

📊 Details: ${BUILD_URL}
📝 Changes: ${BUILD_URL}changes
```

## 3. Huginn Webhook Agent Configuration

### Basic Webhook Agent

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

### Advanced Processing Agent

```json
{
  "secret": "your-webhook-secret",
  "expected_receive_period_in_days": "1",
  "payload_path": "event",
  "verbs": "post",
  "code": "
    event = JSON.parse(request.body)['event']
    
    if event
      # Process the event based on severity
      case event['severity']
      when 'error'
        # Send to error notification channel
        create_event(payload: {
          type: 'alert',
          priority: 'high',
          title: event['title'],
          message: event['content'],
          source: 'Jenkins',
          timestamp: event['timestamp']
        })
      when 'success'
        # Log success
        create_event(payload: {
          type: 'info',
          title: event['title'],
          message: event['content'],
          source: 'Jenkins'
        })
      else
        # Standard processing
        create_event(payload: event)
      end
    end
  "
}
```

## 4. Message Templates

### Basic Template
```
Build ${BUILD_RESULT} for ${PROJECT_NAME} #${BUILD_NUMBER}
Duration: ${BUILD_DURATION}
URL: ${BUILD_URL}
```

### Detailed Template
```
🏗️ Jenkins Build Notification

📋 Project: ${PROJECT_DISPLAY_NAME}
🔢 Build: #${BUILD_NUMBER}
📊 Status: ${BUILD_STATUS}
⏱️ Duration: ${BUILD_DURATION}
📅 Started: ${BUILD_TIMESTAMP}

🔗 Links:
• Build Details: ${BUILD_URL}
• Console Output: ${BUILD_URL}console
• Changes: ${BUILD_URL}changes

Jenkins: ${JENKINS_URL}
```

### Conditional Template (Pipeline)
```groovy
script {
    def message = """
    ${currentBuild.result == 'SUCCESS' ? '✅' : '❌'} Build ${currentBuild.result}
    
    Project: ${env.JOB_NAME}
    Build: #${env.BUILD_NUMBER}
    Branch: ${env.BRANCH_NAME ?: 'N/A'}
    """
    
    if (currentBuild.result != 'SUCCESS') {
        message += "\n🔍 Check logs: ${env.BUILD_URL}console"
    }
    
    huginnNotify(
        title: "Build ${currentBuild.result}",
        message: message,
        severity: currentBuild.result == 'SUCCESS' ? 'success' : 'error'
    )
}
```

## 5. Environment-Specific Configuration

### Development Environment
```groovy
// Jenkinsfile
when {
    branch 'develop'
}
post {
    always {
        huginnNotify(
            title: '[DEV] Build Notification',
            message: 'Development build completed',
            severity: 'info'
        )
    }
}
```

### Production Environment
```groovy
// Jenkinsfile
when {
    branch 'main'
}
post {
    success {
        huginnNotify(
            title: '[PROD] Deployment Success',
            message: 'Production deployment completed successfully',
            severity: 'success'
        )
    }
    failure {
        huginnNotify(
            title: '[PROD] Deployment Failed',
            message: 'URGENT: Production deployment failed!',
            severity: 'error'
        )
    }
}
```

## 6. Global Configuration Examples

### Jenkins Global Config
```
Enable Global Notifications: ✅
Global Webhook URL: https://huginn.example.com/users/1/web_requests/1/webhook
Global API Key: your-api-key-here
Message Language: Auto (Follow System)
```

### Multiple Huginn Agents
```
Agent Configurations:
1. Name: Development
   Webhook URL: https://huginn.example.com/users/1/web_requests/dev/webhook
   API Key: dev-api-key
   Enabled: ✅

2. Name: Production
   Webhook URL: https://huginn.example.com/users/1/web_requests/prod/webhook
   API Key: prod-api-key
   Enabled: ✅
```
