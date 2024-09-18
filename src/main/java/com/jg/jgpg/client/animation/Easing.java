package com.jg.jgpg.client.animation;

public interface Easing {

	public static final String DEFAULT = "empty";
	
	public float get(float prog);
	
}
