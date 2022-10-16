package com.jg.pirateguns.guns;

import com.jg.pirateguns.PirateGuns;

import net.minecraft.world.item.Item;

public class GunItem extends Item {

	public GunItem() {
		super(new Item.Properties().tab(PirateGuns.getTab()).stacksTo(1));
	}
	
}
