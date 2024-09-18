package com.jg.jgpg.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtils {

	public static void setInventory(ItemStack stack, CompoundTag nbt) {
		stack.getOrCreateTag().put(Constants.INVENTORY, nbt);
	}

	public static CompoundTag getInventory(ItemStack stack) {
		return stack.getOrCreateTag().getCompound(Constants.INVENTORY);
	}

	public static String getId(ItemStack stack) {
		return stack.getOrCreateTag().getString(Constants.ID);
	}

	public static void setId(ItemStack stack, String id) {
		stack.getOrCreateTag().putString(Constants.ID, id);
	}

	public static String getBulletPath(ItemStack stack) {
		return stack.getOrCreateTag().getString(Constants.BULLETPATH);
	}

	public static void setBulletPath(ItemStack stack, String magPath) {
		stack.getOrCreateTag().putString(Constants.BULLETPATH, magPath);
	}

	public static int getBullets(ItemStack stack) {
		return stack.getOrCreateTag().getInt(Constants.BULLETS);
	}

	public static void setBullets(ItemStack stack, int bullets) {
		stack.getOrCreateTag().putInt(Constants.BULLETS, bullets);
	}

	public static void setExtraData(ItemStack stack, CompoundTag nbt) {
		stack.getOrCreateTag().put(Constants.EXTRADATA, nbt);
	}

	public static CompoundTag getExtraData(ItemStack stack) {
		return stack.getOrCreateTag().getCompound(Constants.EXTRADATA);
	}

}
