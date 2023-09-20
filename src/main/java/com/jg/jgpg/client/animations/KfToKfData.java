package com.jg.jgpg.client.animations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.utils.LogUtils;

public class KfToKfData {

	private Map<JgModelPart, KeyframeTransformData> currentTr;
	private Map<JgModelPart, KeyframeTransformData> currentRt;
	private Map<JgModelPart, KeyframeTransformData> prevTr;
	private Map<JgModelPart, KeyframeTransformData> prevRt;
	
	public KfToKfData() {
		this.currentTr = new HashMap<>();
		this.currentRt = new HashMap<>();
		this.prevTr = new HashMap<>();
		this.prevRt = new HashMap<>();
	}

	private void pickCurrent(Keyframe kf) {
		currentTr = new HashMap<>(kf.getTraslations());
		currentRt = new HashMap<>(kf.getRotations());
	}
	
	private void pickPrev(Keyframe kf) {
		prevTr = new HashMap<>(kf.getTraslations());
		prevRt = new HashMap<>(kf.getRotations());
	}
	
	public void pick(Keyframe prev, Keyframe current) {
		pickPrev(prev);
		pickCurrent(current);
		synchronize();
	}
	
	public void synchronize() {
		for(Entry<JgModelPart, KeyframeTransformData> e : prevTr.entrySet()) {
			if(!currentTr.containsKey(e.getKey())) {
				currentTr.put(e.getKey(), e.getValue());
			}
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : prevRt.entrySet()) {
			if(!currentRt.containsKey(e.getKey())) {
				currentRt.put(e.getKey(), e.getValue());
			}
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : currentTr.entrySet()) {
			if(!prevTr.containsKey(e.getKey())) {
				prevTr.put(e.getKey(), new KeyframeTransformData(e.getKey().getTransform().pos, 
						e.getValue().getEasing()));
			}
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : currentRt.entrySet()) {
			if(!prevRt.containsKey(e.getKey())) {
				prevRt.put(e.getKey(), new KeyframeTransformData(e.getKey().getTransform().rot, 
						e.getValue().getEasing()));
			}
		}
	}
	
	public void cleanCurrent() {
		currentTr.clear();
		currentRt.clear();
	}
	
	public void cleanPrev() {
		prevTr.clear();
		prevRt.clear();
	}
	
	public void printCurrentTransforms() {
		String all = "";
		for(Entry<JgModelPart, KeyframeTransformData> e : currentTr.entrySet()) {
			all += "Part: " + e.getKey().getName() + " tr: " + Arrays.toString(e.getValue().getVal()) 
			+ " easing: " + e.getValue().getEasing() + "\n";
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : currentRt.entrySet()) {
			all += "Part: " + e.getKey().getName() + " rt: " + Arrays.toString(e.getValue().getVal()) 
			+ " easing: " + e.getValue().getEasing() + "\n";
		}
		LogUtils.log("Animator", "pct(): " + all);
	}
	
	public void printPrevTransforms() {
		String all = "";
		for(Entry<JgModelPart, KeyframeTransformData> e : prevTr.entrySet()) {
			all += "Part: " + e.getKey().getName() + " tr: " + Arrays.toString(e.getValue().getVal()) 
			+ " easing: " + e.getValue().getEasing() + "\n";
		}
		for(Entry<JgModelPart, KeyframeTransformData> e : prevRt.entrySet()) {
			all += "Part: " + e.getKey().getName() + " rt: " + Arrays.toString(e.getValue().getVal()) 
			+ " easing: " + e.getValue().getEasing() + "\n";
		}
		LogUtils.log("Animator", "ppt(): " + all);
	}
	
	public Map<JgModelPart, KeyframeTransformData> getCurrentTr() {
		return currentTr;
	}

	public Map<JgModelPart, KeyframeTransformData> getCurrentRt() {
		return currentRt;
	}

	public Map<JgModelPart, KeyframeTransformData> getPrevTr() {
		return prevTr;
	}

	public Map<JgModelPart, KeyframeTransformData> getPrevRt() {
		return prevRt;
	}
	
}
