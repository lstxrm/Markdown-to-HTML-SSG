import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * PART B - EXECUTION WORKFLOW (Steps 3 and 4)
 * Finds every .md file under the source folder (recursively), pushes them
 * into a queue, then compiles them one by one into the dist/ folder.
 */
public class SiteGenerator {

    private final File sourceDir;
    private final File distDir;
    private final MarkdownEngine engine = new MarkdownEngine();
    private final PageTemplate template = new PageTemplate();

    // Collections: a Queue of files waiting to be processed (FIFO),
    // and a List remembering what we already generated.
    private final Queue<File> pending = new LinkedList<>();
    private final List<String> generated = new ArrayList<>();

    public SiteGenerator(File sourceDir) {
        this.sourceDir = sourceDir;
        this.distDir = new File("dist");                    // Step 4: output folder
        template.loadCustomLayout(new File(sourceDir, "template.html"));   // Step 3
    }

    // Recursive walk through folders, collecting markdown files.
    private void scan(File folder) {
        File[] items = folder.listFiles();
        if (items == null) {
            return;                                         // unreadable folder, skip it
        }
        Arrays.sort(items);                                 // same order every run

        for (File item : items) {
            if (item.isDirectory()) {
                scan(item);                                 // go deeper
            } else if (item.getName().endsWith(".md")) {
                pending.add(item);
            }
        }
    }

    /** Runs the whole job; returns how many pages were written. */
    public int build() throws IOException {
        scan(sourceDir);

        if (!distDir.exists() && !distDir.mkdirs()) {
            throw new IOException("Could not create output folder: " + distDir.getPath());
        }

        while (!pending.isEmpty()) {
            compile(pending.poll());                        // take from the front
        }
        return generated.size();
    }

    private void compile(File markdownFile) throws IOException {
        HTMLDocument doc = engine.convert(markdownFile);    // markdown -> document
        String fullPage = template.apply(doc);              // document -> full page

        File outFile = outputFileFor(markdownFile);
        File parent = outFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();                                // keep sub-folder structure
        }

        // PART 6 - FILE I/O (writing side)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            writer.write(fullPage);
        }
        generated.add(outFile.getPath());
    }

    // docs/notes/a.md  ->  dist/notes/a.html
    private String relativeName(File markdownFile) {
        String root = sourceDir.getAbsolutePath();
        String full = markdownFile.getAbsolutePath();
        String relative = full.substring(root.length());
        if (relative.startsWith(File.separator)) {
            relative = relative.substring(1);
        }
        return relative.substring(0, relative.length() - ".md".length()) + ".html";
    }

    private File outputFileFor(File markdownFile) {
        return new File(distDir, relativeName(markdownFile));
    }

    // Streams used again to print a neat sorted report.
    public void printSummary() {
        if (generated.isEmpty()) {
            System.out.println("No .md files were found in the source directory.");
            return;
        }
        System.out.println("\nGenerated files:");
        String report = generated.stream()
                .sorted()
                .map(name -> "  -> " + name)
                .collect(Collectors.joining("\n"));
        System.out.println(report);
        System.out.println("\nTotal pages: " + generated.size());
    }
}
