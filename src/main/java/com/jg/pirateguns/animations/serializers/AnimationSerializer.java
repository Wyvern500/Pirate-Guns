package com.jg.pirateguns.animations.serializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.client.handlers.GunModelsHandler;
import com.mojang.logging.LogUtils;

public class AnimationSerializer {

	public static String serialize(Animation anim) {
		String res = "";
		res += "animation>start\n";
		res += "animation>dur=" + anim.getDuration() + "\n";
		res += "animation>gunModelItem=" + anim.getGunModelItem() + "\n";
		res += "animation>name=\"" + anim.getName() + "\n";
		res += "animation>current=" + anim.getCurrent() + "\n";
		res += "animation>prog=" + anim.getProg() + "\n";
		res += "animation>end\n";
		for(Keyframe kf :anim.getKeyframes()) {
			res += KeyframeSerializer.serialize(kf);
		}
		return res;
	}

	public static Animation deserialize(String json) {
		if(!json.isBlank()) {
			Map<String, String> anim = new HashMap<>(); 
			List<Keyframe> keyframes = new ArrayList<>();
			String[] sData = json.split("\n");
			boolean building = false;
			boolean onlyKeyframes = false;
			String keyframe = "";
			
			for(int i = 0; i < sData.length; i++) {
				String s = sData[i];
				if(!onlyKeyframes) {
					if(s.contains("=")) {
						// Contains parameters
						
						String[] conv = s.split("=");
						String[] commands = conv[0].split(">");
						
						if(building) {
							if(commands[0].equals("animation")) {
								// Building Animation
								anim.put(commands[1], conv[1].replace("\"", ""));
							}	
						}
					} else {
						// Just a method or function
						String[] commands = s.split(">");
						// Start command
						if(commands[1].equals("start")) {
							building = true;
						}
						// End command
						if(commands[1].equals("end")) {
							onlyKeyframes = true;
							building = false;
						}
					}
				} else {
					String[] commands = s.split(">");
					if(!commands[1].equals("end")) {
						keyframe += s + "\n";
					} else {
						keyframe += "keyframe>end\n";
						keyframes.add(KeyframeSerializer.deserialize(keyframe));
						keyframe = "";
					}
				}
			}
			return new Animation(anim.get("gunModelItem"), anim.get("name")+"Serialized", 
					Arrays.copyOf(keyframes.toArray(), keyframes.size(), Keyframe[].class));
		} else {
			return Animation.EMPTY;
		}
	}
	
}
