package org.rainyville.modulusconverter;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * PACKAGE: org.rainyville.modulusconverter
 * DATE: 7/14/21
 * TIME: 11:19 AM
 * PROJECT: ModulusConverter
 */
public class ProjectCreator {
    private static final String GITHUB_REPO = "https://github.com/FlansMods/FlansMod/archive/refs/heads/1.12.2.zip";
    private final File[] models;
    private final String packageName;

    /**
     * Initializes the ProjectCreator.
     *
     * @param models      Array of model files.
     * @param packageName Package name to copy them to.
     */
    public ProjectCreator(File[] models, String packageName) {
        this.models = models;
        this.packageName = packageName;
    }

    /**
     * Configures a Flan's Mod project at a given location.
     *
     * @param location Location to configure at.
     * @throws IOException Thrown in the event of a file error.
     */
    public void configureProject(File location) throws IOException {
        if (!location.exists()) {
            boolean success = location.mkdirs();
            if (!success) throw new IOException("Could not create directory: " + location.getAbsolutePath());

            System.out.println(ansi().fg(Ansi.Color.GREEN).a("Created directory " + location.getPath() + "!").reset());
        }

        File archive = new File(location, "download.zip");

        FileUtils.copyURLToFile(new URL(GITHUB_REPO), archive, 300, 300);

        System.out.println(ansi().fg(Ansi.Color.CYAN).a("Downloaded FlansMod-1.12.2").reset());

        ZipFile zipFile = new ZipFile(archive);

        // Extract the Flan's project folder from within the archive.
        for (FileHeader header : zipFile.getFileHeaders()) {
            if (!header.isDirectory()) {
                String extractName = header.getFileName().substring("FlansMod-1.12.2/".length());
                zipFile.extractFile(header, location.getAbsolutePath(), extractName);
            }
        }

        System.out.println(ansi().fg(Ansi.Color.CYAN).a("Extracted FlansMod-1.12.2").reset());

        File targetPackageDir = new File(location, "src/main/java/com/flansmod/client/model/" + packageName.replace(".", "/"));
        for (File model : models) {
            FileUtils.copyFileToDirectory(model, targetPackageDir);
            System.out.println(ansi().fg(Ansi.Color.YELLOW).a("Copying: ").fg(Ansi.Color.GREEN).a(model.getName()));
        }
    }
}
