package com.jg.jgpg.client.handler.handlers;

import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.mojang.logging.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class JgModelsHandler {

	private static final Map<String, Supplier<JgModel<?>>> suppliers = new HashMap<>();

	public static void register(String gun, Supplier<JgModel<?>> gunModel) {
		suppliers.put(gun, gunModel);
	}
	
	public static Supplier<JgModel<?>> get(String gun) {
		if(suppliers.containsKey(gun)) {
			return suppliers.get(gun);
		}else {
			for(Entry<String, Supplier<JgModel<?>>> entry : suppliers.entrySet()) {
				LogUtils.getLogger().error("Error with: " + entry.getKey());
			}
			return null;
		}
	}
	
	public static Map<String, Supplier<JgModel<?>>> getModels() {
		return suppliers;
	}

	public static class PlayerData {

	}

}
