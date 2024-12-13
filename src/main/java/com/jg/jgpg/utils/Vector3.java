package com.jg.jgpg.utils;

public class Vector3 {

	public float x;
	public float y;
	public float z;
	
	public Vector3(float[] vec) {
		this(vec[0], vec[1], vec[2]);
	}
	
	public Vector3(Vector3 vec) {
		this(vec.x, vec.y, vec.z);
	}
	
	public Vector3 copy() {
		return new Vector3(x, y, z);
	}
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}	
	
}
