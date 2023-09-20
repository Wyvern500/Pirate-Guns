package com.jg.jgpg.client.animations;

import com.jg.jgpg.client.model.JgModelPart;

public class KeyframeData {

	KeyframeInfoWrapper wrapper;
	JgModelPart part;
	float[] values;
	boolean rot;
	
	public KeyframeData(KeyframeInfoWrapper wrapper, JgModelPart part, float[] values, 
			boolean rot) {
		this.wrapper = wrapper;
		this.part = part;
		this.values = values;
		this.rot = rot;
	}

	public KeyframeInfoWrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(KeyframeInfoWrapper wrapper) {
		this.wrapper = wrapper;
	}

	public JgModelPart getPart() {
		return part;
	}

	public void setPart(JgModelPart part) {
		this.part = part;
	}
	
	public Keyframe getKeyframe() {
		return wrapper.kf;
	}

	public float[] getValues() {
		return values;
	}

	public void setValues(float[] values) {
		this.values = values;
	}

	public boolean isRotValues() {
		return rot;
	}

	public void setRot(boolean rot) {
		this.rot = rot;
	}
	
}
