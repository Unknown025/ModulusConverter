package org.rainyville.modulusconverter;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * PACKAGE: org.rainyville.modulusconverter
 * DATE: 7/14/21
 * TIME: 11:19 AM
 * PROJECT: ModulusConverter
 */
public class ProjectCreator {
    private static final String GITHUB_REPO = "https://github.com/FlansMods/FlansMod/archive/refs/heads/1.12.2.zip";
    private static final String EXW_DEPENDENCY = "deobfCompile 'org.rainyville.modulus:expansive-weaponry:1.12-1.0.5'";

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
        if (!archive.exists()) {
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

            File gradleFile = new File(location, "build.gradle");

            String contents = new String(Files.readAllBytes(gradleFile.toPath()), StandardCharsets.UTF_8);
            String[] lines = contents.split("\n");
            List<String> linesList = new ArrayList<>(Arrays.asList(lines));
            for (int i = 0; i < linesList.size(); i++) {
                String line = linesList.get(i);
                if (line.startsWith("dependencies {")) {
                    linesList.add(i + 1, "\t" + EXW_DEPENDENCY);
                    break;
                }
            }
            contents = String.join("\n", linesList.toArray(new String[0]));
            Files.write(gradleFile.toPath(), contents.getBytes(StandardCharsets.UTF_8));
        } else {
            System.out.println(ansi().fgBrightYellow().a("FlansMod archive found, assuming the project already exists").reset());
        }

        File targetPackageDir = new File(location, "src/main/java/com/flansmod/client/model/" + packageName.replace(".", "/"));
        for (File model : models) {
            FileUtils.copyFileToDirectory(model, targetPackageDir);
            System.out.println(ansi().fg(Ansi.Color.YELLOW).a("Copying: ").fg(Ansi.Color.GREEN).a(model.getName()));
        }
    }
}
