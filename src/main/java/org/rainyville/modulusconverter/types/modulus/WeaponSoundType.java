package org.rainyville.modulusconverter.types.modulus;

import com.google.gson.annotations.SerializedName;

public enum WeaponSoundType {
    /**
     * The sound played upon dry firing
     */
    @SerializedName("weaponDryFire") DryFire("weaponDryFire", 8, "defemptyclick"),

    /**
     * The sound played upon shooting
     */
    @SerializedName("weaponFire") Fire("weaponFire", 64, null),

    /**
     * The sound played upon shooting with a silencer
     */
    @SerializedName("weaponFireSuppressed") FireSuppressed("weaponFireSuppressed", 32, null),

    /**
     * The sound to play upon shooting on last round
     */
    @SerializedName("weaponFireLast") FireLast("weaponFireLast", 16, null),

    /**
     * The sound to play upon reloading
     */
    @SerializedName("weaponReload") Reload("weaponReload", 16, null),

    /**
     * The sound to play when a bullet hit a block
     */
    @SerializedName("impact") Impact("impact", 16, "impact"),

    /**
     * The sound to play when equip a gun
     */
    @SerializedName("equip") Equip("equip", 8, "equip"),


    /**
     * The sound to play when a bullet hit an entity (played to the shooter)
     */
    @SerializedName("hitmarker") Hitmarker("hitmarker", 8, "hitmarker"),

    /**
     * The sound to play when an entity is damaged
     */
    @SerializedName("penetration") Penetration("penetration", 20, "penetration"),

    /**
     * The sound to play upon reloading
     */
    @SerializedName("magIn") MagIn("magIn", 16, null),

    /**
     * The sound to play upon reloading
     */
    @SerializedName("magOut") MagOut("magOut", 16, null),

    /**
     * The sound to play upon reloading when empty
     */
    @SerializedName("emptyReload") EmptyReload("emptyReload", 12, null),

    /**
     * The sound to play upon charging
     */
    @SerializedName("rack") Rack("rack", 16, null),

    /**
     * The sound to play upon switching fire modes
     */
    @SerializedName("weaponModeSwitch") ModeSwitch("weaponModeSwitch", 8, "defweaponmodeswitch"),

    /**
     * The sound of flyby
     */
    @SerializedName("bulletFlyBy") FlyBy("bulletFlyBy", 8, "flyby"),

    /**
     * The impact sound for metal materials.
     */
    @SerializedName("impactMetal") ImpactMetal("impactSoundMetal", 8, "impact.metal"),

    /**
     * The impact sound for soft materials.
     */
    @SerializedName("impactSoft") ImpactSoft("impactSoundSoft", 8, "impact.soft");


    public String eventName;
    public Integer defaultRange;
    public String defaultSound;

    WeaponSoundType(String eventName, int defaultRange, String defaultSound) {
        this.eventName = eventName;
        this.defaultRange = defaultRange;
        this.defaultSound = defaultSound;
    }

    public static WeaponSoundType fromString(String input) {
        for (WeaponSoundType soundType : values()) {
            if (soundType.toString().equalsIgnoreCase(input)) {
                return soundType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return eventName;
    }

}
