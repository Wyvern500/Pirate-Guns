package com.jg.jgpg.common.item;

import com.jg.jgpg.common.entity.GunBullet;
import com.jg.jgpg.common.entity.JgProjectile;
import com.jg.jgpg.common.item.data.GunDataComponent;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.config.GunProperties;
import com.jg.jgpg.registries.DataComponentRegistries;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;

public class PrimitiveRevolver extends JgGunItem {

	public PrimitiveRevolver() {
		super(new Item.Properties().component(DataComponentRegistries.LOADED, false)
				.component(DataComponentRegistries.GUN_DATA,
						new GunDataComponent("", 0))
				.stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	public GunProperties getGunProperties(ItemStack stack) {
		return Config.SERVER.primitiveRevolver;
	}

	@Override
	public ReloadIngredients getAmmo() {
		return new ReloadIngredients(new ItemStack(ItemRegistries.MUSKET_BULLET.get(), 1),
				new ItemStack(Items.GUNPOWDER, Config.SERVER.primitiveRevolver
						.gunpowderToReload.get()));
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
