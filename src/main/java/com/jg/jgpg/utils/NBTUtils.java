package com.jg.jgpg.utils;

import net.minecraft.world.item.ItemStack;

public class NBTUtils {
	
	public static String getId(ItemStack stack) {
		return stack.getOrCreateTag().getString(Constants.ID);
	}
	
	public static void setId(ItemStack stack, String id) {
		stack.getOrCreateTag().putString(Constants.ID, id);
	}
	
	public static boolean isLoaded(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean(Constants.BULLET_LOADED);
	}
	
	public static void setLoaded(ItemStack stack, boolean loaded) {
		stack.getOrCreateTag().putBoolean(Constants.BULLET_LOADED, loaded);
	}
	
	public static int getBullets(ItemStack stack) {
		return stack.getOrCreateTag().getInt(Constants.BULLETS);
	}
	
	public static void setBullets(ItemStack stack, int bullets) {
		stack.getOrCreateTag().putInt(Constants.BULLETS, bullets);
	}
	
	public static boolean hasScope(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean(Constants.HASSCOPE);
	}
	
	public static void setHasScope(ItemStack stack, boolean hasScope) {
		stack.getOrCreateTag().putBoolean(Constants.HASSCOPE, hasScope);
	}
	
}
