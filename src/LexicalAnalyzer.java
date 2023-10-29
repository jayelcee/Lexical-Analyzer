import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.io.IOException;

public class LexicalAnalyzer {

    // List of C++ keywords
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "asm", "else", "new", "this", "auto", "enum", "operator", "throw", "endl",
            "bool", "explicit", "private", "true", "break", "export", "protected",
            "try", "case", "extern", "public", "typedef", "catch", "false", "register",
            "typeid", "char", "float", "reinterpret_cast", "typename", "class", "for",
            "return", "union", "const", "friend", "short", "unsigned", "const_cast",
            "goto", "signed", "using", "continue", "if", "sizeof", "virtual", "default",
            "inline", "static", "void", "delete", "int", "static_cast", "string", "volatile",
            "do", "long", "struct", "wchar_t", "double", "mutable", "switch", "while",
            "dynamic_cast", "namespace", "template", "include", "iostream", "cout", "std"));

    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList(
            "+", "-", "*", "/", "%", // Arithmetic Operators
            "==", "!=", ">", "<", ">=", "<=", // Relational Operators
            "&&", "||", "!", // Logical Operators
            "&", "|", "^", "~", "<<", ">>", // Bitwise Operators
            "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<=", ">>=", // Assignment Operators
            "::", "++", "--" // Scope Resolution, Increment, Decrement Operator
    ));

    private static final Set<String> SYMBOLS = new HashSet<>(Arrays.asList("#", "{", "}", ";", ":", ".", ",", "'", "\"", "(", ")", "%", "\\t", "\\n"));
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    private static final Pattern CONSTANT_PATTERN = Pattern.compile("^-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");

    private static int keywordCount = 0;
    private static int identifierCount = 0;
    private static int operatorCount = 0;
    private static int symbolCount = 0;
    private static int constantCount = 0;

    public static void main(String[] args) {
        try {
            String code = new String(Files.readAllBytes(Paths.get("src/sample.txt")));
            List<String> tokens = tokenize(code);

            List<String> keywords = new ArrayList<>();
            List<String> identifiers = new ArrayList<>();
            List<String> operators = new ArrayList<>();
            List<String> symbols = new ArrayList<>();
            List<String> constants = new ArrayList<>();

            for (String token : tokens) {
                if (KEYWORDS.contains(token)) {
                    keywordCount++;
                    keywords.add(token); // adding unique keyword to set
                    display(token, "a keyword");
                } else if (OPERATORS.contains(token)) {
                    operatorCount++;
                    operators.add(token); // adding unique operator to set
                    display(token, "an operator");
                } else if (SYMBOLS.contains(token)) {
                    symbolCount++;
                    symbols.add(token); // adding unique symbol to set
                    display(token, "a symbol");
                } else if (IDENTIFIER_PATTERN.matcher(token).matches()) {
                    identifierCount++;
                    identifiers.add(token); // adding unique identifier to set
                    display(token, "an identifier");
                } else if (CONSTANT_PATTERN.matcher(token).matches()) {
                    constantCount++;
                    constants.add(token); // adding unique constant to set
                    display(token, "a constant");
                }
            }

            displaySummary(new HashSet<>(keywords), new HashSet<>(identifiers), new HashSet<>(operators), new HashSet<>(symbols), new HashSet<>(constants));
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static List<String> tokenize(String code) {
        List<String> tokens = new ArrayList<>();

        Matcher m = Pattern.compile(
                "[a-zA-Z_][a-zA-Z0-9_]*|[-+*/%=&|^!<>:]+|\\d+\\.?\\d*|\\.|\\t|\\n|\\s|[{}();.'\",\\[\\]#]|<[^>]+>"
        ).matcher(code);

        while (m.find()) {
            String match = m.group();
            if (match.equals("\t")) {
                tokens.add("\\t");
            } else if (match.equals("\n")) {
                tokens.add("\\n");
            } else {
                tokens.add(match);
            }
        }
        return tokens;
    }

    private static void display(String token, String category) {
        System.out.println(token + " is " + category);
        System.out.println("---- SIMULATION SUMMARY ----");
        System.out.println("Keywords: " + keywordCount);
        System.out.println("Identifiers: " + identifierCount);
        System.out.println("Operators: " + operatorCount);
        System.out.println("Symbols: " + symbolCount);
        System.out.println("Constants: " + constantCount);
        System.out.println("-----------------------------");
    }

    private static void displaySummary(Set<String> keywords, Set<String> identifiers, Set<String> operators, Set<String> symbols, Set<String> constants) {
        System.out.println("--------------------------------- FINAL SUMMARY ---------------------------------");
        System.out.println("Keywords: " + keywordCount + "\n" + keywords);
        System.out.println("Identifiers: " + identifierCount + "\n" + identifiers);
        System.out.println("Operators: " + operatorCount + "\n" + operators);
        System.out.println("Symbols: " + symbolCount + "\n" + symbols);
        System.out.println("Constants: " + constantCount + "\n" + constants);
        System.out.println("---------------------------------------------------------------------------------");
    }

}