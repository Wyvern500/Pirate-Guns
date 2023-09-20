package com.jg.jgpg.client.animations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.jgpg.client.model.JgModelPart;

public class Keyframe implements Comparable<Keyframe> {

	Map<JgModelPart, KeyframeTransformData> traslations;
	Map<JgModelPart, KeyframeTransformData> rotations;
	int tick;
	
	public Keyframe(int tick) {
		traslations = new HashMap<JgModelPart, KeyframeTransformData>();
		rotations = new HashMap<JgModelPart, KeyframeTransformData>();
		this.tick = tick;
	}
	
	public void traslate(JgModelPart part, float x, float y, float z) {
		this.traslate(part, x, y, z, Easing.DEFAULT);
	}
	
	public void traslate(JgModelPart part, float x, float y, float z, String easing) {
		traslations.put(part, new KeyframeTransformData(new float[] { x, y, z }, easing));
	}
	
	public void rotate(JgModelPart part, float x, float y, float z) {
		this.rotate(part, x, y, z, Easing.DEFAULT);
	}
	
	public void rotate(JgModelPart part, float x, float y, float z, String easing) {
		rotations.put(part, new KeyframeTransformData(new float[] { x, y, z }, easing));
	}
	
	public Keyframe copy() {
		Keyframe kf = new Keyframe(tick);
		for(Entry<JgModelPart, KeyframeTransformData> e : traslations.entrySet()) {
			kf.traslations.put(e.getKey(), new KeyframeTransformData(new float[] { e.getValue().getVal()[0], 
					e.getValue().getVal()[1] }, e.getValue().getEasing()));
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : rotations.entrySet()) {
			kf.rotations.put(e.getKey(), new KeyframeTransformData(new float[] { e.getValue().getVal()[0], 
					e.getValue().getVal()[1] }, e.getValue().getEasing()));
		}
		return kf;
	}
	
	public Keyframe copyTransformsFrom(Keyframe kf) {
		for(Entry<JgModelPart, KeyframeTransformData> e : kf.traslations.entrySet()) {
			traslations.put(e.getKey(), new KeyframeTransformData(new float[] { e.getValue().getVal()[0], 
					e.getValue().getVal()[1], e.getValue().getVal()[2] }, e.getValue().getEasing()));
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : kf.rotations.entrySet()) {
			rotations.put(e.getKey(), new KeyframeTransformData(new float[] { e.getValue().getVal()[0], 
					e.getValue().getVal()[1], e.getValue().getVal()[2] }, e.getValue().getEasing()));
		}
		return this;
 	}
	
	public Keyframe toZero() {
		for(KeyframeTransformData data : traslations.values()) {
			data.getVal()[0] = 0;
			data.getVal()[1] = 0;
			data.getVal()[2] = 0;
		}
		for(KeyframeTransformData data : rotations.values()) {
			data.getVal()[0] = 0;
			data.getVal()[1] = 0;
			data.getVal()[2] = 0;
		}
		return this;
	}
	
	public Map<JgModelPart, KeyframeTransformData> getTraslations() {
		return traslations;
	}

	public Map<JgModelPart, KeyframeTransformData> getRotations() {
		return rotations;
	}
	
	public void setTick(int tick) {
		this.tick = tick;
	}
	
	public int getTick() {
		return tick;
	}

	@Override
	public String toString() {
		String all = "Tick: " + tick + "\n";
		for(Entry<JgModelPart, KeyframeTransformData> e : traslations.entrySet()) {
			all += "Part: " + e.getKey().getName() + " tr: " + Arrays.toString(e.getValue().getVal())
				+ " easing: " + e.getValue().getEasing() + "\n";
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : rotations.entrySet()) {
			all += "Part: " + e.getKey().getName() + " rt: " + Arrays.toString(e.getValue().getVal())
				+ " easing: " + e.getValue().getEasing() + "\n";
		}
		return all;
	}
	
	@Override
	public int compareTo(Keyframe other) {
		return Integer.compare(tick, other.getTick());
	}
	
}
