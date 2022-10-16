package com.jg.pirateguns.animations;

import java.util.Map;
import java.util.Map.Entry;

import com.jg.pirateguns.client.handlers.ClientHandler;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;

public class Animation {

	public static final Animation EMPTY = new Animation("empty", new Keyframe(0), new Keyframe(0));
	
	private Keyframe[] keyframes;
	private String name;
	private Keyframe prevFrame;
	private Keyframe currentFrame;
	private Keyframe outFrame;
	
	private float prev, prog;
	private float MAX;
	
	private int current;
	
	private boolean finished;
	
	public Animation(String name, Keyframe... keyframes) {
		this.name = name;
		this.keyframes = keyframes;
		assert keyframes.length >= 2;
		this.prevFrame = keyframes[0];
		this.currentFrame = keyframes[1];
		this.outFrame = new Keyframe(0);
		this.outFrame.copyValues(this.prevFrame);
		this.MAX = currentFrame.dur;
		this.current = 1;
		onStart();
	}
	
	public void tick() {
		prev = prog;
		if (prog < MAX) {
			prog += ClientHandler.partialTicks;
			if (prog > MAX) {
				prog = MAX;
			}
		}else {
			if(current < keyframes.length-1) {
				current++;
				MAX = keyframes[current].dur;
				prev = prog = 0;
				this.currentFrame = keyframes[current];
				this.prevFrame = keyframes[current-1];
				LogUtils.getLogger().info("Current++");
				onStartKeyframe();
			} else {
				finished = true;
				onFinish();
			}
		}
		onTick();
		if(!finished) {
			for(Entry<String, float[]> entry : prevFrame.values.entrySet()) {
				String id = entry.getKey();
				float[] values = outFrame.getValue(id);
				values[0] = Mth.lerp(getProgress(), 
						prevFrame.getValue(id)[0], currentFrame.getValue(id)[0]);
				values[1] = Mth.lerp(getProgress(), 
						prevFrame.getValue(id)[1], currentFrame.getValue(id)[1]);
				values[2] = Mth.lerp(getProgress(), 
						prevFrame.getValue(id)[2], currentFrame.getValue(id)[2]);
				outFrame.setValues(id, values);
				LogUtils.getLogger().info("Current: " + current + " PrevFrame values: x: " 
						+ prevFrame.getValue(id)[0] + " y: " + prevFrame.getValue(id)[1] 
						+ " z: " + prevFrame.getValue(id)[2] + " currentFrame: x: " 
						+ currentFrame.getValue(id)[0] + " y: " + currentFrame.getValue(id)[1] 
								+ " z: " + currentFrame.getValue(id)[2] 
				+ " outFrame: x: " + outFrame.getValue(id)[0] + " y: " + outFrame.getValue(id)[1] 
						+ " z: " + outFrame.getValue(id)[2]);
			}
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
	
	public Map<String, float[]> getValues(){
		return outFrame.values;
	}
	
}
