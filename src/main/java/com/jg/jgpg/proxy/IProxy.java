package com.jg.jgpg.proxy;

import net.neoforged.bus.api.IEventBus;

public interface IProxy {

	public void registerModEventListeners(IEventBus bus);

	public void registerNeoForgeEventListeners(IEventBus bus);

}
