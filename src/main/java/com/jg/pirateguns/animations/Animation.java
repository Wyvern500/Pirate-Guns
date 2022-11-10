package com.jg.pirateguns.animations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.handlers.GunModelsHandler;
import com.jg.pirateguns.client.screens.AnimationScreen;
import com.jg.pirateguns.utils.PGMath;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.registries.ForgeRegistries;

public class Animation {
	
	protected String name;
	protected String gunModel;
	protected int dur;
	protected List<Keyframe> keyframes;
	protected int startKeyframeIndex;
	protected int startTick;
	
	public Animation(String name, String gunModel) {
		this.name = name;
		this.gunModel = gunModel;
		this.keyframes = new ArrayList<>();
		this.startKeyframeIndex = -1;
	}
	
	public Animation(String name, String gunModel, int dur) {
		this.name = name;
		this.gunModel = gunModel;
		this.dur = dur;
		this.keyframes = new ArrayList<>();
		this.startKeyframeIndex = -1;
	}
	
	public Animation startKeyframe(int dur) {
		this.startKeyframeIndex++;
		Keyframe kf = new Keyframe(dur);
		kf.startTick = this.startTick;
		kf.startVisualTick = this.startTick + dur;
		this.keyframes.add(kf);
		this.startTick += dur;
		return this;
	}
	
	public Animation startKeyframe(int dur, String easing) {
		this.startKeyframeIndex++;
		Keyframe kf = new Keyframe(dur, easing);
		kf.startTick = this.startTick;
		kf.startVisualTick = this.startTick + dur;
		this.keyframes.add(kf);
		this.startTick += dur;
		return this;
	}
	
	public Animation translate(GunModelPart part, float x, float y, float z) {
		this.keyframes.get(startKeyframeIndex).translations.put(part, 
				new float[] { x, y, z });
		return this;
	}
	
	public Animation rotate(GunModelPart part, float x, float y, float z) {
		this.keyframes.get(startKeyframeIndex).rotations.put(part, 
				new float[] { x, y, z });
		return this;
	}
	
	public Animation end() {
		for(Keyframe kf : keyframes) {
			this.dur += kf.dur;
		}
		return this;
	}

	public int getDuration() {
		return dur;
	}

	public void setDuration(int dur) {
		this.dur = dur;
	}

	public List<Keyframe> getKeyframes() {
		return keyframes;
	}

	public void setKeyframes(List<Keyframe> keyframes) {
		this.keyframes = keyframes;
	}
	
	public String getName() {
		return name;
	}
	
	public String getGunModel() {
		return gunModel;
	}
	
	public void set(Animation anim) {
		this.dur = anim.dur;
		this.startKeyframeIndex = anim.startKeyframeIndex;
		this.startTick = anim.startTick;
		this.keyframes = anim.keyframes;
		this.name = anim.name;
		this.gunModel = anim.gunModel;
	}
	
}
