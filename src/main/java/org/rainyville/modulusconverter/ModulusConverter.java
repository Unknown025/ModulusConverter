package org.rainyville.modulusconverter;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.commons.io.FileUtils;
import org.fusesource.jansi.AnsiConsole;
import org.rainyville.modulusconverter.types.flans.*;
import org.rainyville.modulusconverter.types.modulus.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public class ModulusConverter {
    private static final String modelsPackage = "src/main/java/com/flansmod/client/model/${package}";
    private static String targetPackage = "src/main/java/org/rainyville/modulus/client/model/${package}";
    private static final List<String> ignoreClasses = Arrays.asList("ModelVehicle", "ModelMG", "ModelPlane");

    /**
     * Program start point.
     *
     * @param args Commandline arguments.
     * @throws Exception Thrown if an error occurred.
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String osName = System.getProperty("os.name");
        OptionParser parser = new OptionParser() {
            {
                accepts("path", "Absolute file path of your Flan's mod project.")
                        .withRequiredArg().ofType(File.class).required();
                accepts("package", "Package names of where your models are located.")
                        .withRequiredArg().ofType(String.class).withValuesSeparatedBy(',');
                accepts("pack", "Content pack names of where your config files are located.")
                        .withRequiredArg().ofType(String.class).withValuesSeparatedBy(',');
                accepts("ignoreCompatibility", "Does not check for compatibility.")
                        .withRequiredArg().ofType(Boolean.class);
                accepts("targetPackage", "The target package name for converting models.")
                        .withRequiredArg().ofType(String.class);
                accepts("models", "Directory where your models in *.java form.")
                        .withRequiredArg().ofType(File.class);
                accepts("disableColors", "Disables console colors.").withOptionalArg();
                acceptsAll(Arrays.asList("help", "h", "?"), "Shows all available options.").forHelp();
            }
        };
        OptionSet options = parser.parse(args);

        if (!options.has("disableColors"))
            AnsiConsole.systemInstall();

        if (options.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
            return;
        }

        // Load commandline arguments
        File projectPath = (File) options.valueOf("path");
        List<String> packageNames = (List<String>) options.valuesOf("package");
        List<String> contentPackNames = options.has("pack") ? (List<String>) options.valuesOf("pack") : new ArrayList<>();
        boolean ignoreCompatibility = options.has("ignoreCompatibility") && (boolean) options.valueOf("ignoreCompatibility");
        File modelDir = options.has("models") ? (File) options.valueOf("models") : null;
        if (options.has("targetPackage")) targetPackage = (String) options.valueOf("targetPackage");

        if (modelDir != null && packageNames != null && packageNames.size() > 0) {
            if (modelDir.listFiles() == null || !modelDir.exists()) {
                System.out.println(ansi().fgBrightRed().a("Directory " + modelDir.getCanonicalPath() + " has no files, or does not exist!"));
                throw new IOException("Directory " + modelDir.getCanonicalPath() + " has no files, or does not exist!");
            }

            List<File> models = new ArrayList<>();
            for (File model : modelDir.listFiles()) {
                if (model.getName().endsWith(".java"))
                    models.add(model);
            }

            ProjectCreator creator = new ProjectCreator(models.toArray(new File[0]), packageNames.get(0));
            creator.configureProject(projectPath);
        }

        List<ModelConvert> modelConvertList = new ArrayList<>();

        for (String packageName : packageNames) {
            File packagePath = new File(projectPath, modelsPackage.replace("${package}", packageName));

            System.out.println(ansi().fgBrightYellow().bold().a("Checking package path: " + packagePath.getPath()).reset());
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

        System.out.println(ansi().fgYellow().a("Converted models to be ExW compatible.").reset());

        for (String packName : contentPackNames) {
            translateConfigs(projectPath, packName);
        }

        System.out.println(ansi().fgYellow().a("Loaded ").fgGreen().a(TypeFile.files.size()).fgYellow().a(" configuration files.").reset());

        String shellFile;
        if (osName.startsWith("Windows"))
            shellFile = "gradlew.bat";
        else
            shellFile = "./gradlew";
        System.out.println(ansi().fgBrightGreen().a("Conversion complete! Use ").fgBlue().a(shellFile + " build").fgBrightGreen().a(" to compile your project!").reset());
    }

    /**
     * Recursively looks for model locations.
     *
     * @param list   Current list of located models.
     * @param source Location to search.
     * @param target Location to copy to.
     * @return Returns a list of located models.
     */
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

    /**
     * Translates configuration files from Flansmod to Expansive Weaponry.
     *
     * @param projectPath Path of the Flan's project.
     * @param packageName Package name.
     */
    private static void translateConfigs(File projectPath, String packageName) {
        File configPath = new File(projectPath, "run/Flan/" + packageName);
        File targetPath = new File(projectPath, "run/Expansive Weaponry/" + packageName);
        if (!configPath.exists() || !configPath.isDirectory()) return;

        for (EnumType typeToCheckFor : EnumType.values()) {
            File typesDir = new File(configPath, "/" + typeToCheckFor.folderName + "/");
            if (!typesDir.exists())
                continue;
            for (File file : typesDir.listFiles()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String[] splitName = file.getName().split("/");
                    TypeFile typeFile = new TypeFile(configPath.getName(), typeToCheckFor, splitName[splitName.length - 1].split("\\.")[0]);
                    for (; ; ) {
                        String line;
                        try {
                            line = reader.readLine();
                        } catch (Exception e) {
                            break;
                        }
                        if (line == null)
                            break;
                        typeFile.parseLine(line);
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (EnumType type : EnumType.values()) {
            Class<? extends InfoTypeFlans> typeClass = type.getTypeClass();
            for (TypeFile typeFile : TypeFile.files.get(type)) {
                try {
                    BaseType modulusType = null;

                    InfoTypeFlans infoTypeFlans = (typeClass.getConstructor(TypeFile.class).newInstance(typeFile));
                    infoTypeFlans.read(typeFile);
                    switch (type) {
                        case bullet:
                            BulletTypeFlans flansType = (BulletTypeFlans) infoTypeFlans;

                            AmmoTypeModulus bulletTypeModulus = new AmmoTypeModulus();
                            bulletTypeModulus.displayName = flansType.name;
                            bulletTypeModulus.internalName = flansType.shortName;
                            bulletTypeModulus.iconName = flansType.iconPath;
                            bulletTypeModulus.bulletDamage = flansType.damageVsLiving;
                            bulletTypeModulus.bulletSpread = flansType.bulletSpread;

                            bulletTypeModulus.modelName = flansType.modelString;
                            bulletTypeModulus.modelSkins = new SkinType[1];
                            bulletTypeModulus.modelSkins[0] = new SkinType();
                            bulletTypeModulus.modelSkins[0].skinAsset = flansType.texture;

                            modulusType = bulletTypeModulus;
                            break;
                        case attachment:
                        case vehicle:
                        case plane:
                        case part:
                        case aa:
                        case mechaItem:
                        case mecha:
                        case tool:
                        case box:
                        case armourBox:
                        case playerClass:
                        case team:
                        case itemHolder:
                        case rewardBox:
                        case loadout:
                            System.err.println("Unsupported type: " + infoTypeFlans.shortName);
                            continue;
                        case gun:
                            GunTypeFlans gunTypeFlans = (GunTypeFlans) infoTypeFlans;

                            GunTypeModulus gunTypeModulus = new GunTypeModulus();
                            gunTypeModulus.displayName = gunTypeFlans.name;
                            gunTypeModulus.internalName = convertNamespace(gunTypeFlans.shortName);
                            gunTypeModulus.iconName = gunTypeFlans.iconPath;
                            gunTypeModulus.gunDamage = gunTypeFlans.damage;
                            gunTypeModulus.roundsPerMin = 500; //Does Flans not have RPM?
                            gunTypeModulus.reloadTime = gunTypeFlans.reloadTime;
                            gunTypeModulus.bulletSpread = gunTypeFlans.bulletSpread;
                            gunTypeModulus.acceptedAmmo = new String[gunTypeFlans.ammo.size()];
                            for (int i = 0; i < gunTypeFlans.ammo.size(); i++) {
                                gunTypeModulus.acceptedAmmo[i] = gunTypeFlans.ammo.get(i).shortName;
                            }
                            gunTypeModulus.fireModes = new WeaponFireMode[1];
                            gunTypeModulus.fireModes[0] = getFireModeFromFlans(gunTypeFlans.mode);
                            gunTypeModulus.recoilPitch = gunTypeFlans.recoil;
                            gunTypeModulus.recoilYaw = gunTypeFlans.recoil;
                            String[] name = gunTypeFlans.modelString.split("\\.");

                            name[name.length - 1] = "Model" + name[name.length - 1]; //Flan's automatically pre-appends "Model" to all names, Modulus doesn't.
                            gunTypeModulus.modelName = String.join(".", name);

                            gunTypeModulus.modelSkins = new SkinType[gunTypeFlans.paintjobs.size()];
                            for (int i = 0; i < gunTypeFlans.paintjobs.size(); i++) {
                                Paintjob paintjob = gunTypeFlans.getPaintjob(i);
                                gunTypeModulus.modelSkins[i] = new SkinType();
                                gunTypeModulus.modelSkins[i].skinAsset = convertNamespace(paintjob.textureName);
                            }
                            gunTypeModulus.weaponSoundMap = new HashMap<>();
                            ArrayList<SoundEntry> fireSounds = new ArrayList<>();
                            SoundEntry soundEntry = new SoundEntry();
                            soundEntry.soundEvent = WeaponSoundType.Fire;
                            fireSounds.add(soundEntry);
                            gunTypeModulus.weaponSoundMap.put(WeaponSoundType.Fire, fireSounds);

                            modulusType = gunTypeModulus;
                            break;
                        case grenade:
//                            new ItemGrenade((GrenadeType) infoType).setTranslationKey(infoType.shortName);
                            break;
                        case armour:
//                            armourItems.add((ItemTeamArmour) new ItemTeamArmour((ArmourType) infoType).setTranslationKey(infoType.shortName));
                            break;
                        default:
                            System.err.println(ansi().fgRed().a("Unrecognized type for " + infoTypeFlans.shortName).reset());
                            break;
                    }

                    if (modulusType != null) {
                        try {
                            File parent = new File(targetPath, modulusType.getAssetDir());
                            if (!parent.exists())
                                parent.mkdirs();

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            FileWriter fileWriter = new FileWriter(new File(targetPath, modulusType.getAssetDir() + "/" + modulusType.internalName + ".json"), false);
                            gson.toJson(modulusType, fileWriter);
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to add " + type.name() + " : " + typeFile.name);
                    e.printStackTrace();
                }
            }
            System.out.println(ansi().fgBrightGreen().a("Loaded " + type.name() + "."));
        }

        Path resourcesPath = new File(configPath, "assets/flansmod").toPath();
        Path targetResourcesPath = new File(targetPath, "assets/exw").toPath();

        try {
//            FileUtils.copyDirectory(resourcesPath, targetResourcesPath);


            Files.walkFileTree(resourcesPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.createDirectories(targetResourcesPath.resolve(resourcesPath.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path rel = targetResourcesPath.resolve(resourcesPath.relativize(file));
                    File typeFile = file.toFile();
                    FileUtils.copyFile(file.toFile(), new File(rel.toFile().getParentFile(),
                            !typeFile.getParent().endsWith("lang") ? convertNamespace(typeFile.getName()) : typeFile.getName()), true);
//                    Path resolved = targetResourcesPath.resolve(Paths.get(file.toFile().getParent()) + convertNamespace(file.toFile().getName()));
//                    Files.copy(file, resolved, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
            System.err.println(ansi().fgYellow().a("Copied all assets from Flansmod to ExW").reset());
        } catch (IOException e) {
            System.err.println(ansi().fgRed().a("Could not copy assets from Flansmod to ExW").reset());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Gets the equivalent Modulus fire mode from Flans.
     *
     * @param mode Flans fire mode.
     * @return Returns the Modulus equivalent.
     */
    private static WeaponFireMode getFireModeFromFlans(EnumFireMode mode) {
        switch (mode) {
            case FULLAUTO:
            case MINIGUN:
                return WeaponFireMode.FULL;
            case BURST:
                return WeaponFireMode.BURST;
            case SEMIAUTO:
            default:
                return WeaponFireMode.SEMI;
        }
    }

    /**
     * Converts a namespace to be 1.8+ compatible.
     *
     * @param input Input, such as an item ID or file name.
     * @return Returns a 1.8+ compliant ID.
     */
    private static String convertNamespace(String input) {
        input = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, input);
        input = input.toLowerCase(Locale.ROOT);
        input = input.replace(" ", "_");
        return input;
    }
}
