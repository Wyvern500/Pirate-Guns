package com.jg.jgpg.client.animation;

import java.util.ArrayList;
import java.util.List;

public class ModelPartRailCyclesData {

	List<CycleData> cycles;
	
	public ModelPartRailCyclesData() {
		cycles = new ArrayList<>();
	}
	
	public void addCycle(List<Keyframe> cycleKeyframes, int duration, boolean translate) {
		cycles.add(new CycleData(cycleKeyframes, duration, translate));
	}
	
	public List<CycleData> getCycles() {
		return cycles;
	}
	
}
