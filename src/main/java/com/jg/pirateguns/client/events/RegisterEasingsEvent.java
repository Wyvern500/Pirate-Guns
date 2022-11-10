package com.jg.pirateguns.client.events;

import com.jg.pirateguns.animations.Animator;
import com.jg.pirateguns.animations.Animator.Easing;

import net.minecraftforge.eventbus.api.Event;

public class RegisterEasingsEvent extends Event {

	public RegisterEasingsEvent() {
		
	}

	public void register(String name, Easing easing) {
		Animator.easings.put(name, easing);
	}
	
	public Easing getEasing(String name) {
		return Animator.easings.get(name);
	}
	
}
