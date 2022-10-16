package com.jg.pirateguns.utils;

import net.minecraft.world.item.ItemStack;

public class NBTUtils {

	public static void setLoaded(ItemStack stack, boolean loaded) {
		stack.getOrCreateTag().putBoolean(Constants.loaded, loaded);
	}
	
	public static boolean getLoaded(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean(Constants.loaded);
	}
	
}
