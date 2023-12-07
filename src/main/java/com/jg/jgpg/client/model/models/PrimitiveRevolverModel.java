package com.jg.jgpg.client.model.models;

import java.util.List;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractGunModel;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.model.player.JgHumanoidModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel.ArmPose;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.NBTUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderTypeHelper;

public class PrimitiveRevolverModel extends AbstractGunModel {

	public Animation LOOK;
	public Animation SHOOT;
	public Animation RELOAD;
	public Animation HIT;
	
	public PrimitiveRevolverModel(ClientHandler client) {
		super(client);
	}

	@Override
	public void initParts() {
		addPart(new JgModelPart("aim", -1.2939917f, 1.0899994f, 0.35999992f, -0.21991137f, -0.1745329f, 0.0f));
		addPart(new JgModelPart("all", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("chamber", -0.49999982f, -0.49999982f, -0.49999982f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("gun", 1.6759992f, -0.588f, -2.1219976f, 0.17004529f, 0.1749066f, 0.0f));
		addPart(new JgModelPart("gunwithchamber", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("gunwithhammer", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("hammer", -0.48399937f, -0.5080001f, 0.09800065f, -1.3658453f, 0.035280257f, 0.06981317f));
		addPart(new JgModelPart("leftarm", 0.0f, -0.56f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("recoil", -0.09999999f, -0.97999936f, 0.039999984f, 0.6283214f, 0.0f, 0.15707956f));
		addPart(new JgModelPart("rightarm", 1.0179993f, -0.43199795f, 1.1040033f, -0.13613564f, 0.3106685f, 0.5096381f));
		addPart(new JgModelPart("rightarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("sprint", 0.16f, -1.5760018f, 1.4901161E-8f, 0.9424782f, -0.20943952f, 0.0f));
	}

	@Override
	public void initAnimations() {
		// Original
		LOOK = new Animation("Look")
				.addKeyframe(5)
				.traslate(getPart("all"), 0.56999975f, 0.46999982f, 0.0f, "easeOutSine")
				.rotate(getPart("all"), 0.0f, 0.0f, -0.6099997f, "easeOutSine")
				.addKeyframe(36)
				.traslate(getPart("all"), 0.56999975f, 0.46999982f, 0.0f, "easeInSine")
				.rotate(getPart("all"), 0.0f, 0.0f, -0.6099997f, "easeInSine")
				.addKeyframe(44)
				.traslate(getPart("all"), 0.19861826f, -0.44113925f, 0.42999986f, "easeInSine")
				.rotate(getPart("all"), 0.0f, 0.4999998f, 0.605598f, "easeInSine")
				.addKeyframe(62)
				.traslate(getPart("all"), 0.19861826f, -0.44113925f, 0.42999986f, "easeInSine")
				.rotate(getPart("all"), 0.0f, 0.4999998f, 0.605598f, "easeInSine")
				.addKeyframe(68)
				.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.traslate(getPart("gun"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.traslate(getPart("all"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.traslate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.rotate(getPart("all"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.end();
		
		RELOAD = new Animation("ReloadAnimation").addKeyframe(4).traslate(getPart("gun"), 0, 0, 0).end();
		
		SHOOT = new Animation("ShootAnimation").addKeyframe(4).traslate(getPart("gun"), 0, 0, 0).end();
		
		// Original
		HIT = new Animation("HitAnimation")
				.addKeyframe(5)
				.traslate(getPart("rightarmgun"), 0.0f, -0.69999963f, 0.7599996f, "easeInQuint")
				.rotate(getPart("rightarmgun"), 0.02f, 0.02f, 0.79999954f, "easeInQuint")
				.addKeyframe(11)
				.traslate(getPart("rightarmgun"), 0.0f, -0.69999963f, 0.7599996f, "empty")
				.rotate(getPart("rightarmgun"), 0.02f, 0.02f, 0.79999954f, "empty")
				.addKeyframe(14)
				.traslate(getPart("rightarmgun"), -0.4799998f, -1.0799993f, -0.8199995f, "easeOutBack")
				.rotate(getPart("rightarmgun"), 0.4999998f, 0.02f, 0.79999954f, "easeOutBack")
				.addKeyframe(22)
				.traslate(getPart("rightarmgun"), -0.4799998f, -1.0799993f, -0.8199995f, "empty")
				.rotate(getPart("rightarmgun"), 0.4999998f, 0.02f, 0.79999954f, "empty")
				.addKeyframe(27)
				.traslate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.end();
	}

	@Override
	public void tick() {
		animator.tick();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		/*if(NBTUtils.isLoaded(stack)) {
			getPart("hammer").getDtransform().set(0.776f, -0.7079999f, -1.4219983f, 0.17004529f, 
					0.14f, 0);
		} else {
			getPart("hammer").getDtransform().set(0.83599997f, -0.84799975f, -0.82199883f, 
					-1.3658453f, 0.035280257f, 0.06981317f);
		}*/
		
		if(client.shouldRenderDefault()) {
			renderDefault(player, stack, buffer, matrix, light);
		}
		
		// float p = getShootProgress(stack);
		float p = 0;
		//p = EasingHandler.INSTANCE.getEasing("easeOutCirc").get(p);
		
		if(player.getCooldowns().isOnCooldown(stack.getItem())) {
			p = getShootProgress(stack, 0.2f, "easeOutExpo", "easeInOutQuint");
			//LogUtils.log("PiratePistolModel", "ShootProgress: " + p);
		}
		
		matrix.pushPose();
		if(client.shouldRenderDefault()) {
			traslateRotate("recoil", matrix);
		} else {
			lerpToTransform("recoil", matrix, p);
		}
		traslateRotate("all", matrix);
		lerpToTransform("aim", matrix, client.getAimHandler().getProgress(stack.getItem()));
		lerpToTransform("sprint", matrix, client.getSprintHandler().getProgress(stack.getItem()));
		matrix.pushPose();
		traslateRotate("rightarmgun", matrix);
		traslateRotate("rightarm", matrix);
		matrix.scale(1f, 1f, 3f);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT);
		matrix.popPose();
		if(animator.getCurrent() != null) {
			if(animator.getCurrent() == RELOAD) {
				matrix.pushPose();
				traslateRotate("leftarm", matrix);
				RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT);
				matrix.popPose();
			}
		}
		
		matrix.pushPose();
		traslateRotate("rightarmgun", matrix);
		traslateRotate("gunwithhammer", matrix);
		// Gun
		traslateRotate("gun", matrix);
		matrix.translate(-0.5F, -0.5F, -0.5F);
		RenderHelper.renderItem(matrix, stack, buffer, light);
		
		/*Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, 
				matrix, buffer, light, OverlayTexture.NO_OVERLAY, model);*/
		matrix.popPose();
		
		matrix.pushPose();
		traslateRotate("gun", matrix);
		traslateRotate("rightarmgun", matrix);
		traslateRotate("gunwithhammer", matrix);
		traslateRotate("hammer", matrix);
		
		// Hammer
		RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.PRVHAMMER);
		matrix.popPose();
		
		matrix.pushPose();
		traslateRotate("gun", matrix);
		traslateRotate("rightarmgun", matrix);
		traslateRotate("gunwithchamber", matrix);
		traslateRotate("chamber", matrix);
		
		// Chamber
		RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.PRVCHAMBER);
		matrix.popPose();
		
		matrix.popPose();
	}

	@Override
	public void renderDefault(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light) {
		
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
		return List.of(LOOK, HIT, RELOAD, SHOOT);
	}

	@Override
	public ArmPose getArmPose(ItemStack stack) {
		return ArmPose.ITEM;
	}

	@Override
	public <T extends LivingEntity> void handleArmPose(ArmPose pose, ItemStack stack, JgHumanoidModel<T> model,
			HumanoidArm arm) {
		
	}

	@Override
	public boolean canReload(LocalPlayer player) {
		return false;
	}

	@Override
	public void reload(LocalPlayer player) {
		
	}

	@Override
	public Animation getLookAnimAnimation(LocalPlayer player) {
		return LOOK;
	}

	@Override
	public Animation getKickbackAnimAnimation(LocalPlayer player) {
		return HIT;
	}

}
