package com.jg.jgpg.client.animations;

import java.util.ArrayList;
import java.util.List;

public class RepetitiveAnimation extends Animation {

	private int cycles;
	private boolean isCycleActive;
	
	public RepetitiveAnimation(String name) {
		super(name);
		this.cycles = 1;
	}
	
	@Override
	public Animation addKeyframe(int tick) {
		keyframeIndex++;
		Keyframe kfToDelete = null;
		for(Keyframe kf : keyframes) {
			if(kf.getTick() == tick) {
				kfToDelete = kf;
				break;
			}
		}
		if(kfToDelete != null ) {
			keyframes.remove(kfToDelete);
		}
		Keyframe kf = new Keyframe(tick);
		if(isCycleActive) {
			kf.setRepetitive(true);
		}
		keyframes.add(kf);
		sort();
		return this;
	}
	
	@Override
	public Animation addKeyframe(Keyframe kf) {
		if(keyframes.contains(kf)) {
			keyframes.remove(kf);
		}
		if(isCycleActive) {
			kf.setRepetitive(true);
		}
		keyframes.add(kf);
		sort();
		return this;
	}
	
	public void build() {
		List<Keyframe> keyframes = new ArrayList<>();
		int start = -1;
		int end = -1;
		// The tick before repetitive keyframes start
		int prevTickToStartTick = 0;
		// The tick of the last repetitive keyframe, as it will be the new start keyframe
		int endTick = -1;
		// I substract 1 from cycles because i want to use it to scale the cycle // cycles by def is 1
		for(int i = 0; i < this.keyframes.size(); i++) {
			Keyframe kf = this.keyframes.get(i);
			keyframes.add(kf);
			// Cloning the repetitive cycle if needed
			if(kf.isRepetitive() && !kf.isShadow()) {
				if(start == -1) {
					start = i;
					if(i - 1 > 0) {
						prevTickToStartTick = this.keyframes.get(i - 1).tick;
					}
				} else {
					if(i + 1 < this.keyframes.size() - 1) {
						if(!this.keyframes.get(i + 1).isRepetitive()) {
							end = i;
							endTick = kf.tick;
						}
					}
				}
			}
			// A start and end has been found in the keyframes, time to clone the necessary stuff
			if(start != -1 && end != -1) {
				int duration = endTick - prevTickToStartTick;
				for(int i2 = start; i2 < end + 1; i2++) {
					keyframes.add(this.keyframes.get(i2));
				}
				for(int c = 1; c < cycles + 1; c++) {
					for(int i2 = start; i2 < end + 1; i2++) {
						int offset = cycles * duration + this.keyframes.get(i2).getTick();
						keyframes.add(this.keyframes.get(i2).copy().setShadow(true).setTick(offset));
					}
				}
			}
			
		}
	}
	
	public void setCycles(int cycles) {
		this.cycles = cycles;
	}
	
	public int getCycles() {
		return cycles;
	}
	
	public void startCycle() {
		isCycleActive = true;
	}
	
	public void stopCycle() {
		isCycleActive = false;
	}

}
