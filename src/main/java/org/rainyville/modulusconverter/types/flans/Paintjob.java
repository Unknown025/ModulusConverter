package org.rainyville.modulusconverter.types.flans;

import java.util.HashMap;

public class Paintjob
{
	private static HashMap<Integer, Paintjob> paintjobs = new HashMap<>();
	
	public PaintableType parent;
	public String iconName;
	public String textureName;
	public int ID;

	public Paintjob(PaintableType parent, int id, String iconName, String textureName)
	{
		this.parent = parent;
		this.ID = id;
		this.iconName = iconName;
		this.textureName = textureName;

		paintjobs.put(hashCode(), this);
	}
	
	@Override
	public int hashCode()
	{
		return parent.hashCode() ^ ID;
	}
}
