import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Transformer transformer = new Transformer();
        Files.lines(Path.of("src/main/resources/test.java"))
                .forEach(transformer::startTransform);
        transformer.showLexems();
    }
}