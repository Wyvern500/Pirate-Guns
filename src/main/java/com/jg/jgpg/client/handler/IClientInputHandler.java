package com.jg.jgpg.client.handler;

import net.neoforged.neoforge.client.event.InputEvent;

public interface IClientInputHandler {

	public void handleMouse(InputEvent.MouseButton e);
	
	public void handleKeyboard(InputEvent.Key e);
	
}
