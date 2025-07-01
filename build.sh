#!/bin/bash

echo "Building Huginn Notify Plugin..."

# Clean and package the plugin
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful!"
    echo "Plugin file: target/huginn-notify.hpi"
    echo ""
    echo "To install:"
    echo "1. Go to Jenkins -> Manage Jenkins -> Manage Plugins"
    echo "2. Click on \"Advanced\" tab"
    echo "3. Upload the huginn-notify.hpi file"
else
    echo ""
    echo "Build failed!"
    exit 1
fi
