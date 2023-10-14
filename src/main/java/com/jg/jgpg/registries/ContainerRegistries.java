package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.containers.GunAndAmmoCraftingContainer;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistries {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.MENU_TYPES, PirateGuns.MODID);

	public static final RegistryObject<MenuType<GunAndAmmoCraftingContainer>> CRAFTING_CONTAINER = CONTAINERS
			.register("crafting_container", () -> new MenuType<>(GunAndAmmoCraftingContainer::new, 
					FeatureFlags.DEFAULT_FLAGS));
	
}
