import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * PART B - EXECUTION WORKFLOW (Steps 1 and 2)
 * Entry point: asks for the folder, validates it, then hands over to SiteGenerator.
 */
public class Main {

    // Step 2 - Validation: throws explicit file exceptions for bad paths.
    private static File validateSource(String path) throws FileNotFoundException {
        if (path.isEmpty()) {
            throw new FileNotFoundException("You didn't type any path.");
        }
        File dir = new File(path);
        if (!dir.exists()) {
            throw new FileNotFoundException("Folder does not exist: " + path);
        }
        if (!dir.isDirectory()) {
            throw new FileNotFoundException("That path is a file, not a folder: " + path);
        }
        return dir;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== Markdown to HTML Static Site Generator ===");

        // Step 1 - System startup: keep asking until we get a usable folder.
        File sourceDir = null;
        while (sourceDir == null) {
            System.out.print("Enter path to the source directory (.md files): ");
            if (!input.hasNextLine()) {
                break;                                  // input ended, stop asking
            }
            try {
                sourceDir = validateSource(input.nextLine().trim());
            } catch (FileNotFoundException e) {         // clean catch, no stack trace
                System.out.println("Invalid path -> " + e.getMessage() + " Try again.\n");
            }
        }

        if (sourceDir != null) {
            try {
                SiteGenerator generator = new SiteGenerator(sourceDir);
                generator.build();
                generator.printSummary();
            } catch (IOException e) {
                System.out.println("Something went wrong while generating the site: " + e.getMessage());
            }
        }
        input.close();
    }
}
