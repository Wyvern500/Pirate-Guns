package com.jg.jgpg.client.handlers;

import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.mojang.logging.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class JgModelsHandler {

	private static final Map<String, JgModel<?>> jgModels = new HashMap<>();
	private static final Map<String, Supplier<JgModel<?>>> suppliers = new HashMap<>();

	public static void register(String gun, JgModel<?> gunModel) {
		jgModels.put(gun, gunModel);
	}

	public static void registerSupplier(String gun, Supplier<JgModel<?>> gunModel) {
		suppliers.put(gun, gunModel);
	}
	
	public static JgModel<?> get(String gun) {
		if(jgModels.containsKey(gun)) {
			return jgModels.get(gun);
		}else {
			for(Entry<String, JgModel<?>> entry : jgModels.entrySet()) {
				LogUtils.getLogger().error("Error with: " + entry.getKey());
			}
			return null;
		}
	}

	public static Supplier<JgModel<?>> getSupplier(String gun) {
		if(suppliers.containsKey(gun)) {
			return suppliers.get(gun);
		}else {
			for(Entry<String, Supplier<JgModel<?>>> entry : suppliers.entrySet()) {
				LogUtils.getLogger().error("Error with: " + entry.getKey());
			}
			return null;
		}
	}
	
	public static Map<String, JgModel<?>> getModels() {
		return jgModels;
	}

	public static class PlayerData {

	}

}
