# Config reference for VehicleType (extends [BaseType](https://github.com/Unknown025/ModulusConverter/blob/master/docs/BaseType.md))

For each type, the table will list each config and potentially its purpose and default values.

| Keyword | Type | Default | Purpose |
|---|---|---|---|
| rotateWheels | boolean | false | Whether the front wheels should be rotated. |
| turretOrigin | Vector3f | - | Origin of the vehicle's turret (if applicable). |
| primaryWeaponVectors | Vector3f[] | - | - |
| secondaryWeaponVectors | Vector3f[] | - | - |
| seats | [SeatInfo](https://github.com/Unknown025/ModulusConverter/blob/master/docs/SoundType.md), ArrayList\<[SoundEntry](https://github.com/Unknown025/ModulusConverter/blob/master/docs/SeatInfo.md )[] | - | Seats for this vehicle. |
| width | float | 2 | Width of the vehicle's hitbox. |
| height | float | 2 | Height of the vehicle's hitbox. |
| leftTrackVectors | Vector3f | Vector3f\[0] | Left track vectors. |
| rightTrackVectors | Vector3f | Vector3f\[0] | Right track vectors. |
| trackLinkLength | float | 0 | Track link length. |
| trackLinkFix | int | 5 | - |
| flipLinkFix | boolean | false | - |
| animationFrames | int | 2 | Animation frames. |
| maxVelocity | double | 0 | Maximum velocity this vehicle can reach. |
| allowDefaultSounds | boolean | true | Whether default ExW sounds are allowed. |
| emptyPitch | float | 0.05F | - |
| vehicleSoundMap | HashMap<[SoundType](https://github.com/Unknown025/ModulusConverter/blob/master/docs/SoundType.md), ArrayList\<[SoundEntry](https://github.com/Unknown025/ModulusConverter/blob/master/docs/SoundEntry.md )>> | - | Vehicle sound map. |