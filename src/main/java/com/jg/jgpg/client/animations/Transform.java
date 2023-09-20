package com.jg.jgpg.client.animations;

import java.math.MathContext;
import java.util.Arrays;

import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;

public class Transform {

	public float[] pos;
	public float[] rot;
	
	public Transform() {
		this(new float[] { 0, 0, 0 }, new float[] { 0, 0, 0 });
	}
	
	public Transform(Transform other) {
		this.pos = new float[] { other.pos[0], other.pos[1], other.pos[2] };
		this.rot = new float[] { other.rot[0], other.rot[1], other.rot[2] };
	}
	
	public Transform(float x, float y, float z, float rx, float ry, float rz) {
		this.pos = new float[] { x, y, z };
		this.rot = new float[] { rx, ry, rz };
	}
	
	public Transform(float[] pos, float[] rot) {
		this.pos = pos;
		this.rot = rot;
	}
	
	public Transform combine(Transform other) {
		Transform copy = copy();
		copy.add(other);
		return copy;
	}
	
	public void add(Transform other) {
		this.pos[0] += other.pos[0];
		this.pos[1] += other.pos[1];
		this.pos[2] += other.pos[2];
		this.rot[0] += other.rot[0];
		this.rot[1] += other.rot[1];
		this.rot[2] += other.rot[2];
	}
	
	public void add(float x, float y, float z, boolean isRot) {
		if(isRot) {
			rot[0] += x;
			rot[1] += y;
			rot[2] += z;
		} else {
			pos[0] += x;
			pos[1] += y;
			pos[2] += z;
		}
		LogUtils.log("Transform", toString());
	}
	
	public void lerp(Transform from, Transform target, float progress) {
		this.pos[0] = MathUtils.lerp(from.pos[0], target.pos[0], progress);
		this.pos[1] = MathUtils.lerp(from.pos[1], target.pos[1], progress);
		this.pos[2] = MathUtils.lerp(from.pos[2], target.pos[2], progress);
		this.rot[0] = MathUtils.lerp(from.rot[0], target.rot[0], progress);
		this.rot[1] = MathUtils.lerp(from.rot[1], target.rot[1], progress);
		this.rot[2] = MathUtils.lerp(from.rot[2], target.rot[2], progress);
	}
	
	public Transform copy() {
		return new Transform(this);
	}
	
	@Override
	public String toString() {
		return "Tr: " + Arrays.toString(pos) + " Rt: " + Arrays.toString(rot);
	}
	
}
