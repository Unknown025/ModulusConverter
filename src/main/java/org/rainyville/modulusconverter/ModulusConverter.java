package org.rainyville.modulusconverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.fusesource.jansi.AnsiConsole;
import org.rainyville.modulusconverter.types.flans.*;
import org.rainyville.modulusconverter.types.modulus.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
                accepts("pack", "Content pack names of where your config files are located.")
                        .withRequiredArg().ofType(String.class).withValuesSeparatedBy(',');
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
        List<String> contentPackNames = options.has("pack") ? (List<String>) options.valuesOf("pack") : new ArrayList<>();
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

        System.out.println(ansi().fgBrightGreen().a("Converted models to be Modulus compatible.").reset());

        for (String packName : contentPackNames) {
            translateConfigs(projectPath, packName);
        }

        System.out.println(ansi().fgBrightBlue().a("Loaded ").fgBrightGreen().a(TypeFile.files.size()).fgBrightBlue().a(" configuration files.").reset());
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

    private static void translateConfigs(File projectPath, String packageName) {
        File configPath = new File(projectPath, "run/Flan/" + packageName);
        File targetPath = new File(projectPath, "run/Modular Warfare/" + packageName);
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
                            gunTypeModulus.internalName = gunTypeFlans.shortName;
                            gunTypeModulus.iconName = gunTypeFlans.iconPath;
                            gunTypeModulus.gunDamage = gunTypeFlans.damage;
                            gunTypeModulus.roundsPerMin = gunTypeFlans.numBurstRounds; //Does Flans not have RPM?
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
                                gunTypeModulus.modelSkins[i].skinAsset = paintjob.textureName;
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
                            System.err.println("Unrecognised type for " + infoTypeFlans.shortName);
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
    }

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
}
