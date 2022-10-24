package com.jg.pirateguns.client.events;

import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.client.handlers.GunModelsHandler;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;

public class RegisterGunModelsEvent extends Event {
	
	public RegisterGunModelsEvent() {
		
	}
	
	public void register(Item item, GunModel model) {
		GunModelsHandler.register(item, model);
	}
	
	public void getModel(Item item) {
		GunModelsHandler.get(item);
	}
	
}
