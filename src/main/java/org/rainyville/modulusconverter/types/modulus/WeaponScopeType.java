package org.rainyville.modulusconverter.types.modulus;

import com.google.gson.annotations.SerializedName;

public enum WeaponScopeType {

	@SerializedName("2x") TWO,

	@SerializedName("4x") FOUR,

	@SerializedName("8x") EIGHT,

	@SerializedName("15x") FIFTEEN;

	public static WeaponScopeType fromString(String modeName) {
		for (WeaponScopeType scopeType : values()) {
			if (scopeType.name().equalsIgnoreCase(modeName)) {
				return scopeType;
			}
		}
		return null;
	}
}
