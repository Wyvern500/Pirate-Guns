package com.jg.jgpg.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

public abstract class JgGunItem extends Item {

	public JgGunItem(Properties prop) {
		super(prop);
	}
	
	public abstract float getPower();
	
	public abstract float getInaccuracy();
	
	public abstract float getDamage();
	
	public abstract int getRange();
	
	public abstract float getRangeDamageReduction();
	
	public abstract int getBulletsPerShoot();
	
	public abstract int getShootCooldown();
	
	public abstract float getRecoilWeight();
	
	public abstract float getVerticalRecoilMultiplier();
	
	public abstract float getHorizontalRecoilMultiplier();
	
	public abstract SoundEvent getSound();

}
