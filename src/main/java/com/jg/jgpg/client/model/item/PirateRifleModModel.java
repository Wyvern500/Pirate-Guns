package com.jg.jgpg.client.model.item;

import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.NBTUtils;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PirateRifleModModel extends ToShowModModel {

	public PirateRifleModModel(BakedModel origin) {
		super(origin);
	}

	@Override
	public void rebuild(ItemStack stack, BlockState blockState, RandomSource random) {
		addItem(ItemRegistries.PIRATERIFLE.get(), blockState, random);
		addSpecial(Constants.PRHAMMER, blockState, random);
		if(NBTUtils.hasScope(stack)) {
			addSpecial(Constants.PRSCOPE, blockState, random);
		}
	}

}

