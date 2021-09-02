package org.rainyville.modulusconverter.types.modulus;

import com.google.gson.annotations.SerializedName;

/**
 * Different weapon types (not currently implemented).
 * CUSTOM, PISTOL, MP, SMG, CARBINE, RIFLE, AR, DMR, SNIPER, SHOTGUN, etc
 */
public enum WeaponType {
    @SerializedName("custom") CUSTOM("custom"),
    @SerializedName("pistol") PISTOL("pistol"),
    @SerializedName("mp") MP("mp"),
    @SerializedName("smg") SMG("smg"),
    @SerializedName("carbine") Carbine("carbine"),
    @SerializedName("rifle") RIFLE("rifle"),
    @SerializedName("ar") AR("ar"),
    @SerializedName("dmr") DMR("dmr"),
    @SerializedName("semisniper") SEMI_SNIPER("semisniper"),
    @SerializedName("boltsniper") BOLT_SNIPER("boltsniper"),
    @SerializedName("shotgun") SHOTGUN("shotgun");

    public String typeName;

    WeaponType(String typeName) {
        this.typeName = typeName;
    }

    public static WeaponType fromEventName(String typeName) {
        if (typeName != null) {
            for (WeaponType soundType : values()) {
                if (soundType.typeName.equalsIgnoreCase(typeName)) {
                    return soundType;
                }
            }
        }
        return null;
    }

}