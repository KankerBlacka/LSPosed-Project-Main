# 🚀 GitHub Automatic Build Guide

Since local Java setup can be tricky, let's use GitHub to build your LSPosed module automatically! This is **easier** and **more reliable**.

## 🎯 Why GitHub Build?

✅ **No local setup needed** - No Java, Android SDK, or PATH issues  
✅ **Always works** - Consistent Ubuntu environment  
✅ **Automatic** - Builds on every code change  
✅ **Free** - GitHub provides free build minutes  
✅ **Professional** - Same way real apps are built  

## 📋 Prerequisites

- GitHub account (free at https://github.com)
- Your project files (already ready!)

## 🚀 Quick Setup (3 Minutes)

### Step 1: Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `lsposed-module` (or any name you like)
3. Make it **Public** (required for free builds)
4. ✅ Check "Add a README file"
5. Click **"Create repository"**

### Step 2: Upload Your Project

**Option A: Drag & Drop (Easiest)**
1. Download/zip your entire `XposedModule_Working_Jar` folder
2. Go to your new GitHub repository
3. Click **"uploading an existing file"**
4. Drag your zip file or click "choose your files"
5. Commit with message: "Initial LSPosed module"

**Option B: Git Commands (if you have Git)**
```bash
git clone https://github.com/YOUR_USERNAME/lsposed-module.git
cd lsposed-module
# Copy all your project files here
git add .
git commit -m "Initial LSPosed module"
git push
```

### Step 3: Enable GitHub Actions

1. Go to your repository on GitHub
2. Click the **"Actions"** tab
3. If prompted, click **"I understand my workflows, go ahead and enable them"**
4. The build will start automatically! 🎉

## 📦 Download Your APK

After the build completes (usually 2-3 minutes):

1. Go to **"Actions"** tab in your repository
2. Click on the latest workflow run (green checkmark ✅)
3. Scroll down to **"Artifacts"** section
4. Download **"lsposed-module-debug-apk"**
5. Extract the zip file to get your APK

## 🔄 Automatic Builds

Your APK will automatically rebuild when you:
- Push new code changes
- Make edits directly on GitHub
- Click "Run workflow" in the Actions tab

## 📱 Installation & Usage

1. **Install APK**: Copy to your Android device and install
2. **Enable in LSPosed**: 
   - Open LSPosed Manager
   - Go to "Modules" tab
   - Enable "LSPosed Example Module"
   - Select target apps (SystemUI recommended)
   - Reboot device
3. **Verify**: Check LSPosed logs for "LSPosedExampleModule" entries

## 🛠 Customizing Your Module

Want to modify the code? Easy!

1. **Online editing**: Click any `.java` file in GitHub and click the pencil icon ✏️
2. **Local editing**: Download files, edit, and re-upload
3. **Clone & edit**: Use Git to clone, edit locally, and push changes

Every change triggers a new build automatically!

## 🎯 Pro Tips

- **Manual builds**: Go to Actions tab → "Build LSPosed Module APK" → "Run workflow"
- **Build status**: Green ✅ = success, Red ❌ = failed (check logs)
- **Multiple versions**: Each commit creates a new APK version
- **Release builds**: Use the advanced workflow for signed releases

## ❌ Troubleshooting

**Build fails?**
- Check the Actions logs for error details
- Make sure all files were uploaded correctly
- Verify the project structure is intact

**APK not working?**
- Ensure LSPosed is properly installed
- Check module is enabled in LSPosed Manager
- Verify target app scope is set correctly

**Need help?**
- Check the Actions logs for detailed error messages
- Make sure your project structure matches the original

---

## 🎉 You're Done!

No more local setup headaches - GitHub handles everything! Your LSPosed module will build automatically and be ready to download in minutes.

**Next**: Check out `QUICK_START.md` for installation and usage instructions.
