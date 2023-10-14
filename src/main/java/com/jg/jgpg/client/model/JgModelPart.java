package com.jg.jgpg.client.model;

import com.jg.jgpg.client.animations.Transform;

public class JgModelPart {

	String name;
	Transform transform;
	Transform dtransform;
	
	public JgModelPart() {
		this("default", new Transform(), new Transform());
	}
	
	public JgModelPart(String name, float x, float y, float z, float rx, float ry, float rz) {
		this(name, new Transform(), new Transform(x, y, z, rx, ry, rz));
	}
	
	public JgModelPart(String name, Transform tr, Transform dtr) {
		this.name = name;
		this.transform = tr;
		this.dtransform = dtr;
	}
	
	@Override
	public String toString() {
		return "Name: " + name + " dtr: " + dtransform.toString() + " tr: " + transform.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public Transform getCombined() {
		return dtransform.combine(transform);
	}

	public Transform getTransform() {
		return transform;
	}

	public Transform getDtransform() {
		return dtransform;
	}
	
}
