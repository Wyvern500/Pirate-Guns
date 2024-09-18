package com.jg.jgpg.client.model.item;

import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.Constants;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PiratePistolModModel extends ToShowModModel {

	public PiratePistolModModel(BakedModel origin) {
		super(origin);
	}

	@Override
	public void rebuild(ItemStack stack, BlockState blockState, RandomSource random) {
		addItem(ItemRegistries.PIRATEPISTOL.get(), blockState, random);
		addSpecial(Constants.PPHAMMER, blockState, random);
	}

}
