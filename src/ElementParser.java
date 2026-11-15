/**
 * PART 1 - ABSTRACTION
 * Blueprint for every markdown syntax parser in the project.
 * Each parser must say (a) whether it understands a line and
 * (b) how to turn that line into HTML. The engine only ever
 * talks to this type, so it doesn't care which parser it holds.
 */
public abstract class ElementParser {

    // Does this parser know how to handle the given text?
    public abstract boolean canParse(String text);

    // Convert the text into its HTML form.
    public abstract String render(String text);

    // Small helper shared by all children so that characters like < or &
    // in a markdown file don't break the generated page.
    protected String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }
}
