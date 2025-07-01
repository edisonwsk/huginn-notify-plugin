[简体中文文档](README_zh_CN.md)

# Huginn Notify Plugin

A Jenkins plugin that sends build notifications to Huginn agents via webhooks.

## Features

- **Multi-platform Support**: Works with both traditional freestyle jobs and pipeline jobs
- **Flexible Configuration**: Global and per-job configuration options
- **Customizable Notifications**: Configure when to send notifications (success, failure, unstable, aborted)
- **Variable Substitution**: Support for Jenkins build variables in custom messages
- **Multi-language Support**: English and Chinese language support
- **Secure Communication**: Support for API key authentication
- **Connection Testing**: Built-in connection test functionality

## Installation

1. Build the plugin:
   ```bash
   mvn clean package
   ```

2. Install the generated `.hpi` file in Jenkins:
   - Go to Jenkins → Manage Jenkins → Manage Plugins
   - Click on "Advanced" tab
   - Upload the `.hpi` file from `target/huginn-notify.hpi`

## Configuration

### Global Configuration

1. Go to Jenkins → Manage Jenkins → Configure System
2. Find the "Huginn Notification Plugin" section
3. Configure the following settings:
   - **Enable Global Notifications**: Enable/disable global notifications
   - **Global Webhook URL**: Default webhook URL for all jobs
   - **Global API Key**: Default API key for authentication
   - **Message Language**: Choose between Auto, English, or Chinese

### Job Configuration

#### For Freestyle Jobs

1. In your job configuration, go to "Post-build Actions"
2. Add "Huginn Notification"
3. Configure the notification settings:
   - **Webhook URL**: Override global webhook URL (optional)
   - **API Key**: Override global API key (optional)
   - **Notification Conditions**: Choose when to send notifications
   - **Custom Message**: Define custom message template (optional)

#### For Pipeline Jobs

Use the `huginnNotify` step in your pipeline:

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                // Your build steps here
            }
        }
    }
    post {
        always {
            huginnNotify(
                webhookUrl: 'https://your-huginn-instance.com/users/1/web_requests/1/webhook',
                apiKey: 'your-api-key',
                title: 'Build Notification',
                message: 'Build ${BUILD_RESULT} for ${PROJECT_NAME} #${BUILD_NUMBER}',
                severity: 'info'
            )
        }
        success {
            huginnNotify(
                title: 'Build Success',
                message: 'Build completed successfully!',
                severity: 'success'
            )
        }
        failure {
            huginnNotify(
                title: 'Build Failed',
                message: 'Build failed. Please check the logs.',
                severity: 'error'
            )
        }
    }
}
```

## Message Variables

You can use the following variables in your custom messages:

- `${BUILD_NUMBER}` - Build number
- `${BUILD_ID}` - Build ID
- `${BUILD_DISPLAY_NAME}` - Build display name
- `${BUILD_URL}` - Build URL
- `${BUILD_DURATION}` - Build duration
- `${BUILD_TIMESTAMP}` - Build timestamp
- `${BUILD_RESULT}` - Build result (SUCCESS, FAILURE, etc.)
- `${BUILD_STATUS}` - Localized build status
- `${PROJECT_NAME}` - Project name
- `${PROJECT_DISPLAY_NAME}` - Project display name
- `${PROJECT_URL}` - Project URL
- `${JENKINS_URL}` - Jenkins root URL

## Huginn Agent Configuration

To receive notifications in Huginn, you need to set up a Webhook Agent:

1. Create a new Webhook Agent in Huginn
2. Configure the agent to receive POST requests
3. Set the webhook URL in the Jenkins plugin configuration
4. Optionally configure authentication using API keys

Example Huginn Webhook Agent configuration:
```json
{
  "secret": "your-secret-key",
  "expected_receive_period_in_days": "1",
  "payload_path": "event"
}
```

## Supported Huginn Event Format

The plugin sends events in the following format:

```json
{
  "event": {
    "title": "Build Notification",
    "content": "Build details and status",
    "severity": "info|success|warning|error",
    "source": "Jenkins",
    "timestamp": 1234567890
  }
}
```

## Building from Source

Requirements:
- Java 11 or higher
- Maven 3.6 or higher

```bash
git clone https://github.com/example/huginn-notify-plugin.git
cd huginn-notify-plugin
mvn clean package
```

The built plugin will be available at `target/huginn-notify.hpi`.

## Development

To run the plugin in development mode:

```bash
mvn hpi:run
```

This will start a Jenkins instance with the plugin installed at `http://localhost:8080/jenkins`.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for your changes
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue on GitHub
- Check existing issues for similar problems
- Provide detailed information about your environment and configuration
