# ModulusConverter
### Easily convert your Flan's mod content pack to Modulus/Expansive Weaponry/Modular Warfare!
## Example Usage:
`java -jar ModulusConverter.jar --path "%USERPROFILE%\IdeaProjects\FlansMod" --package ww2,nerf`

Converts all available content packs in the given path to be Modulus compatible, and converts the models in the 
given packages under `com.flansmod.client.model`.
## Options:
`--path`* Path of your Flan's mod project.

`--package`* Package names of the models to convert.

`--pack`* Names of the content packs to convert.

`--ignoreCompatibility` Ignores compatibility checks.

`--targetPackage` If you're converting for Modulus or Modular Warfare, use this flag to change the target package.

`--help` Help command.

`*` = required.