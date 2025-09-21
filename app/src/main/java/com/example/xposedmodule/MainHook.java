package com.example.xposedmodule;

import android.app.Activity;
import android.widget.Toast;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    
    private static final String TAG = "SwordMasterMod";
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Log EVERYTHING
        XposedBridge.log(TAG + ": ===== MODULE LOADED =====");
        XposedBridge.log(TAG + ": Package: " + lpparam.packageName);
        XposedBridge.log(TAG + ": Process: " + lpparam.processName);
        XposedBridge.log(TAG + ": =========================");
        
        // Hook Sword Master
        if (lpparam.packageName.equals("com.superplanet.swordmaster")) {
            XposedBridge.log(TAG + ": SWORD MASTER DETECTED!");
            
            // Hook Application.onCreate - this is more reliable than Activity.onCreate
            try {
                XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(TAG + ": Application.onCreate called!");
                        
                        // Get context from application
                        android.content.Context context = (android.content.Context) param.thisObject;
                        
                        // Show toast immediately
                        Toast.makeText(context, "SWORD MASTER MOD ACTIVE!", Toast.LENGTH_LONG).show();
                        XposedBridge.log(TAG + ": Toast shown!");
                        
                        // Also log to system log
                        android.util.Log.i(TAG, "SWORD MASTER MOD IS ACTIVE!");
                    }
                });
                
                XposedBridge.log(TAG + ": Application hook installed!");
                
            } catch (Exception e) {
                XposedBridge.log(TAG + ": Application hook failed: " + e.getMessage());
            }
            
            // Also hook Activity.onCreate as backup
            try {
                XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Activity activity = (Activity) param.thisObject;
                        XposedBridge.log(TAG + ": Activity: " + activity.getClass().getSimpleName());
                        
                        // Show toast
                        Toast.makeText(activity, "ACTIVITY HOOKED!", Toast.LENGTH_SHORT).show();
                        XposedBridge.log(TAG + ": Activity toast shown!");
                    }
                });
                
                XposedBridge.log(TAG + ": Activity hook installed!");
                
            } catch (Exception e) {
                XposedBridge.log(TAG + ": Activity hook failed: " + e.getMessage());
            }
        }
        
        // TEMPORARY: Test with SystemUI to verify module works
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log(TAG + ": SYSTEMUI DETECTED - Testing module...");
            
            try {
                XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Activity activity = (Activity) param.thisObject;
                        XposedBridge.log(TAG + ": SystemUI Activity: " + activity.getClass().getSimpleName());
                        
                        // Show toast
                        Toast.makeText(activity, "MODULE WORKS!", Toast.LENGTH_SHORT).show();
                        XposedBridge.log(TAG + ": SystemUI toast shown!");
                    }
                });
                
                XposedBridge.log(TAG + ": SystemUI hook installed!");
                
            } catch (Exception e) {
                XposedBridge.log(TAG + ": SystemUI hook failed: " + e.getMessage());
            }
        }
    }
}