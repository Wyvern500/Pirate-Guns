package com.jg.jgpg.client.render.entity;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.model.entity.WunderwaffleBulletModel;
import com.jg.jgpg.entity.entities.HandCannonBullet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class WunderwaffleBulletRenderer extends EntityRenderer<HandCannonBullet> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(PirateGuns
			.MODID, "textures/entity/wunderwafflebullet.png");
	private WunderwaffleBulletModel model;
	
	public WunderwaffleBulletRenderer(Context p_174008_) {
		super(p_174008_);
		this.model = new WunderwaffleBulletModel(p_174008_.bakeLayer(WunderwaffleBulletModel
				.LAYER_LOCATION));
	}
	
	@Override
	public void render(HandCannonBullet p_114485_, float p_114486_, float p_114487_,
                       PoseStack matrix, MultiBufferSource p_114489_, int p_114490_) {
		matrix.pushPose();
		matrix.translate(0.0F, -1.2F, 0.0F);
		VertexConsumer vertexconsumer = p_114489_.getBuffer(this.model.renderType(TEXTURE));
	    this.model.renderToBuffer(matrix, vertexconsumer, p_114490_, 
	    		OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	    /*
	    VertexConsumer lightning = p_114489_.getBuffer(RenderType.lightning());
	    Matrix4f matrix4f = matrix.last().pose();
	    
	    */
	    
		matrix.popPose();
		super.render(p_114485_, p_114486_, p_114487_, matrix, p_114489_, p_114490_);
	}

	@Override
	public ResourceLocation getTextureLocation(HandCannonBullet p_114482_) {
		return TEXTURE;
	}

}
