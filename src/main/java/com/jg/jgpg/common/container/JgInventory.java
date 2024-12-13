package com.jg.jgpg.common.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Predicate;

public class JgInventory implements Container {

	public final ItemStackHandler handler;
	public final Predicate<Player> canAcces;
	
	public JgInventory(int size, Predicate<Player> canAcces) {
		this.handler = new ItemStackHandler(size);
		this.canAcces = canAcces;
	} 
	
	@Override
	public void clearContent() {
		for (int i = 0; i < handler.getSlots(); ++i) {
			handler.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public int getContainerSize() {
		return handler.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < handler.getSlots(); ++i) {
			if (!handler.getStackInSlot(i).isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return handler.getStackInSlot(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		if (count < 0)
			throw new IllegalArgumentException("count should be >= 0:" + count);
		return handler.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		int maxPossibleItemStackSize = handler.getSlotLimit(index);
		return handler.extractItem(index, maxPossibleItemStackSize, false);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		handler.setStackInSlot(index, stack);
	}

	@Override
	public void setChanged() {
		
	}

	@Override
	public boolean stillValid(Player player) {
		return canAcces.test(player);
	}

}
