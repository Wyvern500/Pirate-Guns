package com.jg.jgpg.client.animation;

import com.jg.jgpg.utils.Vector3;

public class Keyframe {
	
	Vector3 vec;
	String easing;
	int tick;
	boolean translate;
	boolean isRepetitive;
	boolean isShadow;
	
	public Keyframe(int tick, String easing, Vector3 vec, boolean translate) {
		this(tick, easing, vec, translate, false);
	}
	
	public Keyframe(int tick, String easing, Vector3 vec, boolean translate, boolean isRepetitive) {
		this.tick = tick;
		this.easing = easing;
		this.vec = new Vector3(vec);
		this.translate = translate;
		this.isRepetitive = isRepetitive;
	}

	public Keyframe toZero() {
		vec.set(0, 0, 0);
		return this;
	}
	
	public Keyframe copy() {
		return new Keyframe(tick, easing, vec, translate, isRepetitive);
	}
	
	public int getTick() {
		return tick;
	}

	public Keyframe setTick(int tick) {
		this.tick = tick;
		return this;
	}

	public String getEasing() {
		return easing;
	}

	public Keyframe setEasing(String easing) {
		this.easing = easing;
		return this;
	}

	public Vector3 getVec() {
		return vec;
	}

	public Keyframe setVec(Vector3 vec) {
		this.vec = vec;
		return this;
	}

	public boolean isRepetitive() {
		return isRepetitive;
	}

	public Keyframe setRepetitive(boolean isRepetitive) {
		this.isRepetitive = isRepetitive;
		return this;
	}

	public boolean isShadow() {
		return isShadow;
	}

	public Keyframe setShadow(boolean isShadow) {
		this.isShadow = isShadow;
		return this;
	}

	public boolean isTranslate() {
		return translate;
	}

	public void setTranslate(boolean translate) {
		this.translate = translate;
	}

}
