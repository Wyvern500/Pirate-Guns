package com.jg.pirateguns.items;

import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.registries.SoundRegistries;

import net.minecraft.sounds.SoundEvent;

public class Trabuco extends GunItem {

	public Trabuco() {
		
	}

	@Override
	public float getDamage() {
		return 3f;
	}

	@Override
	public float getPower() {
		return 5;
	}

	@Override
	public int getBulletsPerShoot() {
		return 8;
	}

	@Override
	public int getRange() {
		return 4;
	}

	@Override
	public float getRangeDamageReduction() {
		return 0.4f;
	}

	@Override
	public float getInnacuracy() {
		return 10f;
	}

	@Override
	public boolean hasScope() {
		return false;
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundRegistries.FLINTLOCK_RIFLE_SHOOT.get();
	}

}
