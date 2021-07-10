# Config reference for SeatInfo

For each type, the table will list each config and potentially its purpose and default values.

| Keyword | Type | Default | Purpose |
|---|---|---|---|
| gunName | String | - | - |
| ~~driver~~ | boolean | false | Whether this seat can drive this vehicle.<br/>**Deprecated:** Vehicle driver will be determined by the first seat provided in the array. |
| xOffset | double | 0D | Offset for this seat. |
| yOffset | double | 0D | Offset for this seat. |
| zOffset | double | 0D | Offset for this seat. |
| rotateWithTurret | boolean | false | Whether this seat should rotate with its turret (when applicable). |