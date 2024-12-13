package com.jg.jgpg.common.item;

import com.jg.jgpg.client.jgmodel.itemmodel.IAimable;
import com.jg.jgpg.common.entity.JgProjectile;
import com.jg.jgpg.config.GunProperties;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class JgGunItem extends JgItem implements IAimable {
	
	public JgGunItem(Properties prop) {
		super(prop);
	}
	
	public boolean isAuto() {
		return true;
	}
	
	public float getPower(ItemStack stack) {
		return getGunProperties(stack).power.get();
	}
	
	public float getInaccuracy(ItemStack stack) {
		return getGunProperties(stack).innacuracy.get().floatValue();
	}
	
	public float getGunDamage(ItemStack stack) {
		return getGunProperties(stack).damage.get().floatValue();
	}
	
	public int getRange(ItemStack stack) {
		return getGunProperties(stack).range.get();
	}
	
	public float getRangeDamageReduction(ItemStack stack) {
		return getGunProperties(stack).rangeDamageReduction.get().floatValue();
	}
	
	public int getBulletsPerShoot(ItemStack stack) {
		return getGunProperties(stack).bulletsPerShot.get();
	}
	
	public int getShootCooldown(ItemStack stack) {
		return getGunProperties(stack).shootTime.get();
	}

	public float getAimSensitivity(ItemStack stack) {
		return getGunProperties(stack).aimingSensitivityReduction.get().floatValue();
	}

	public float getRecoilWeight(ItemStack stack) {
		return getGunProperties(stack).recoilWeight.get().floatValue();
	}
	
	public float getRecoilTime(ItemStack stack) {
		return getGunProperties(stack).recoilTime.get().floatValue();
	}

	public float getBulletGravity(ItemStack stack) {
		return getGunProperties(stack).bulletGravity.get().floatValue();
	}

	public float getVerticalRecoilMultiplier(ItemStack stack) {
		return getGunProperties(stack).verticalRecoil.get().floatValue();
	}
	
	public float getHorizontalRecoilMultiplier(ItemStack stack) {
		return getGunProperties(stack).horizontalRecoil.get().floatValue();
	}
	
	public abstract GunProperties getGunProperties(ItemStack stack);
	
	public abstract ReloadIngredients getAmmo();
	
	public abstract SoundEvent getSound();

	public abstract <T extends JgProjectile> Function<Player, T> getBullet();

	public static class ReloadIngredients {

		ItemStack[] data;

		public ReloadIngredients(Supplier<Item> ammo, int amount) {
			this(new ItemStack[] { new ItemStack(ammo.get(), 1) });
		}

		public ReloadIngredients(Item ammo) {
			this(new ItemStack[] { new ItemStack(ammo, 1) });
		}

		public ReloadIngredients(ItemStack... data) {
			this.data = data;
		}

		public ItemStack[] getData() {
			return data;
		}

		public List<ItemStack> getAsItemStackList() {
			return Arrays.stream(data).toList();
		}

	}
	
}
