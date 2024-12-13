package com.jg.jgpg.client.animation;

import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.Vector3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Animation {

	String name;
	int duration;
	Map<JgModelPart, ModelPartKeyframesHandler> partHandlers;
	
	public Animation(String name) {
		this.name = name;
		partHandlers = new HashMap<>();
	}
	
	public Animation translate(JgModelPart part, int tick, String easing, float x, float y, float z) {
		return translate(part, tick, easing, new Vector3(x, y, z));
	}
	
	public Animation rotate(JgModelPart part, int tick, String easing, float rx, float ry, float rz) {
		return rotate(part, tick, easing, new Vector3(rx, ry, rz));
	}
	
	public Animation translate(JgModelPart part, int tick, String easing, Vector3 vec) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		if(part == null) {
			LogUtils.log("Animation", "null");
		}
		partHandlers.get(part).addKeyframe(tick, easing, true, vec);
		return this;
	}
	
	public Animation rotate(JgModelPart part, int tick, String easing, Vector3 vec) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addKeyframe(tick, easing, false, vec);
		return this;
	}
	
	public Animation translate(JgModelPart part, int tick, String easing) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addKeyframe(tick, easing, true);
		return this;
	}
	
	public Animation rotate(JgModelPart part, int tick, String easing) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addKeyframe(tick, easing, false);
		return this;
	}

	public Animation end() {
		duration = 0;
		for(ModelPartKeyframesHandler handler : partHandlers.values()) {
			Collections.sort(handler.translations, (i1, i2) -> Integer.compare(i1.getTick(), 
					i2.getTick()));
			Collections.sort(handler.rotations, (i1, i2) -> Integer.compare(i1.getTick(), 
					i2.getTick()));
			//LogUtils.log("Animation", "Handler");
			if(!handler.translations.isEmpty()) {
				Keyframe kf = handler.translations.get(handler.translations.size() - 1);
				//LogUtils.log("Animation", "T Tick " + kf.getTick() + " duration: " + duration);
				if(kf.getTick() > duration) {
					duration = kf.getTick();
					//LogUtils.log("Animation", "T Tick " + kf.getTick());
				}
			}
			if(!handler.rotations.isEmpty()) {
				Keyframe kf = handler.rotations.get(handler.rotations.size() - 1);
				if(kf.getTick() > duration) {
					duration = kf.getTick();
					//LogUtils.log("Animation", "R Tick " + kf.getTick());
				}
			}
		}
		return this;
	}
	
	public void sort() {
		for(ModelPartKeyframesHandler handler : partHandlers.values()) {
			Collections.sort(handler.translations, (i1, i2) -> Integer.compare(i1.getTick(), 
					i2.getTick()));
			Collections.sort(handler.rotations, (i1, i2) -> Integer.compare(i1.getTick(), 
					i2.getTick()));
		}
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public Map<JgModelPart, ModelPartKeyframesHandler> getPartHandlers() {
		return partHandlers;
	}
	
}
