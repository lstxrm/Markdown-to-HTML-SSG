/**
 * PART 2 - INHERITANCE  /  PART A - Headings
 * Handles "# Title" -> <h1> and "## Title" -> <h2>.
 * One class is enough for both because the only thing that changes is the level,
 * so I just pass the level through the constructor.
 */
public class HeadingParser extends ElementParser {

    private final int level;
    private final String marker;   // e.g. "## " for level 2

    public HeadingParser(int level) {
        this.level = level;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append('#');
        }
        sb.append(' ');
        this.marker = sb.toString();
    }

    @Override
    public boolean canParse(String text) {
        return text.startsWith(marker);
    }

    @Override
    public String render(String text) {
        String content = escapeHtml(text.substring(marker.length()).trim());
        return "<h" + level + ">" + content + "</h" + level + ">";
    }
}
