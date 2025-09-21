package com.example.xposedmodule;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    
    private static final String TAG = "LSPosedExampleModule";
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Log that our module is loaded
        XposedBridge.log(TAG + ": Module loaded for package: " + lpparam.packageName);
        
        // Example: Hook into SystemUI to modify status bar
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log(TAG + ": Hooking SystemUI");
            hookSystemUI(lpparam);
        }
        
        // Example: Hook into specific apps (you can customize this)
        if (shouldHookPackage(lpparam.packageName)) {
            XposedBridge.log(TAG + ": Hooking general methods for: " + lpparam.packageName);
            hookGeneralMethods(lpparam);
        }
    }
    
    private boolean shouldHookPackage(String packageName) {
        // Define which packages you want to hook
        // Avoid hooking too many packages to prevent performance issues
        return packageName.equals("com.android.systemui") || 
               packageName.equals("android") ||
               packageName.contains("com.example"); // Hook your test apps
    }
    
    private void hookSystemUI(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Try multiple potential StatusBar class names for different Android versions
            String[] possibleClasses = {
                "com.android.systemui.statusbar.phone.StatusBar",
                "com.android.systemui.statusbar.phone.CentralSurfaces",
                "com.android.systemui.statusbar.phone.CentralSurfacesImpl"
            };
            
            for (String className : possibleClasses) {
                try {
                    Class<?> statusBarClass = XposedHelpers.findClass(className, lpparam.classLoader);
                    
                    XposedHelpers.findAndHookMethod(statusBarClass, "start", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": SystemUI StatusBar started - " + className);
                            // You can add your modifications here
                        }
                    });
                    
                    XposedBridge.log(TAG + ": Successfully hooked " + className);
                    break; // If successful, no need to try other classes
                    
                } catch (ClassNotFoundException e) {
                    // This class doesn't exist in this version, try the next one
                    continue;
                }
            }
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error hooking SystemUI: " + e.getMessage());
        }
    }
    
    private void hookGeneralMethods(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Hook into Activity.onCreate to log activity launches
            XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onCreate", 
                android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    android.app.Activity activity = (android.app.Activity) param.thisObject;
                    XposedBridge.log(TAG + ": Activity launched: " + activity.getClass().getSimpleName() + 
                                   " in package: " + lpparam.packageName);
                }
            });
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error hooking general methods: " + e.getMessage());
        }
    }
}
