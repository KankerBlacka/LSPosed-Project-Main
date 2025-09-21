package com.example.xposedmodule;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        
        // Add test button for mod menu
        Button testButton = findViewById(R.id.testModMenuButton);
        if (testButton != null) {
            testButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Test the mod menu directly
                    ModMenuService.showMenu(MainActivity.this);
                }
            });
        }
    }
    
    private boolean isXposedActive() {
        // This method will return false when running normally
        // It would return true only when the module is loaded by Xposed/LSPosed
        return false;
    }
}
