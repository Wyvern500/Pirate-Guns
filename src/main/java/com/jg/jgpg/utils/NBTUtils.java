package com.jg.jgpg.utils;

import net.minecraft.world.item.ItemStack;

public class NBTUtils {
	
	public static String getId(ItemStack stack) {
		return stack.getOrCreateTag().getString(Constants.ID);
	}
	
	public static void setId(ItemStack stack, String id) {
		stack.getOrCreateTag().putString(Constants.ID, id);
	}
	
}
