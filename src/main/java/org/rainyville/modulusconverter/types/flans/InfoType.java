package org.rainyville.modulusconverter.types.flans;

import java.util.HashMap;
import java.util.Random;

import static org.fusesource.jansi.Ansi.ansi;

public class InfoType {
    /**
     * infoTypes
     */
    public static HashMap<Integer, InfoType> infoTypes = new HashMap<>();

    public final String contentPack;
    public int colour = 0xffffff;
    public String[] recipeLine;
    public char[][] recipeGrid = new char[3][3];
    public int recipeOutput = 1;
    public boolean shapeless;
    public String smeltableFrom = null;
    public String modelString = null;
    public String name = "";
    public String shortName = "";
    public String texture = "";
    public String description = "";
    public String iconPath = "";
    public float modelScale = 1F;
    /**
     * If this is set to false, then this item cannot be dropped
     */
    public boolean canDrop = true;

    public int hash = 0;

    /**
     * The probability that this item will appear in a dungeon chest.
     * Scaled so that each chest is likely to have a fixed number of Flan's Mod items.
     * Must be greater than or equal to 0, and should probably not exceed 100
     */
    public int dungeonChance = 1;

    public static Random random = new Random();

    /**
     * Used for scaling
     */
    public static int totalDungeonChance = 0;

    public InfoType(TypeFile file) {
        contentPack = file.contentPack;
    }

    public void read(TypeFile file) {
        preRead(file);
        for (; ; ) {
            String line;
            line = file.readLine();
            if (line == null)
                break;
            if (line.startsWith("//"))
                continue;
            String[] split = line.split(" ");
            if (split.length < 2)
                continue;
            read(split, file);
        }
        postRead(file);

        hash = file.hashCode();
        infoTypes.put(shortName.hashCode(), this);
        totalDungeonChance += dungeonChance;
    }

    /**
     * Method for performing actions prior to reading the type file
     */
    protected void preRead(TypeFile file) {
    }

    /**
     * Method for performing actions after reading the type file
     */
    protected void postRead(TypeFile file) {
        // Check that recommended values were set
        if (shortName.isEmpty()) {
            System.err.println("ShortName not set: " + file.name);
        }
        if (name.isEmpty()) {
            System.err.println("Name not set: " + file.name);
        }
    }

    /**
     * Pack reader
     */
    protected void read(String[] split, TypeFile file) {
        try {
            // Standard line reads
            shortName = Read(split, "ShortName", shortName);
            name = ReadAndConcatenateMultipleStrings(split, "Name", name);
            description = ReadAndConcatenateMultipleStrings(split, "Description", description);

            modelString = Read(split, "Model", modelString);
            modelScale = Read(split, "ModelScale", modelScale);
            texture = Read(split, "Texture", texture);

            iconPath = Read(split, "Icon", iconPath);

            dungeonChance = Read(split, "DungeonProbability", dungeonChance);
            dungeonChance = Read(split, "DungeonLootChance", dungeonChance);

            recipeOutput = Read(split, "RecipeOutput", recipeOutput);

            smeltableFrom = Read(split, "SmeltableFrom", smeltableFrom);
            canDrop = Read(split, "CanDrop", canDrop);

            // More complicated line reads
            if (split[0].equals("Colour") || split[0].equals("Color")) {
                colour = (Integer.parseInt(split[1]) << 16) + ((Integer.parseInt(split[2])) << 8) + ((Integer.parseInt(split[3])));
            }

            if (split[0].equals("Recipe")) {
                for (int i = 0; i < 3; i++) {
                    String line = null;
                    line = file.readLine();
                    if (line == null) {
                        continue;
                    }
                    if (line.startsWith("//")) {
                        i--;
                        continue;
                    }

                    if (line.length() > 3)
                        System.err.println("Looks like a bad recipe in " + shortName + ". Double check whether '"
                                + line + "' is supposed to be part of the recipe");

                    for (int j = 0; j < 3; j++) {
                        recipeGrid[i][j] = j < line.length() ? line.charAt(j) : ' ';
                    }
                }
                recipeLine = split;
                shapeless = false;
            } else if (split[0].equals("ShapelessRecipe")) {
                recipeLine = split;
                shapeless = true;
            }
        } catch (Exception e) {
            System.err.println("Reading file failed : " + shortName);
            e.printStackTrace();
        }
    }

    /** -------------------------------------------------------------------------------------------------------- */
    /** HELPER FUNCTIONS FOR READING. Should give better debug output                                            */
    /**
     * --------------------------------------------------------------------------------------------------------
     */
    protected boolean KeyMatches(String[] split, String key) {
        return split != null && split.length > 1 && key != null && split[0].toLowerCase().equals(key.toLowerCase());
    }

    protected int Read(String[] split, String key, int currentValue) {
        if (KeyMatches(split, key)) {
            if (split.length == 2) {
                try {
                    currentValue = Integer.parseInt(split[1]);
                } catch (Exception e) {
                    InfoType.LogError(shortName, "Incorrect format for " + key + ". Passed in value is not an integer");
                }
            } else {
                InfoType.LogError(shortName, "Incorrect format for " + key + ". Should be \"" + key + " <integer value>\"");
            }
        }

        return currentValue;
    }

    protected float Read(String[] split, String key, float currentValue) {
        if (KeyMatches(split, key)) {
            if (split.length == 2) {
                try {
                    currentValue = Float.parseFloat(split[1]);
                } catch (Exception e) {
                    InfoType.LogError(shortName, "Incorrect format for " + key + ". Passed in value is not an float");
                }
            } else {
                InfoType.LogError(shortName, "Incorrect format for " + key + ". Should be \"" + key + " <float value>\"");
            }
        }

        return currentValue;
    }

    protected double Read(String[] split, String key, double currentValue) {
        if (KeyMatches(split, key)) {
            if (split.length == 2) {
                try {
                    currentValue = Double.parseDouble(split[1]);
                } catch (Exception e) {
                    InfoType.LogError(shortName, "Incorrect format for " + key + ". Passed in value is not an float");
                }
            } else {
                InfoType.LogError(shortName, "Incorrect format for " + key + ". Should be \"" + key + " <float value>\"");
            }
        }

        return currentValue;
    }

    protected String Read(String[] split, String key, String currentValue) {
        if (KeyMatches(split, key)) {
            if (split.length == 2) {
                currentValue = split[1];
            } else {
                InfoType.LogError(shortName, "Incorrect format for " + key + ". Should be \"" + key + " <singleWord>\"");
            }
        }

        return currentValue;
    }

    protected String ReadAndConcatenateMultipleStrings(String[] split, String key, String currentValue) {
        if (KeyMatches(split, key)) {
            if (split.length > 1) {
                currentValue = split[1];
                for (int i = 0; i < split.length - 2; i++) {
                    currentValue = currentValue + " " + split[i + 2];
                }
            } else {
                InfoType.LogError(shortName, "Incorrect format for " + key + ". Should be \"" + key + " <long string>\"");
            }
        }

        return currentValue;
    }

    protected boolean Read(String[] split, String key, boolean currentValue) {
        if (KeyMatches(split, key)) {
            if (split.length == 2) {
                try {
                    currentValue = Boolean.parseBoolean(split[1]);
                } catch (Exception e) {
                    InfoType.LogError(shortName, "Incorrect format for " + key + ". Passed in value is not an boolean");
                }
            } else {
                InfoType.LogError(shortName, "Incorrect format for " + key + ". Should be \"" + key + " <true/false>\"");
            }
        }

        return currentValue;
    }
    /** -------------------------------------------------------------------------------------------------------- */
    /**                                                                                                          */
    /**
     * --------------------------------------------------------------------------------------------------------
     */

    protected static void LogError(String shortName, String s) {
        System.err.println(ansi().fgBrightRed().a("[Problem in " + shortName + ".txt]" + s));
    }

    @Override
    public String toString() {
        return super.getClass().getSimpleName() + ": " + shortName;
    }


    /**
     * To be overriden by subtypes for model reloading
     */
    public void reloadModel() {

    }

    @Override
    public int hashCode() {
        return shortName.hashCode();
    }

    public static InfoType getType(String s) {
        return infoTypes.get(s.hashCode());
    }

    public static InfoType getType(int hash) {
        return infoTypes.get(hash);
    }
}
