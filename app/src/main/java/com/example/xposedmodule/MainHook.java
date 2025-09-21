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
        // Log that module is loaded
        XposedBridge.log(TAG + ": Module loaded for: " + lpparam.packageName);
        
        // Test with Sword Master
        if (lpparam.packageName.equals("com.superplanet.swordmaster")) {
            XposedBridge.log(TAG + ": SWORD MASTER DETECTED!");
            
            // Simple hook - just show toast when any activity starts
            XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity activity = (Activity) param.thisObject;
                    XposedBridge.log(TAG + ": Activity: " + activity.getClass().getSimpleName());
                    
                    // Show toast
                    Toast.makeText(activity, "SWORD MASTER MOD ACTIVE!", Toast.LENGTH_LONG).show();
                }
            });
        }
        
        // TEMPORARY: Test with SystemUI to verify module works at all
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log(TAG + ": SYSTEMUI DETECTED - Testing module...");
            
            XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity activity = (Activity) param.thisObject;
                    XposedBridge.log(TAG + ": SystemUI Activity: " + activity.getClass().getSimpleName());
                    Toast.makeText(activity, "MODULE WORKS!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}