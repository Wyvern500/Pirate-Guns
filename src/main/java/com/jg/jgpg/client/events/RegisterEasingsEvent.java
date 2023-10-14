package com.jg.jgpg.client.events;

import com.jg.jgpg.client.animations.Easing;
import com.jg.jgpg.client.handlers.EasingHandler;

import net.minecraftforge.eventbus.api.Event;

public class RegisterEasingsEvent extends Event {

	public RegisterEasingsEvent() {
		
	}

	public void register(String name, Easing easing) {
		EasingHandler.INSTANCE.register(name, easing);
	}
	
	public Easing getEasing(String name) {
		return EasingHandler.INSTANCE.getEasing(name);
	}
	
}
