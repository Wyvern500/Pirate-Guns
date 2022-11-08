package com.jg.pirateguns.registries;

import java.util.function.Supplier;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.items.PiratePistol;
import com.jg.pirateguns.items.PirateRifle;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistries {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister
			.create(ForgeRegistries.ITEMS, PirateGuns.MODID);
	
	public static final RegistryObject<Item> PIRATEPISTOL = regAdd("pirate_pistol", 
			() -> new PiratePistol());
	
	public static final RegistryObject<Item> PIRATERIFLE = regAdd("pirate_rifle", 
			() -> new PirateRifle());
	
	public static <I extends Item> RegistryObject<I> regAdd(String name, final Supplier<? extends I> sup) {
		RegistryObject<I> obj = ITEMS.register(name, sup);
		return obj;
	}
}
