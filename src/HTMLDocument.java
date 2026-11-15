/**
 * PART 3 - ENCAPSULATION
 * Holds everything that makes up one web page. All fields are private;
 * the outside world can only touch them through the methods below, which
 * also protect against bad values (null titles, null lines, etc.).
 */
public class HTMLDocument {

    private String title;
    private String header;
    private final StringBuilder body;   // compiled HTML, built up line by line

    public HTMLDocument(String title) {
        this.body = new StringBuilder();
        setTitle(title);
        this.header = this.title;       // by default the header repeats the title
    }

    // ---- getters ----
    public String getTitle()  { return title; }
    public String getHeader() { return header; }
    public String getBody()   { return body.toString(); }

    // ---- controlled mutations ----
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            this.title = "Untitled Page";
        } else {
            this.title = title.trim();
        }
    }

    public void setHeader(String header) {
        if (header != null && !header.trim().isEmpty()) {
            this.header = header.trim();
        }
    }

    // The only way to add content to the body.
    public void appendToBody(String htmlLine) {
        if (htmlLine == null || htmlLine.isEmpty()) {
            return;                      // ignore empty stuff silently
        }
        body.append("    ").append(htmlLine).append("\n");
    }
}
