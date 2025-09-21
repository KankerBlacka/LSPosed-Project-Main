# 🚀 Quick Start Guide - LSPosed Module

## 🌟 **EASIEST METHOD: GitHub Build (Recommended)**

Skip local setup completely! Build your APK in the cloud:

### Step 1: Upload to GitHub (2 minutes)
1. Go to https://github.com/new
2. Create repository: `lsposed-module` 
3. Upload your project files (drag & drop the folder)

### Step 2: Auto-Build APK (3 minutes)
1. Go to "Actions" tab in your GitHub repository
2. Click "I understand my workflows, go ahead and enable them"
3. Wait for build to complete (green checkmark ✅)

### Step 3: Download APK
1. Click the latest workflow run
2. Download "lsposed-module-debug-apk" from Artifacts
3. Extract and install on your device!

👉 **[Full GitHub Guide](GITHUB_BUILD_GUIDE.md)**

---

## 💻 **Alternative: Local Build**

If you prefer building locally:

### Step 1: Setup Environment
```cmd
setup_environment.bat
```
This will check if you have Java and Android SDK installed, and help you install them if needed.

### Step 2: Build APK
```cmd
build_apk.bat
```
This will compile your module into a ready-to-install APK file.

## Step 3: Install & Use

### Install APK on Device
- **Easy way**: Copy `app\build\outputs\apk\debug\app-debug.apk` to your phone and tap it
- **ADB way**: `adb install app\build\outputs\apk\debug\app-debug.apk`

### Enable in LSPosed
1. Install LSPosed Manager on your rooted device
2. Open LSPosed Manager
3. Go to "Modules" tab
4. Find "LSPosed Example Module" and enable it
5. Select which apps to hook (SystemUI recommended for testing)
6. Reboot your device

### Verify It's Working
1. Open the module app on your device
2. Check LSPosed Manager logs
3. Look for log entries tagged "LSPosedExampleModule"

## 🛠 Common Issues

**"Java not found"**
→ Run `setup_environment.bat` and follow the Java installation guide

**"Build failed"**
→ Make sure you're in the project directory and Java is installed

**"Module not showing in LSPosed"**
→ Check if the APK installed correctly and LSPosed is running

**"Hooks not working"**
→ Make sure you selected the target apps in LSPosed scope settings

## 🎯 Next Steps

Want to customize your module? Edit these files:
- `app/src/main/java/com/example/xposedmodule/MainHook.java` - Your hook logic
- `app/src/main/res/values/strings.xml` - App name and target scope
- `app/src/main/AndroidManifest.xml` - Module metadata

Happy hooking! 🪝
