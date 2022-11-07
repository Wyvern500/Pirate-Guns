package com.jg.pirateguns.utils;

import java.util.List;

import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.mojang.logging.LogUtils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class Utils {
	
	public static void spawnParticlesOnPlayerView(Player player, int amount, float ox, float oy, float oz) {
		Vec3 view = player.getViewVector(1.0f);
		if(amount <= 0) {
			amount = 50;
		}
		for(int i = 0; i < amount; i++) {
			player.level.addParticle(ParticleTypes.SMOKE, (player.getX()+view.x)+ox, 
					(player.getEyeY())+oy, 
					(player.getZ()+view.z)+oz, view.x*0.1f, 0, 0);
		}
	}
	
	public static GunModelPart getGunPartByName(GunModel model, String name) {
		for(GunModelPart part : model.getGunParts()) {
			if(part.getName().equals(name)) {
				return part;
			}
		}
		return null;
	}
	
	public static <T> void insertInto(int index, List<T> list, T object) {
		if(index < list.size()-1) {
			T next = null;
			T temp = null;
			LogUtils.getLogger().info("index: " + index + " list size-1: " + (list.size()-1));
			for (int i = index; i < list.size() - 1; i++) {
				LogUtils.getLogger().info("i: " + i);
				if (temp == null) {
					temp = list.get(i + 1);
					list.set(i + 1, list.get(i));
				} else {
					next = list.get(i + 1);
					list.set(i + 1, temp);
					temp = next;
				}
			}
			list.add(temp);
			list.set(index, object);
			LogUtils.getLogger().info("temp == null: " + (temp == null) + " next == null: " 
					+ (next == null));
		} else if(list.size()-1 == 1){
			list.add(list.get(1));
			list.set(1, object);
			LogUtils.getLogger().info("2");
		} else if(list.size()-1 == 0) {
			list.add(list.get(0));
			list.set(0, object);
			LogUtils.getLogger().info("3");
		} else if(list.size()-1 == index){
			list.add(list.get(list.size()-1));
			list.set(list.size()-2, object);
			LogUtils.getLogger().info("index: " + index + " list size-1: " + (list.size()-1));
		} else {
			LogUtils.getLogger().info("index: " + index + " list size-1: " + (list.size()-1));
		}
		//list.add(temp);
		//list.set(index, object);
	}
	
	public static void updateKeyframesFromAnimation(Animation animation) {
		int dur = 0;
		for(int i = 0;i < animation.getKeyframes().size(); i++) {
			Keyframe kf = animation.getKeyframes().get(i);
			if(kf == null)continue;
			kf.startTick = dur;
			kf.startVisualTick = kf.startTick + kf.dur;
			dur += kf.dur;
		}
		animation.setDuration(dur);
	}
	
}
