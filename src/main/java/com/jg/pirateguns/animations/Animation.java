package com.jg.pirateguns.animations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.handlers.GunModelsHandler;
import com.jg.pirateguns.client.screens.AnimationScreen;
import com.jg.pirateguns.utils.PGMath;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.registries.ForgeRegistries;

public class Animation {

	public static final Animation EMPTY = new Animation("", "empty", new Keyframe(0), new Keyframe(0));
	
	protected GunModel model;
	
	protected Keyframe[] keyframes;
	protected String name;
	protected Keyframe prevFrame;
	protected Keyframe currentFrame;
	protected Keyframe outFrame;
	
	protected float prev, prog;
	protected float MAX;
	
	protected String gunModelItem;

	protected int current;
	protected int tick;
	protected int dur;
	
	protected boolean finished;
	
	public Animation(String gunModelItem, String name, Keyframe... keyframes) {
		this.gunModelItem = gunModelItem;
		this.name = name;
		assert keyframes.length >= 1;
		if(gunModelItem != null) {
			this.model = GunModelsHandler.get(ForgeRegistries.ITEMS.getValue(
					new ResourceLocation(gunModelItem)));
			System.out.println("out");
			if(this.model != null) {
				System.out.println("not null");
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
				this.prevFrame = this.keyframes[0].copy();
				this.currentFrame = this.keyframes[1].copy();
				this.outFrame = new Keyframe(0);
				this.MAX = currentFrame.dur;
			} else {
				LogUtils.getLogger().info("Model for " + name + " is " + gunModelItem);
			}
		} 
	}
	
	public void tick() {
		if(model == null)return;
		prev = prog;
		if(model.isDebugModeEnabled()) {
			for(GunModelPart part : model.getGunParts()) {
				part.getTransform().setPos(outFrame.getPos(part.getName()));
				part.getTransform().setRot(outFrame.getRot(part.getName()));
			}
		}
		if (prog < MAX) {
			if(model.canPlayAnimation()) {
				prog += ClientHandler.partialTicks;
				if (prog > MAX) {
					prog = MAX;
				}
				tick = currentFrame.startTick+(int)Mth.floor(prog);
			}
			for(GunModelPart part : model.getGunParts()) {
				part.getTransform().setPos(outFrame.getPos(part.getName()));
				part.getTransform().setRot(outFrame.getRot(part.getName()));
			}
		} else {
			if(current < keyframes.length-1 && model.canPlayAnimation()) {
				nextKeyframe();
			} else if(model.canPlayAnimation()){
				finished = true;
				if(model != null) {
					model.setAnimation(EMPTY);
				}
				Screen screen = Minecraft.getInstance().screen;
				if(screen instanceof AnimationScreen) {
					((AnimationScreen)screen).getPosList().cleanKeys();
					((AnimationScreen)screen).getPosList().cleanKeys();
				}
				onFinish();
			}
		}
		onTick();
		if(!finished) {
			if(!model.isDebugModeEnabled() && model.canPlayAnimation()) {
				System.out.println("first");
				for(Entry<String, float[]> entry : prevFrame.pos.entrySet()) {
					String id = entry.getKey();
					float[] values = new float[] { 0, 0, 0 };
					values[0] = Mth.lerp(getProgress(), 
							prevFrame.getPos(id)[0], currentFrame.getPos(id)[0]);
					values[1] = Mth.lerp(getProgress(), 
							prevFrame.getPos(id)[1], currentFrame.getPos(id)[1]);
					values[2] = Mth.lerp(getProgress(), 
							prevFrame.getPos(id)[2], currentFrame.getPos(id)[2]);
					outFrame.setPos(id, values);
				}
				for(Entry<String, float[]> entry : prevFrame.rot.entrySet()) {
					String id = entry.getKey();
					float[] values = new float[] { 0, 0, 0 };
					values[0] = PGMath.rotLerp(getProgress(), 
							prevFrame.getRot(id)[0], currentFrame.getRot(id)[0]);
					values[1] = PGMath.rotLerp(getProgress(), 
							prevFrame.getRot(id)[1], currentFrame.getRot(id)[1]);
					values[2] = PGMath.rotLerp(getProgress(), 
							prevFrame.getRot(id)[2], currentFrame.getRot(id)[2]);
					outFrame.setRot(id, values);
				}
			} else {
				for(Entry<String, float[]> entry : prevFrame.pos.entrySet()) {
					String id = entry.getKey();
					float[] values = new float[] { 0, 0, 0 };
					values[0] = currentFrame.getPos(id)[0];
					values[1] = currentFrame.getPos(id)[1];
					values[2] = currentFrame.getPos(id)[2];
					for(Entry<String, float[]> entry2 : currentFrame.pos.entrySet()) {
						//System.out.println(Arrays.toString(entry2.getValue()));
					}
					outFrame.setPos(id, values);
				}
				for(Entry<String, float[]> entry : prevFrame.rot.entrySet()) {
					String id = entry.getKey();
					float[] values = new float[] { 0, 0, 0 };
					values[0] = currentFrame.getRot(id)[0];
					values[1] = currentFrame.getRot(id)[1];
					values[2] = currentFrame.getRot(id)[2];
					outFrame.setRot(id, values);
				}
			}
		}
	}
	
	public void calculateDur() {
		for(Keyframe keyframe : keyframes) {
			dur += keyframe.dur;
		}
		
	}
	
	public void nextDebugKeyframe() {
		current++;
		MAX = keyframes[current].dur;
		prev = prog = MAX;
		this.currentFrame = keyframes[current].copy();
		this.prevFrame = keyframes[current-1].copy();
		LogUtils.getLogger().info("Current++");
		onStartKeyframe();
	}
	
	public void nextKeyframe() {
		current++;
		MAX = keyframes[current].dur;
		prev = prog = 0;
		this.currentFrame = keyframes[current].copy();
		this.prevFrame = keyframes[current-1].copy();
		LogUtils.getLogger().info("Current++");
		onStartKeyframe();
	}
	
	public void prevDebugKeyframe() {
		current--;
		if(current < 0) {
			current = 1;
			MAX = keyframes[current].dur;
			prev = prog = MAX;
			this.currentFrame = keyframes[current].copy();
			this.prevFrame = keyframes[current-1].copy();
			LogUtils.getLogger().info("Current--");
			onStartKeyframe();
		}
	}
	
	public void prevKeyframe() {
		current--;
		if(current < 0) {
			current = 1;
			MAX = keyframes[current].dur;
			prev = prog = 0;
			this.currentFrame = keyframes[current].copy();
			this.prevFrame = keyframes[current-1].copy();
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
		this.prevFrame = keyframes[0].copy();
		this.currentFrame = keyframes[1].copy();
		this.outFrame = new Keyframe(0);
		this.MAX = currentFrame.dur;
		finished = false;
	}

	public GunModel getModel() {
		return model;
	}

	public void setModel(GunModel model) {
		this.model = model;
	}

	public Keyframe[] getKeyframes() {
		return keyframes;
	}

	public void setKeyframes(Keyframe[] keyframes) {
		this.keyframes = keyframes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Keyframe getPrevFrame() {
		return prevFrame;
	}

	public void setPrevFrame(Keyframe prevFrame) {
		this.prevFrame = prevFrame;
	}

	public Keyframe getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(Keyframe currentFrame) {
		this.currentFrame = currentFrame;
	}

	public Keyframe getOutFrame() {
		return outFrame;
	}

	public void setOutFrame(Keyframe outFrame) {
		this.outFrame = outFrame;
	}

	public float getPrev() {
		return prev;
	}

	public void setPrev(float prev) {
		this.prev = prev;
	}

	public float getProg() {
		return prog;
	}

	public void setProg(float prog) {
		this.prog = prog;
	}

	public float getMAX() {
		return MAX;
	}

	public void setMAX(float mAX) {
		MAX = mAX;
	}

	public String getGunModelItem() {
		return gunModelItem;
	}

	public void setGunModelItem(String gunModelItem) {
		this.gunModelItem = gunModelItem;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public int getDuration() {
		return dur;
	}

	public void setDuration(int dur) {
		this.dur = dur;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public Map<String, float[]> getPos(){
		return outFrame.pos;
	}
	
	public Map<String, float[]> getRot(){
		return outFrame.rot;
	}
	
}
