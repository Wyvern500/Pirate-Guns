package com.jg.pirateguns.utils;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.network.ConsumeItemMessage;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class InventoryUtils {

	public static int getIndexForItem(Player player, Item item) {
		Inventory inv = player.getInventory();
		for(int i = 0; i < inv.items.size(); i++) {
			if(inv.items.get(i).getItem() == item) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getCountForItem(Player player, Item item) {
		Inventory inv = player.getInventory();
		for(int i = 0; i < inv.items.size(); i++) {
			if(inv.items.get(i).getItem() == item) {
				return inv.items.get(i).getCount();
			}
		}
		return -1;
	}
	
	public static int[] getCountAndIndexForItem(Player player, Item item) {
		int[] data = new int[] { -1, -1 };
		Inventory inv = player.getInventory();
		for(int i = 0; i < inv.items.size(); i++) {
			if(inv.items.get(i).getItem() == item) {
				data[0] = i;
				data[1] = inv.items.get(i).getCount();
				return data;
			}
		}
		return data;
	}
	
	public static void consumeItem(Player player, int index, int amount) {
		PirateGuns.channel.sendToServer(new ConsumeItemMessage(index, amount));
	}
	
	public static void consumeItems(Player player, int[] indexes, int[] amounts) {
		PirateGuns.channel.sendToServer(new ConsumeItemMessage(indexes, amounts));
	}
	
	public static void consumeItem(Player player, Item item, int amount) {
		PirateGuns.channel.sendToServer(new ConsumeItemMessage(
				getIndexForItem(player, item), amount));
	}

}
