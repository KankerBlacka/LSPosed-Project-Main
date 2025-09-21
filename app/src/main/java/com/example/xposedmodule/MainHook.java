package com.example.xposedmodule;

import android.app.Activity;
import android.widget.Toast;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    
    private static final String TAG = "TestModule";
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // ALWAYS log - this will tell us if module is loaded at all
        XposedBridge.log(TAG + ": ===== MODULE IS LOADING =====");
        XposedBridge.log(TAG + ": Package: " + lpparam.packageName);
        XposedBridge.log(TAG + ": Process: " + lpparam.processName);
        XposedBridge.log(TAG + ": ==============================");
        
        // Hook EVERYTHING temporarily to test
        try {
            XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity activity = (Activity) param.thisObject;
                    String packageName = activity.getPackageName();
                    
                    XposedBridge.log(TAG + ": Activity in " + packageName + ": " + activity.getClass().getSimpleName());
                    
                    // Show toast for Sword Master
                    if (packageName.equals("com.superplanet.swordmaster")) {
                        Toast.makeText(activity, "SWORD MASTER MOD ACTIVE!", Toast.LENGTH_LONG).show();
                        XposedBridge.log(TAG + ": SWORD MASTER TOAST SHOWN!");
                    }
                    
                    // Show toast for any app to test
                    if (packageName.contains("sword") || packageName.contains("superplanet")) {
                        Toast.makeText(activity, "MODULE DETECTED: " + packageName, Toast.LENGTH_SHORT).show();
                        XposedBridge.log(TAG + ": DETECTED PACKAGE: " + packageName);
                    }
                }
            });
            
            XposedBridge.log(TAG + ": Hook installed successfully!");
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}