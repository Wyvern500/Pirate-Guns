package com.jg.pirateguns.items;

import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.registries.SoundRegistries;

import net.minecraft.sounds.SoundEvent;

public class PiratePistol extends GunItem {

	public PiratePistol() {
		
	}

	@Override
	public float getDamage() {
		return 8f;
	}

	@Override
	public float getPower() {
		return 5f;
	}

	@Override
	public int getRange() {
		return 10;
	}

	@Override
	public float getRangeDamageReduction() {
		return 0.7f;
	}

	@Override
	public float getInnacuracy() {
		return 0.1f;
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundRegistries.FLINTLOCK_PISTOL_SHOOT.get();
	}

	@Override
	public boolean hasScope() {
		return false;
	}
	
}
