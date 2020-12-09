package org.rainyville.modulusconverter.types.modulus;

import java.util.Map;

public class BaseType {
    /**
     * The model file for this type.
     */
    public String modelName;
    /**
     * Max stack size
     */
    public Integer maxStackSize;
    /**
     * Model skins/textures.
     */
    public SkinType[] modelSkins;
    public String internalName;
    /**
     * Used to generate .lang files automatically
     */
    public String displayName;
    public String iconName;
    public transient int id;
    public transient String contentPackID;
    public transient String contentPack;
    public Map<String, Object> customVariables;

    /**
     * Method for sub types to use for loading extra values
     */
    public void loadExtraValues() {
    }

    public void loadBaseValues() {
        if (modelSkins == null)
            modelSkins = new SkinType[]{SkinType.getDefaultSkin(this)};

        if (iconName == null)
            iconName = internalName;
    }

    /**
     * Returns internal name if not overridden by sub-type
     */
    @Override
    public String toString() {
        return internalName;
    }

    public String getAssetDir() {
        return "";
    }
}
