package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.common.container.containers.GunCraftingStationContainer;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistries {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.MENU_TYPES, PirateGuns.MODID);
	
	public static final RegistryObject<MenuType<GunCraftingStationContainer>> GUN_CRAFTING_STATION_CONTAINER = CONTAINERS
			.register("gun_crafting_station_container", () -> new MenuType<>(GunCraftingStationContainer::new, 
					FeatureFlags.DEFAULT_FLAGS));
	
}
