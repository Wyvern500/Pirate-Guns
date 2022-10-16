package com.jg.pirateguns.animations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Keyframe {

	public int dur;
	public Map<String, float[]> values;
	
	public Keyframe(int dur) {
		this.dur = dur;
		values = new HashMap<>();
	}
	
	public Keyframe addValue(String id, float[] values) {
		this.values.put(id, values);
		return this;
	}
	
	public void copyValues(Keyframe keyframe) {
		for(Entry<String, float[]> entry : values.entrySet()) {
			this.values.put(entry.getKey(), entry.getValue().clone());
		}
	}
	
	public void setValues(String id, float[] values) {
		if(!this.values.containsKey(id)) {
			this.values.put(id, new float[] { 0.0f, 0.0f, 0.0f });
		}
		this.values.get(id)[0] = values[0];
		this.values.get(id)[1] = values[1];
		this.values.get(id)[2] = values[2];
	}
	
	public float[] getValue(String id){
		if(!this.values.containsKey(id)) {
			this.values.put(id, new float[] { 0.0f, 0.0f, 0.0f });
		}
		return values.get(id);
	}
	
}
