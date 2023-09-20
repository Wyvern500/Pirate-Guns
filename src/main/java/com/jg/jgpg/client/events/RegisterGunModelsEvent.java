package com.jg.jgpg.client.events;

import java.util.Map;

import com.jg.jgpg.client.handlers.GunModelsHandler;
import com.jg.jgpg.client.model.AbstractJgModel;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;

public class RegisterGunModelsEvent extends Event {
	
	public RegisterGunModelsEvent() {
		
	}
	
	public void register(Item item, AbstractJgModel model) {
		GunModelsHandler.register(ForgeRegistries.ITEMS.getKey(item).toString(), model);
	}
	
	public void get(Item item) {
		GunModelsHandler.get(ForgeRegistries.ITEMS.getKey(item).toString());
	}
	
	public Map<String, AbstractJgModel> getModels() {
		return GunModelsHandler.getModels();
	}
	
}
