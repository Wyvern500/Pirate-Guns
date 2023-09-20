package com.jg.jgpg.utils;

public class MathUtils {

	public static float lerp(float a, float b, float p) {
		return a + p * (b - a);
	}
	
	public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
	
	public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
	
	public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
	
	public static float rotLerp(float a, float b, float p){
	    double cs = ((1 - p) * Math.cos(a) + p * Math.cos(b));
	    double sn = (1 - p) * Math.sin(a) + p * Math.sin(b);
	    return (float) Math.atan2(sn, cs);
	}
	
}
