package com.jg.jgpg.client.model;

import com.jg.jgpg.client.animations.Transform;

public class JgModelPart {

	String name;
	Transform transform;
	Transform dtransform;
	float w;
	float h;
	
	public JgModelPart() {
		this("default", new Transform(), new Transform(), 0, 0);
	}
	
	public JgModelPart(String name, float x, float y, float z, float rx, float ry, float rz, float w, float h) {
		this(name, new Transform(), new Transform(x, y, z, rx, ry, rz), w, h);
	}
	
	public JgModelPart(String name, Transform tr, Transform dtr, float w, float h) {
		this.name = name;
		this.transform = tr;
		this.dtransform = dtr;
		this.w = w;
		this.h = h;
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

	public float getWidth() {
		return w;
	}

	public float getHeight() {
		return h;
	}
	
}
