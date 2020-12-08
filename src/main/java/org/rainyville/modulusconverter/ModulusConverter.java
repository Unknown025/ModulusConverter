package org.rainyville.modulusconverter;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class ModulusConverter {
    private static final String modelsPackage = "src/main/java/com/flansmod/client/model/${package}";
    private static final String targetPackage = "src/main/java/org/rainyville/modulus/client/model/${package}";
    private static final List<String> ignoreClasses = Arrays.asList("ModelVehicle", "ModelMG", "ModelPlane");

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        AnsiConsole.systemInstall();
        OptionParser parser = new OptionParser() {
            {
                accepts("path", "Absolute file path of your Flan's mod project.")
                        .withRequiredArg().ofType(File.class).required();
                accepts("package", "Package names of where your models are located.")
                        .withRequiredArg().ofType(String.class).withValuesSeparatedBy(',').required();
                accepts("ignoreCompatibility", "Does not check for compatibility.")
                        .withRequiredArg().ofType(Boolean.class);
                acceptsAll(Arrays.asList("help", "h", "?"), "Shows all available options.").forHelp();
            }
        };
        OptionSet options = parser.parse(args);

        if (options.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }

        File projectPath = (File) options.valueOf("path");
        List<String> packageNames = (List<String>) options.valuesOf("package");
        List<ModelConvert> modelConvertList = new ArrayList<>();

        boolean ignoreCompatibility = options.has("ignoreCompatibility") && (boolean) options.valueOf("ignoreCompatibility");

        for (String packageName : packageNames) {
            File packagePath = new File(projectPath, modelsPackage.replace("${package}", packageName));

            System.out.println(ansi().fgBrightYellow().bold().a("Checking package path: " + packagePath.getAbsolutePath()).reset());
            if (!packagePath.exists() || !packagePath.isDirectory())
                throw new RuntimeException("Given path does not exist, or is not a directory! " + packagePath.getAbsolutePath());
            modelConvertList.addAll(findModels(new ArrayList<>(), packagePath, new File(projectPath, targetPackage.replace("${package}", packageName))));
        }

        for (ModelConvert modelConvert : modelConvertList) {
            boolean ignoreModel = false;

            if (!modelConvert.target.getParentFile().exists())
                modelConvert.target.getParentFile().mkdirs();

            String contents = new String(Files.readAllBytes(modelConvert.source.toPath()), StandardCharsets.UTF_8);
            contents = contents.replace("com.flansmod.common.vector.Vector3f", "org.lwjgl.util.vector.Vector3f");
            contents = contents.replace("com.flansmod.client", "org.rainyville.modulus.client");
            contents = contents.replace("ModelCustomArmour", "ModelArmor");

            if (!ignoreCompatibility) {
                if (contents.contains("org.rainyville.modulus.client.model.EnumAnimationType"))
                    ignoreModel = true;

                String[] words = contents.split(" ");
                for (String ignore : ignoreClasses) {
                    for (int i = 0; i < words.length; i++) {
                        if (words[i].equals("extends")) {
                            if (words[i + 1].startsWith(ignore)) {
                                ignoreModel = true;
                            }
                            break;
                        }
                    }
                }
            }

            if (ignoreModel) {
                System.out.println(ansi().fgBrightRed().a("Could not convert incompatible model: " + modelConvert.source.getName()).reset());
            } else {
                Files.write(modelConvert.target.toPath(), contents.getBytes(StandardCharsets.UTF_8));
            }
        }

        System.out.println(ansi().fg(Ansi.Color.GREEN).bold().a("Converted models to be Modulus compatible.").reset());
    }

    private static List<ModelConvert> findModels(List<ModelConvert> list, File source, File target) {
        for (File model : source.listFiles()) {
            if (model.isDirectory()) {
                return findModels(list, model, new File(target, model.getName()));
            } else {
                list.add(new ModelConvert(model, new File(target, model.getName())));
            }
        }
        return list;
    }
}
