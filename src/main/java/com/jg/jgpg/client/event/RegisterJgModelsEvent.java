package com.jg.jgpg.client.event;

import com.jg.jgpg.client.handlers.JgModelsHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

public class RegisterJgModelsEvent extends Event {
	
	public RegisterJgModelsEvent() {
		
	}
	
	public void register(Item item, Supplier<JgModel<?>> sup) {
		JgModel<?> model = sup.get();
		model.setItem(item);
		LogUtils.log("RegisterJgModelsEvent", "Item: " + (item == null ?
				"Motherfucker this is null" : item.getDescriptionId()));
		String id = ForgeRegistries.ITEMS.getKey(item).toString();
		JgModelsHandler.register(id, model);
		JgModelsHandler.registerSupplier(id, sup);
	}
	
	public void get(Item item) {
		JgModelsHandler.get(ForgeRegistries.ITEMS.getKey(item).toString());
	}
	
	public Map<String, JgModel<?>> getModels() {
		return JgModelsHandler.getModels();
	}
	
}
