package com.jg.pirateguns.client.rendering.entities;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.client.models.entities.CanonModel;
import com.jg.pirateguns.entities.Canon;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class CanonRenderer extends MobRenderer<Canon, CanonModel> {

	public CanonRenderer(Context context) {
		super(context, new CanonModel(context.bakeLayer(CanonModel.LAYER_LOCATION)), 1.0f);
	}

	@Override
	public void render(Canon canon, float yaw, float partialTicks, PoseStack matrix,
			MultiBufferSource buffer, int packedLight) {
		matrix.pushPose();
		CanonModel model = getModel();
		if(model != null) {
			//model.
		}
		super.render(canon, yaw, partialTicks, matrix, buffer, packedLight);
		matrix.popPose();
	}
	
	@Override
	public ResourceLocation getTextureLocation(Canon p_114482_) {
		return new ResourceLocation(PirateGuns.MODID, "textures/entity/canon_texture.png");
	}

}
