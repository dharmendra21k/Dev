package com.mobiledev.androidstudio;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mobiledev.androidstudio.terminal.TerminalView;
import com.mobiledev.androidstudio.utils.TerminalManager;

/**
 * Activity for terminal interface
 */
public class TerminalActivity extends AppCompatActivity implements TerminalView.OnCommandExecutedListener {
    
    private static final String TAG = "TerminalActivity";
    
    private TerminalView mTerminalView;
    private ProgressBar mProgressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        
        // Set up action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Terminal");
        }
        
        // Initialize views
        mTerminalView = findViewById(R.id.terminal_view);
        mProgressBar = findViewById(R.id.progress_bar);
        
        // Set command listener
        mTerminalView.setOnCommandExecutedListener(this);
        
        // Initialize terminal environment
        initializeTerminal();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void initializeTerminal() {
        // Show progress
        mProgressBar.setVisibility(View.VISIBLE);
        
        // Initialize terminal environment in background
        TerminalManager.getInstance(this).initializeEnvironment(new TerminalManager.EnvironmentCallback() {
            @Override
            public void onEnvironmentReady() {
                // Hide progress
                runOnUiThread(() -> {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(TerminalActivity.this, "Terminal ready", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onEnvironmentError(String error) {
                // Hide progress and show error
                runOnUiThread(() -> {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(TerminalActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    @Override
    public void onCommandExecuted(String command, String output, int exitCode) {
        // Log command execution
        Log.d(TAG, "Command executed: " + command + ", Exit code: " + exitCode);
    }
}