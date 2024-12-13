package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.common.block.GunCraftingStationBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockRegistries {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister
			.create(Registries.BLOCK, PirateGuns.MODID);
	
	public static final Supplier<Block> GUN_CRAFTING_STATION_BLOCK =
			regAdd("gun_crafting_station_block",
					() -> new GunCraftingStationBlock(BlockBehaviour
					.Properties.of().strength(2.0f, 3.0f)
							.sound(SoundType.METAL)));
	
	public static Supplier<Block> regAdd(String name, final Supplier<Block> sup) {
		Supplier<Block> obj = BLOCKS.register(name, sup);
		return obj;
	}
	
}
