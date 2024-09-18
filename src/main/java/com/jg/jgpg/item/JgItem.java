package com.jg.jgpg.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JgItem extends Item {

	public JgItem(Properties p_41383_) {
		super(p_41383_);
	}

	public boolean shouldBreakBlocks(ItemStack stack) { return false; }

	public boolean shouldHurt(ItemStack stack) { return false; }

	public boolean isCustomRendered(ItemStack stack) {
		return true;
	}
	
}
