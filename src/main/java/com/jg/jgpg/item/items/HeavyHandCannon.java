package com.jg.jgpg.item.items;

import com.jg.jgpg.config.Config;
import com.jg.jgpg.config.GunProperties;
import com.jg.jgpg.entity.JgProjectile;
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

public class HeavyHandCannon extends JgGunItem {

    public HeavyHandCannon() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public GunProperties getGunProperties(ItemStack stack) {
        return Config.SERVER.heavyHandCannon;
    }

    @Override
    public ReloadIngredients getAmmo() {
        return new ReloadIngredients(new ItemStack(ItemRegistries.HEAVYHANDCANNONBULLET.get(),
                1), new ItemStack(Items.GUNPOWDER, 5));
    }

    @Override
    public SoundEvent getSound() {
        return SoundRegistries.CANNON_SHOOT.get();
    }

    @Override
    public <T extends JgProjectile> Function<Player, T> getBullet() {
        return (player) -> (T) new HandCannonBullet(player.level(), player);
    }
}
