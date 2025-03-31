package com.mobiledev.androidstudio.editor;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.mobiledev.androidstudio.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides syntax highlighting for code editing
 */
public class SyntaxHighlighter {

    private final Context context;
    private final String language;
    private final Map<Pattern, Integer> patternColorMap = new HashMap<>();

    /**
     * Create a new syntax highlighter for the given language
     *
     * @param context Context
     * @param language Programming language
     */
    public SyntaxHighlighter(Context context, String language) {
        this.context = context;
        this.language = language;
        
        // Initialize patterns and colors based on language
        initializePatterns();
    }

    /**
     * Initialize patterns for syntax highlighting
     */
    private void initializePatterns() {
        switch (language.toLowerCase()) {
            case "java":
                initializeJavaPatterns();
                break;
            case "kotlin":
                initializeKotlinPatterns();
                break;
            case "xml":
                initializeXmlPatterns();
                break;
            case "python":
                initializePythonPatterns();
                break;
            case "javascript":
            case "js":
                initializeJavaScriptPatterns();
                break;
            case "c":
            case "cpp":
                initializeCPatternsPatterns();
                break;
            default:
                // Basic patterns for unknown languages
                initializeBasicPatterns();
                break;
        }
    }

    /**
     * Initialize Java patterns
     */
    private void initializeJavaPatterns() {
        // Keywords
        patternColorMap.put(
                Pattern.compile("\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while)\\b"),
                ContextCompat.getColor(context, R.color.syntax_keyword));
        
        // Primitive types
        patternColorMap.put(
                Pattern.compile("\\b(boolean|byte|char|double|float|int|long|short|void)\\b"),
                ContextCompat.getColor(context, R.color.syntax_type));
        
        // Strings
        patternColorMap.put(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                ContextCompat.getColor(context, R.color.syntax_string));
        
        // Single-line comments
        patternColorMap.put(
                Pattern.compile("//.*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_comment));
        
        // Numbers
        patternColorMap.put(
                Pattern.compile("\\b(\\d+\\.\\d*|\\.\\d+|\\d+)([eE][+-]?\\d+)?[fFdD]?\\b"),
                ContextCompat.getColor(context, R.color.syntax_number));
        
        // Annotations
        patternColorMap.put(
                Pattern.compile("@[a-zA-Z_][a-zA-Z0-9_]*"),
                ContextCompat.getColor(context, R.color.syntax_annotation));
    }

    /**
     * Initialize Kotlin patterns
     */
    private void initializeKotlinPatterns() {
        // Keywords
        patternColorMap.put(
                Pattern.compile("\\b(abstract|actual|as|break|by|catch|class|companion|const|constructor|continue|crossinline|data|do|dynamic|else|enum|expect|external|false|final|finally|for|fun|get|if|import|in|infix|init|inline|inner|interface|internal|is|lateinit|noinline|null|object|open|operator|out|override|package|private|protected|public|reified|return|sealed|set|super|suspend|tailrec|this|throw|true|try|typealias|val|var|vararg|when|where|while)\\b"),
                ContextCompat.getColor(context, R.color.syntax_keyword));
        
        // Primitive types
        patternColorMap.put(
                Pattern.compile("\\b(Boolean|Byte|Char|Double|Float|Int|Long|Short|String|Unit)\\b"),
                ContextCompat.getColor(context, R.color.syntax_type));
        
        // Strings
        patternColorMap.put(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                ContextCompat.getColor(context, R.color.syntax_string));
        
        // Single-line comments
        patternColorMap.put(
                Pattern.compile("//.*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_comment));
        
        // Numbers
        patternColorMap.put(
                Pattern.compile("\\b(\\d+\\.\\d*|\\.\\d+|\\d+)([eE][+-]?\\d+)?[fFdD]?\\b"),
                ContextCompat.getColor(context, R.color.syntax_number));
        
        // Annotations
        patternColorMap.put(
                Pattern.compile("@[a-zA-Z_][a-zA-Z0-9_]*"),
                ContextCompat.getColor(context, R.color.syntax_annotation));
    }

    /**
     * Initialize XML patterns
     */
    private void initializeXmlPatterns() {
        // Tags
        patternColorMap.put(
                Pattern.compile("<[^>]*>"),
                ContextCompat.getColor(context, R.color.syntax_tag));
        
        // Attributes
        patternColorMap.put(
                Pattern.compile("\\s([a-zA-Z_:][a-zA-Z0-9_:.-]*)\\s*=\\s*"),
                ContextCompat.getColor(context, R.color.syntax_attribute));
        
        // Attribute values
        patternColorMap.put(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                ContextCompat.getColor(context, R.color.syntax_string));
        
        // Comments
        patternColorMap.put(
                Pattern.compile("<!--.*?-->", Pattern.DOTALL),
                ContextCompat.getColor(context, R.color.syntax_comment));
    }

    /**
     * Initialize Python patterns
     */
    private void initializePythonPatterns() {
        // Keywords
        patternColorMap.put(
                Pattern.compile("\\b(and|as|assert|break|class|continue|def|del|elif|else|except|exec|finally|for|from|global|if|import|in|is|lambda|not|or|pass|print|raise|return|try|while|with|yield)\\b"),
                ContextCompat.getColor(context, R.color.syntax_keyword));
        
        // Builtins
        patternColorMap.put(
                Pattern.compile("\\b(abs|all|any|bin|bool|bytearray|callable|chr|classmethod|compile|complex|delattr|dict|dir|divmod|enumerate|eval|execfile|file|filter|float|format|frozenset|getattr|globals|hasattr|hash|help|hex|id|input|int|isinstance|issubclass|iter|len|list|locals|map|max|memoryview|min|next|object|oct|open|ord|pow|property|range|raw_input|reduce|reload|repr|reversed|round|set|setattr|slice|sorted|staticmethod|str|sum|super|tuple|type|unichr|unicode|vars|xrange|zip)\\b"),
                ContextCompat.getColor(context, R.color.syntax_builtin));
        
        // Strings
        patternColorMap.put(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                ContextCompat.getColor(context, R.color.syntax_string));
        patternColorMap.put(
                Pattern.compile("'([^'\\\\]|\\\\.)*'"),
                ContextCompat.getColor(context, R.color.syntax_string));
        
        // Single-line comments
        patternColorMap.put(
                Pattern.compile("#.*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_comment));
        
        // Numbers
        patternColorMap.put(
                Pattern.compile("\\b(\\d+\\.\\d*|\\.\\d+|\\d+)([eE][+-]?\\d+)?[jJ]?\\b"),
                ContextCompat.getColor(context, R.color.syntax_number));
        
        // Decorators
        patternColorMap.put(
                Pattern.compile("@[a-zA-Z_][a-zA-Z0-9_]*"),
                ContextCompat.getColor(context, R.color.syntax_annotation));
    }

    /**
     * Initialize JavaScript patterns
     */
    private void initializeJavaScriptPatterns() {
        // Keywords
        patternColorMap.put(
                Pattern.compile("\\b(await|break|case|catch|class|const|continue|debugger|default|delete|do|else|export|extends|finally|for|function|if|import|in|instanceof|new|return|super|switch|this|throw|try|typeof|var|void|while|with|yield)\\b"),
                ContextCompat.getColor(context, R.color.syntax_keyword));
        
        // Types
        patternColorMap.put(
                Pattern.compile("\\b(Array|Boolean|Date|Error|Function|Math|Number|Object|RegExp|String|Promise)\\b"),
                ContextCompat.getColor(context, R.color.syntax_type));
        
        // Declarations
        patternColorMap.put(
                Pattern.compile("\\b(let|const|var)\\b"),
                ContextCompat.getColor(context, R.color.syntax_keyword));
        
        // Strings
        patternColorMap.put(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                ContextCompat.getColor(context, R.color.syntax_string));
        patternColorMap.put(
                Pattern.compile("'([^'\\\\]|\\\\.)*'"),
                ContextCompat.getColor(context, R.color.syntax_string));
        patternColorMap.put(
                Pattern.compile("`([^`\\\\]|\\\\.)*`"),
                ContextCompat.getColor(context, R.color.syntax_string));
        
        // Single-line comments
        patternColorMap.put(
                Pattern.compile("//.*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_comment));
        
        // Numbers
        patternColorMap.put(
                Pattern.compile("\\b(\\d+\\.\\d*|\\.\\d+|\\d+)([eE][+-]?\\d+)?\\b"),
                ContextCompat.getColor(context, R.color.syntax_number));
    }

    /**
     * Initialize C/C++ patterns
     */
    private void initializeCPatternsPatterns() {
        // Keywords
        patternColorMap.put(
                Pattern.compile("\\b(auto|break|case|char|const|continue|default|do|double|else|enum|extern|float|for|goto|if|int|long|register|return|short|signed|sizeof|static|struct|switch|typedef|union|unsigned|void|volatile|while)\\b"),
                ContextCompat.getColor(context, R.color.syntax_keyword));
        
        // Preprocessor
        patternColorMap.put(
                Pattern.compile("^\\s*#\\s*(include|define|undef|if|ifdef|ifndef|else|elif|endif|error|pragma).*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_preprocessor));
        
        // Strings
        patternColorMap.put(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                ContextCompat.getColor(context, R.color.syntax_string));
        
        // Single-line comments
        patternColorMap.put(
                Pattern.compile("//.*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_comment));
        
        // Numbers
        patternColorMap.put(
                Pattern.compile("\\b(\\d+\\.\\d*|\\.\\d+|\\d+)([eE][+-]?\\d+)?[fFlL]?\\b"),
                ContextCompat.getColor(context, R.color.syntax_number));
    }

    /**
     * Initialize basic patterns for unknown languages
     */
    private void initializeBasicPatterns() {
        // Strings
        patternColorMap.put(
                Pattern.compile("\"([^\"\\\\]|\\\\.)*\""),
                ContextCompat.getColor(context, R.color.syntax_string));
        patternColorMap.put(
                Pattern.compile("'([^'\\\\]|\\\\.)*'"),
                ContextCompat.getColor(context, R.color.syntax_string));
        
        // Numbers
        patternColorMap.put(
                Pattern.compile("\\b(\\d+\\.\\d*|\\.\\d+|\\d+)\\b"),
                ContextCompat.getColor(context, R.color.syntax_number));
        
        // Comments (assuming C-style)
        patternColorMap.put(
                Pattern.compile("//.*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_comment));
        patternColorMap.put(
                Pattern.compile("#.*$", Pattern.MULTILINE),
                ContextCompat.getColor(context, R.color.syntax_comment));
    }

    /**
     * Highlight the given text
     *
     * @param text Text to highlight
     */
    public void highlight(Editable text) {
        // Remove existing spans
        ForegroundColorSpan[] spans = text.getSpans(0, text.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan span : spans) {
            text.removeSpan(span);
        }
        
        // Apply highlighting for each pattern
        for (Map.Entry<Pattern, Integer> entry : patternColorMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(text);
            while (matcher.find()) {
                text.setSpan(
                        new ForegroundColorSpan(entry.getValue()),
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }
    }
}