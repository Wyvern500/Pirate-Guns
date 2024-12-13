package com.jg.jgpg.client.event;

import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handler.handlers.JgModelsHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;

import java.util.Map;
import java.util.function.Supplier;

public class RegisterJgModelsEvent extends Event {

	AbstractClientHandler client;
	
	public void register(Item item, Supplier<JgModel<?>> sup) {
		LogUtils.log("RegisterJgModelsEvent", "Item: " + (item == null ?
				"Motherfucker this is null" : item.getDescriptionId()));
		String id = BuiltInRegistries.ITEM.getKey(item).toString();
		JgModelsHandler.register(id, sup);
	}

	public void setCustomClient(AbstractClientHandler client) {
		this.client = client;
	}

	public void register(Item item, IJgModel factory) {
		String id = BuiltInRegistries.ITEM.getKey(item).toString();
		JgModelsHandler.register(id, () -> factory.create(client, item));
	}
	
	public void get(Item item) {
		JgModelsHandler.get(BuiltInRegistries.ITEM.getKey(item).toString());
	}
	
	public Map<String, Supplier<JgModel<?>>> getModels() {
		return JgModelsHandler.getModels();
	}

	public interface IJgModel {
		JgModel<?> create(AbstractClientHandler client, Item item);
	}

}
