package com.jg.pirateguns.animations.serializers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jg.pirateguns.animations.Keyframe;

public class KeyframeSerializer {
	
	public static String serialize(Keyframe keyframe) {
		String ser = "";
		ser += "keyframe>start\n";
		ser += "keyframe>dur=" + keyframe.dur + "\n";
		ser += "keyframe>startTick=" + keyframe.startTick + "\n";
		for(Entry<String, float[]> entry : keyframe.pos.entrySet()) {
			ser += "keyframe>pos>" + entry.getKey() + "=" + vectorToString(entry.getValue()) + "\n";
		}
		for(Entry<String, float[]> entry : keyframe.rot.entrySet()) {
			ser += "keyframe>rot>" + entry.getKey() + "=" + vectorToString(entry.getValue()) + "\n";
		}
		ser += "keyframe>end\n";
		return ser;
	}
	
	public static Keyframe deserialize(String json) {
		Keyframe kf = new Keyframe(0);
		String[] sData = json.split("\n");
		
		for(int i = 0; i < sData.length; i++) {
			String s = sData[i];
			if(s.contains("=")) {
				// Contains parameters
				String[] conv = s.split("=");
				String[] commands = conv[0].split(">");
				
				if(commands[1].equals("pos")) {
					kf.pos.put(commands[2], getVectorFromString(conv[1]));
				} else if(commands[1].equals("rot")) {
					kf.rot.put(commands[2], getVectorFromString(conv[1]));
				} else if(commands[1].equals("dur")) {
					kf.dur = Integer.parseInt(conv[1]);
				} else if(commands[1].equals("startTick")) {
					kf.startTick = Integer.parseInt(conv[1]);
				}
			}
		}
		return kf;
	}
	
	private static float[] getVectorFromString(String s) {
		String[] vec = s.split(",");
		return new float[] { Float.parseFloat(vec[0]), Float.parseFloat(vec[1]),
				Float.parseFloat(vec[2]) };
	}
	
	private static String vectorToString(float[] vec) {
		String res = "";
		for(int i = 0; i < vec.length; i++) {
			res += String.valueOf(vec[i]);
			if(i != 2) {
				res += ",";
			}
		}
		return res;
	}
	
	public static void showMap(Map<String, float[]> map) {
		for(Entry<String, float[]> entry : map.entrySet()) {
			System.out.println("Key: " + entry.getKey() + " value: " + entry.getValue()[0]
					+ " " + entry.getValue()[1] + " " + entry.getValue()[2]);
		}
	}
	
}
