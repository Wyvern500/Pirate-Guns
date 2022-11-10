package com.jg.pirateguns.guns;

import com.jg.pirateguns.PirateGuns;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

public abstract class GunItem extends Item {

	public GunItem() {
		super(new Item.Properties().tab(PirateGuns.getTab()).stacksTo(1));
	}
	
	// Abstract methods
	
	public abstract float getDamage();
	
	public abstract float getPower();
	
	public abstract int getBulletsPerShoot();
	
	public abstract int getRange();
	
	public abstract float getRangeDamageReduction();
	
	public abstract float getInnacuracy();
	
	public abstract boolean hasScope();
	
	public abstract SoundEvent getShootSound();
	
}
