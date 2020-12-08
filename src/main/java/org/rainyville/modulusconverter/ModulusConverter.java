package org.rainyville.modulusconverter;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.fusesource.jansi.Ansi.ansi;

public class ModulusConverter {
    public static void main(String[] args) throws IOException {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(Ansi.Color.CYAN).a("Running ModulusConverter."));
        OptionParser parser = new OptionParser() {
            {
                accepts("path").withRequiredArg().ofType(File.class)
                        .describedAs("Location of your Flan's mod project.").required();
                acceptsAll(Arrays.asList("help", "h", "?")).forHelp();
            }
        };
        OptionSet options = parser.parse(args);

        File projectPath = (File) options.valueOf("path");
        System.out.println(ansi().fg(Ansi.Color.YELLOW).bold().a("Checking project path: " + projectPath.getAbsolutePath()));
    }
}
