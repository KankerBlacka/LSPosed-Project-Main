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
                    
                    // Show toast for Sword Master with delay
                    if (packageName.equals("com.superplanet.swordmaster")) {
                        XposedBridge.log(TAG + ": SWORD MASTER ACTIVITY DETECTED!");
                        
                        // Use Handler to show toast on main thread
                        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                            try {
                                Toast.makeText(activity, "SWORD MASTER MOD ACTIVE!", Toast.LENGTH_LONG).show();
                                XposedBridge.log(TAG + ": SWORD MASTER TOAST SHOWN!");
                                
                                // Also create a simple floating window
                                createFloatingWindow(activity);
                                
                            } catch (Exception e) {
                                XposedBridge.log(TAG + ": Toast error: " + e.getMessage());
                            }
                        }, 1000);
                    }
                    
                    // Show toast for any app to test
                    if (packageName.contains("sword") || packageName.contains("superplanet")) {
                        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                            try {
                                Toast.makeText(activity, "MODULE DETECTED: " + packageName, Toast.LENGTH_SHORT).show();
                                XposedBridge.log(TAG + ": DETECTED PACKAGE: " + packageName);
                            } catch (Exception e) {
                                XposedBridge.log(TAG + ": Toast error: " + e.getMessage());
                            }
                        }, 500);
                    }
                }
            });
            
            XposedBridge.log(TAG + ": Hook installed successfully!");
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createFloatingWindow(Activity activity) {
        try {
            XposedBridge.log(TAG + ": Creating floating window...");
            
            android.view.WindowManager windowManager = (android.view.WindowManager) activity.getSystemService(android.content.Context.WINDOW_SERVICE);
            
            // Create simple floating view
            android.widget.TextView floatingView = new android.widget.TextView(activity);
            floatingView.setText("🎮 Sword Master Mod");
            floatingView.setTextColor(0xFFFFFFFF);
            floatingView.setTextSize(16);
            floatingView.setBackgroundColor(0x80000000);
            floatingView.setPadding(20, 10, 20, 10);
            
            // Window parameters
            android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                params.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = android.view.WindowManager.LayoutParams.TYPE_PHONE;
            }
            
            params.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.format = android.graphics.PixelFormat.TRANSLUCENT;
            params.width = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = android.view.Gravity.TOP | android.view.Gravity.START;
            params.x = 50;
            params.y = 100;
            
            windowManager.addView(floatingView, params);
            XposedBridge.log(TAG + ": Floating window created!");
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Floating window error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}