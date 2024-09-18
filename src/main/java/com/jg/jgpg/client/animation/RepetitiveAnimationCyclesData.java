package com.jg.jgpg.client.animation;

import com.jg.jgpg.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepetitiveAnimationCyclesData {

	Map<ModelPartRail, ModelPartRailCyclesData> rails;

	public RepetitiveAnimationCyclesData() {
		rails = new HashMap<>();
	}
	
	public void setup(RepetitiveAnimation animation) {
		List<ModelPartRail> railsFound = new ArrayList<>();
		for(ModelPartKeyframesHandler handler : animation.getPartHandlers().values()) {
			// Adding the necessary stuff
			// Translations
			int count = 0;
			for(int i = 0; i < handler.getTranslations().size(); i++) {
				Keyframe kf = handler.getTranslations().get(i);
				if(kf.isRepetitive()) {
					count++;
					if(count >= 2) {
						railsFound.add(new ModelPartRail(handler, true));
						count = 0;
						break;
					}
				}
			}
			// Rotations
			for(int i = 0; i < handler.getRotations().size(); i++) {
				Keyframe kf = handler.getRotations().get(i);
				if(kf.isRepetitive()) {
					count++;
					if(count >= 2) {
						railsFound.add(new ModelPartRail(handler, false));
						count = 0;
						break;
					}
				}
			}
		}
		
		// Adding the cycles
		for (ModelPartRail rail : railsFound) {
			List<Keyframe> keyframes = rail.isTranslate() ? rail.getHandler().getTranslations()
					: rail.getHandler().getRotations();
			int start = -1;
			int end = -1;
			int duration = 0;
			List<Keyframe> cycleKeyframes = new ArrayList<>();
			
			// Keyframes
			for (int i = 0; i < keyframes.size(); i++) {
				Keyframe kf = keyframes.get(i);
				if (kf.isRepetitive()) {
					// Is this start of the cycle
					if (start == -1) {
						start = i;
					} else {
						end = i;
					}
				}
				
				// Can start counting the duration
				if (start != -1) {
					cycleKeyframes.add(kf);
					// Cycle Duration
					if (i == 0) {
						duration += kf.getTick();
					} else {
						duration += kf.getTick() - keyframes.get(i - 1).getTick();
					}
				}

				// Checking if the start and end has been found
				if (start != -1 && end != -1) {

					//LogUtils.log("RACD", "Adding Cycle: " + cycleKeyframes.size());
					
					// Start and End has been found
					if (!rails.containsKey(rail)) {
						rails.put(rail, new ModelPartRailCyclesData());
					}
					rails.get(rail).getCycles().add(new CycleData(new ArrayList<>(cycleKeyframes), duration, 
							rail.isTranslate()));

					// Set RepetitiveAnimation data

					animation.getCyclesData().add(new CycleData.CycleShortData(keyframes
							.get(start).getTick(), keyframes.get(end).getTick(),
							duration));

					/*LogUtils.log("RepetitiveAnimationCyclesData", "Adding data: "
						+ "StartTick: " + keyframes.get(start).getTick() + " EndTick: "
						+ keyframes.get(end).getTick() + " Duration: " + duration);*/

					// Reseting all
					start = -1;
					end = -1;
					cycleKeyframes.clear();
					duration = 0;
				}
			}
			
		}
		
		// Getting the shadows
		for (ModelPartRail rail : railsFound) {
			List<Keyframe> keyframes = rail.isTranslate() ? rail.getHandler().getTranslations()
					: rail.getHandler().getRotations();
			List<Keyframe> shadowKeyframes = new ArrayList<>();
			int shadows = 0;
			boolean startedCounting = false;
			
			for(CycleData data : rails.get(rail).getCycles()) {
				if(data.getKeyframes().isEmpty()) continue;
				
				Keyframe lastKeyframe = data.getKeyframes().get(data.getKeyframes().size() - 1);
				int index = keyframes.indexOf(lastKeyframe);
				
				if(index == -1) {
					LogUtils.log("RACD", "Index = -1");
					continue;
				}
				
				for (int i = index; i < keyframes.size(); i++) {
					Keyframe kf = keyframes.get(i);
					if(kf.isShadow()) {
						shadowKeyframes.add(kf);
						shadows++;
						startedCounting = true;
					} else if(startedCounting) {
						data.setShadows(shadows);
						break;
					}
				}
			}
			
			// Removing the shadow keyframes
			keyframes.removeAll(shadowKeyframes);
		}
	}

	public Map<ModelPartRail, ModelPartRailCyclesData> getRails() {
		return rails;
	}

}
