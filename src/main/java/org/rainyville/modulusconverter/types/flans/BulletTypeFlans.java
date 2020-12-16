package org.rainyville.modulusconverter.types.flans;

import org.rainyville.modulusconverter.types.modulus.PotionEffectEnum;
import org.rainyville.modulusconverter.types.modulus.PotionEntry;

import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class BulletTypeFlans extends ShootableType {
    /**
     * The number of flak particles to spawn upon exploding
     */
    public int flak = 0;
    /**
     * The type of flak particles to spawn
     */
    public String flakParticles = "largesmoke";

    /**
     * If true then this bullet will burn entites it hits
     */
    public boolean setEntitiesOnFire = false;

    /**
     * Exclusively for driveable usage. Replaces old isBomb and isShell booleans with something more flexible
     */
    public EnumWeaponType weaponType = EnumWeaponType.NONE;

    public String hitSound;
    public float hitSoundRange = 50;

    public boolean hasLight = false;
    public float penetratingPower = 1F;
    /**
     * Lock on variables. If true, then the bullet will search for a target at the moment it is fired
     */
    public boolean lockOnToPlanes = false, lockOnToVehicles = false, lockOnToMechas = false, lockOnToPlayers = false, lockOnToLivings = false;
    /**
     * Lock on maximum angle for finding a target
     */
    public float maxLockOnAngle = 45F;
    /**
     * Lock on force that pulls the bullet towards its prey
     */
    public float lockOnForce = 1F;

    public String trailTexture = "defaultBulletTrail";

    public ArrayList<PotionEntry> hitEffects = new ArrayList<>();

    /**
     * The static bullets list
     */
    public static List<BulletTypeFlans> bullets = new ArrayList<>();

    public BulletTypeFlans(TypeFile file) {
        super(file);
        texture = "defaultBullet";
        bullets.add(this);
    }

    @Override
    protected void read(String[] split, TypeFile file) {
        super.read(split, file);
        try {
            if (split[0].equals("FlakParticles"))
                flak = Integer.parseInt(split[1]);
            else if (split[0].equals("FlakParticleType"))
                flakParticles = split[1];
            else if (split[0].equals("SetEntitiesOnFire"))
                setEntitiesOnFire = Boolean.parseBoolean(split[1]);
            else if (split[0].equals("HitSound"))
                hitSound = split[1];
            else if (split[0].equals("HitSoundRange"))
                hitSoundRange = Float.parseFloat(split[1]);
            else if (split[0].equals("Penetrates"))
                penetratingPower = (Boolean.parseBoolean(split[1].toLowerCase()) ? 1F : 0.25F);
            else if (split[0].equals("Penetration") || split[0].equals("PenetratingPower"))
                penetratingPower = Float.parseFloat(split[1]);
            else if (split[0].equals("Bomb"))
                weaponType = EnumWeaponType.BOMB;
            else if (split[0].equals("Shell"))
                weaponType = EnumWeaponType.SHELL;
            else if (split[0].equals("Missile"))
                weaponType = EnumWeaponType.MISSILE;
            else if (split[0].equals("WeaponType"))
                weaponType = EnumWeaponType.valueOf(split[1].toUpperCase());
            else if (split[0].equals("TrailTexture"))
                trailTexture = split[1];
            else if (split[0].equals("HasLight"))
                hasLight = Boolean.parseBoolean(split[1].toLowerCase());
            else if (split[0].equals("LockOnToDriveables"))
                lockOnToPlanes = lockOnToVehicles = lockOnToMechas = Boolean.parseBoolean(split[1].toLowerCase());
            else if (split[0].equals("LockOnToVehicles"))
                lockOnToVehicles = Boolean.parseBoolean(split[1].toLowerCase());
            else if (split[0].equals("LockOnToPlanes"))
                lockOnToPlanes = Boolean.parseBoolean(split[1].toLowerCase());
            else if (split[0].equals("LockOnToMechas"))
                lockOnToMechas = Boolean.parseBoolean(split[1].toLowerCase());
            else if (split[0].equals("LockOnToPlayers"))
                lockOnToPlayers = Boolean.parseBoolean(split[1].toLowerCase());
            else if (split[0].equals("LockOnToLivings"))
                lockOnToLivings = Boolean.parseBoolean(split[1].toLowerCase());
            else if (split[0].equals("MaxLockOnAngle"))
                maxLockOnAngle = Float.parseFloat(split[1]);
            else if (split[0].equals("LockOnForce") || split[0].equals("TurningForce"))
                lockOnForce = Float.parseFloat(split[1]);
            else if (split[0].equals("PotionEffect"))
                hitEffects.add(getPotionEffect(split, file));
        } catch (Exception e) {
            System.err.println("Reading bullet file failed.");
            e.printStackTrace(System.err);
        }
    }

    private static PotionEntry getPotionEffect(String[] split, TypeFile file) {
        int potionID = Integer.parseInt(split[1]);
        int duration = Integer.parseInt(split[2]);
        int amplifier = Integer.parseInt(split[3]);

        System.err.println(ansi().fgBrightYellow().a("Cannot get potion type from ID, you may need to manually change this field. [" + file.name + "]").reset());

        PotionEntry potionEntry = new PotionEntry();
        if (potionID > 0 && potionID < PotionEffectEnum.values().length)
            potionEntry.potionEffect = PotionEffectEnum.values()[potionID];
        potionEntry.duration = duration;
        potionEntry.level = amplifier;

        return potionEntry;
    }
}
