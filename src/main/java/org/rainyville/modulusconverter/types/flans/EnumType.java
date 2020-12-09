package org.rainyville.modulusconverter.types.flans;

public enum EnumType {
    part("parts"), bullet("bullets"), attachment("attachments"), grenade("grenades"), gun("guns"),
    aa("aaguns"), vehicle("vehicles"), plane("planes"), mechaItem("mechaItems"), mecha("mechas"),
    tool("tools"), armour("armorFiles"), armourBox("armorBoxes"), box("boxes"), playerClass("classes"),
    team("teams"), itemHolder("itemHolders"), rewardBox("rewardBoxes"), loadout("loadouts");

    public String folderName;

    EnumType(String s) {
        folderName = s;
    }

    public static EnumType get(String s) {
        for (EnumType e : values()) {
            if (e.folderName.equals(s))
                return e;
        }
        return null;
    }

    public Class<? extends InfoType> getTypeClass()
    {
        switch(this)
        {
//            case bullet: return BulletType.class;
            case gun: return GunType.class;
//            case grenade: return GrenadeType.class;
//            case armour: return ArmourType.class;
            default: return InfoType.class;
        }
    }
}
