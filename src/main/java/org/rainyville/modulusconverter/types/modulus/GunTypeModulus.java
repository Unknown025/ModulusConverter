package org.rainyville.modulusconverter.types.modulus;

import java.util.ArrayList;
import java.util.HashMap;

public class GunTypeModulus extends BaseType {
    /**
     * Weapon Classification for later use with default animations etc
     */
    public WeaponType weaponType;

    public WeaponScopeType scopeType = null;

    //Munition variables
    /**
     * Damage inflicted per bullet. Multiplied by the bullet damage value.
     */
    public float gunDamage = 0;

    /**
     * Damage inflicted per bullet. Multiplied by the bullet damage value.
     */
    public float gunDamageHeadshotBonus = 2;

    /**
     * Weapon block range
     */
    public int weaponMaxRange = 100;
    /**
     * Weapon effective max block range
     */
    public int weaponEffectiveRange = 50;
    /**
     * The number of bullet entities created by each shot
     */
    public int numBullets = 1;
    /**
     * The amount that bullets spread out when fired from this gun
     */
    public float bulletSpread;
    /**
     * Modifier for bullet spread while sneaking
     */
    public float sneakMod = 0.8f;

    public float aimMod = 0.2f;
    /**
     * The fire rate of the gun in RPM, 1200 = MAX
     */
    public int roundsPerMin = 1;
    /**
     * For when RPM is converted to ticks - Do not use
     */
    public transient int fireTickDelay = 0;
    /**
     * The number of bullets to fire per burst in burst mode
     */
    public int numBurstRounds = 3;
    /**
     * For one shot items like a panzerfaust
     */
    public boolean consumeGunUponUse = false;

    //Recoil Variables
    /**
     * Base value for Upwards cursor/view recoil
     */
    public float recoilPitch = 10.0F;
    /**
     * Base value for Left/Right cursor/view recoil
     */
    public float recoilYaw = 1.0F;
    /**
     * Modifier for setting the maximum pitch divergence when randomizing recoil (Recoil 2 + rndRecoil 0.5 == 1.5-2.5 Recoil range)
     */
    public float randomRecoilPitch = 0.5F;
    /**
     * Modifier for setting the maximum yaw divergence when randomizing recoil (Recoil 2 + rndRecoil 0.5 == 1.5-2.5 Recoil range)
     */
    public float randomRecoilYaw = 0.5F;

    /**
     * Modifier for reducing recoil if crouched
     */
    public float crouchRecoilModifier = 0.8f;

    /**
     * The firing modes of the gun. SEMI, FULL, BURST
     */
    public WeaponFireMode[] fireModes = new WeaponFireMode[]{WeaponFireMode.SEMI};

    /**
     * Attachment Types
     */
    public HashMap<AttachmentEnum, ArrayList<String>> acceptedAttachments = new HashMap<>();

    // Reload Variables
    /**
     * The time (in ticks) it takes to reload this gun
     */
    public int reloadTime = 40;
    /**
     * The time (in ticks) it takes to charge this gun
     */
    public int chargeTime = 40;
    /**
     * The time (in ticks) it takes to offhand reload this gun
     */
    public Integer offhandReloadTime;

    // Shared Ammo/Bullet Variables
    /**
     * If true, numBullets determined by loaded ammo type
     */
    public boolean ammoNumBullets = false;
    /**
     * If true, bulletSpeed determined by loaded ammo type
     */
    public boolean ammoBulletSpeed = false;
    /**
     * If true, spread is determined by loaded ammo type
     */
    public boolean ammoBulletSpread = false;
    /**
     * If true, reload time is determined by loaded ammo type
     */
    public boolean ammoReloadTime = false;
    /**
     * If true, recoil pitch is determined by loaded ammo type
     */
    public boolean ammoRecoilPitch = false;
    /**
     * If true, recoil yaw is determined by loaded ammo type
     */
    public boolean ammoRecoilYaw = false;

    // Ammo Variables
    /**
     * Ammo types which can be used in the gun
     */
    public String[] acceptedAmmo;
    /**
     * If true &amp;&amp; != null, ammo model will be set by ammo type used. Used built-in ammo model by default.
     */
    public boolean dynamicAmmo = false;

    // Bullet Variables
    public Integer internalAmmoStorage;
    public String[] acceptedBullets;
    // Misc Settings
    public boolean allowSprintFiring = false;
    public boolean allowDefaultSounds = true;

    //Sound Variables
    private SoundEntry[] weaponSounds;
    //Increases pitch incrementally over last 5 rounds, 0.05F recommended
    public float emptyPitch = 0.05F;
    public HashMap<WeaponSoundType, ArrayList<SoundEntry>> weaponSoundMap;

    @Override
    public String getAssetDir() {
        return "guns";
    }
}