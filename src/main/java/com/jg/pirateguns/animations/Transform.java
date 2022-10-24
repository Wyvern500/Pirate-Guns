package com.jg.pirateguns.animations;

import java.util.Arrays;

import com.jg.pirateguns.utils.PGMath;

import net.minecraft.util.Mth;

public class Transform {
	
	public static final Transform EMPTY = new Transform();
	
	public float[] pos;
	public float[] rot;
	
	public Transform() {
		pos = new float[] { 0.0f, 0.0f, 0.0f };
		rot = new float[] { 0.0f, 0.0f, 0.0f };
	}
	
	public Transform(float x, float y, float z, float rx, float ry, float rz) {
		pos = new float[] { x, y, z };
		rot = new float[] { rx, ry, rz };
	}
	
	public Transform(Transform transform) {
		pos = new float[] { transform.pos[0], transform.pos[1], transform.pos[2] };
		rot = new float[] { transform.rot[0], transform.rot[1], transform.rot[2] };
	}
	
	public void setPos(float[] pos) {
		this.pos[0] = pos[0];
		this.pos[1] = pos[1];
		this.pos[2] = pos[2];
	}
	
	public void setRot(float[] rot) {
		this.rot[0] = rot[0];
		this.rot[1] = rot[1];
		this.rot[2] = rot[2];
	}
	
	public void setPos(float x, float y, float z) {
		pos[0] = x;
		pos[1] = y;
		pos[2] = z;
	}
	
	public void setRot(float rx, float ry, float rz) {
		rot[0] = rx;
		rot[1] = ry;
		rot[2] = rz;
	}
	
	public void addPos(float[] pos) {
		this.pos[0] = this.pos[0] + pos[0];
		this.pos[1] = this.pos[1] + pos[1];
		this.pos[2] = this.pos[2] + pos[2];
	}
	
	public void addRot(float[] rot) {
		this.rot[0] = this.rot[0] + rot[0];
		this.rot[1] = this.rot[1] + rot[1];
		this.rot[2] = this.rot[2] + rot[2];
	}
	
	public void addPos(float x, float y, float z) {
		pos[0] = pos[0] + x;
		pos[1] = pos[1] + y;
		pos[2] = pos[2] + z;
	}
	
	public void addRot(float rx, float ry, float rz) {
		rot[0] = rot[0] + rx;
		rot[1] = rot[1] + ry;
		rot[2] = rot[2] + rz;
	}
	
	public Transform lerp(float p, Transform other) {
		Transform t = new Transform();
		t.pos[0] = Mth.lerp(p, this.pos[0], other.pos[0]);
		t.pos[1] = Mth.lerp(p, this.pos[1], other.pos[1]);
		t.pos[2] = Mth.lerp(p, this.pos[2], other.pos[2]);
		t.rot[0] = PGMath.rotLerp(p, this.rot[0], other.rot[0]);
		t.rot[1] = PGMath.rotLerp(p, this.rot[1], other.rot[1]);
		t.rot[2] = PGMath.rotLerp(p, this.rot[2], other.rot[2]);
		return t;
	}
	
	public Transform combine(Transform transform) {
		Transform t = new Transform();
		t.addPos(transform.pos);
		t.addRot(transform.rot);
		t.addPos(pos);
		t.addRot(rot);
		return t;
	}
	
	@Override
	public String toString() {
		return "Pos: " + Arrays.toString(pos) + " Rot: " + Arrays.toString(rot);
	}
	
}
