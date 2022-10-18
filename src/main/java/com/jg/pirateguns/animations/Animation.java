package com.jg.pirateguns.animations;

import java.util.Map;
import java.util.Map.Entry;

import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.utils.PGMath;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;

public class Animation {

	public static final Animation EMPTY = new Animation(null, "empty", new Keyframe(0), new Keyframe(0));
	
	protected GunModel model;
	
	protected Keyframe[] keyframes;
	protected String name;
	protected Keyframe prevFrame;
	protected Keyframe currentFrame;
	protected Keyframe outFrame;
	
	protected float prev, prog;
	protected float MAX;
	
	protected int current;
	protected int tick;
	protected int dur;
	
	protected boolean finished;
	
	public Animation(GunModel model, String name, Keyframe... keyframes) {
		this.model = model;
		this.name = name;
		assert keyframes.length >= 1;
		if(model != null) {
			//LogUtils.getLogger().info("Model is null for animation: " + name);
			this.keyframes = new Keyframe[keyframes.length+1];
			Keyframe cero = new Keyframe(0);
			for(GunModelPart part : model.getGunParts()) {
				cero.addPos(part.getName(), part.getTransform().pos);
				cero.addRot(part.getName(), part.getTransform().rot);
			}
			this.keyframes[0] = cero;
			int totalDur = 0;
			for(int i = 1; i < this.keyframes.length; i++) {
				this.keyframes[i] = keyframes[i-1];
				totalDur += this.keyframes[i].dur;
				this.keyframes[i].startTick = totalDur;
			}
			calculateDur();
			this.prevFrame = this.keyframes[0];
			this.currentFrame = this.keyframes[1];
			this.outFrame = cero;
			this.MAX = currentFrame.dur;
		}
	}
	
	public void tick() {
		if(model == null)return;
		prev = prog;
		/*LogUtils.getLogger().info("Prog: " + prog + " Prev: " + prev + " Current: " +
		current + " keyframes length-1: " + (keyframes.length-1));*/
		if (prog < MAX) {
			if(model.canPlayAnimation()) {
				prog += ClientHandler.partialTicks;
				if (prog > MAX) {
					prog = MAX;
				}
				tick = currentFrame.startTick+(int)Mth.floor(prog);
				for(GunModelPart part : model.getGunParts()) {
					part.getTransform().setPos(outFrame.getPos(part.getName()));
					part.getTransform().setRot(outFrame.getRot(part.getName()));
				}
			}
		}else {
			if(current < keyframes.length-1) {
				nextKeyframe();
			} else {
				finished = true;
				if(model != null) {
					model.setAnimation(EMPTY);
				}
				onFinish();
			}
		}
		onTick();
		if(!finished) {
			if(!model.isDebugModeEnabled()) {
				for(Entry<String, float[]> entry : prevFrame.pos.entrySet()) {
					String id = entry.getKey();
					float[] values = outFrame.getPos(id);
					values[0] = Mth.lerp(getProgress(), 
							prevFrame.getPos(id)[0], currentFrame.getPos(id)[0]);
					values[1] = Mth.lerp(getProgress(), 
							prevFrame.getPos(id)[1], currentFrame.getPos(id)[1]);
					values[2] = Mth.lerp(getProgress(), 
							prevFrame.getPos(id)[2], currentFrame.getPos(id)[2]);
					outFrame.setPos(id, values);
					LogUtils.getLogger().info("Current: " + current + " PrevFrame values: x: " 
							+ prevFrame.getPos(id)[0] + " y: " + prevFrame.getPos(id)[1] 
							+ " z: " + prevFrame.getPos(id)[2] + " currentFrame: x: " 
							+ currentFrame.getPos(id)[0] + " y: " + currentFrame.getPos(id)[1] 
									+ " z: " + currentFrame.getPos(id)[2] 
					+ " outFrame: x: " + outFrame.getPos(id)[0] + " y: " + outFrame.getPos(id)[1] 
							+ " z: " + outFrame.getPos(id)[2]);
				}
				for(Entry<String, float[]> entry : prevFrame.rot.entrySet()) {
					String id = entry.getKey();
					float[] values = outFrame.getRot(id);
					values[0] = PGMath.rotLerp(getProgress(), 
							prevFrame.getRot(id)[0], currentFrame.getRot(id)[0]);
					values[1] = PGMath.rotLerp(getProgress(), 
							prevFrame.getRot(id)[1], currentFrame.getRot(id)[1]);
					values[2] = PGMath.rotLerp(getProgress(), 
							prevFrame.getRot(id)[2], currentFrame.getRot(id)[2]);
					outFrame.setRot(id, values);
					LogUtils.getLogger().info("Current: " + current + " PrevFrame values: x: " 
							+ prevFrame.getRot(id)[0] + " y: " + prevFrame.getRot(id)[1] 
							+ " z: " + prevFrame.getRot(id)[2] + " currentFrame: x: " 
							+ currentFrame.getRot(id)[0] + " y: " + currentFrame.getRot(id)[1] 
									+ " z: " + currentFrame.getRot(id)[2] 
					+ " outFrame: x: " + outFrame.getRot(id)[0] + " y: " + outFrame.getRot(id)[1] 
							+ " z: " + outFrame.getRot(id)[2]);
				}
			} else {
				for(Entry<String, float[]> entry : prevFrame.pos.entrySet()) {
					String id = entry.getKey();
					float[] values = outFrame.getPos(id);
					values[0] = currentFrame.getPos(id)[0];
					values[1] = currentFrame.getPos(id)[1];
					values[2] = currentFrame.getPos(id)[2];
					outFrame.setPos(id, values);
					LogUtils.getLogger().info("Current: " + current + " PrevFrame values: x: " 
							+ prevFrame.getPos(id)[0] + " y: " + prevFrame.getPos(id)[1] 
							+ " z: " + prevFrame.getPos(id)[2] + " currentFrame: x: " 
							+ currentFrame.getPos(id)[0] + " y: " + currentFrame.getPos(id)[1] 
									+ " z: " + currentFrame.getPos(id)[2] 
					+ " outFrame: x: " + outFrame.getPos(id)[0] + " y: " + outFrame.getPos(id)[1] 
							+ " z: " + outFrame.getPos(id)[2]);
				}
				for(Entry<String, float[]> entry : prevFrame.rot.entrySet()) {
					String id = entry.getKey();
					float[] values = outFrame.getRot(id);
					values[0] = currentFrame.getRot(id)[0];
					values[1] = currentFrame.getRot(id)[1];
					values[2] = currentFrame.getRot(id)[2];
					outFrame.setRot(id, values);
					LogUtils.getLogger().info("Current: " + current + " PrevFrame values: x: " 
							+ prevFrame.getRot(id)[0] + " y: " + prevFrame.getRot(id)[1] 
							+ " z: " + prevFrame.getRot(id)[2] + " currentFrame: x: " 
							+ currentFrame.getRot(id)[0] + " y: " + currentFrame.getRot(id)[1] 
									+ " z: " + currentFrame.getRot(id)[2] 
					+ " outFrame: x: " + outFrame.getRot(id)[0] + " y: " + outFrame.getRot(id)[1] 
							+ " z: " + outFrame.getRot(id)[2]);
				}
			}
		}
	}
	
	public void calculateDur() {
		for(Keyframe keyframe : keyframes) {
			dur += keyframe.dur;
		}
	}
	
	public void nextKeyframe() {
		current++;
		MAX = keyframes[current].dur;
		prev = prog = 0;
		this.currentFrame = keyframes[current];
		this.prevFrame = keyframes[current-1];
		LogUtils.getLogger().info("Current++");
		onStartKeyframe();
	}
	
	public void prevKeyframe() {
		current--;
		if(current < 0) {
			current = 1;
			MAX = keyframes[current].dur;
			prev = prog = 0;
			this.currentFrame = keyframes[current];
			this.prevFrame = keyframes[current-1];
			LogUtils.getLogger().info("Current--");
			onStartKeyframe();
		}
	}
	
	public void onTick() {
		
	}
	
	public void onStart() {
		
	}
	
	public void onStartKeyframe() {
		
	}
	
	public void onFinish() {
		
	}
	
	private float getProgress() {
		return (prev + (prog - prev)
				* (prev == 0 || 
				prev == MAX ? 0 : 
				ClientHandler.partialTicks)) / MAX;
	}
	
	public void reset() {
		if(model == null)return;
		this.prog = 0;
		this.prev = 0;
		this.current = 1;
		this.prevFrame = keyframes[0];
		this.currentFrame = keyframes[1];
		this.outFrame = new Keyframe(0);
		this.MAX = currentFrame.dur;
		finished = false;
	}
	
	public Map<String, float[]> getPos(){
		return outFrame.pos;
	}
	
	public Map<String, float[]> getRot(){
		return outFrame.rot;
	}
	
}
