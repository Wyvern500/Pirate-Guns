package com.jg.pirateguns.animations.parts;

import com.jg.pirateguns.animations.Transform;

public class GunModelPart {

	private Transform transform;
	private Transform dtransform;
	private String name;
	
	public GunModelPart(String name) {
		this.name = name;
		transform = new Transform();
		dtransform = new Transform();
	}
	
	public GunModelPart(String name, float x, float y, float z, float rx, float ry, float rz) {
		this.name = name;
		dtransform = new Transform(x, y, z, rx, ry, rz);
		transform = new Transform();
	}
	
	public void reset() {
		for(int i = 0; i < transform.pos.length; i++) {
			transform.pos[i] = 0;
		}
		for(int i = 0; i < transform.rot.length; i++) {
			transform.rot[i] = 0;
		}
	}

	public Transform getCombined() {
		return dtransform.combine(transform);
	}
	
	public Transform getTransform() {
		return transform;
	}

	public Transform getDTransform() {
		return dtransform;
	}
	
	public String getName() {
		return name;
	}
	
}
