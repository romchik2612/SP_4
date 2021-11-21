import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transformer {
    private List<String> reservedWords = Arrays.asList("boolean", "break", "byte", "case", "char", "const", "default",
            "do", "double", "else", "enum", "false", "final", "finally", "float", "public", "class", "private",
            "static", "for", "goto", "if", "int", "long", "new", "null", "else", "return", "short", "switch", "throw",
            "true", "try", "void", "while", "continue", "package", "import", "String");
    private List<String> operators = Arrays.asList(">", ">=", "&&", "||", "<", "<=", "=", "==", "!=", "*", "*=", "%",
            "%=", "/", "/=", "+", "++", "+=", "-", "--", "-=", "::");
    private List<String> punctuators = Arrays.asList("(", ")", "{", "}", "[", "]", ",", ";", ".");
    private boolean isComment = false;
    private List<JavaLexem> lexems = new ArrayList<>();

    public void showLexems() {
        for (var lexem : lexems) {
            System.out.println(lexem.showLexem());
        }
    }

    public void startTransform(String line) {
        transformLine(line);
    }

    private void transformLine(String line) {
        line = line.trim();
        char prev = 'A';
        boolean isStr = false;
        String res = "";
        boolean isActive = false;
        for (var symbol : line.toCharArray()) {
            if(isStr) {
                res+=symbol;
                if(symbol=='\"') {
                    isStr = false;
                    isActive = false;
                    res = startTransformation(true, res);
                }
                continue;
            }
            String str = String.valueOf(prev) + String.valueOf(symbol);
            if (operators.contains(str) && !isComment) {
                lexems.add(new JavaLexem(JavaLexemsStatus.OPERATOR, str));
                res = startTransformation(isActive, res);
                isActive = false;
            } else if (isActive && Character.isDigit(prev) && symbol == '.') {
                res += symbol;
            } else if(punctuators.contains(String.valueOf(symbol)) && !isComment) {
                lexems.add(new JavaLexem(JavaLexemsStatus.PUNCTUATOR, String.valueOf(symbol)));
                res = startTransformation(isActive, res);
                isActive = false;
            } else if(operators.contains(String.valueOf(symbol)) && !isComment) {
                lexems.add(new JavaLexem(JavaLexemsStatus.OPERATOR, String.valueOf(symbol)));
                res = startTransformation(isActive, res);
                isActive = false;
            } else if (str.equals("/*") && !isComment) {
                isComment = true;
            } else if (str.equals("*/") && isComment) {
                isComment = false;
            } else if (Character.isDigit(symbol) || Character.isLetter(symbol) || symbol == '\'' || symbol == '\"') {
                if(symbol == '\"') isStr = true;
                isActive = true;
                res += String.valueOf(symbol);
            } else if (Character.isWhitespace(symbol)) {
                res = startTransformation(isActive, res);
                isActive = false;
            }
            prev = symbol;
        }
    }

    private String startTransformation(boolean isActive, String res) {
        if (isActive) {
            transformString(res);
        }
        return "";
    }

    private void transformString(String word) {
        if (reservedWords.contains(word)) {
            lexems.add(new JavaLexem(JavaLexemsStatus.RESERVED, word));
        } else if (checkNumber(word)) {
            lexems.add(new JavaLexem(JavaLexemsStatus.NUMBER, word));
        } else if (checkString(word)) {
            lexems.add(new JavaLexem(JavaLexemsStatus.STRING, word));
        } else if (checkChar(word)) {
            lexems.add(new JavaLexem(JavaLexemsStatus.CHARACTER, word));
        } else if (checkIdentifier(word)) {
            lexems.add(new JavaLexem(JavaLexemsStatus.IDENTIFIER, word));
        } else {
            lexems.add(new JavaLexem(JavaLexemsStatus.ERROR, word));
        }
    }

    private boolean checkNumber(String word) {
        for (var i : word.toCharArray()) {
            if (!Character.isDigit(i) && i != '.') {
                return false;
            }
        }
        return true;
    }

    private boolean checkString(String word) {
        if (word.toCharArray()[0] == '\"' && word.toCharArray()[word.length() - 1] == '\"') {
            return true;
        }
        return false;
    }

    private boolean checkChar(String word) {
        if (word.toCharArray()[0] == '\'' && word.toCharArray()[word.length() - 1] == '\'') {
            return true;
        }
        return false;
    }

    private boolean checkIdentifier(String word) {
        if (!Character.isLetter(word.charAt(0))) {
            return false;
        }
        for (var i : word.toCharArray()) {
            if (Character.isLetter(i) || Character.isDigit(i) || i == '_') {
                continue;
            }
            return false;
        }
        return true;
    }
}
