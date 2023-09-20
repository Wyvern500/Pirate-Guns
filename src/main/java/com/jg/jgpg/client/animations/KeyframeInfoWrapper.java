package com.jg.jgpg.client.animations;

public class KeyframeInfoWrapper {

	Keyframe kf;
	int tick;
	float[] pos;
	float[] rot;
	
	public KeyframeInfoWrapper(Keyframe kf, int tick) {
		this(kf, tick, null, null);
	}
	
	public KeyframeInfoWrapper(Keyframe kf, int tick, float[] pos, float[] rot) {
		this.kf = kf;
		this.tick = tick;
		this.pos = pos;
		this.rot = rot;
	}

	public Keyframe getKeyframe() {
		return kf;
	}
	
	public int getTick() {
		return tick;
	}

	public float[] getPos() {
		return pos;
	}

	public float[] getRot() {
		return rot;
	}

	public void setKf(Keyframe kf) {
		this.kf = kf;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public void setPos(float[] pos) {
		this.pos = pos;
	}

	public void setRot(float[] rot) {
		this.rot = rot;
	}
	
}
