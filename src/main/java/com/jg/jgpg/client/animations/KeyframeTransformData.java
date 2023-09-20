package com.jg.jgpg.client.animations;

public class KeyframeTransformData {

	public static KeyframeTransformData DEFAULT = new KeyframeTransformData(new float[] { 0, 0, 0 }, "empty");
	
	private float[] val;
	private String easing;
	
	/*public KeyframeTransformData(float[] val) {
		this(val, "empty");
	}*/
	
	public KeyframeTransformData(float[] val, String easing) {
		this.val = val;
		this.easing = easing;
	}

	public float[] getVal() {
		return val;
	}

	public void setVal(float[] val) {
		this.val = val;
	}

	public String getEasing() {
		return easing;
	}

	public void setEasing(String easing) {
		this.easing = easing;
	}
	
}
