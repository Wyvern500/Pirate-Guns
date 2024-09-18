package com.jg.jgpg.client.animation;

import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.Pair;
import com.jg.jgpg.utils.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class RepetitiveAnimation extends Animation {

	private int cycles;
	private int oldCycles;
	private List<CycleData.CycleShortData> cyclesData;
	
	public RepetitiveAnimation(String name) {
		super(name);
		cyclesData = new ArrayList<>();
	}
	
	public RepetitiveAnimation translate(JgModelPart part, int tick, String easing, float x, float y, float z,
			boolean isRepetitive) {
		return translate(part, tick, easing, new Vector3(x, y, z), isRepetitive);
	}

	public RepetitiveAnimation rotate(JgModelPart part, int tick, String easing, float rx, float ry, float rz,
			boolean isRepetitive) {
		return rotate(part, tick, easing, new Vector3(rx, ry, rz), isRepetitive);
	}

	public RepetitiveAnimation translate(JgModelPart part, int tick, String easing, Vector3 vec, 
			boolean isRepetitive) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addRepetitiveKeyframe(tick, easing, true, vec, isRepetitive);
		return this;
	}

	public RepetitiveAnimation rotate(JgModelPart part, int tick, String easing, Vector3 vec, 
			boolean isRepetitive) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addRepetitiveKeyframe(tick, easing, false, vec, isRepetitive);
		return this;
	}

	@Override
	public RepetitiveAnimation translate(JgModelPart part, int tick, String easing, float x, float y, float z) {
		return translate(part, tick, easing, new Vector3(x, y, z));
	}
	
	@Override
	public RepetitiveAnimation rotate(JgModelPart part, int tick, String easing, float rx, float ry, float rz) {
		return rotate(part, tick, easing, new Vector3(rx, ry, rz));
	}
	
	@Override
	public RepetitiveAnimation translate(JgModelPart part, int tick, String easing, Vector3 vec) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		if(part == null) {
			LogUtils.log("Animation", "null");
		}
		partHandlers.get(part).addKeyframe(tick, easing, true, vec);
		return this;
	}
	
	@Override
	public RepetitiveAnimation rotate(JgModelPart part, int tick, String easing, Vector3 vec) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addKeyframe(tick, easing, false, vec);
		return this;
	}
	
	@Override
	public RepetitiveAnimation translate(JgModelPart part, int tick, String easing) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addRepetitiveKeyframe(tick, easing, true, false);
		return this;
	}

	@Override
	public RepetitiveAnimation rotate(JgModelPart part, int tick, String easing) {
		if(!partHandlers.containsKey(part)) {
			partHandlers.put(part, new ModelPartKeyframesHandler(part));
		}
		partHandlers.get(part).addRepetitiveKeyframe(tick, easing, false, false);
		return this;
	}

	public boolean isTick(float target, float tick) {
        for (CycleData.CycleShortData cycleData : cyclesData) {
            if (target >= cycleData.startTick && target <= cycleData.endTick) {
				//LogUtils.log("TestItemModel", "Ticking inside");
                float t = tick - cycleData.startTick;
                int current = (int) Math.floor(t / cycleData.duration);
                target = target - cycleData.startTick;
                int times = cycles + 1;
                return tick > cycleData.startTick && current < times
                        && t - (current * cycleData.duration) == target && tick < cycleData
                        .startTick + (cycleData.duration * times);
            }
        }
		return false;
	}

	public void build() {
		// Fourth try of making build to work
		
		LogUtils.log("RepetitiveAnimation", "Building");

		cyclesData.clear();

		RepetitiveAnimationCyclesData data = new RepetitiveAnimationCyclesData();
		data.setup(this);
		doBackwardOffset(data);
		doForwardOffset(data);
		addShadowKeyframes(data);
		end();
		
		LogUtils.log("RepetitiveAnimation", "Built");
	}
	
	// New Try
	
	public void addShadowKeyframes(RepetitiveAnimationCyclesData data) {
		for(Entry<ModelPartRail, ModelPartRailCyclesData> entry : data.getRails().entrySet()) {
			List<Keyframe> keyframes = entry.getKey().isTranslate() ? entry.getKey().getHandler()
					.getTranslations() : entry.getKey().getHandler().getRotations();
			for(CycleData cycleData : entry.getValue().getCycles()) {
				for(int c = 0; c < cycles; c++) {
					int offset = (c + 1) * cycleData.getDuration();
					for(Keyframe cycleKf : cycleData.getKeyframes()) {
						keyframes.add(cycleKf.copy().setTick(cycleKf.getTick() + offset).setShadow(true)
								.setRepetitive(false));
					}
				}
			}
		}
	}
	
	public void doForwardOffset(RepetitiveAnimationCyclesData data) {
		for(Entry<ModelPartRail, ModelPartRailCyclesData> entry : data.getRails().entrySet()) {
			List<Keyframe> keyframes = entry.getKey().isTranslate() ? entry.getKey().getHandler()
					.getTranslations() : entry.getKey().getHandler().getRotations();
			for(CycleData cycleData : entry.getValue().getCycles()) {
				int forwardOffset = cycles * cycleData.getDuration();
				int cycleEndIndex = keyframes.indexOf(cycleData.getKeyframes().get(cycleData.getKeyframes()
						.size() - 1));
				
				/*LogUtils.log("RepetiveAnimation", "forwardOffset: " + forwardOffset + " cycleEndIndex: "
						+ cycleEndIndex + " keyframesSize: " + keyframes.size());*/
				
				if(cycleEndIndex + 1 < keyframes.size()) {
					for(int i = cycleEndIndex + 1; i < keyframes.size(); i++) {
						Keyframe kf = keyframes.get(i);
						/*LogUtils.log("RepetiveAnimation", "Moving from " + kf.getTick() + " to: " +
								(kf.getTick() + forwardOffset));*/
						kf.setTick(kf.getTick() + forwardOffset);
					}
				}
			}
		}
	}
	
	public void doBackwardOffset(RepetitiveAnimationCyclesData data) {
		for(Entry<ModelPartRail, ModelPartRailCyclesData> entry : data.getRails().entrySet()) {
			List<Keyframe> keyframes = entry.getKey().isTranslate() ? entry.getKey().getHandler()
					.getTranslations() : entry.getKey().getHandler().getRotations();
			for(CycleData cycleData : entry.getValue().getCycles()) {
				int backwardOffset = (cycleData.getShadows() / cycleData.getKeyframes().size()) 
						* cycleData.getDuration();
				int cycleEndIndex = keyframes.indexOf(cycleData.getKeyframes().get(cycleData.getKeyframes()
						.size() - 1));
				// Is there more keyframes in front of the end of the cycle?
				if(cycleEndIndex + 1 < keyframes.size()) {
					for(int i = cycleEndIndex + 1; i < keyframes.size(); i++) {
						Keyframe kf = keyframes.get(i);
						kf.setTick(kf.getTick() - backwardOffset);
					}
				}
				
			}
		}
	}
	
	// End of the new try
	
	public void printKeyframes() {
		List<Pair<JgModelPart, Keyframe>> data = new ArrayList<>();
		for(ModelPartKeyframesHandler handler : partHandlers.values()) {
			for(Keyframe kf : handler.getTranslations()) {
				data.add(new Pair<JgModelPart, Keyframe>(handler.getPart(), kf));
			}
			for(Keyframe kf : handler.getRotations()) {
				data.add(new Pair<JgModelPart, Keyframe>(handler.getPart(), kf));
			}
		}
		Collections.sort(data, (i1, i2) -> Integer.compare(i1.getRight().getTick(), i2
				.getRight().getTick()));
		//LogUtils.log("RepetitiveAnimation", "Printing Keyframes");
		for(Pair<JgModelPart, Keyframe> kf : data) {
			/*LogUtils.log("RepetitiveAnimation", "Part: " + kf.getLeft().getName() + " KfTick: " + kf
					.getRight().getTick() + " isShadow? " + kf.getRight().isShadow());*/
		}
	}
	
	@Override
	public RepetitiveAnimation end() {
		duration = 0;
		for(ModelPartKeyframesHandler handler : partHandlers.values()) {
			// Sorting
			Collections.sort(handler.translations, (i1, i2) -> Integer.compare(i1.getTick(), 
					i2.getTick()));
			Collections.sort(handler.rotations, (i1, i2) -> Integer.compare(i1.getTick(), 
					i2.getTick()));
			// Finding the duration
			if(!handler.translations.isEmpty()) {
				Keyframe kf = handler.translations.get(handler.translations.size() - 1);
				if(kf.getTick() > duration) {
					//LogUtils.log("RepetitiveAnimation", "T Tick: " + kf.getTick() + " Duration: " + duration);
					duration = kf.getTick();
				}
			}
			if(!handler.rotations.isEmpty()) {
				Keyframe kf = handler.rotations.get(handler.rotations.size() - 1);
				if(kf.getTick() > duration) {
					//LogUtils.log("RepetitiveAnimation", "R Tick: " + kf.getTick() + " Duration: " + duration);
					duration = kf.getTick();
				}
			}
		}
		return this;
	}
	
	public void setCycles(int cycles) {
		if(this.cycles != cycles) {
			this.oldCycles = this.cycles;
		}
		this.cycles = cycles;
	}
	
	public int getCycles() {
		return cycles;
	}

	public int getOldCycles() {
		return oldCycles;
	}

	public List<CycleData.CycleShortData> getCyclesData() {
		return cyclesData;
	}

}
