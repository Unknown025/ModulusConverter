# Config reference for ArmorInfo

For each type, the table will list each config and potentially its purpose and default values.

| Keyword | Type | Default | Purpose |
|---|---|---|---|
| displayName | String | - | Display name of this item. |
| internalName | String | - | Internal name of this item. |
| backpack | [BackpackInfo](BackpackInfo.md) | - | If provided, defines the characteristics of how many slots are contained, along with the location of the backpack's container texture.<br>**Note:** This can only be defined on [backpacks](MArmorType.md)! |