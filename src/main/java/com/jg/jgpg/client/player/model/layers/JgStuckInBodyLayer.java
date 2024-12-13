package com.jg.jgpg.client.player.model.layers;

import com.jg.jgpg.client.player.model.JgPlayerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class JgStuckInBodyLayer<T extends LivingEntity, M extends JgPlayerModel<T>> extends RenderLayer<T, M> {

	public JgStuckInBodyLayer(LivingEntityRenderer<T, M> p_117564_) {
		super(p_117564_);
	}

	protected abstract int numStuck(T p_117565_);

	protected abstract void renderStuckItem(PoseStack p_117566_, MultiBufferSource p_117567_, int p_117568_,
			Entity p_117569_, float p_117570_, float p_117571_, float p_117572_, float p_117573_);

	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
					   T livingEntity, float limbSwing, float limbSwingAmount,
					   float partialTicks, float ageInTicks, float netHeadYaw,
					   float headPitch) {
		int i = this.numStuck(livingEntity);
		RandomSource randomsource = RandomSource.create((long)livingEntity.getId());
		if (i > 0) {
			for(int j = 0; j < i; ++j) {
				poseStack.pushPose();
				ModelPart modelpart = this.getParentModel()
						.getRandomModelPart(randomsource);
				ModelPart.Cube modelpart$cube = modelpart.getRandomCube(randomsource);
				modelpart.translateAndRotate(poseStack);
				float f = randomsource.nextFloat();
				float f1 = randomsource.nextFloat();
				float f2 = randomsource.nextFloat();
				float f3 = Mth.lerp(f, modelpart$cube.minX, modelpart$cube.maxX) / 16.0F;
				float f4 = Mth.lerp(f1, modelpart$cube.minY, modelpart$cube.maxY) / 16.0F;
				float f5 = Mth.lerp(f2, modelpart$cube.minZ, modelpart$cube.maxZ) / 16.0F;
				poseStack.translate(f3, f4, f5);
				f = -1.0F * (f * 2.0F - 1.0F);
				f1 = -1.0F * (f1 * 2.0F - 1.0F);
				f2 = -1.0F * (f2 * 2.0F - 1.0F);
				this.renderStuckItem(poseStack, buffer, packedLight, livingEntity, f, f1, f2, partialTicks);
				poseStack.popPose();
			}
		}

	}

}
