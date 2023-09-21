package com.jg.jgpg.client.animations;

import java.util.Map.Entry;

import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.ItemInHandRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animator {
	
	AbstractJgModel model;
	
	Animation current;
	
	KfToKfData data;
	
	Keyframe currentKf;
	Keyframe last;
	
	boolean play;
	boolean shouldUpdate;
	boolean prevKf;
	boolean forward;
	
	float prevTick;
	float tick;
	
	int prevKeyframeIndex;
	int keyframeIndex;
	int type;
	
	public Animator() {
		data = new KfToKfData();
		forward = true;
		LogUtils.log("Animator", "Starting Animator");
	}
	
	public void tick() {
		/*LogUtils.log("Animator", "DeltaFrameTime: " + Minecraft.getInstance().getDeltaFrameTime() + 
				" FrameTime: " + Minecraft.getInstance().getFrameTime() + " PartialTick: " + 
				Minecraft.getInstance().getPartialTick() + " FrameTimeNs" + 
				Minecraft.getInstance().getFrameTimeNs());*/
		if(model != null) {
			if(current != null) {
				if(play) {
					if(forward) {
						if(tick < current.getDuration()) { // type: 2 = Debug
							if(tick >= currentKf.getTick() && tick - prevTick > 0 && !prevKf) {
								pickNewKeyframe();
							} else if(tick >= last.getTick() && tick - prevTick > 0 && prevKf) {
								pickNewKeyframe();
							}
							
							prevTick = tick;
							tick += Minecraft.getInstance().getDeltaFrameTime() * 2f;
							
							if(tick >= current.getDuration()) {
								tick = current.getDuration();
							}
							
							updateAnimTickMarker();
							
							lerpValues();
							
							updateAnimTickMarker();
						} else {
							if(type != 2) { // Debug Mode
								// End of the animation
								endAnimation();
							} else {
								stop();
							}
						}
					} else {
						if(tick > 0) {
							if(tick <= last.getTick() && tick - prevTick < 0 && !prevKf) {
								pickPrevKeyframe();
							} else if(tick <= currentKf.getTick() && tick - prevTick < 0 && prevKf) {
								pickPrevKeyframe();
							}
							
							prevTick = tick;
							tick -= 1f;
							
							if(tick < 0) {
								tick = 0;
							}
							
							LogUtils.log("Animator", "CurrentKf: " + currentKf.getTick() + 
									" lastKf: " + last.getTick() + " tick: " + tick);
							
							updateAnimTickMarker();
							
							lerpValues();
							
							updateAnimTickMarker();
						} else {
							stop();
							//pickNewKeyframe();
							
							clearAll();
							prevTick = 0;
							tick = 0;
							keyframeIndex = 0;
							pickNewKeyframe();
							LogUtils.log("Animator", "Reseting all");
						}
					}
				} else {
					// Play is false
					if(shouldUpdate) {
						if(tick < current.getDuration()) {
							float dif = this.tick - prevTick;
							if(dif > 0) {
								if(this.tick >= currentKf.getTick() && !prevKf) {
									pickNewKeyframe();
								} else if(this.tick >= last.getTick() && prevKf) {
									pickNewKeyframe();
								}
							} else if(dif < 0) {
								if(this.tick < last.getTick() && !prevKf) {
									pickPrevKeyframe();
								} else if(this.tick < currentKf.getTick() && prevKf) {
									pickPrevKeyframe();
								}
							}
							
							LogUtils.log("Animator", "currentTick: " + currentKf.getTick() + 
									" prevTick: " + last.getTick() + " tick: " + tick + " kfIndex: " + 
									keyframeIndex + " prevKfIndex: " + prevKeyframeIndex);
							
							lerpValues();
							
							updateAnimTickMarker();
							
							shouldUpdate = false;
						} else {
							lerpValues();
							// Animation ended
							endAnimation();
							shouldUpdate = false;
						}
					}
				}
			}
		}	
	}
	
	public void setTick(float tick) {
		this.prevTick = this.tick;
		this.tick = tick;
		
		if(this.tick < current.getDuration()) {
			float dif = this.tick - prevTick;
			if(dif > 0) {
				if(this.tick >= currentKf.getTick() && !prevKf) {
					pickNewKeyframe();
				} else if(this.tick >= last.getTick() && prevKf) {
					pickNewKeyframe();
				}
			} else if(dif < 0) {
				if(this.tick < last.getTick() && !prevKf) {
					pickPrevKeyframe();
				} else if(this.tick < currentKf.getTick() && prevKf) {
					pickPrevKeyframe();
				}
			}

			if(type == 2) {
				lerpValues();
			}
		} else {
			if(type == 2) {
				lerpValues();
			}
			// Animation ended
			endAnimation();
		}
	}
	
	public void lerpValues() {
		model.onAnimationTick(current, prevTick, tick);
		
		float currentTick = (tick - last.getTick());
		float currentPrevTick = (prevTick - last.getTick());
		int duration = currentKf.getTick() - last.getTick();
		float partialTick = Minecraft.getInstance().getPartialTick();
		float prog = ((currentPrevTick + (currentTick - currentPrevTick) * partialTick) / duration);
		if(tick == currentKf.tick) {
			prog = 1.0f;
		}
		
		/*LogUtils.log("Animator", "Prog: " + prog + " currentTick: " + currentTick  + " currentPrevTick: " + 
				currentPrevTick + " dur: " + duration + " dif: " + dif + " test: " + 
				((currentPrevTick + dif * partialTick) / duration) + " pTick: " + partialTick + 
				" test2: " + MathUtils.lerp(currentPrevTick, currentTick, partialTick));*/
		
		Map<JgModelPart, KeyframeTransformData> currentTr = data.getCurrentTr();
		Map<JgModelPart, KeyframeTransformData> prevTr = data.getPrevTr();
		for(Entry<JgModelPart, KeyframeTransformData> e : data.getCurrentTr().entrySet()) {
			if(!data.getPrevTr().containsKey(e.getKey())) {
				data.getPrevTr().put(e.getKey(), e.getValue());
			}
			
			// KeyframeTransformPart Easing
			float kfTransformPartProg = EasingHandler.INSTANCE
					.getEasing(e.getValue().getEasing()).get(prog);
			
			e.getKey().getTransform().pos[0] = MathUtils.lerp(prevTr
					.get(e.getKey()).getVal()[0], 
					currentTr
					.get(e.getKey()).getVal()[0], kfTransformPartProg);
			e.getKey().getTransform().pos[1] = MathUtils.lerp(prevTr
					.get(e.getKey()).getVal()[1], 
					currentTr
					.get(e.getKey()).getVal()[1], kfTransformPartProg);
			e.getKey().getTransform().pos[2] = MathUtils.lerp(prevTr
					.get(e.getKey()).getVal()[2], 
					currentTr
					.get(e.getKey()).getVal()[2], kfTransformPartProg);
			/*LogUtils.log("Animator", "Part: " + e.getKey().getName() + " prev x: " 
					+ prevTr.get(e.getKey()).getVal()[0] 
					+ " prev y: " + prevTr.get(e.getKey()).getVal()[1] 
					+ " curr x: " + currentTr.get(e.getKey()).getVal()[0]
					+ " curr y: " + currentTr.get(e.getKey()).getVal()[1] + " prog: " + 
					prog + " tick: " + tick + " kfDur: " + 
					currentKf.getTick() + " dur: " + current.getDuration() + 
					" cKf: " + currentKf.getTick() + " lKf: " + last.getTick() + 
					" partTr: " + e.getKey().getTransform().toString());*/
		}
		
		Map<JgModelPart, KeyframeTransformData> currentRt = data.getCurrentRt();
		Map<JgModelPart, KeyframeTransformData> prevRt = data.getPrevRt();
		for(Entry<JgModelPart, KeyframeTransformData> e : currentRt.entrySet()) {
			if(!data.getPrevRt().containsKey(e.getKey())) {
				data.getPrevRt().put(e.getKey(), e.getValue());
			}
			
			// KeyframeTransformPart Easing
			float kfTransformPartProg = EasingHandler.INSTANCE
					.getEasing(e.getValue().getEasing()).get(prog);
			
			e.getKey().getTransform().rot[0] = MathUtils.rotLerp(prevRt
					.get(e.getKey()).getVal()[0], 
					currentRt
					.get(e.getKey()).getVal()[0], kfTransformPartProg);
			e.getKey().getTransform().rot[1] = MathUtils.rotLerp(prevRt
					.get(e.getKey()).getVal()[1], 
					currentRt
					.get(e.getKey()).getVal()[1], kfTransformPartProg);
			e.getKey().getTransform().rot[2] = MathUtils.rotLerp(prevRt
					.get(e.getKey()).getVal()[2], 
					currentRt
					.get(e.getKey()).getVal()[2], kfTransformPartProg);
			/*LogUtils.log("Animator", "prev x: " + prevRotations.get(e.getKey())[0] 
					+ " prev y: " + prevRotations.get(e.getKey()).getVal()[1] 
					+ " curr x: " + currentRotations.get(e.getKey()).getVal()[0]
					+ " curr y: " + currentRotations.get(e.getKey()).getVal()[1] + " prog: " + 
					prog + " tick: " + tick + " kfDur: " + 
					currentKf.getTick() + " dur: " + current.getDuration());*/
		}
	}
	
	public void updateAnimTickMarker() { 
		Screen screen = Minecraft.getInstance().screen;
		if(screen instanceof AnimationGui) {
			AnimationGui gui = (AnimationGui) screen;
			gui.getKeyframeManager().getTickManager().update(tick, gui.getKeyframeManager().getOffset());
		}
	}
	
	public void nextTick(AnimationGui gui) {
		if(current != null) {
			if(tick < current.getDuration()) {
				prevTick = tick;
				tick++;
				if(tick > current.getDuration()) {
					tick = current.getDuration();
				}
				gui.getKeyframeManager().getTickManager().update(tick, gui.getKeyframeManager()
						.getOffset());
				shouldUpdate = true;
			}
		}
	}
	
	public void prevTick(AnimationGui gui) {
		if(current != null) {
			if(tick > 0) {
				prevTick = tick;
				tick--;
				if(tick < 0) {
					tick = 0;
				}
				gui.getKeyframeManager().getTickManager().update(tick, gui.getKeyframeManager()
						.getOffset());
				LogUtils.log("Animator", "Tick: " + tick + " duration: " + current.getDuration());
				shouldUpdate = true;
			}
		}
	}
	
	public void save() {
		String all = "= new Animation(\"" + current.getName() + "\")\n";
		for(Keyframe kf : current.getKeyframes()) {
			all += ".addKeyframe(" + kf.getTick() + ")\n";
			for(Entry<JgModelPart, KeyframeTransformData> entry : kf.getTraslations().entrySet()) {
				all += ".traslate(getPart(\"" + entry.getKey().getName() + "\")" + ", " 
						+ entry.getValue().getVal()[0] + "f, " + entry.getValue().getVal()[1] + "f, " 
						+ entry.getValue().getVal()[2] + "f, "
						+ "\"" + entry.getValue().getEasing() + "\"" + ")\n";
			}
			for(Entry<JgModelPart, KeyframeTransformData> entry : kf.getRotations().entrySet()) {
				all += ".rotate(getPart(\"" + entry.getKey().getName() + "\")" + ", " 
						+ entry.getValue().getVal()[0] + "f, " + entry.getValue().getVal()[1] + "f, " 
						+ entry.getValue().getVal()[2] + "f, "
						+ "\"" + entry.getValue().getEasing() + "\"" + ")\n";
			}
		}
		all += ".end();";
		LogUtils.log("Animator", all);
	}
	
	public void setCurrent(AbstractJgModel model, Animation current) {
		clearAll();
		this.model = model;
		this.current = current;
		this.shouldUpdate = false;
		this.play = false;
		this.prevKf = false;
		this.prevKeyframeIndex = 0;
		this.keyframeIndex = 0;
		this.prevTick = 0;
		this.tick = 0;
		if(model != null) {
			model.getHandler().getAnimationDataHandler().setAnimation(current);
			Screen screen = Minecraft.getInstance().screen;
			if(screen instanceof AnimationGui) {
				AnimationGui gui = (AnimationGui) screen;
				gui.setModel(model);
				gui.getKeyframeManager().getTickManager().update(tick, 0);
			}
			pickNewKeyframe();
			model.setupModelPartsForAnimation(last);
		}
	}
	
	public void endAnimation() {
		if(type == 0) { // Normal
			prevTick = 0;
			tick = 0;
			keyframeIndex = 0;
			setCurrent(null, null);
			clearAll();
		} else if(type == 1){ //Loop
			clearAll();
			prevTick = 0;
			tick = 0;
			keyframeIndex = 0;
			setCurrent(model, current);
		} else if(type == 2) { // Debug
			
		}
	}
	
	public void updateAnimation(Animation current) {
		this.current = current;
		if(model != null) {
			model.onAnimationStart(current);
			pickNewKeyframe();
		}
	}
	
	public void update() {
		if(model != null && current != null) {
			
			/*List<Keyframe> keyframes = current.getKeyframes();
			if(!keyframes.isEmpty()) {
				for(int i = 0; i < keyframes.size() - 1; i++) {
					Keyframe kf = keyframes.get(i);
					if(tick < kf.getTick()) {
						keyframeIndex = i;
						if(i == 0) {
							if(kf.getTick() != 0) {
								last = new Keyframe(0).copyTransformsFrom(kf).toZero();
								currentKf = kf;
								data.pick(last, kf);
							} else {
								last = kf;
								currentKf = keyframes.get(i + 1);
								data.pick(last, kf);
							}
						} else {
							last = keyframes.get(i - 1);
							currentKf = kf;
							data.pick(last, kf);
						}
						break;
					}
				}
				current.end();
				LogUtils.log("Animator", "Update currentKf: " + currentKf.getTick() + " prevKf: " + 
						last.getTick() + " tick: " + tick);
			}*/
			pickKeyframe();
			current.end();
		}
	}
	
	private void pickKeyframe() { // With this method i select a prev and current kf according to a tick 
		List<Keyframe> keyframes = current.getKeyframes();
		for(int i = 0; i < keyframes.size(); i++) {
			Keyframe kf = keyframes.get(i);
			if(tick <= kf.getTick()) {
				/*if(tick - prevTick > 0) {
					// First keyframe, this is important because if there is no a keyframe at 0 then you have
					// to create it, so it wont throw an error
					if(i == 0) {
						if(kf.getTick() == 0) { // There is a kf at 0, nice, so we do it as any kf
							currentKf = keyframes.get(i + 1);
							last = kf;
							data.pick(last, currentKf);
						} else { // Oh no, there is no a kf at 0, we have to create it
							Keyframe zero = new Keyframe(0).copyTransformsFrom(kf).toZero();
							currentKf = kf;
							last = zero;
							data.pick(last, currentKf);
						}
					} else { // Select kf as any kf
						currentKf = kf;
						last = keyframes.get(i - 1);
						data.pick(last, currentKf);
					}
				} else {
					
				}*/
				// First keyframe, this is important because if there is no a keyframe at 0 then you have
				// to create it, so it wont throw an error
				if(i == 0) {
					if(kf.getTick() == 0) { // There is a kf at 0, nice, so we do it as any kf
						currentKf = keyframes.get(i + 1);
						last = kf;
						data.pick(last, currentKf);
					} else { // Oh no, there is no a kf at 0, we have to create it
						Keyframe zero = new Keyframe(0).copyTransformsFrom(kf).toZero();
						currentKf = kf;
						last = zero;
						data.pick(last, currentKf);
					}
				} else { // Select kf as any kf
					currentKf = kf;
					last = keyframes.get(i - 1);
					data.pick(last, currentKf);
				}
				break;
			}
		}
	}
	
	private void pickNewKeyframe() {
		prevKf = false;
		List<Keyframe> keyframes = current.getKeyframes();
		/*LogUtils.log("Animator", "keyframeIndex: " + keyframeIndex + " keyframeIndex + 1: " + 
				(keyframeIndex + 1) + " keyframesSize: " + keyframes.size());*/
		if(!keyframes.isEmpty() && keyframeIndex + 1 < keyframes.size()) {
			if(keyframeIndex == 0 && currentKf == null) { // I have to make null currentKf when update();
				if(keyframes.get(0).getTick() != 0) {
					currentKf = keyframes.get(0);
					Keyframe zero = new Keyframe(0).copyTransformsFrom(currentKf).toZero();
					last = zero;
					data.pick(last, currentKf);
					/*LogUtils.log("Animator", "Zero CurrentKf: " + currentKf.getTick() 
						+ " last: " + last.getTick());
					LogUtils.log("Animator", "Zero Transform: " + zero.toString());*/
					/*data.printCurrentTransforms();
					data.printPrevTransforms();*/
				} else {
					// This is necessary because some parts get losed when they are not used
					last = keyframes.get(keyframeIndex);
					currentKf = keyframes.get(++keyframeIndex);
					data.pick(last, currentKf);
					//LogUtils.log("Animator", "1st CurrentKf Transform: " + currentKf.toString());
					/*data.printCurrentTransforms();
					data.printPrevTransforms();*/
				}
			} else {
				last = keyframes.get(keyframeIndex);
				currentKf = keyframes.get(++keyframeIndex);
				data.pick(last, currentKf);
				/*LogUtils.log("Animator", "CurrentKf: " + currentKf.getTick() + " last: " + last.getTick());
				LogUtils.log("Animator", "2nd CurrentKf Transform: " + currentKf.toString());*/
				/*data.printCurrentTransforms();
				data.printPrevTransforms();*/
			}
		}
	}
	
	private void pickPrevKeyframe() {
		prevKf = true;
		List<Keyframe> keyframes = current.getKeyframes();
		if(!keyframes.isEmpty() && keyframeIndex - 1 >= 0) {
			last = keyframes.get(--keyframeIndex);
			int currentKfIndex = keyframeIndex - 1;
			if(currentKfIndex < 0) {
				if(last.getTick() != 0) {
					currentKf = new Keyframe(0).copyTransformsFrom(last).toZero();
					
					// I do this because if not all goes weird
					List<JgModelPart> partsWithTransforms = new ArrayList<>();
					for(Keyframe kf : current.getKeyframes()) {
						for(JgModelPart part : kf.getTraslations().keySet()) {
							if(!partsWithTransforms.contains(part)) {
								partsWithTransforms.add(part);
								currentKf.traslate(part, 0, 0, 0);
							}
						}
						for(JgModelPart part : kf.getRotations().keySet()) {
							if(!partsWithTransforms.contains(part)) {
								partsWithTransforms.add(part);
								currentKf.rotate(part, 0, 0, 0);
							}
						}
					}
					
					data.pick(last, currentKf);
					/*LogUtils.log("Animator", "Zero CurrentKf: " + currentKf.getTick() + 
							" last: " + last.getTick());
					LogUtils.log("Animator", "Zero: " + currentKf.toString());*/
					/*data.printPrevTransforms();
					data.printCurrentTransforms();*/
				}
			} else {
				currentKf = keyframes.get(keyframeIndex - 1);
				data.pick(last, currentKf);
				
				/*LogUtils.log("Animator", "CurrentKf: " + currentKf.getTick() + " last: " + 
						last.getTick());
				LogUtils.log("Animator", "CurrentKf: " + currentKf.toString());*/
				/*data.printCurrentTransforms();
				data.printPrevTransforms();*/
			}
		}
	}
	
	private void clearAll() {
		data.cleanCurrent();
		data.cleanPrev();
		last = null;
		currentKf = null;
	}
	
	public void play() {
		play = true;
	}
	
	public void stop() {
		play = false;
	}
	
	public Animation getCurrent() {
		return current;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	public float getPrevTick() {
		return prevTick;
	}

	public float getTick() {
		return tick;
	}

	public boolean getMovementType() {
		return forward;
	}
	
	public void setMovementType(boolean forward) {
		this.forward = forward;
	}
	
}
