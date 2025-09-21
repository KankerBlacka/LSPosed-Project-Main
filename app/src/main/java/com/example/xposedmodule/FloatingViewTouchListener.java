package com.example.xposedmodule;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatingViewTouchListener implements View.OnTouchListener {
    
    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    
    public FloatingViewTouchListener(WindowManager windowManager, View floatingView) {
        this.windowManager = windowManager;
        this.floatingView = floatingView;
        this.params = (WindowManager.LayoutParams) floatingView.getLayoutParams();
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = params.x;
                initialY = params.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
                
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getRawX() - initialTouchX);
                int deltaY = (int) (event.getRawY() - initialTouchY);
                
                params.x = initialX + deltaX;
                params.y = initialY + deltaY;
                
                windowManager.updateViewLayout(floatingView, params);
                return true;
                
            case MotionEvent.ACTION_UP:
                // Optional: Add click detection here if needed
                return true;
        }
        return false;
    }
}
