/**
 * PART 2 - INHERITANCE  /  PART A - Inline Styling
 * Replaces **text** with <strong>text</strong>.
 * Unlike the block parsers, this one works on a line that is already HTML,
 * so it can bold words inside headings and paragraphs alike.
 * I used indexOf instead of regex to keep the logic easy to trace by hand.
 */
public class BoldTextParser extends ElementParser {

    private static final String MARK = "**";

    @Override
    public boolean canParse(String text) {
        int first = text.indexOf(MARK);
        // we need an opening ** AND a closing ** later on
        return first != -1 && text.indexOf(MARK, first + MARK.length()) != -1;
    }

    @Override
    public String render(String text) {
        StringBuilder result = new StringBuilder();
        int position = 0;

        while (position < text.length()) {
            int open = text.indexOf(MARK, position);
            if (open == -1) {
                break;
            }
            int close = text.indexOf(MARK, open + MARK.length());
            if (close == -1) {
                break;                  // lonely ** - leave it as plain text
            }
            result.append(text, position, open);
            result.append("<strong>")
                  .append(text, open + MARK.length(), close)
                  .append("</strong>");
            position = close + MARK.length();
        }
        result.append(text.substring(position));   // whatever is left over
        return result.toString();
    }
}
