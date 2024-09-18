package com.jg.jgpg.client.handler;

import net.minecraftforge.client.event.InputEvent;

public interface IClientInputHandler {

	public void handleMouse(InputEvent.MouseButton e);
	
	public void handleKeyboard(InputEvent.Key e);
	
}
