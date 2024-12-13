package com.jg.jgpg.client.model.item;

import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.registries.ItemRegistries;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class HeavyHandCannonModModel extends ToShowModModel {

    public HeavyHandCannonModModel(BakedModel origin) {
        super(origin);
    }

    @Override
    public void rebuild(ItemStack stack, BlockState blockState, RandomSource random) {
        addItem(ItemRegistries.HEAVYHANDCANNON.get(), blockState, random);
    }
}
