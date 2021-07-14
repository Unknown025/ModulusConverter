## NOTE: If you _do not_ have the source files for the content pack you want to use with Expansive Weaponry, use the provided Flan's/Modulus compatibility mode in Expansive Weaponry's `mod_config.json`.

# ModulusConverter
### Easily convert your Flan's mod content pack to Modulus/Expansive Weaponry/Modular Warfare!

## Example Usage:
`java -jar ModulusConverter.jar --path "%USERPROFILE%\IdeaProjects\FlansMod" --package ww2,nerf`

Converts all available content packs in the given path to be Modulus compatible, and converts the models in the 
given packages under `com.flansmod.client.model`.
## Options:
`--path`* Path of your Flan's mod project (or where you want one to be created).

`--package`* Package names of the models to convert. (NOTE: Only one package name will be
used if copying your files to a fresh Flan's mod project.)

`--pack`* Names of the content packs to convert.

`--ignoreCompatibility` Ignores compatibility checks.

`--targetPackage` Use this flag to change the target package.

`--models` Location of the directory where `.java` models are located.
**Specifying this option will download and create a _new_ Flan's Mod project.**

`--help` Help command.

`*` = required.