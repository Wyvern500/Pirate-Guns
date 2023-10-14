package com.jg.jgpg.client.animations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jg.jgpg.client.model.JgModelPart;

public class Animation {

	String name;
	int duration;
	
	List<Keyframe> keyframes;
	int keyframeIndex;
	
	public Animation(String name) {
		this.name = name;
		keyframes = new ArrayList<Keyframe>();
		keyframeIndex = -1;
	}
	
	public Animation addKeyframe(int tick) {
		keyframeIndex++;
		Keyframe kfToDelete = null;
		for(Keyframe kf : keyframes) {
			if(kf.getTick() == tick) {
				kfToDelete = kf;
				break;
			}
		}
		if(kfToDelete != null ) {
			keyframes.remove(kfToDelete);
		}
		Keyframe kf = new Keyframe(tick);
		keyframes.add(kf);
		sort();
		return this;
	}
	
	public Animation traslate(JgModelPart part, float x, float y, float z) {
		return traslate(part, x, y, z, Easing.DEFAULT);
	}
	
	public Animation traslate(JgModelPart part, float x, float y, float z, String easing) {
		keyframes.get(keyframeIndex).traslate(part, x, y, z, easing);
		return this;
	}
	
	public Animation rotate(JgModelPart part, float x, float y, float z) {
		return rotate(part, x, y, z, Easing.DEFAULT);
	}
	
	public Animation rotate(JgModelPart part, float x, float y, float z, String easing) {
		keyframes.get(keyframeIndex).rotate(part, x, y, z, easing);
		return this;
	}
	
	public Animation end() {
		duration = keyframes.get(keyframes.size() - 1).tick;
		return this;
	}
	
	public Animation addKeyframe(Keyframe kf) {
		if(keyframes.contains(kf)) {
			keyframes.remove(kf);
		}
		keyframes.add(kf);
		sort();
		return this;
	}
	
	public void removeKeyframe(Keyframe kf) {
		keyframes.remove(kf);
		sort();
	}
	
	public void sort() {
		Collections.sort(keyframes, (i1, i2) -> Integer.compare(i1.getTick(), i2.getTick()));
	}

	public String getName() {
		return name;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public List<Keyframe> getKeyframes() {
		return keyframes;
	}
	
	private class KeyframeDataWrapper {
		
		public JgModelPart part;
		public boolean isRot;
		
		public KeyframeDataWrapper(JgModelPart part, boolean isRot) {
			this.part = part;
			this.isRot = isRot;
		}
		
	}
	
}
