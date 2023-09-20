package com.jg.jgpg.client.handlers;

import com.jg.jgpg.client.model.AbstractJgModel;
import com.mojang.logging.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GunModelsHandler {

	private static final Map<String, AbstractJgModel> gunModels = new HashMap<>();
	
	public static void register(String gun, AbstractJgModel gunModel) {
		gunModels.put(gun, gunModel);
	}
	
	public static AbstractJgModel get(String gun) {
		if(gunModels.containsKey(gun)) {
			return gunModels.get(gun);
		}else {
			for(Entry<String, AbstractJgModel> entry : gunModels.entrySet()) {
				LogUtils.getLogger().error("Error with: " + entry.getKey());
			}
			return null;
		}
	}
	
	public static Map<String, AbstractJgModel> getModels() {
		return gunModels;
	}
	
}
