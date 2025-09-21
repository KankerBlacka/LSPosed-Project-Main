package com.example.xposedmodule;

import android.content.Context;
import android.content.Intent;
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
        XposedBridge.log(TAG + ": ===== MODULE LOADED =====");
        XposedBridge.log(TAG + ": Package: " + lpparam.packageName);
        XposedBridge.log(TAG + ": Process: " + lpparam.processName);
        XposedBridge.log(TAG + ": =========================");
        
        // Example: Hook into SystemUI to modify status bar
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log(TAG + ": Hooking SystemUI");
            hookSystemUI(lpparam);
        }
        
        // Example: Hook into specific apps (you can customize this)
        if (shouldHookPackage(lpparam.packageName)) {
            XposedBridge.log(TAG + ": 🎯 TARGET PACKAGE DETECTED: " + lpparam.packageName);
            XposedBridge.log(TAG + ": Setting up hooks for Sword Master...");
            hookGeneralMethods(lpparam);
            hookModMenuTrigger(lpparam);
            hookAlternativeMethods(lpparam);
        }
    }
    
    private boolean shouldHookPackage(String packageName) {
        // Define which packages you want to hook
        // Avoid hooking too many packages to prevent performance issues
        
        // Log all package names to help debug
        XposedBridge.log(TAG + ": Checking package: " + packageName);
        
        // Target specific game: Sword Master
        boolean shouldHook = packageName.equals("com.superplanet.swordmaster");
        
        if (shouldHook) {
            XposedBridge.log(TAG + ": ✓ TARGET GAME DETECTED: " + packageName);
        }
        
        return shouldHook;
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
    
    private void hookModMenuTrigger(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedBridge.log(TAG + ": Setting up mod menu trigger for Sword Master: " + lpparam.packageName);
            
            // Hook into Activity.onCreate to show mod menu when Sword Master starts
            XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onCreate", 
                android.os.Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    android.app.Activity activity = (android.app.Activity) param.thisObject;
                    Context context = activity.getApplicationContext();
                    
                    XposedBridge.log(TAG + ": Sword Master Activity onCreate: " + activity.getClass().getSimpleName());
                    
                    // Show mod menu after a short delay to ensure the game is fully loaded
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                        try {
                            XposedBridge.log(TAG + ": 🎮 SWORD MASTER DETECTED - Showing mod menu!");
                            
                            // Start the service first
                            Intent serviceIntent = new Intent(context, ModMenuService.class);
                            serviceIntent.setAction("SHOW_MENU");
                            context.startService(serviceIntent);
                            
                            XposedBridge.log(TAG + ": ✅ Mod menu service started for Sword Master");
                        } catch (Exception e) {
                            XposedBridge.log(TAG + ": ❌ Error showing mod menu: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }, 3000); // 3 second delay for game to fully load
                }
            });
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error hooking Sword Master mod menu trigger: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void hookAlternativeMethods(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedBridge.log(TAG + ": Setting up alternative hooks for Sword Master...");
            
            // Hook Application.onCreate as backup
            XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", 
                new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log(TAG + ": 🎮 Sword Master Application.onCreate called!");
                    showModMenuFromContext(param.thisObject);
                }
            });
            
            // Hook Activity.onResume as another backup
            XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onResume", 
                new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    android.app.Activity activity = (android.app.Activity) param.thisObject;
                    XposedBridge.log(TAG + ": 🎮 Sword Master Activity.onResume: " + activity.getClass().getSimpleName());
                    
                    // Only show menu once per session
                    if (!isMenuShown) {
                        showModMenuFromContext(activity);
                        isMenuShown = true;
                    }
                }
            });
            
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error setting up alternative hooks: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean isMenuShown = false;
    
    private void showModMenuFromContext(Object contextObject) {
        try {
            Context context = null;
            
            if (contextObject instanceof Context) {
                context = (Context) contextObject;
            } else if (contextObject instanceof android.app.Activity) {
                context = ((android.app.Activity) contextObject).getApplicationContext();
            }
            
            if (context != null) {
                XposedBridge.log(TAG + ": 🎮 Attempting to show mod menu from context...");
                
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    try {
                        Intent serviceIntent = new Intent(context, ModMenuService.class);
                        serviceIntent.setAction("SHOW_MENU");
                        context.startService(serviceIntent);
                        XposedBridge.log(TAG + ": ✅ Mod menu service started from alternative hook!");
                    } catch (Exception e) {
                        XposedBridge.log(TAG + ": ❌ Error in alternative hook: " + e.getMessage());
                        e.printStackTrace();
                    }
                }, 2000);
            }
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Error showing mod menu from context: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
