package com.jg.jgpg.proxy;

import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import net.neoforged.bus.api.IEventBus;

public class ClientProxy implements IProxy {

	@Override
	public void registerModEventListeners(IEventBus bus) {
		ClientEventHandler.registerModEventListeners(bus);
	}

	@Override
	public void registerNeoForgeEventListeners(IEventBus bus) {
		ClientEventHandler.registerNeoForgeEventListeners(bus);
	}

}
