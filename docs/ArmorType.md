# Config reference for ArmorType (extends [BaseType](https://github.com/Unknown025/ModulusConverter/blob/master/docs/BaseType.md))

For each type, the table will list each config and potentially its purpose and default values.

| Keyword | Type | Default | Purpose |
|---|---|---|---|
| durability | Integer | -1 | Durability of this armor. If `-1`, the armor will be indestructible. |
| defense | double | - | Defense this armor provides to the wearer. |
| armorTypes | HashMap<[MArmorType](https://github.com/Unknown025/ModulusConverter/blob/master/docs/MArmorType.md), [ArmorInfo](https://github.com/Unknown025/ModulusConverter/blob/master/docs/ArmorInfo.md )> | - | Individual specifications for different armor pieces. |