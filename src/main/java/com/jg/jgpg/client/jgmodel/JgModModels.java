package com.jg.jgpg.client.jgmodel;

import com.jg.jgpg.common.item.JgItem;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class JgModModels {

	public static Map<JgItem, Supplier<ToShowModModel>> models = new HashMap<>();
	
	public JgModModels() {
		
	}
	
	public static ToShowModModel getModel(Item item, BakedModel origin) {
		return models.get((JgItem) item).get();
	}
	
	public static void addModel(Item item, Supplier<ToShowModModel> model) {
		models.put((JgItem) item, model);
	}
	
}
