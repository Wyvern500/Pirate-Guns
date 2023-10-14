package com.jg.jgpg.registries;

import java.util.function.Supplier;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.block.blocks.GunAndAmmoCraftingBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistries {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister
			.create(ForgeRegistries.BLOCKS, PirateGuns.MODID);
	
	public static final RegistryObject<Block> GUN_AND_AMMO_CRAFTING_BLOCK = 
			regAdd("gun_and_ammo_crafting_block", () -> new GunAndAmmoCraftingBlock(BlockBehaviour
					.Properties.of().strength(2.0f, 3.0f).sound(SoundType.WOOD)));
	
	public static RegistryObject<Block> regAdd(String name, final Supplier<Block> sup) {
		RegistryObject<Block> obj = BLOCKS.register(name, sup);
		return obj;
	}
	
}
