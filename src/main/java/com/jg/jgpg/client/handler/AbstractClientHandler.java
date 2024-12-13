package com.jg.jgpg.client.handler;

import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClientHandler {
	
	protected ClientHandler client;
	protected List<AbstractClientHandlerPart<?>> parts;
	
	public AbstractClientHandler(ClientHandler client) {
		this.client = client;
		parts = new ArrayList<>();
	}
	
	public void onTickPre(LocalPlayer player, ItemStack stack, String stackId) {
		for(AbstractClientHandlerPart<?> part : parts) {
			if(part.shouldWork(stack, player)) {
				part.onTickPre(player, stack, stackId);
			}
		}
	}
	
	public void onTickPost(LocalPlayer player, ItemStack stack, String stackId) {
		for(AbstractClientHandlerPart<?> part : parts) {
			if(part.shouldWork(stack, player)) {
				part.onTickPost(player, stack, stackId);
			}
		}
	}
	
	public void addClientHandlerPart(AbstractClientHandlerPart<?> part) {
		parts.add(part);
	}
	
	public void pickModel(ItemStack stack) {
		
	}
	
	public void onSetModel(JgModel<?> model) {
		
	}
	
	public abstract float getProgress(Item item);
	
	public ClientHandler getClient() {
		return client;
	}
	
}
