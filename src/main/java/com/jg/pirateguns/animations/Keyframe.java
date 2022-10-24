package com.jg.pirateguns.animations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.pirateguns.animations.parts.GunModel;

public class Keyframe {

	public int dur;
	public int startTick;
	public final Map<String, float[]> pos;
	public final Map<String, float[]> rot;
	
	public Keyframe(int dur) {
		this.dur = dur;
		this.pos = new HashMap<>();
		this.rot = new HashMap<>();
	}
	
	public Keyframe(int dur, int startTick, Map<String, float[]> pos, Map<String, float[]> rot) {
		this.dur = dur;
		this.startTick = startTick;
		this.pos = pos;
		this.rot = rot;
	}
	
	public Keyframe addPos(String id, float x, float y, float z) {
		this.pos.put(id, new float[] { x, y, z } );
		return this;
	}
	
	public Keyframe addPos(String id, float[] values) {
		this.pos.put(id, values);
		return this;
	}
	
	public Keyframe addRot(String id, float rx, float ry, float rz) {
		this.rot.put(id, new float[] { rx, ry, rz } );
		return this;
	}
	
	public Keyframe addRot(String id, float[] values) {
		this.rot.put(id, values);
		return this;
	}
	
	public void setPos(String id, float[] pos) {
		if(!this.pos.containsKey(id)) {
			this.pos.put(id, new float[] { 0.0f, 0.0f, 0.0f });
		}
		this.pos.get(id)[0] = pos[0];
		this.pos.get(id)[1] = pos[1];
		this.pos.get(id)[2] = pos[2];
	}
	
	public void setRot(String id, float[] rot) {
		if(!this.rot.containsKey(id)) {
			this.rot.put(id, new float[] { 0.0f, 0.0f, 0.0f });
		}
		this.rot.get(id)[0] = rot[0];
		this.rot.get(id)[1] = rot[1];
		this.rot.get(id)[2] = rot[2];
	}
	
	public float[] getPos(String id){
		if(!this.pos.containsKey(id)) {
			this.pos.put(id, new float[] { 0.0f, 0.0f, 0.0f });
		}
		return pos.get(id);
	}
	
	public float[] getRot(String id){
		if(!this.rot.containsKey(id)) {
			this.rot.put(id, new float[] { 0.0f, 0.0f, 0.0f });
		}
		return rot.get(id);
	}
	
	public Keyframe copy() {
		return new Keyframe(this.dur, this.startTick, copyMap(pos), copyMap(rot));
	}
	
	public Map<String, float[]> copyMap(Map<String, float[]> map){
		Map<String, float[]> newmap = new HashMap<>();
		for(Entry<String, float[]> entry : map.entrySet()) {
			float[] original = entry.getValue();
			newmap.put(entry.getKey(), new float[] { original[0], original[1], original[2] });
		}
		return newmap;
	}
	
	@Override
	public String toString() {
		return "Duration: " + dur + " startTick: " + startTick + " pos size: " 
				+ pos.size() + " rot size: " + rot.size();
	}
	
}
