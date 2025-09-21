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
            
            // Hook EVERYTHING in Sword Master
            hookEverything(lpparam);
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
                        showToast(activity, "SYSTEMUI HOOKED!");
                    }
                });
                XposedBridge.log(TAG + ": SystemUI hook installed!");
            } catch (Exception e) {
                XposedBridge.log(TAG + ": SystemUI hook failed: " + e.getMessage());
            }
        }
    }
    
    private void hookEverything(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedBridge.log(TAG + ": Starting to hook everything...");
            
            // Hook Application.onCreate
            try {
                XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(TAG + ": Application.onCreate called!");
                        showToast((android.content.Context) param.thisObject, "APP STARTED!");
                    }
                });
                XposedBridge.log(TAG + ": Application hook installed!");
            } catch (Exception e) {
                XposedBridge.log(TAG + ": Application hook failed: " + e.getMessage());
            }
            
            // Hook Activity.onCreate
            try {
                XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Activity activity = (Activity) param.thisObject;
                        XposedBridge.log(TAG + ": Activity.onCreate: " + activity.getClass().getSimpleName());
                        showToast(activity, "ACTIVITY: " + activity.getClass().getSimpleName());
                    }
                });
                XposedBridge.log(TAG + ": Activity hook installed!");
            } catch (Exception e) {
                XposedBridge.log(TAG + ": Activity hook failed: " + e.getMessage());
            }
            
            // Hook Activity.onResume
            try {
                XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Activity activity = (Activity) param.thisObject;
                        XposedBridge.log(TAG + ": Activity.onResume: " + activity.getClass().getSimpleName());
                        showToast(activity, "RESUMED: " + activity.getClass().getSimpleName());
                    }
                });
                XposedBridge.log(TAG + ": Activity.onResume hook installed!");
            } catch (Exception e) {
                XposedBridge.log(TAG + ": Activity.onResume hook failed: " + e.getMessage());
            }
            
            // Hook Activity.onStart
            try {
                XposedHelpers.findAndHookMethod(Activity.class, "onStart", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Activity activity = (Activity) param.thisObject;
                        XposedBridge.log(TAG + ": Activity.onStart: " + activity.getClass().getSimpleName());
                        showToast(activity, "STARTED: " + activity.getClass().getSimpleName());
                    }
                });
                XposedBridge.log(TAG + ": Activity.onStart hook installed!");
            } catch (Exception e) {
                XposedBridge.log(TAG + ": Activity.onStart hook failed: " + e.getMessage());
            }
            
            // Hook Object.toString (this will fire for EVERY object)
            try {
                XposedHelpers.findAndHookMethod(Object.class, "toString", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.thisObject instanceof Activity) {
                            Activity activity = (Activity) param.thisObject;
                            XposedBridge.log(TAG + ": Object.toString called on Activity: " + activity.getClass().getSimpleName());
                            showToast(activity, "TOSTRING: " + activity.getClass().getSimpleName());
                        }
                    }
                });
                XposedBridge.log(TAG + ": Object.toString hook installed!");
            } catch (Exception e) {
                XposedBridge.log(TAG + ": Object.toString hook failed: " + e.getMessage());
            }
            
            XposedBridge.log(TAG + ": All hooks installed!");
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Hook everything failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showToast(android.content.Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            XposedBridge.log(TAG + ": Toast shown: " + message);
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Toast failed: " + e.getMessage());
        }
    }
}