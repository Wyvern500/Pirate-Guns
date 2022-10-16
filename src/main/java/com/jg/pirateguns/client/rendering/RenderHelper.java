package com.jg.pirateguns.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class RenderHelper {
	
	public static void renderPlayerArm(PoseStack matrix, MultiBufferSource buffer, int light, float p_109350_,
			float p_109351_, HumanoidArm p_109352_) {
		boolean flag = p_109352_ != HumanoidArm.LEFT;
		float f = flag ? 1.0F : -1.0F;
		float f1 = Mth.sqrt(p_109351_);
		float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
		float f3 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
		float f4 = -0.4F * Mth.sin(p_109351_ * (float) Math.PI);
		matrix.translate((double) (f * (f2 + 0.64000005F)), (double) (f3 + -0.6F + p_109350_ * -0.6F),
				(double) (f4 + -0.71999997F));
		matrix.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
		float f5 = Mth.sin(p_109351_ * p_109351_ * (float) Math.PI);
		float f6 = Mth.sin(f1 * (float) Math.PI);
		matrix.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
		matrix.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
		AbstractClientPlayer abstractclientplayer = Minecraft.getInstance().player;
		RenderSystem.setShaderTexture(0, abstractclientplayer.getSkinTextureLocation());
		matrix.translate((double) (f * -1.0F), (double) 3.6F, 3.5D);
		matrix.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
		matrix.mulPose(Vector3f.XP.rotationDegrees(200.0F));
		matrix.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
		matrix.translate((double) (f * 5.6F), 0.0D, 0.0D);
		PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
				.<AbstractClientPlayer>getRenderer(abstractclientplayer);
		if (flag) {
			playerrenderer.renderRightHand(matrix, buffer, light, abstractclientplayer);
		} else {
			playerrenderer.renderLeftHand(matrix, buffer, light, abstractclientplayer);
		}
	}
	
}
