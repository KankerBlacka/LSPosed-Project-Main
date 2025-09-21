package com.example.xposedmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    
    private static final String TAG = "SwordMasterMod";
    private static boolean menuShown = false;
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log(TAG + ": Module loaded for: " + lpparam.packageName);
        
        // TEMPORARY: Log ALL packages to help find the correct one
        XposedBridge.log(TAG + ": === PACKAGE DETECTED ===");
        XposedBridge.log(TAG + ": Package: " + lpparam.packageName);
        XposedBridge.log(TAG + ": Process: " + lpparam.processName);
        XposedBridge.log(TAG + ": ======================");
        
        // Check for Sword Master with multiple possible names
        if (isSwordMasterPackage(lpparam.packageName)) {
            XposedBridge.log(TAG + ": 🎮 SWORD MASTER DETECTED! Package: " + lpparam.packageName);
            hookSwordMaster(lpparam);
        }
    }
    
    private boolean isSwordMasterPackage(String packageName) {
        // Check multiple possible package names for Sword Master
        String[] possibleNames = {
            "com.superplanet.swordmaster",
            "com.superplanet.swordmasterstory", 
            "com.superplanet.swordmaster.story",
            "com.superplanet.swordmasterstory.story",
            "com.superplanet.swordmasterstory.main",
            "com.superplanet.swordmaster.main",
            "com.superplanet.swordmasterstory.game",
            "com.superplanet.swordmaster.game"
        };
        
        for (String name : possibleNames) {
            if (packageName.equals(name)) {
                XposedBridge.log(TAG + ": ✓ Found Sword Master with package: " + name);
                return true;
            }
        }
        
        // Also check if package contains swordmaster keywords
        String lowerPackage = packageName.toLowerCase();
        if (lowerPackage.contains("swordmaster") || lowerPackage.contains("sword") || lowerPackage.contains("superplanet")) {
            XposedBridge.log(TAG + ": 🔍 POTENTIAL SWORD MASTER PACKAGE: " + packageName);
            return true;
        }
        
        return false;
    }
    
    private void hookSwordMaster(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Simple and reliable hook - just hook Activity.onCreate
            XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (menuShown) return; // Only show once
                    
                    Activity activity = (Activity) param.thisObject;
                    XposedBridge.log(TAG + ": Activity created: " + activity.getClass().getSimpleName());
                    
                    // Show menu after a delay
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        try {
                            showModMenu(activity);
                            menuShown = true;
                        } catch (Exception e) {
                            XposedBridge.log(TAG + ": Error showing menu: " + e.getMessage());
                        }
                    }, 2000);
                }
            });
            
            XposedBridge.log(TAG + ": Hooks set up successfully!");
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error setting up hooks: " + e.getMessage());
        }
    }
    
    private void showModMenu(Activity activity) {
        try {
            XposedBridge.log(TAG + ": Showing mod menu...");
            
            // Start the service
            Intent intent = new Intent(activity, ModMenuService.class);
            intent.setAction("SHOW_MENU");
            activity.startService(intent);
            
            XposedBridge.log(TAG + ": Mod menu service started!");
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error starting mod menu: " + e.getMessage());
        }
    }
}
