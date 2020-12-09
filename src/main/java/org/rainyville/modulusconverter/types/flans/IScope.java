package org.rainyville.modulusconverter.types.flans;

public interface IScope
{
	float getFOVFactor();
	
	float getZoomFactor();
	
	boolean hasZoomOverlay();
	
	String getZoomOverlay();
}
