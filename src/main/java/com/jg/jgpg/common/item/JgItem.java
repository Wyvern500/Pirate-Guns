package com.jg.jgpg.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JgItem extends Item {

	public JgItem(Properties p_41383_) {
		super(p_41383_);
	}

	@Override
	public ItemStack getDefaultInstance() {
		return super.getDefaultInstance();
	}

	public boolean shouldBreakBlocks(ItemStack stack) { return false; }

	public boolean shouldHurt(ItemStack stack) { return false; }

	public boolean isCustomRendered(ItemStack stack) {
		return true;
	}
	
}
