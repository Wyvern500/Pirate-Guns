package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.common.container.GunCraftingStationContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ContainerRegistries {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
			.create(Registries.MENU, PirateGuns.MODID);
	
	public static final Supplier<MenuType<GunCraftingStationContainer>>
			GUN_CRAFTING_STATION_CONTAINER = CONTAINERS
			.register("gun_crafting_station_container",
					() -> new MenuType<>(GunCraftingStationContainer::new,
					FeatureFlags.DEFAULT_FLAGS));
	
}
