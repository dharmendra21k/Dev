package com.mobiledev.androidstudio.editor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Custom EditText view for code editing with syntax highlighting
 */
public class CodeEditorView extends AppCompatEditText {
    private static final String TAG = "CodeEditorView";
    private static final int SYNTAX_HIGHLIGHT_DELAY_MS = 500;
    
    private SyntaxHighlighter syntaxHighlighter;
    private Handler handler;
    private Runnable highlightRunnable;
    private String currentFilePath;
    private String fileExtension;
    private boolean isHighlighting = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    /**
     * Constructor for CodeEditorView
     */
    public CodeEditorView(Context context) {
        super(context);
        init();
    }
    
    /**
     * Constructor for CodeEditorView with attributes
     */
    public CodeEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    /**
     * Constructor for CodeEditorView with attributes and style
     */
    public CodeEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    /**
     * Initialize the code editor
     */
    private void init() {
        handler = new Handler(Looper.getMainLooper());
        
        // Set up text change listener for syntax highlighting
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                // Cancel any pending highlight operations
                handler.removeCallbacks(highlightRunnable);
                
                // Schedule a new highlight operation with delay
                if (syntaxHighlighter != null) {
                    highlightRunnable = () -> highlightSyntax(s);
                    handler.postDelayed(highlightRunnable, SYNTAX_HIGHLIGHT_DELAY_MS);
                }
            }
        });
    }
    
    /**
     * Load a file into the editor
     * 
     * @param file File to load
     * @throws IOException If an I/O error occurs
     */
    public void loadFile(File file) throws IOException {
        currentFilePath = file.getAbsolutePath();
        fileExtension = getFileExtension(file.getName());
        
        // Select appropriate syntax highlighter
        if (fileExtension.equals("java")) {
            syntaxHighlighter = new JavaSyntaxHighlighter();
        } else if (fileExtension.equals("xml")) {
            syntaxHighlighter = new XmlSyntaxHighlighter();
        } else if (fileExtension.equals("gradle")) {
            syntaxHighlighter = new GroovySyntaxHighlighter();
        } else {
            syntaxHighlighter = null;
        }
        
        // Load file content asynchronously
        executor.execute(() -> {
            try {
                final String content = readFile(file);
                
                // Update UI on main thread
                handler.post(() -> {
                    setText(content);
                    if (syntaxHighlighter != null) {
                        highlightSyntax(getText());
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "Error loading file: " + e.getMessage());
            }
        });
    }
    
    /**
     * Save the current content to a file
     * 
     * @return true if save was successful
     */
    public boolean saveFile() {
        if (currentFilePath == null) {
            return false;
        }
        
        try {
            writeFile(new File(currentFilePath), getText().toString());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Save the current content to the specified file
     * 
     * @param file File to save to
     * @return true if save was successful
     */
    public boolean saveFile(File file) {
        try {
            writeFile(file, getText().toString());
            currentFilePath = file.getAbsolutePath();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the file extension from a filename
     * 
     * @param fileName File name
     * @return File extension or empty string if none
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    /**
     * Read the content of a file
     * 
     * @param file File to read
     * @return File content as string
     * @throws IOException If an I/O error occurs
     */
    private String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
    
    /**
     * Write content to a file
     * 
     * @param file File to write to
     * @param content Content to write
     * @throws IOException If an I/O error occurs
     */
    private void writeFile(File file, String content) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }
    
    /**
     * Apply syntax highlighting to the text
     * 
     * @param editable Editable text to highlight
     */
    private void highlightSyntax(Editable editable) {
        if (isHighlighting || syntaxHighlighter == null) {
            return;
        }
        
        isHighlighting = true;
        
        executor.execute(() -> {
            // Create a copy of the text to work with
            final String text = editable.toString();
            
            // Apply syntax highlighting and get spans
            final SyntaxHighlighter.HighlightResult result = 
                    syntaxHighlighter.highlight(text);
            
            // Apply spans on UI thread
            handler.post(() -> {
                // Remove existing spans
                Object[] spans = editable.getSpans(0, editable.length(), Object.class);
                for (Object span : spans) {
                    if (span instanceof SyntaxHighlighter.SyntaxSpan) {
                        editable.removeSpan(span);
                    }
                }
                
                // Apply new spans
                for (SyntaxHighlighter.SyntaxSpan span : result.spans) {
                    editable.setSpan(
                            span.getSpan(),
                            span.getStart(),
                            span.getEnd(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                
                isHighlighting = false;
            });
        });
    }
}