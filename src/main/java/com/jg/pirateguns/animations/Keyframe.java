package com.jg.pirateguns.animations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.pirateguns.animations.parts.GunModel;

public class Keyframe {

	public int dur;
	public int startTick;
	public Map<String, float[]> pos;
	public Map<String, float[]> rot;
	
	public Keyframe(int dur) {
		this.dur = dur;
		this.pos = new HashMap<>();
		this.rot = new HashMap<>();
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
	
}
