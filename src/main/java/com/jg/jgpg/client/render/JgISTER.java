package com.jg.jgpg.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class JgISTER extends BlockEntityWithoutLevelRenderer {

    public JgISTER() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext,
                             PoseStack matrix, MultiBufferSource pBuffer,
                             int pPackedLight, int pPackedOverlay) {

    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {

    }
}
