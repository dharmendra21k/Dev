package com.mobiledev.androidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobiledev.androidstudio.models.Project;
import com.mobiledev.androidstudio.utils.FileUtils;
import com.mobiledev.androidstudio.utils.ProjectManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinnerFiles;
    private EditText etCode;
    
    private ProjectManager projectManager;
    private Project project;
    private File currentFile;
    private List<File> projectFiles;
    private boolean hasUnsavedChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        spinnerFiles = findViewById(R.id.spinnerFiles);
        etCode = findViewById(R.id.etCode);
        
        // Set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Get project from intent
        String projectName = getIntent().getStringExtra("projectName");
        String projectPath = getIntent().getStringExtra("projectPath");
        String packageName = getIntent().getStringExtra("packageName");
        
        if (projectName == null || projectPath == null || packageName == null) {
            Toast.makeText(this, "Invalid project data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize project
        project = new Project(projectName, projectPath, packageName);
        projectManager = new ProjectManager(this);
        
        // Set up project files spinner
        loadProjectFiles();
        
        // Set up file selection listener
        spinnerFiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < projectFiles.size()) {
                    // Check for unsaved changes
                    if (hasUnsavedChanges) {
                        showSaveDialog(projectFiles.get(position));
                    } else {
                        openFile(projectFiles.get(position));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do
            }
        });
        
        // Update toolbar title
        getSupportActionBar().setTitle(projectName);
    }
    
    private void loadProjectFiles() {
        // Get project files
        List<File> sourceFiles = projectManager.getSourceFiles(project);
        List<File> layoutFiles = projectManager.getLayoutFiles(project);
        
        // Combine files
        projectFiles = new ArrayList<>();
        projectFiles.addAll(sourceFiles);
        projectFiles.addAll(layoutFiles);
        
        // Set up spinner adapter
        List<String> fileNames = new ArrayList<>();
        for (File file : projectFiles) {
            fileNames.add(file.getName());
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, fileNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiles.setAdapter(adapter);
        
        // Select first file
        if (!projectFiles.isEmpty()) {
            spinnerFiles.setSelection(0);
            openFile(projectFiles.get(0));
        }
    }
    
    private void openFile(File file) {
        try {
            currentFile = file;
            String content = FileUtils.readTextFromFile(file);
            etCode.setText(content);
            hasUnsavedChanges = false;
        } catch (IOException e) {
            Toast.makeText(this, "Error opening file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void saveCurrentFile() {
        if (currentFile != null) {
            try {
                String content = etCode.getText().toString();
                FileUtils.writeTextToFile(currentFile, content);
                Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
                hasUnsavedChanges = false;
            } catch (IOException e) {
                Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void showSaveDialog(final File nextFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved Changes");
        builder.setMessage("Do you want to save changes to " + currentFile.getName() + "?");
        builder.setPositiveButton("Save", (dialog, which) -> {
            saveCurrentFile();
            openFile(nextFile);
        });
        builder.setNegativeButton("Discard", (dialog, which) -> {
            hasUnsavedChanges = false;
            openFile(nextFile);
        });
        builder.setNeutralButton("Cancel", (dialog, which) -> {
            // Revert spinner selection
            for (int i = 0; i < projectFiles.size(); i++) {
                if (projectFiles.get(i).equals(currentFile)) {
                    spinnerFiles.setSelection(i);
                    break;
                }
            }
        });
        builder.show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            if (hasUnsavedChanges) {
                showExitSaveDialog();
                return true;
            }
            finish();
            return true;
        } else if (id == R.id.action_save) {
            saveCurrentFile();
            return true;
        } else if (id == R.id.action_build) {
            saveCurrentFile();
            buildProject();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void showExitSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved Changes");
        builder.setMessage("Do you want to save changes before exiting?");
        builder.setPositiveButton("Save", (dialog, which) -> {
            saveCurrentFile();
            finish();
        });
        builder.setNegativeButton("Discard", (dialog, which) -> finish());
        builder.setNeutralButton("Cancel", (dialog, which) -> {
            // Stay in editor
        });
        builder.show();
    }
    
    private void buildProject() {
        Intent intent = new Intent(this, BuildActivity.class);
        intent.putExtra("projectName", project.getName());
        intent.putExtra("projectPath", project.getPath());
        intent.putExtra("packageName", project.getPackageName());
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
        if (hasUnsavedChanges) {
            showExitSaveDialog();
        } else {
            super.onBackPressed();
        }
    }
    
    // Text change listener to detect unsaved changes
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        hasUnsavedChanges = true;
    }
}