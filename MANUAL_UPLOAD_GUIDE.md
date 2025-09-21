# Manual Upload Guide (No Git Required)

## Step 1: Create GitHub Repository
1. Go to https://github.com/new
2. Repository name: `xposed-module`
3. Make it **Public**
4. Click **"Create repository"**

## Step 2: Upload Files Manually
1. **Download all files** from your XposedModule folder
2. **Zip the entire folder**
3. **Upload to GitHub**:
   - Go to your new repository
   - Click **"uploading an existing file"**
   - Drag and drop your zip file
   - Or click **"choose your files"** and select the zip

## Step 3: Extract Files on GitHub
1. After uploading, **extract the zip file**
2. **Delete the zip file**
3. **Commit the changes**

## Step 4: Enable GitHub Actions
1. Go to **"Actions"** tab in your repository
2. Click **"I understand my workflows, go ahead and enable them"**
3. The build will start automatically!

## Step 5: Download Your APK
1. Go to **"Actions"** tab
2. Click on the latest workflow run
3. Scroll down to **"Artifacts"**
4. Download **"xposed-module-apk"**
5. Extract the APK file

## Step 6: Install on Android Device
1. **Enable Developer Options** on your Android device
2. **Enable USB Debugging**
3. **Connect device via USB**
4. **Install APK** using ADB or file manager
5. **Enable in Xposed Framework**
6. **Reboot device**

## Alternative: Use GitHub Desktop
If you prefer a GUI:
1. Download GitHub Desktop: https://desktop.github.com/
2. Sign in with your GitHub account
3. Add your XposedModule folder
4. Publish to GitHub
