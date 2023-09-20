package com.jg.jgpg.utils;

import java.util.Map;
import java.util.Map.Entry;

public class Utils {

	public static <T, R> String mapToString(Map<T, R> map) {
		String all = "";
		for(Entry<T, R> e : map.entrySet()) {
			all += e.getKey().toString();
			all += e.getValue().toString() + "\n";
		}
 		return all;
	}
	
	public static boolean collides(int x, int y, int w, int h, int x2, int y2) {
		return x2 > x && x2 < x + w && y2 > y && y2 < y + h;
	}
	
	public static boolean collides(double x, double y, double w, double h, double x2, double y2) {
		return x2 > x && x2 < x + w && y2 > y && y2 < y + h;
	}
	
}
