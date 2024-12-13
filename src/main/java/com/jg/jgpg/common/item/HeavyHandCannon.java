package com.jg.jgpg.common.item;

import com.jg.jgpg.common.entity.HandCannonBullet;
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

public class HeavyHandCannon extends JgGunItem {

    public HeavyHandCannon() {
        super(new Item.Properties().component(DataComponentRegistries.GUN_DATA,
                        new GunDataComponent("", 0))
                .stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public GunProperties getGunProperties(ItemStack stack) {
        return Config.SERVER.heavyHandCannon;
    }

    @Override
    public ReloadIngredients getAmmo() {
        return new ReloadIngredients(new ItemStack(ItemRegistries.HEAVYHANDCANNONBULLET.get(),
                1), new ItemStack(Items.GUNPOWDER, Config.SERVER
                .heavyHandCannon.gunpowderToReload.get()));
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
