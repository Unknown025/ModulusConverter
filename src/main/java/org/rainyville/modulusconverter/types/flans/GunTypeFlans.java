package org.rainyville.modulusconverter.types.flans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GunTypeFlans extends PaintableType implements IScope {
    //Gun Behaviour Variables
    /**
     * The list of bullet types that can be used in this gun
     */
    public List<ShootableType> ammo = new ArrayList<>(), nonExplosiveAmmo = new ArrayList<>();
    /**
     * Whether the player can press the reload key (default R) to reload this gun
     */
    public boolean canForceReload = true;
    /**
     * The time (in ticks) it takes to reload this gun
     */
    public int reloadTime;
    /**
     * The amount to recoil the player's view by when firing a single shot from this gun
     */
    public int recoil;
    /**
     * The amount that bullets spread out when fired from this gun
     */
    public float bulletSpread;
    /**
     * Damage inflicted by this gun. Multiplied by the bullet damage.
     */
    public float damage = 0;
    /**
     * The damage inflicted upon punching someone with this gun
     */
    public float meleeDamage = 1;
    /**
     * The speed of bullets upon leaving this gun. 0.0f means instant.
     */
    public float bulletSpeed = 0.0f;
    /**
     * The number of bullet entities created by each shot
     */
    public int numBullets = 1;
    /**
     * The delay between shots in ticks (1/20ths of seconds)
     */
    public float shootDelay = 1.0f;
    /**
     * Number of ammo items that the gun may hold. Most guns will hold one magazine.
     * Some may hold more, such as Nerf pistols, revolvers or shotguns
     */
    public int numAmmoItemsInGun = 1;
    /**
     * The firing mode of the gun. One of semi-auto, full-auto, minigun or burst
     */
    public EnumFireMode mode = EnumFireMode.FULLAUTO;
    /**
     * The number of bullets to fire per burst in burst mode
     */
    public int numBurstRounds = 3;
    /**
     * The required speed for minigun mode guns to start firing
     */
    public float minigunStartSpeed = 15F;
    /**
     * The maximum speed a minigun mode gun can reach
     */
    public float minigunMaxSpeed = 30F;
    /**
     * Whether this gun can be used underwater
     */
    public boolean canShootUnderwater = true;
    /**
     * The amount of knockback to impact upon the player per shot
     */
    public float knockback = 0F;
    /**
     * If true, then this gun can be dual wielded
     */
    public boolean oneHanded = false;
    /**
     * For one shot items like a panzerfaust
     */
    public boolean consumeGunUponUse = false;
    /**
     * Item to drop on shooting
     */
    public String dropItemOnShoot = null;
    //Custom Melee Stuff
    /**
     * The time delay between custom melee attacks
     */
    public int meleeTime = 1;
    /**
     * Set these to make guns only usable by a certain type of entity
     */
    public boolean usableByPlayers = true, usableByMechas = true;

    //Information
    //Show any variables into the GUI when hovering over items.
    /**
     * If false, then attachments wil not be listed in item GUI
     */
    public boolean showAttachments = true;
    /**
     * Show statistics
     */
    public boolean showDamage = false, showRecoil = false, showSpread = false;
    /**
     * Show reload time in seconds
     */
    public boolean showReloadTime = false;

    //Shields
    //A shield is actually a gun without any shoot functionality (similar to knives or binoculars)
    //and a load of shield code on top. This means that guns can have in built shields (think Nerf Stampede)
    /**
     * Whether or not this gun has a shield piece
     */
    public boolean shield = false;
    /**
     * Float between 0 and 1 denoting the proportion of damage blocked by the shield
     */
    public float shieldDamageAbsorption = 0F;

    //Sounds
    /**
     * The sound played upon shooting
     */
    public String shootSound;
    /**
     * The length of the sound for looping sounds
     */
    public int shootSoundLength;
    /**
     * Whether to distort the sound or not. Generally only set to false for looping sounds
     */
    public boolean distortSound = true;
    /**
     * The sound to play upon reloading
     */
    public String reloadSound;

    //Looping sounds
    /**
     * Whether the looping sounds should be used. Automatically set if the player sets any one of the following sounds
     */
    public boolean useLoopingSounds = false;
    /**
     * Played when the player starts to hold shoot
     */
    public String warmupSound;
    public int warmupSoundLength = 20;
    /**
     * Played in a loop until player stops holding shoot
     */
    public String loopedSound;
    public int loopedSoundLength = 20;
    /**
     * Played when the player stops holding shoot
     */
    public String cooldownSound;


    /**
     * The sound to play upon weapon swing
     */
    public String meleeSound;
    /**
     * The sound to play while holding the weapon in the hand
     */
    public String idleSound;
    public int idleSoundLength;


    //Deployable Settings
    /**
     * If true, then the bullet does not shoot when right clicked, but must instead be placed on the ground
     */
    public boolean deployable = false;
    /**
     * The deployable model's texture
     */
    public String deployableTexture;
    /**
     * Various deployable settings controlling the player view limits and standing position
     */
    public float standBackDist = 1.5F, topViewLimit = -60F, bottomViewLimit = 30F, sideViewLimit = 45F, pivotHeight = 0.375F;

    //Default Scope Settings. Overriden by scope attachments
    //In many cases, this will simply be iron sights
    /**
     * Default scope overlay texture
     */
    public String defaultScopeTexture;
    /**
     * Whether the default scope has an overlay
     */
    public boolean hasScopeOverlay = false;
    /**
     * The zoom level of the default scope
     */
    public float zoomLevel = 1.0F;
    /**
     * The FOV zoom level of the default scope
     */
    public float FOVFactor = 1.5F;

    //Attachment settings
    /**
     * If this is true, then all attachments are allowed. Otherwise the list is checked
     */
    public boolean allowAllAttachments = false;
    /**
     * Whether each attachment slot is available
     */
    public boolean allowBarrelAttachments = false, allowScopeAttachments = false,
            allowStockAttachments = false, allowGripAttachments = false;
    /**
     * The number of generic attachment slots there are on this gun
     */
    public int numGenericAttachmentSlots = 0;

    /**
     * The static hashmap of all guns by shortName
     */
    public static HashMap<Integer, GunTypeFlans> guns = new HashMap<>();
    /**
     * The static list of all guns
     */
    public static ArrayList<GunTypeFlans> gunList = new ArrayList<>();

    //Modifiers
    /**
     * Speeds up or slows down player movement when this item is held
     */
    public float moveSpeedModifier = 1F;
    /**
     * Gives knockback resistance to the player
     */
    public float knockbackModifier = 0F;


    public GunTypeFlans(TypeFile file) {
        super(file);
    }

    @Override
    public void postRead(TypeFile file) {
        super.postRead(file);
        gunList.add(this);
        guns.put(shortName.hashCode(), this);
    }

    @Override
    protected void read(String[] split, TypeFile file) {
        super.read(split, file);
        try {
            damage = Read(split, "Damage", damage);
            canForceReload = Read(split, "CanForceReload", canForceReload);
            reloadTime = Read(split, "ReloadTime", reloadTime);
            recoil = Read(split, "Recoil", recoil);
            knockback = Read(split, "Knockback", knockback);
            bulletSpread = Read(split, "Accuracy", bulletSpread);
            bulletSpread = Read(split, "Spread", bulletSpread);
            numBullets = Read(split, "NumBullets", numBullets);
            consumeGunUponUse = Read(split, "ConsumeGunOnUse", consumeGunUponUse);
            dropItemOnShoot = Read(split, "DropItemOnShoot", dropItemOnShoot);
            numBurstRounds = Read(split, "NumBurstRounds", numBurstRounds);
            minigunStartSpeed = Read(split, "MinigunStartSpeed", minigunStartSpeed);
            if (split[0].equals("MeleeDamage")) {
                meleeDamage = Float.parseFloat(split[1]);
            }

            //Information
            showAttachments = Read(split, "ShowAttachments", showAttachments);
            showDamage = Read(split, "ShowDamage", showDamage);
            showRecoil = Read(split, "ShowRecoil", showRecoil);
            showSpread = Read(split, "ShowAccuracy", showSpread);
            showReloadTime = Read(split, "ShowReloadTime", showReloadTime);

            //Sounds
            shootDelay = Read(split, "ShootDelay", shootDelay);
            shootSoundLength = Read(split, "SoundLength", shootSoundLength);
            distortSound = Read(split, "DistortSound", distortSound);
            idleSoundLength = Read(split, "IdleSoundLength", idleSoundLength);
            warmupSoundLength = Read(split, "WarmupSoundLength", warmupSoundLength);
            loopedSoundLength = Read(split, "LoopedSoundLength", loopedSoundLength);
            loopedSoundLength = Read(split, "SpinSoundLength", loopedSoundLength);
            if (split[0].equals("ShootSound")) {
                shootSound = split[1];
            } else if (split[0].equals("ReloadSound")) {
                reloadSound = split[1];
            } else if (split[0].equals("IdleSound")) {
                idleSound = split[1];
            } else if (split[0].equals("MeleeSound")) {
                meleeSound = split[1];
            }

            //Looping sounds
            else if (split[0].equals("WarmupSound")) {
                warmupSound = split[1];
            } else if (split[0].equals("LoopedSound") || split[0].equals("SpinSound")) {
                loopedSound = split[1];
                useLoopingSounds = true;
            } else if (split[0].equals("CooldownSound")) {
                cooldownSound = split[1];
            }

            //Modes and zoom settings
            if (split[0].equals("Scope")) {
                hasScopeOverlay = true;
                if (split[1].equals("None"))
                    hasScopeOverlay = false;
                else defaultScopeTexture = split[1];
            } else if (split[0].equals("ZoomLevel")) {
                zoomLevel = Float.parseFloat(split[1]);
            } else if (split[0].equals("FOVZoomLevel")) {
                FOVFactor = Float.parseFloat(split[1]);
            } else if (split[0].equals("Deployable"))
                deployable = split[1].equals("True");

            deployableTexture = Read(split, "DeployedTexture", deployableTexture);
            standBackDist = Read(split, "StandBackDistance", standBackDist);
            topViewLimit = Read(split, "TopViewLimit", topViewLimit);
            bottomViewLimit = Read(split, "BottomViewLimit", bottomViewLimit);
            sideViewLimit = Read(split, "SideViewLimit", sideViewLimit);
            pivotHeight = Read(split, "PivotHeight", pivotHeight);
            numAmmoItemsInGun = Read(split, "NumAmmoSlots", numAmmoItemsInGun);
            numAmmoItemsInGun = Read(split, "NumAmmoItemsInGun", numAmmoItemsInGun);
            numAmmoItemsInGun = Read(split, "LoadIntoGun", numAmmoItemsInGun);
            canShootUnderwater = Read(split, "CanShootUnderwater", canShootUnderwater);
            oneHanded = Read(split, "OneHanded", oneHanded);
            usableByPlayers = Read(split, "UsableByPlayers", usableByPlayers);
            usableByMechas = Read(split, "UsableByMechas", usableByMechas);

            if (split[0].equals("Ammo")) {
                ShootableType type = ShootableType.getShootableType(split[1]);
                if (type != null) {
                    ammo.add(type);
                    if (type.explosionRadius <= 0F)
                        nonExplosiveAmmo.add(type);
                } else System.err.println("Could not find " + split[1] + " when reading ammo types for " + shortName);
            } else if (split[0].equals("BulletSpeed")) {
                if (split[1].equalsIgnoreCase("instant")) {
                    bulletSpeed = 0.0f;
                } else bulletSpeed = Float.parseFloat(split[1]);

                if (bulletSpeed > 3.0f) {
                    bulletSpeed = 0.0f;
                }
            }

            meleeTime = Read(split, "MeleeTime", meleeTime);

            //Player modifiers
            moveSpeedModifier = Read(split, "MoveSpeedModifier", moveSpeedModifier);
            moveSpeedModifier = Read(split, "Slowness", moveSpeedModifier);
            knockbackModifier = Read(split, "KnockbackReduction", knockbackModifier);
            knockbackModifier = Read(split, "KnockbackModifier", knockbackModifier);

            //Attachment settings
            allowAllAttachments = Read(split, "AllowAllAttachments", allowAllAttachments);

            allowBarrelAttachments = Read(split, "AllowBarrelAttachments", allowBarrelAttachments);
            allowScopeAttachments = Read(split, "AllowScopeAttachments", allowScopeAttachments);
            allowStockAttachments = Read(split, "AllowStockAttachments", allowStockAttachments);
            allowGripAttachments = Read(split, "AllowGripAttachments", allowGripAttachments);
            numGenericAttachmentSlots = Read(split, "NumGenericAttachmentSlots", numGenericAttachmentSlots);
        } catch (Exception e) {
            System.err.println("Reading gun file failed.");
            e.printStackTrace();
        }
    }

    @Override
    public float getZoomFactor() {
        return zoomLevel;
    }

    @Override
    public boolean hasZoomOverlay() {
        return hasScopeOverlay;
    }

    @Override
    public String getZoomOverlay() {
        return defaultScopeTexture;
    }

    @Override
    public float getFOVFactor() {
        return FOVFactor;
    }

    @Override
    protected void preRead(TypeFile file) {
    }
}
