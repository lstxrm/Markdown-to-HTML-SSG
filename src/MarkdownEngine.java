import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PART 4 - RUNTIME POLYMORPHISM  +  PART 5 - COLLECTIONS & STREAMS
 * The brain of the project. It keeps its parsing rules in two Lists of
 * ElementParser and, for every line, calls canParse()/render() on them.
 * Which render() actually runs (Heading? Paragraph? Bold?) is decided only
 * at runtime depending on the object sitting in the list.
 *
 * Because the rules live in a list, there is no long if-else ladder anywhere.
 * Adding a new syntax later = write a new class + one add() line below.
 */
public class MarkdownEngine {

    // block-level rules: exactly ONE of these is applied to a line
    private final List<ElementParser> blockParsers = new ArrayList<>();
    // inline rules: ALL of these get a chance on the same line
    private final List<ElementParser> inlineParsers = new ArrayList<>();

    public MarkdownEngine() {
        // Order matters! "## " must be checked before "# ", and the
        // paragraph parser (which accepts anything) must stay last.
        blockParsers.add(new HeadingParser(2));
        blockParsers.add(new HeadingParser(1));
        blockParsers.add(new ParagraphParser());

        inlineParsers.add(new BoldTextParser());
    }

    // Turns one markdown line into one HTML line.
    private String convertLine(String line) {
        // Streams: pick the first parser that says it can handle this line.
        ElementParser blockParser = blockParsers.stream()
                .filter(p -> p.canParse(line))
                .findFirst()
                .orElse(new ParagraphParser());

        String html = blockParser.render(line);          // polymorphic call

        for (ElementParser inline : inlineParsers) {      // polymorphic call again
            if (inline.canParse(html)) {
                html = inline.render(html);
            }
        }
        return html;
    }

    /**
     * PART 6 - FILE I/O (reading side)
     * Reads the file one line at a time with BufferedReader so even a very
     * large .md file never has to be loaded into memory in one go.
     * try-with-resources closes the reader for us, even if an exception occurs.
     */
    public HTMLDocument convert(File markdownFile) throws IOException {
        HTMLDocument doc = new HTMLDocument(titleFrom(markdownFile));

        try (BufferedReader reader = new BufferedReader(new FileReader(markdownFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;                  // blank lines just separate blocks
                }
                doc.appendToBody(convertLine(line));
            }
        }
        return doc;
    }

    // "my-notes.md" -> "my-notes"
    private String titleFrom(File file) {
        String name = file.getName();
        return name.substring(0, name.length() - ".md".length());
    }
}
