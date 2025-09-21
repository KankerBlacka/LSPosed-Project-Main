package com.example.xposedmodule;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TextView statusText = findViewById(R.id.statusText);
        
        // Check if Xposed Framework is active
        if (isXposedActive()) {
            statusText.setText("✓ Module Status: Active in LSPosed");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            statusText.setText("⚠ Module Status: LSPosed Framework not detected");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
    }
    
    private boolean isXposedActive() {
        // This method will return false when running normally
        // It would return true only when the module is loaded by Xposed/LSPosed
        return false;
    }
}
