package org.rainyville.modulusconverter.types.modulus;

import java.util.HashMap;

public class BulletTypeModulus extends BaseType {
	// Bullet Type
	public HashMap<String, BulletProperty> bulletProperties = new HashMap<>();
	
	// AmmoType Overrides
	/** Base value for Upwards cursor/view recoil */
	public float recoilPitch = 0.0F;
	/** Base value for Left/Right cursor/view recoil */
	public float recoilYaw = 0.0F;
	/** The amount that bullets spread out when fired from this gun */
	public float bulletSpread;

	@Override
	public String getAssetDir()
	{
		return "bullets";
	}
}
