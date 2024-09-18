package com.jg.jgpg.item.items;

import com.jg.jgpg.config.Config;
import com.jg.jgpg.config.GunProperties;
import com.jg.jgpg.entity.JgProjectile;
import com.jg.jgpg.entity.entities.GunBullet;
import com.jg.jgpg.entity.entities.HandCannonBullet;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;

public class PiratePistol extends JgGunItem {

	public PiratePistol() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	public GunProperties getGunProperties(ItemStack stack) {
		return Config.SERVER.piratePistol;
	}

	@Override
	public ReloadIngredients getAmmo() {
		return new ReloadIngredients(new ItemStack(ItemRegistries.MUSKET_BULLET.get(), 1),
				new ItemStack(Items.GUNPOWDER, 1));
	}

	@Override
	public SoundEvent getSound() {
		return SoundRegistries.FLINTLOCK_PISTOL_SHOOT.get();
	}

	@Override
	public <T extends JgProjectile> Function<Player, T> getBullet() {
		return (player) -> (T) new GunBullet(player.level(), player);
	}

}
