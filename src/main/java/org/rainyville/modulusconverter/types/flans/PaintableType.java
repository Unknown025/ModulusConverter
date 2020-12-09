package org.rainyville.modulusconverter.types.flans;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PaintableType extends InfoType {
    //Paintjobs
    /**
     * The list of all available paintjobs for this gun
     */
    public ArrayList<Paintjob> paintjobs = new ArrayList<>();
    /**
     * The default paintjob for this gun. This is created automatically in the load process from existing info
     */
    public Paintjob defaultPaintjob;
    /**
     * Assigns IDs to paintjobs
     */
    private int nextPaintjobID = 1;

    private static HashMap<Integer, PaintableType> paintableTypes = new HashMap<>();

    public static PaintableType GetPaintableType(int iHash) {
        return paintableTypes.get(iHash);
    }

    public static PaintableType GetPaintableType(String name) {
        return paintableTypes.get(name.hashCode());
    }

    public PaintableType(TypeFile file) {
        super(file);
    }

    @Override
    public void postRead(TypeFile file) {
        super.postRead(file);

        //After all lines have been read, set up the default paintjob
        defaultPaintjob = new Paintjob(this, 0, "", texture);
        //Move to a new list to ensure that the default paintjob is always first
        ArrayList<Paintjob> newPaintjobList = new ArrayList<>();
        newPaintjobList.add(defaultPaintjob);
        newPaintjobList.addAll(paintjobs);
        paintjobs = newPaintjobList;
        if (infoTypes.containsKey(shortName.hashCode())) {
            System.err.println("Duplicate info type name " + shortName);
        }

        // Add all custom paintjobs to dungeon loot. Equal chance for each
        totalDungeonChance += dungeonChance * (paintjobs.size() - 1);

        paintableTypes.put(shortName.hashCode(), this);
    }

    /**
     * Pack reader
     */
    protected void read(String[] split, TypeFile file) {
        super.read(split, file);
        try {
            //Paintjobs
            if (KeyMatches(split, "Paintjob")) {
                if (split[1].contains("_")) {
                    String[] splat = split[1].split("_");
                    if (splat[0].equals(iconPath))
                        split[1] = splat[1];
                }
                paintjobs.add(new Paintjob(this, nextPaintjobID++, split[1], split[2]));
            }
        } catch (Exception e) {
            System.err.println("Reading file failed : " + shortName);
            e.printStackTrace();
        }
    }

    public Paintjob getPaintjob(int i) {
        return paintjobs.get(i);
    }
}
