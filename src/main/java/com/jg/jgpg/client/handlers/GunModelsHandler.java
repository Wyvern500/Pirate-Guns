package com.jg.jgpg.client.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.jgpg.client.model.AbstractGunModel;
import com.mojang.logging.LogUtils;

public class GunModelsHandler {

	private static final Map<String, AbstractGunModel> gunModels = new HashMap<>();
	
	public static void register(String gun, AbstractGunModel gunModel) {
		gunModels.put(gun, gunModel);
	}
	
	public static AbstractGunModel get(String gun) {
		if(gunModels.containsKey(gun)) {
			return gunModels.get(gun);
		}else {
			for(Entry<String, AbstractGunModel> entry : gunModels.entrySet()) {
				LogUtils.getLogger().error("Error with: " + entry.getKey());
			}
			return null;
		}
	}
	
	public static Map<String, AbstractGunModel> getModels() {
		return gunModels;
	}
	
}
