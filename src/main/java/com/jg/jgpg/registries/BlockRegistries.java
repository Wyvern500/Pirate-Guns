package com.jg.jgpg.registries;

import java.util.function.Supplier;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.block.blocks.GunCraftingStationBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistries {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister
			.create(ForgeRegistries.BLOCKS, PirateGuns.MODID);
	
	public static final RegistryObject<Block> GUN_CRAFTING_STATION_BLOCK = 
			regAdd("gun_crafting_station_block", () -> new GunCraftingStationBlock(BlockBehaviour
					.Properties.of().strength(2.0f, 3.0f).sound(SoundType.METAL)));
	
	public static RegistryObject<Block> regAdd(String name, final Supplier<Block> sup) {
		RegistryObject<Block> obj = BLOCKS.register(name, sup);
		return obj;
	}
	
}
