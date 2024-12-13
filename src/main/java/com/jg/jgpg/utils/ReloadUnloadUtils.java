package com.jg.jgpg.utils;

import com.jg.jgpg.common.item.JgGunItem;
import com.jg.jgpg.common.network.Networking;
import com.jg.jgpg.common.network.SetBulletsMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class ReloadUnloadUtils {
	
	public static boolean processReloadIngredients(JgGunItem.ReloadIngredients ingredients) {
		
		boolean isEverythingRight = true;
		
		int[] data = InventoryUtils.getDataForItems(ingredients.getAsItemStackList()); 
		
		LogUtils.log("ReloadUnloadUtils", "Data: " + Arrays.toString(data));
		
		for(int i = 0; i < data.length; i += 2) {
			if(data[i] == -1) {
				return false;
			}
		}
		
		InventoryUtils.consumeData(data);
		
		return isEverythingRight;
	}
	
	public static void setBullets(int bullets) {
		ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
		Networking.sendToServer(new SetBulletsMessage(bullets));
	}
	
	public static void addBullet() {
		ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
		Networking.sendToServer(new SetBulletsMessage(NBTUtils.getBullets(stack) + 1));
	}
	
	public static void substractBullet() {
		ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
		Networking.sendToServer(new SetBulletsMessage(NBTUtils.getBullets(stack) - 1));
	}
	
}
