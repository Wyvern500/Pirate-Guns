package com.jg.jgpg.client.model.models;

import java.util.List;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.LogUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PiratePistolModel extends AbstractJgModel {

	public Animation TEST;
	
	public PiratePistolModel(ClientHandler handler) {
		super(handler);
	}

	@Override
	public void initParts() {
		
		addPart(new JgModelPart("gun", 0, 0, 0, 0, 0, 0, 0, 0));
		addPart(new JgModelPart("leftarm", 0, 0, 0, 0, 0, 0, 0, 0));
		addPart(new JgModelPart("rightarm", 0, 0, 0, 0, 0, 0, 0, 0));
		addPart(new JgModelPart("all", 0, 0, 0, 0, 0, 0, 0, 0));
		
		TEST = new Animation("Test")
				.addKeyframe(4)
				.traslate(getPart("all"), -0.16f, -0.24000001f, -0.40000004f, "empty")
				.addKeyframe(10)
				.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("gun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("all"), 0.6f, -0.6f, -1.0000001f, "empty")
				.traslate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(26)
				.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.traslate(getPart("gun"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.traslate(getPart("all"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.traslate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.end();
		
		animator.setCurrent(this, TEST);
	}

	@Override
	public void tick() {
		animator.tick();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, 
			int light) {
		matrix.pushPose();
		traslateRotate("all", matrix);
		matrix.pushPose();
		traslateRotate("rightarm", matrix);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT);
		matrix.popPose();
		matrix.pushPose();
		traslateRotate("gun", matrix);
		Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, 
				false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, Minecraft.getInstance()
				.getItemRenderer().getItemModelShaper().getItemModel(stack));
		matrix.popPose();
		matrix.popPose();
	}

	@Override
	public void onAnimationEnd(Animation anim) {
		
	}

	@Override
	public void onAnimationStart(Animation anim) {
		
	}

	@Override
	public void onAnimationTick(Animation anim, float prevTick, float tick) {
		
	}

	@Override
	public List<Animation> getAnimations() {
		return List.of(TEST);
	}

}
