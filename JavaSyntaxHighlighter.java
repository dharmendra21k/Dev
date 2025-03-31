package com.mobiledev.androidstudio.editor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Syntax highlighter for Java code
 */
public class JavaSyntaxHighlighter extends SyntaxHighlighter {
    // Regular expressions for different code elements
    private static final Pattern PATTERN_KEYWORDS = 
            Pattern.compile("\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while|true|false|null)\\b");
    
    private static final Pattern PATTERN_METHODS = 
            Pattern.compile("\\b([a-zA-Z_$][a-zA-Z0-9_$]*)\\s*\\(");
    
    private static final Pattern PATTERN_NUMBERS = 
            Pattern.compile("\\b(\\d+\\.\\d+[fFlL]?|\\d+[fFlLdD]|0x[0-9a-fA-F]+|\\d+)\\b");
    
    private static final Pattern PATTERN_STRINGS = 
            Pattern.compile("\"[^\"\\\\]*(\\\\.[^\"\\\\]*)*\"|'.'");
    
    private static final Pattern PATTERN_SINGLE_COMMENT = 
            Pattern.compile("//[^\\n]*");
    
    private static final Pattern PATTERN_MULTI_COMMENT = 
            Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/");
    
    // Java annotations
    private static final Pattern PATTERN_ANNOTATIONS = 
            Pattern.compile("@[a-zA-Z_$][a-zA-Z0-9_$]*");
    
    // Set of Java keywords for additional validation
    private static final Set<String> JAVA_KEYWORDS = new HashSet<>();
    
    static {
        // Initialize the set of Java keywords
        for (String keyword : new String[]{
                "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", 
                "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", 
                "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", 
                "interface", "long", "native", "new", "package", "private", "protected", "public", 
                "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", 
                "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null"
        }) {
            JAVA_KEYWORDS.add(keyword);
        }
    }
    
    @Override
    public HighlightResult highlight(String text) {
        HighlightResult result = new HighlightResult();
        
        // Add spans for different code elements
        addPatternSpans(result, text, PATTERN_SINGLE_COMMENT, COLOR_COMMENT);
        addPatternSpans(result, text, PATTERN_MULTI_COMMENT, COLOR_COMMENT);
        addPatternSpans(result, text, PATTERN_STRINGS, COLOR_STRING);
        addPatternSpans(result, text, PATTERN_KEYWORDS, COLOR_KEYWORD);
        addPatternSpans(result, text, PATTERN_ANNOTATIONS, COLOR_ATTRIBUTE);
        addPatternSpans(result, text, PATTERN_NUMBERS, COLOR_NUMBER);
        
        // Methods require special handling to avoid highlighting keywords
        Matcher methodMatcher = PATTERN_METHODS.matcher(text);
        while (methodMatcher.find()) {
            String methodName = methodMatcher.group(1);
            // Only highlight as method if not a keyword
            if (!JAVA_KEYWORDS.contains(methodName)) {
                result.addColorSpan(COLOR_METHOD, methodMatcher.start(1), methodMatcher.end(1));
            }
        }
        
        return result;
    }
    
    /**
     * Add spans for all matches of a pattern
     * 
     * @param result HighlightResult to add spans to
     * @param text Text to search in
     * @param pattern Pattern to search for
     * @param color Color to apply to matches
     */
    private void addPatternSpans(HighlightResult result, String text, Pattern pattern, int color) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result.addColorSpan(color, matcher.start(), matcher.end());
        }
    }
}