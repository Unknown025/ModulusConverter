# ModulusConverter
### Easily convert your Flan's mod content pack to Modulus/Expansive Weaponry/Modular Warfare!

### NOTE: If you do not have the source files for the content pack you want to use with Expansive Weaponry, use the provided Flan's/Modulus compatibility mode in Expansive Weaponry's `mod_config.json`.

## Example Usage:
`java -jar ModulusConverter.jar --path "%USERPROFILE%\IdeaProjects\FlansMod" --package ww2,nerf`

Converts all available content packs in the given path to be Modulus compatible, and converts the models in the 
given packages under `com.flansmod.client.model`.
## Options:
`--path`* Path of your Flan's mod project.

`--package`* Package names of the models to convert.

`--pack`* Names of the content packs to convert.

`--ignoreCompatibility` Ignores compatibility checks.

`--targetPackage` Use this flag to change the target package.

`--help` Help command.

`*` = required.