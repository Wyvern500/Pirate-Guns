package com.jg.jgpg.client.handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;

public abstract class AbstractHandler {

	protected ClientHandler client;
	
	public AbstractHandler(ClientHandler client) {
		this.client = client;
	}
	
	public abstract void tick(LocalPlayer player);
	
	public abstract float getProgress(Item item);
	
	public ClientHandler getClient() {
		return client;
	}
	
}
