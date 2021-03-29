# Config reference for CraftOption

For each type, the table will list each config and potentially its purpose and default values.

| Keyword | Type | Default | Purpose |
|---|---|---|---|
| displayName | String | itemStack.getDisplayName() | Name displayed in the crafting GUI. |
| craftingResult | [JsonItemStack](https://github.com/Unknown025/ModulusConverter/blob/master/docs/JsonItemStack.md) | - | Crafting result (item stack given to the player). |
| requiredItems | [JsonItemStack](https://github.com/Unknown025/ModulusConverter/blob/master/docs/JsonItemStack.md )[] | - | List of required items.* |

\*This has multiple states. If left undefined, the recipe will show up in the crafting UI, 
but will be uncraftable. An empty list will make the item essentially free.
Defining a list of items will require the player to have the corresponding items.