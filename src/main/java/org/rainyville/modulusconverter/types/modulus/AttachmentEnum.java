package org.rainyville.modulusconverter.types.modulus;

import com.google.gson.annotations.SerializedName;

public enum AttachmentEnum {
	
	@SerializedName("sight") Sight("sight"),
	@SerializedName("slide") Slide("slide"),
	@SerializedName("charm") Charm("charm"),
	@SerializedName("barrel") Barrel("barrel");
	
	public String typeName;
	
	AttachmentEnum(String typeName)
	{
		this.typeName = typeName;
	}

}
