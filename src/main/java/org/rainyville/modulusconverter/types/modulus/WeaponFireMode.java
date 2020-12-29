package org.rainyville.modulusconverter.types.modulus;

import com.google.gson.annotations.SerializedName;

public enum WeaponFireMode {

    /**
     * SemiAutomatic fire mode
     */
    @SerializedName("semi") SEMI,

    /**
     * Fully automatic fire mode
     */
    @SerializedName("full") FULL,

    /**
     * Burst of shots fire mode
     */
    @SerializedName("burst") BURST;

    @Override
    public String toString() {
        String name = name().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
