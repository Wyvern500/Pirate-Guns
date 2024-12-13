package com.jg.jgpg.utils;

public class Color {

	public static int rgba(int r, int g, int b, int a){
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
	
}
