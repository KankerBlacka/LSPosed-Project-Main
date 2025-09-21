package com.example.xposedmodule;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    
    private static final String TAG = "SwordMasterMod";
    private static boolean menuShown = false;
    private static WindowManager windowManager;
    private static View floatingView;
    
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Log EVERYTHING for debugging
        XposedBridge.log(TAG + ": ===== MODULE LOADED =====");
        XposedBridge.log(TAG + ": Package: " + lpparam.packageName);
        XposedBridge.log(TAG + ": Process: " + lpparam.processName);
        XposedBridge.log(TAG + ": =========================");
        
        // TEMPORARY: Hook ALL packages to test if module works at all
        XposedBridge.log(TAG + ": Testing with package: " + lpparam.packageName);
        
        // Hook Sword Master specifically
        if (lpparam.packageName.contains("sword") || 
            lpparam.packageName.contains("superplanet") ||
            lpparam.packageName.equals("com.superplanet.swordmaster")) {
            
            XposedBridge.log(TAG + ": 🎮 SWORD MASTER DETECTED: " + lpparam.packageName);
            hookTargetApp(lpparam);
        }
        
        // TEMPORARY: Also hook any app to test if module works
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log(TAG + ": 🔧 SYSTEMUI DETECTED - Testing module...");
            hookTargetApp(lpparam);
        }
    }
    
    private void hookTargetApp(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedBridge.log(TAG + ": Setting up hooks for: " + lpparam.packageName);
            
            // Hook Activity.onCreate - the most reliable method
            XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity activity = (Activity) param.thisObject;
                    XposedBridge.log(TAG + ": 🎮 Activity created: " + activity.getClass().getSimpleName());
                    
                    if (menuShown) {
                        XposedBridge.log(TAG + ": Menu already shown");
                        return;
                    }
                    
                    // Show floating menu directly - no service needed
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        try {
                            showFloatingMenu(activity);
                            menuShown = true;
                        } catch (Exception e) {
                            XposedBridge.log(TAG + ": Error: " + e.getMessage());
                        }
                    }, 2000);
                }
            });
            
            XposedBridge.log(TAG + ": ✅ Hooks ready!");
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": ❌ Hook error: " + e.getMessage());
        }
    }
    
    private void showFloatingMenu(Activity activity) {
        try {
            XposedBridge.log(TAG + ": Creating floating menu...");
            
            // Get window manager
            windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            
            // Create simple floating view
            floatingView = new TextView(activity);
            ((TextView) floatingView).setText("🎮 Sword Master Mod");
            ((TextView) floatingView).setTextColor(0xFFFFFFFF);
            ((TextView) floatingView).setTextSize(16);
            ((TextView) floatingView).setBackgroundColor(0x80000000);
            ((TextView) floatingView).setPadding(20, 10, 20, 10);
            
            // Set up window parameters
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.format = PixelFormat.TRANSLUCENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.TOP | Gravity.START;
            params.x = 50;
            params.y = 100;
            
            // Add the view
            windowManager.addView(floatingView, params);
            
            XposedBridge.log(TAG + ": ✅ Menu created!");
            Toast.makeText(activity, "Sword Master Mod Active!", Toast.LENGTH_LONG).show();
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": ❌ Menu error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
