package com.jg.jgpg.item.items;

import com.jg.jgpg.config.Config;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.registries.SoundRegistries;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class Trabuco extends JgGunItem {

	public Trabuco() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	public float getPower() {
		return Config.SERVER.tPower.get();
	}

	@Override
	public float getInaccuracy() {
		return Config.SERVER.tInaccuracy.get().floatValue();
	}

	@Override
	public float getDamage() {
		return Config.SERVER.prDamage.get().floatValue();
	}
	
	@Override
	public int getRange() {
		return Config.SERVER.tRange.get();
	}

	@Override
	public float getRangeDamageReduction() {
		return Config.SERVER.tRangeDamageReduction.get().floatValue();
	}

	@Override
	public int getBulletsPerShoot() {
		return 1;
	}

	@Override
	public SoundEvent getSound() {
		return SoundRegistries.FLINTLOCK_RIFLE_SHOOT.get();
	}
	
}
