package com.jg.jgpg.client.jgmodel.itemmodel.itemmodels;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.AbstractGunModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.model.item.TrabucoModModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoseHandlers;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class TrabucoModel extends AbstractGunModel {

	Animation LOOK;
	Animation HIT;
	Animation RELOAD;
	boolean renderStick;

	public TrabucoModel(AbstractClientHandler client) {
		super(client);
	}

	@Override
	public boolean canReload(Player player) {
		int bullets = InventoryUtils.getCountForItem(player, ItemRegistries.TRABUCO_BULLET.get());
		int gunpowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
		return NBTUtils.getBullets(player.getMainHandItem()) <= 0 && gunpowder >= 2 && bullets >= 8;
	}

	@Override
	public void reload(Player player) {
		animator.setAnimation(RELOAD);
		animator.play();
	}

	@Override
	public Animation getLookAnimAnimation(Player player) {
		return LOOK;
	}

	@Override
	public Animation getKickbackAnimAnimation(Player player) {
		return HIT;
	}

	@Override
	public void cooldownFinished(Player player) {
		
	}

	@Override
	public JgPlayerPoseHandler getPlayerPoseHandler() {
		return PlayerPoseHandlers.TWO_HANDED;
	}

	@Override
	public JgPlayerPose getPose(Player player) {
		if(client.getAimHandler().getProgress(item) > 0.5) {
			return PlayerPoses.AIMING;
		} else if(player.isSprinting()){
			return PlayerPoses.RUNNING;
		} else if(animator.getAnimation() == RELOAD) {
			return PlayerPoses.RELOADING;
		}
		return PlayerPoses.HOLDINGGUN;
	}

	@Override
	public ToShowModModel getModModel(BakedModel origin) {
		return new TrabucoModModel(origin);
	}

	@Override
	public void initParts() {
		addPart(new JgModelPart("aim", -0.6506027f, 0.8457973f, 0.30999607f, -0.17104232f, -0.08988424f, 0.0f));
		addPart(new JgModelPart("all", -0.7599996f, 0.29999998f, 0.69999963f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("breathing", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("gun", 1.3399991f, -1.2199992f, -1.6999989f, 0.06981317f, 0.06981318f, 0.0f));
		addPart(new JgModelPart("hammer", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarm", 0.4199998f, -0.5199998f, -1.359999f, 0.0f, -1.2217312f, 0.0f));
		addPart(new JgModelPart("leftarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarmstick", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("muzzle", 1.1399993f, -0.8199995f, -3.0199976f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("recoil", 0.0f, 0.007999802f, 0.14000022f, 1.8626451E-8f, 0.0f, 0.0f));
		addPart(new JgModelPart("recoilaccumulation", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarm", 0.57999974f, -0.61999965f, -0.93999934f, -0.52359873f, 0.17453296f, 0.6981318f));
		addPart(new JgModelPart("rightarmgun", 0.0f, 0.0f, 0.0f, 0.034906626f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarmgunaroundpoint", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarmgunaroundpointvisualizer", 1.1f, -1.4f, -1.2600005f, 3.7252903E-9f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarmgunpump", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("sprint", 1.6599988f, -0.6599997f, 0.3f, 0.10471979f, 0.872665f, 0.0f));
		addPart(new JgModelPart("stick", 0.85999954f, -0.93999946f, -1.0799997f, 0.2094395f, -0.1047197f, 0.27925268f));
	}

	@Override
	public void initAnimations() {
		LOOK = new Animation("Look")
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.66322523f, 0.0f, -0.94247824f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.35999992f, 0.3799999f, 0.5399998f)
				.translate(getPart("rightarmgun"), 40, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 40, "easeInOutSine", 0.66322523f, 0.0f, -0.94247824f)
				.translate(getPart("leftarm"), 40, "easeInOutSine", -0.35999992f, 0.3799999f, 0.5399998f)
				.translate(getPart("rightarmgun"), 50, "easeInOutSine", 0.43999985f, 0.11999998f, 0.059999995f)
				.rotate(getPart("rightarmgun"), 50, "easeInOutSine", 0.66322523f, 0.0f, 1.1519179f)
				.translate(getPart("leftarm"), 50, "easeInOutSine", -0.6399997f, 0.26000002f, 0.5199998f)
				.translate(getPart("rightarmgun"), 80, "easeInOutSine", 0.43999985f, 0.11999998f, 0.059999995f)
				.rotate(getPart("rightarmgun"), 80, "easeInOutSine", 0.66322523f, 0.0f, 1.1519179f)
				.translate(getPart("leftarm"), 80, "easeInOutSine", -0.6399997f, 0.26000002f, 0.5199998f)
				.translate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		HIT = new Animation("Hit")
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("gun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("gun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("all"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("all"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 3, "easeInOutSine", -0.49199983f, 0.015999999f, 0.111999996f)
				.translate(getPart("rightarm"), 3, "easeInOutSine", -0.26000002f, 0.011999996f, -0.06799997f)
				.translate(getPart("leftarm"), 6, "easeInOutSine", -0.7739996f, -0.08799999f, 0.18400003f)
				.translate(getPart("rightarm"), 6, "easeInOutSine", -0.34999996f, -0.031000001f, -0.06099993f)
				.translate(getPart("leftarm"), 9, "easeInOutSine", -0.9893327f, -0.12533331f, 0.43599987f)
				.translate(getPart("rightarm"), 9, "easeInOutSine", -0.43999985f, -0.094f, -0.12733321f)
				.translate(getPart("leftarm"), 15, "easeInOutSine", -0.26000002f, -0.02f, 0.26000002f)
				.translate(getPart("gun"), 15, "easeInOutSine", -0.39999992f, -0.07999999f, -1.0199994f)
				.rotate(getPart("gun"), 15, "easeInOutSine", 0.0f, 3.1066813f, -1.1175871E-8f)
				.translate(getPart("all"), 15, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarm"), 15, "easeInOutSine", -0.2f, -0.04f, -0.5399998f)
				.translate(getPart("all"), 25, "easeInOutSine", -0.35999992f, -0.20000002f, 0.41999987f)
				.rotate(getPart("all"), 25, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("all"), 28, "easeInOutBack", -0.20000003f, -0.3799999f, 0.67999965f)
				.rotate(getPart("all"), 28, "easeInOutBack", 0.31415927f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 33, "easeInOutSine", -0.26000002f, -0.02f, 0.26000002f)
				.translate(getPart("gun"), 33, "easeInOutSine", -0.39999992f, -0.07999999f, -1.0199994f)
				.rotate(getPart("gun"), 33, "easeInOutSine", 0.0f, 3.1066813f, -1.1175871E-8f)
				.translate(getPart("all"), 33, "easeInOutSine", -0.20000003f, -0.3799999f, 0.67999965f)
				.rotate(getPart("all"), 33, "easeInOutSine", 0.31415927f, 0.0f, 0.0f)
				.translate(getPart("rightarm"), 33, "easeInOutSine", -0.2f, -0.04f, -0.5399998f)
				.translate(getPart("leftarm"), 36, "easeInOutSine", -0.51809967f, -0.15369998f, 0.4780997f)
				.translate(getPart("rightarm"), 36, "easeInOutSine", -0.2969999f, -0.12739997f, -0.18989964f)
				.translate(getPart("leftarm"), 39, "easeInOutSine", -0.93795425f, -0.14753498f, 0.3589545f)
				.translate(getPart("rightarm"), 39, "easeInOutSine", -0.42634994f, -0.07506996f, -0.1284446f)
				.translate(getPart("leftarm"), 46, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("gun"), 46, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("gun"), 46, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("all"), 46, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("all"), 46, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarm"), 46, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		RELOAD = new Animation("Reload")
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.3999999f, -0.28f, 0.69999963f)
				.rotate(getPart("leftarm"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 20, "easeInOutSine", -0.3799999f, 0.17999999f, 0.57999974f)
				.rotate(getPart("leftarm"), 20, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 20, "easeInOutSine", 0.83999956f, -0.8999995f, -0.5199998f)
				.rotate(getPart("rightarmgun"), 20, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", -0.31999996f, 0.6399997f, 0.28000003f)
				.rotate(getPart("leftarm"), 30, "easeInOutSine", -0.90757155f, 3.7252903E-9f, -0.6283186f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", 0.83999956f, -0.8999995f, -0.5199998f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 35, "easeInOutSine", -0.31999996f, 0.6399997f, 0.28000003f)
				.rotate(getPart("leftarm"), 35, "easeInOutSine", -0.90757155f, 3.7252903E-9f, -0.6283186f)
				.translate(getPart("leftarm"), 45, "easeInOutSine", -0.8399995f, -0.7599996f, -0.07999998f)
				.rotate(getPart("leftarm"), 45, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 60, "easeInOutSine", -0.8399995f, -0.7599996f, -0.07999998f)
				.rotate(getPart("leftarm"), 60, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 70, "easeInOutSine", -0.3799999f, 0.17999999f, 0.57999974f)
				.rotate(getPart("leftarm"), 70, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 80, "easeInOutSine", -0.31999996f, 0.16f, 0.57999974f)
				.rotate(getPart("leftarm"), 80, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 85, "easeInOutSine", -0.28f, 0.09999999f, 0.57999974f)
				.rotate(getPart("leftarm"), 85, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 95, "easeInOutSine", -0.8399995f, -0.7599996f, -0.07999998f)
				.rotate(getPart("leftarm"), 95, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 110, "easeInOutSine", -0.83999944f, -0.7599996f, -0.07999998f)
				.rotate(getPart("leftarm"), 110, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 120, "easeInOutSine", -0.31999993f, 0.37999994f, 0.5399998f)
				.rotate(getPart("leftarm"), 120, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 130, "easeInOutBack", -0.26f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 130, "easeInOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 135, "easeInOutSine", -0.26f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 135, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 145, "easeInOutSine", -0.31999993f, 0.37999994f, 0.5399998f)
				.rotate(getPart("leftarm"), 145, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 155, "easeInOutBack", -0.26f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 155, "easeInOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 160, "easeInOutSine", -0.26f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 160, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 160, "easeInOutSine", 0.83999956f, -0.8999995f, -0.5199998f)
				.rotate(getPart("rightarmgun"), 160, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 170, "easeInOutSine", -0.29999995f, 0.24000002f, 0.57999974f)
				.rotate(getPart("leftarm"), 170, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 170, "easeInOutSine", 0.039999984f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 170, "easeInOutSine", 0.0f, 0.0f, 0.9424782f)
				.translate(getPart("leftarm"), 180, "easeInOutBack", -0.27999997f, 0.22000001f, 0.69999963f)
				.rotate(getPart("leftarm"), 180, "easeInOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 185, "easeInOutSine", -0.27999997f, 0.22000001f, 0.69999963f)
				.rotate(getPart("leftarm"), 185, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 195, "easeInOutSine", -0.9799993f, -0.35999995f, 0.5399998f)
				.rotate(getPart("leftarm"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 205, "easeInOutSine", -0.9799993f, -0.35999995f, 0.5399998f)
				.rotate(getPart("leftarm"), 205, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 215, "easeInOutSine", -0.3399999f, 0.3399999f, 0.57999974f)
				.rotate(getPart("leftarm"), 215, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 225, "easeInOutCubic", -0.29999995f, 0.24000002f, 0.57999974f)
				.rotate(getPart("leftarm"), 225, "easeInOutCubic", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 235, "easeInOutSine", -0.3399999f, 0.3399999f, 0.57999974f)
				.rotate(getPart("leftarm"), 235, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 245, "easeInOutCubic", -0.29999995f, 0.24000002f, 0.57999974f)
				.rotate(getPart("leftarm"), 245, "easeInOutCubic", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 245, "easeInOutSine", 0.039999984f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 245, "easeInOutSine", 0.0f, 0.0f, 0.9424782f)
				.translate(getPart("leftarm"), 250, "easeInOutSine", -0.20499994f, 0.18000002f, 0.71499956f)
				.rotate(getPart("leftarm"), 250, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, BufferSource impl,
			PoseStack matrix, int light) {
		// Check if the gun has bullets
		boolean outOfBullets = NBTUtils.getBullets(stack) == 0;
		if (outOfBullets) {
			getPart("hammer").getDtransform().set(0.0f, 0.3799999f, 0.09999999f, -1.431171f, 0.0f, 0.0f);
		} else {
			getPart("hammer").getDtransform().set(0, 0, 0, 0, 0, 0);
		}

		matrix.pushPose();

		Easing easing = EasingHandler.INSTANCE.getEasing("easeOutBack");

		if (client.getClient().shouldRenderDefault()) {
			traslateRotate("recoil", matrix);
		}

		float aimProgress = client.getAimHandler().getProgress(stack.getItem());
		
		shootAndRecoilTransformation(matrix, easing);

		float shootProgress = client.getClient().getCooldownHandler().getCooldownPercent(item,
				0);
		float compensator = 1f;
		if(aimProgress > 0.5f) {
			compensator = 0.5f;
		}
		lerpToTransform("recoil", matrix, shootProgress * compensator);

		// Breathing simulation
		breath(matrix, 0.01f);

		// LogUtils.log("Colt1911Model", "P: " +
		// client.getBreathingHandler().getProgress(null));

		traslateRotate("all", matrix);
		lerpToTransform("aim", matrix, aimProgress);
		lerpToTransform("sprint", matrix, client.getClient().getSprintHandler().getProgress(stack.getItem()));

		matrix.pushPose();
		
		traslateRotate("rightarmgunaroundpointvisualizer", matrix);
		
		/*matrix.pushPose();
		RenderHelper.renderSpecial(matrix, stack, buffer, light, "com.jgpg:special/debug");
		matrix.popPose();*/
		/*Minecraft.getInstance().getItemRenderer().render(new ItemStack(ItemRegistries.MUSKET_BULLET.get()), 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, light, 
				OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getItemRenderer().getItemModelShaper()
				.getItemModel(ItemRegistries.MUSKET_BULLET.get()));*/
		
		matrix.popPose();
		
		// Right Arm
		matrix.pushPose();

		//traslateRotate("rightarmgunaroundpoint", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("rightarm", matrix);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT, client
				.getClient().playerRenderer);

		matrix.popPose();

		// Stick
		
		if(renderStick) {
			matrix.pushPose();
			
			//traslateRotate("leftarmstick", matrix);
			traslateRotate("stick", matrix);
			traslateRotate("leftarm", matrix);
			
			RenderHelper.renderItem(matrix, new ItemStack(Items.STICK), buffer, light);
			
			matrix.popPose();
		}
		
		// Left Arm
		matrix.pushPose();

		traslateRotate("leftarmgun", matrix);
		traslateRotate("leftarmstick", matrix);
		traslateRotate("leftarm", matrix);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT, client
				.getClient().playerRenderer);

		matrix.popPose();

		// Gun
		matrix.pushPose();

		//traslateRotate("rightarmgunaroundpoint", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("leftarmgun", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("gun", matrix);
		/*Minecraft.getInstance().getItemRenderer().render(new ItemStack(ItemRegistries.PIRATERIFLE.get()), 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, light, 
				OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getItemRenderer().getItemModelShaper()
				.getItemModel(ItemRegistries.PIRATERIFLE.get()));*/
		RenderHelper.renderItem(matrix, stack, buffer, light);

		matrix.popPose();

		// Hammer
		matrix.pushPose();

		// Shooting

		//traslateRotate("rightarmgunaroundpoint", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("leftarmgun", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("gun", matrix);
		traslateRotate("hammer", matrix);

		RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.THAMMER);

		matrix.popPose();

		matrix.popPose();
		
	}

	@Override
	public void onAnimationStart(Animation anim) {
		super.onAnimationStart(anim);
		renderStick = false;
	}

	@Override
	public void onAnimationTick(Animation anim, float prevTick, float tick) {
		if(anim == HIT) {
			if(tick == 24) {
				Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
			} else if(tick == 27) {
				MeleeHelper.hit(client.getClient(), Config.SERVER.trabuco.meleeDamage.get(),
						2);
			}
		} else if(anim == RELOAD) {
			if(tick == 30) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
			} else if(tick == 85) {
				Utils.playSoundOnServer(SoundRegistries.MULTIPLE_BULLETS_HITTING_METAL.get());
			} else if(tick == 100) {
				renderStick = true;
			} else if(tick == 128) {
				Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
			} else if(tick == 153) {
				Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
			} else if(tick == 177) {
				Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
			} else if(tick == 200) {
				renderStick = false;
			} else if(tick == 224) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
			} else if(tick == 244) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
				
				// Remove reload materials
				if(ReloadUnloadUtils.processReloadIngredients(getGun().getAmmo())) {
					ReloadUnloadUtils.addBullet();
				}
			}
		}
	}

	@Override
	public List<Animation> getAnimations() {
		return Utils.of(LOOK, HIT, RELOAD);
	}
	
}
