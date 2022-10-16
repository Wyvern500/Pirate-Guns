package com.jg.pirateguns.client.handlers;

import java.util.HashMap;
import java.util.Map;

import com.jg.pirateguns.animations.parts.GunModel;
import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;

public class GunModelsHandler {

	public static final Map<Item, GunModel> gunModels = new HashMap<>();
	
	public static void register(Item gun, GunModel gunModel) {
		gunModels.putIfAbsent(gun, gunModel);
	}
	
	public static GunModel get(Item gun) {
		if(gunModels.containsKey(gun)) {
			return gunModels.get(gun);
		}else {
			LogUtils.getLogger().error("No gunmodel linked with item: " + gun.getRegistryName().toString());
			return null;
		}
	}
	
}
