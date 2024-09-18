package com.jg.jgpg.client.animation;

import java.util.List;

public class CycleData {

	List<Keyframe> keyframes;
	int duration;
	boolean translate;
	int shadows;
	
	public CycleData(List<Keyframe> keyframes, int duration, boolean isTranslate) {
		this.keyframes = keyframes;
		this.duration = duration;
		this.translate = isTranslate;
	}

	public List<Keyframe> getKeyframes() {
		return keyframes;
	}

	public void setKeyframes(List<Keyframe> keyframes) {
		this.keyframes = keyframes;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isTranslate() {
		return translate;
	}

	public void setTranslate(boolean translate) {
		this.translate = translate;
	}

	public int getShadows() {
		return shadows;
	}

	public void setShadows(int shadows) {
		this.shadows = shadows;
	}

	public static class CycleShortData {

		public int startTick;
		public int endTick;
		public int duration;

		public CycleShortData(int startTick, int endTick, int duration) {
			this.startTick = startTick;
			this.endTick = endTick;
			this.duration = duration;
		}

	}

}
