public class JavaLexem {
    private JavaLexemsStatus lexem;
    private String value;

    public JavaLexem (JavaLexemsStatus lexem, String value) {
        this.lexem = lexem;
        this.value = value;
    }

    public String showLexem() {
        return lexem + ":" + value;
    }
}
