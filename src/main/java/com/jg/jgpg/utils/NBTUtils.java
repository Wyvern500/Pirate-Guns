package com.jg.jgpg.utils;

import com.jg.jgpg.common.item.data.GunDataComponent;
import com.jg.jgpg.registries.DataComponentRegistries;
import net.minecraft.world.item.ItemStack;

public class NBTUtils {

	public static String getId(ItemStack stack) {
		return stack.getOrDefault(DataComponentRegistries.GUN_DATA.get(),
				new GunDataComponent("", 0)).id();
	}

	public static void setId(ItemStack stack, String id) {
		stack.set(DataComponentRegistries.GUN_DATA.get(),
				new GunDataComponent(id, 0));
	}

	public static int getBullets(ItemStack stack) {
		return stack.get(DataComponentRegistries.GUN_DATA).bullets();
	}

	public static void setBullets(ItemStack stack, int bullets) {
		stack.set(DataComponentRegistries.GUN_DATA, stack
				.get(DataComponentRegistries.GUN_DATA).withBullets(bullets));
	}

	public static void setLoaded(ItemStack stack, boolean isLoaded) {
		stack.set(DataComponentRegistries.LOADED, isLoaded);
	}

	public static boolean isLoaded(ItemStack stack) {
		return Boolean.TRUE.equals(stack.get(DataComponentRegistries.LOADED));
	}

	public static void setScope(ItemStack stack, boolean hasScope) {
		stack.set(DataComponentRegistries.SCOPE, hasScope);
	}

	public static boolean hasScope(ItemStack stack) {
		return Boolean.TRUE.equals(stack.get(DataComponentRegistries.SCOPE));
	}

}
