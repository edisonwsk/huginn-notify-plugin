# Installation Guide

## Prerequisites

- Jenkins 2.426.1 or higher
- Java 11 or higher
- Maven 3.6 or higher (for building from source)

## Installation Methods

### Method 1: Install from .hpi file

1. **Build the plugin** (if not already built):
   ```bash
   # On Windows
   build.bat
   
   # On Linux/macOS
   ./build.sh
   ```

2. **Upload to Jenkins**:
   - Go to Jenkins Dashboard
   - Navigate to "Manage Jenkins" → "Manage Plugins"
   - Click on the "Advanced" tab
   - In the "Upload Plugin" section, click "Choose File"
   - Select the `huginn-notify.hpi` file from the `target/` directory
   - Click "Upload"
   - Restart Jenkins when prompted

### Method 2: Development Installation

For development and testing:

```bash
mvn hpi:run
```

This will start a Jenkins instance at `http://localhost:8080/jenkins` with the plugin pre-installed.

## Post-Installation Configuration

### 1. Global Configuration

1. Go to "Manage Jenkins" → "Configure System"
2. Scroll down to find "Huginn Notification Plugin" section
3. Configure global settings:
   - **Enable Global Notifications**: Check to enable notifications globally
   - **Global Webhook URL**: Enter your Huginn webhook URL
   - **Global API Key**: Enter your API key (if required)
   - **Message Language**: Select preferred language (Auto/English/Chinese)

### 2. Test Global Configuration

- Click "Test Connection" to verify your Huginn setup
- You should see "Connection test successful!" if everything is configured correctly

### 3. Job-Level Configuration

#### For Freestyle Jobs:
1. Open your job configuration
2. Scroll to "Post-build Actions" section
3. Click "Add post-build action"
4. Select "Huginn Notification"
5. Configure notification preferences:
   - Override webhook URL (optional)
   - Override API key (optional)
   - Select notification conditions (Success, Failure, Unstable, Aborted)
   - Add custom message template (optional)

#### For Pipeline Jobs:
Add the `huginnNotify` step to your pipeline script:

```groovy
huginnNotify(
    title: 'Build Notification',
    message: 'Build ${BUILD_RESULT} for ${PROJECT_NAME}',
    severity: 'info'
)
```

## Verification

1. **Test the configuration**:
   - Run a build in a configured job
   - Check Jenkins console output for notification status
   - Verify that Huginn receives the webhook

2. **Check logs** (if issues occur):
   - Go to "Manage Jenkins" → "System Log"
   - Look for entries related to "HuginnNotifyService"

## Troubleshooting

### Common Issues:

1. **Connection test fails**:
   - Verify webhook URL is correct and accessible
   - Check API key if authentication is required
   - Ensure Huginn instance is running and reachable

2. **Notifications not sent**:
   - Check notification conditions match build results
   - Verify webhook URL is configured (global or job-level)
   - Check Jenkins system logs for error messages

3. **Variable substitution not working**:
   - Ensure correct variable syntax: `${VARIABLE_NAME}`
   - Check that variables are available in the build context

### Getting Help:

- Check the plugin documentation
- Review Jenkins system logs
- Verify Huginn webhook agent configuration
- Test with a simple message first

## Next Steps

After successful installation:
1. Configure your Huginn webhook agents
2. Set up notification rules for your jobs
3. Customize message templates as needed
4. Test with different build scenarios
