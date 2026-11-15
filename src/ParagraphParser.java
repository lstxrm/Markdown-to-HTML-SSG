/**
 * PART 2 - INHERITANCE  /  PART A - Paragraphs
 * The fallback parser: any normal line that isn't a heading becomes <p>...</p>.
 * It says "yes" to everything, so it must always sit LAST in the parser list.
 */
public class ParagraphParser extends ElementParser {

    @Override
    public boolean canParse(String text) {
        return true;
    }

    @Override
    public String render(String text) {
        return "<p>" + escapeHtml(text.trim()) + "</p>";
    }
}
