package com.jg.jgpg.item.items;

import com.jg.jgpg.config.Config;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.registries.SoundRegistries;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class PiratePistol extends JgGunItem {

	public PiratePistol() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}
	
	@Override
	public float getPower() {
		return Config.SERVER.ppPower.get();
	}

	@Override
	public float getInaccuracy() {
		return Config.SERVER.ppInaccuracy.get().floatValue();
	}

	@Override
	public float getDamage() {
		return Config.SERVER.ppDamage.get().floatValue();
	}
	
	@Override
	public int getRange() {
		return Config.SERVER.ppRange.get();
	}

	@Override
	public float getRangeDamageReduction() {
		return Config.SERVER.ppRangeDamageReduction.get().floatValue();
	}

	@Override
	public int getBulletsPerShoot() {
		return 1;
	}

	@Override
	public int getShootCooldown() {
		return Config.SERVER.ppCooldown.get();
	}
	
	@Override
	public float getRecoilWeight() {
		return 4f;
	}

	@Override
	public float getVerticalRecoilMultiplier() {
		return 0.5f;
	}

	@Override
	public float getHorizontalRecoilMultiplier() {
		return 0.1f;
	}
	
	@Override
	public SoundEvent getSound() {
		return SoundRegistries.FLINTLOCK_PISTOL_SHOOT.get();
	}

}
