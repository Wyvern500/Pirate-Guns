package com.jg.pirateguns.items;

import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.registries.SoundRegistries;

import net.minecraft.sounds.SoundEvent;

public class PirateRifle extends GunItem {

	public PirateRifle() {
		
	}

	@Override
	public float getDamage() {
		return 12f;
	}

	@Override
	public float getPower() {
		return 8f;
	}

	@Override
	public int getRange() {
		return 20;
	}

	@Override
	public float getRangeDamageReduction() {
		return 0.9f;
	}

	@Override
	public float getInnacuracy() {
		return 0.01f;
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundRegistries.FLINTLOCK_RIFLE_SHOOT.get();
	}

	@Override
	public boolean hasScope() {
		return true;
	}

	@Override
	public int getBulletsPerShoot() {
		return 1;
	}
	
}
