package com.jg.jgpg.client.player.model.layers;

import com.jg.jgpg.client.player.model.JgPlayerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JgArrowLayer<T extends LivingEntity, M extends JgPlayerModel<T>> 
	extends JgStuckInBodyLayer<T, M> {

	private final EntityRenderDispatcher dispatcher;

	public JgArrowLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer) {
		super(renderer);
		this.dispatcher = context.getEntityRenderDispatcher();
	}

	protected int numStuck(T entity) {
		return entity.getArrowCount();
	}

	protected void renderStuckItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Entity entity, float x, float y, float z, float partialTick) {
		float f = Mth.sqrt(x * x + z * z);
		Arrow arrow = new Arrow(entity.level(), entity.getX(), entity.getY(), entity.getZ(), ItemStack.EMPTY, (ItemStack)null);
		arrow.setYRot((float)(Math.atan2((double)x, (double)z) * 180.0 / 3.1415927410125732));
		arrow.setXRot((float)(Math.atan2((double)y, (double)f) * 180.0 / 3.1415927410125732));
		arrow.yRotO = arrow.getYRot();
		arrow.xRotO = arrow.getXRot();
		this.dispatcher.render(arrow, 0.0, 0.0, 0.0, 0.0F, partialTick, poseStack, buffer, packedLight);
	}

}
