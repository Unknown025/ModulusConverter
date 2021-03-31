# Config reference for AmmoType (extends [BaseType](https://github.com/Unknown025/ModulusConverter/blob/master/docs/BaseType.md))

For each type, the table will list each config and potentially its purpose and default values.

| Keyword | Type | Default | Purpose |
|---|---|---|---|
| numBullets | int | 1 | Number of bullets fired per shot. |
| ammoCapacity | int | 30 | Ammo capacity amount. |
| magazineCount | Integer | - | Magazine count. |
| reloadTime | float | - | Number of seconds it should take to reload. |
| recoilPitch | float | 0.0F | Base value for upwards cursor/view recoil. |
| recoilYaw | float | 0.0F | Base value for left/right cursor/view recoil. |
| bulletDamage | float | 1 | Damage inflicted per bullet, multiplied by the gun damage value. |
| bulletSpread | float | - | The amount that bullets spread out when fired. |
| emptyOnCraft | boolean | false | Will this ammo item be loaded or empty when crafted? |
| allowEmptyMagazines | boolean | true | Override ammo deletion, to allow for enabling or disabling of returned empty magazines. |
| subAmmo | String[] | - | The ammo type(s) that can be loaded into this item. |