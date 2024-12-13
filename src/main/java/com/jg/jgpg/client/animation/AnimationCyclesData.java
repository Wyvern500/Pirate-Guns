package com.jg.jgpg.client.animation;

import com.jg.jgpg.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationCyclesData {

	Map<ModelPartKeyframesHandlerRoad, BakedCycleData> cycles;
	
	public AnimationCyclesData() {
		cycles = new HashMap<>();
	}
	
	public void getCycles(RepetitiveAnimation animation) {
		cycles.clear();
		List<ModelPartKeyframesHandlerRoad> lineRoads = new ArrayList<>();
		// Getting the modelPartKeyframesHandlerRoads (translation and rotation roads)
		for(ModelPartKeyframesHandler handler : animation.getPartHandlers().values()) {
			int count = 0;
			for(Keyframe kf : handler.getTranslations()) {
				if(kf.isRepetitive()) {
					count++;
					if(count >= 2) {
						count = 0;
						lineRoads.add(new ModelPartKeyframesHandlerRoad(handler, true));
						/*LogUtils.log("AnimationCyclesData", "Adding translationRoad " + handler.getPart()
						.getName());*/
						break;
					}
				}
			}
			count = 0;
			for(Keyframe kf : handler.getRotations()) {
				if(kf.isRepetitive()) {
					count++;
					if(count >= 2) {
						count = 0;
						lineRoads.add(new ModelPartKeyframesHandlerRoad(handler, false));
						/*LogUtils.log("AnimationCyclesData", "Adding rotationRoad " + handler.getPart()
						.getName());*/
						break;
					}
				}
			}
		}
		// Identifying the keyframes, start, end and duration of the cycles
		for(ModelPartKeyframesHandlerRoad road : lineRoads) {
			List<Keyframe> keyframes = null;
			int start = -1;
			int end = -1;
			int duration = 0;
			if(road.isTranslate()) {
				keyframes = road.getHandler().getTranslations();
			} else {
				keyframes = road.getHandler().getRotations();
			}
			
			// Process
			for(int i = 0; i < keyframes.size(); i++) {
				Keyframe kf = keyframes.get(i);
				if(kf.isRepetitive()) {
					if(start == -1) {
						start = i;
					} else {
						end = i;
					}
				}
				// A cycle has been found
				if(start != -1 && end != -1) {
					List<KeyframeCycleData> keyframeItems = new ArrayList<>();
					for(int j = start; j < end + 1; j++) {
						Keyframe prev = null;
						Keyframe current = keyframes.get(j);
						// Adding keyframeItem to the list
						keyframeItems.add(new KeyframeCycleData(current, road));
						if(j - 1 >= 0) {
							prev = keyframes.get(j - 1);
						}
						// There is a previous keyframe
						if(prev != null) {
							duration += current.getTick() - prev.getTick();
						} else {
							// There is not a previuos keyframe, so the kf tick, will be, the duration
							duration += current.getTick();
						}
					}
					// Adding the bakedCycleData if there is no one
					if(!cycles.containsKey(road)) {
						cycles.put(road, new BakedCycleData());
					}
					BakedCycleData bakedData = cycles.get(road);
					bakedData.addCycle(keyframeItems, duration, keyframes.get(start).getTick(), 
							keyframes.get(end).getTick());
					// Add RawCycleData
					start = -1;
					end = -1;
					duration = 0;
				}
			}
		}
	}

	public void update() {
		// Identifying the keyframes, start, end and duration of the cycles
		for (ModelPartKeyframesHandlerRoad road : cycles.keySet()) {
			List<Keyframe> keyframes = null;
			int start = -1;
			int end = -1;
			if (road.isTranslate()) {
				keyframes = road.getHandler().getTranslations();
			} else {
				keyframes = road.getHandler().getRotations();
			}

			// Process
			int cycle = 0;
			for (int i = 0; i < keyframes.size(); i++) {
				Keyframe kf = keyframes.get(i);
				if (kf.isRepetitive()) {
					if (start == -1) {
						start = i;
					} else {
						end = i;
					}
				}
				// A cycle has been found
				if (start != -1 && end != -1) {
					BakedCycleData bakedData = cycles.get(road);
					// Update RawCycleData
					bakedData.getCycles().get(cycle).getRight().setStartTick(keyframes.get(start).getTick());
					bakedData.getCycles().get(cycle).getRight().setEndTick(keyframes.get(end).getTick());
					/*LogUtils.log("AnimationCyclesData", "StartTick: " + keyframes.get(start).getTick() + 
							" EndTick: " + keyframes.get(end).getTick());*/
					// Incrementing cycle index
					cycle++;
					start = -1;
					end = -1;
				}
			}
		}
	}
	
	public Map<ModelPartKeyframesHandlerRoad, BakedCycleData> getCycles() {
		return cycles;
	}

	public static class KeyframeCycleData {
		
		Keyframe keyframe;
		ModelPartKeyframesHandlerRoad road;
		
		public KeyframeCycleData(Keyframe keyframe, ModelPartKeyframesHandlerRoad road) {
			this.keyframe = keyframe;
			this.road = road;
		}

		public Keyframe getKeyframe() {
			return keyframe;
		}

		public ModelPartKeyframesHandlerRoad getRoad() {
			return road;
		}
		
	}
	
	public static class ModelPartKeyframesHandlerRoad {
		
		ModelPartKeyframesHandler handler;
		boolean translate;
		
		public ModelPartKeyframesHandlerRoad(ModelPartKeyframesHandler handler, boolean translate) {
			this.handler = handler;
			this.translate = translate;
		}

		public ModelPartKeyframesHandler getHandler() {
			return handler;
		}

		public boolean isTranslate() {
			return translate;
		}
		
	}
	
	public static class BakedCycleData {
		
		List<Pair<List<KeyframeCycleData>, RawCycleData>> cycles;
		
		public BakedCycleData() {
			cycles = new ArrayList<>();
		}
		
		public void addCycle(List<KeyframeCycleData> cycle, int duration, int startTick, int endTick) {
			cycles.add(new Pair<List<KeyframeCycleData>, RawCycleData>(cycle, new RawCycleData(duration, 
					startTick, endTick)));
		}
		
		public List<Pair<List<KeyframeCycleData>, RawCycleData>> getCycles() {
			return cycles;
		}
		
	}
	
	public static class RawCycleData {
		
		int duration;
		int startTick;
		int endTick;
		
		public RawCycleData() {
			this(0, 0, 0);
		}
		
		public RawCycleData(int duration, int startTick, int endTick) {
			this.duration = duration;
			this.startTick = startTick;
			this.endTick = endTick;
		}

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public int getStartTick() {
			return startTick;
		}

		public void setStartTick(int startTick) {
			this.startTick = startTick;
		}

		public int getEndTick() {
			return endTick;
		}

		public void setEndTick(int endTick) {
			this.endTick = endTick;
		}
		
	}
	
}
