package com.jg.jgpg.client.animations;

import com.jg.jgpg.client.model.JgModelPart;

public class PreviewTransformData {

	Animation anim;
	JgModelPart part;
	int startTick;
	int tick;
	int offset;
	boolean isRot;
	float[] val;
	String easing;
	
	public PreviewTransformData(Animation animation, JgModelPart part, int tick, int offset, boolean isRot, 
			float[] val, String easing) {
		this.anim = animation;
		this.part = part;
		this.startTick = tick;
		this.tick = tick;
		this.offset = offset;
		this.isRot = isRot;
		this.val = val;
		this.easing = easing;
	}

	public int getStartTick() {
		return startTick;
	}
	
	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public void addOffset(int offset) {
		this.tick = Math.max(0, offset - this.offset);
		/*LogUtils.log("PreviewTransformData", "tick: " + tick + " offset: " + offset + " kfoffset: " + 
				this.offset);*/
	}

	public Animation getAnim() {
		return anim;
	}
	
	public JgModelPart getPart() {
		return part;
	}

	public boolean isRot() {
		return isRot;
	}

	public float[] getVal() {
		return val;
	}
	
	public String getEasing() {
		return easing;
	}
	
}
