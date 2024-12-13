package com.jg.jgpg.client.handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractClientHandlerPart<T extends AbstractClientHandler> {

	protected T parent;
	
	public AbstractClientHandlerPart(AbstractClientHandler parent) {
		this.parent = (T) parent;
	}
	
	public abstract void onTickPre(LocalPlayer player, ItemStack stack, String stackId);
	
	public abstract void onTickPost(LocalPlayer player, ItemStack stack, String stackId);
	
	public abstract float getProgress(Item item);

	public abstract boolean shouldWork(ItemStack stack, LocalPlayer player);
	
}
