package com.jg.jgpg.client.jgmodel.itemmodel.itemmodels;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handler.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.AbstractGunModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.model.item.PiratePistolModModel;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class PiratePistolModel extends AbstractGunModel {

	Animation LOOK;
	Animation HIT;
	Animation RELOAD;
	boolean renderStick;

	public PiratePistolModel(AbstractClientHandler client, Item item) {
		super(client, item);
	}

	@Override
	public boolean canReload(Player player) {
		int bullets = InventoryUtils.getCountForItem(player, ItemRegistries.MUSKET_BULLET.get());
		int gunpowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
		return NBTUtils.getBullets(player.getMainHandItem()) <= 0 && gunpowder > 0 && bullets > 0;
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
		return PlayerPoseHandlers.ONE_HANDED;
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
		return new PiratePistolModModel(origin);
	}

	@Override
	public void initParts() {
		addPart(new JgModelPart("aim", -0.31060255f, 0.7657981f, 0.24599682f, -0.17104232f, -0.023561731f, 0.0f));
		addPart(new JgModelPart("all", -0.7599996f, 0.29999998f, 0.69999963f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("breathing", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("gun", 1.2799991f, -1.0799993f, -2.2599983f, 0.10471975f, 0.034906596f, 0.0f));
		addPart(new JgModelPart("hammer", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarm", 0.25999996f, -0.47999984f, -0.91999936f, 0.0f, -1.2217312f, 0.0f));
		addPart(new JgModelPart("leftarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarmstick", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("muzzle", 0.9799994f, -0.79999954f, -3.1399975f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("recoil", 0.0f, 0.007999802f, 0.14000022f, 1.8626451E-8f, 0.0f, 0.0f));
		addPart(new JgModelPart("recoilaccumulation", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarm", 0.37999994f, -0.4599998f, -0.8799994f, -0.52359873f, 0.17453296f, 0.6981318f));
		addPart(new JgModelPart("rightarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarmgunaroundpoint", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarmgunaroundpointvisualizer", 1.1f, -1.4f, -1.2600005f, 3.7252903E-9f, 0.0f, 0.0f));
		addPart(new JgModelPart("rightarmgunpump", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("sprint", 0.89999944f, -1.379999f, 0.3f, 0.69813186f, 0.24434613f, 0.0f));
		addPart(new JgModelPart("stick", 0.85999954f, -0.93999946f, -1.0799997f, 0.2094395f, -0.1047197f, 0.27925268f));
	}

	@Override
	public void initAnimations() {
		LOOK = new Animation("Look")
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.35999992f, 0.3799999f, 0.33999994f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.66322523f, 0.0f, -0.94247824f)
				.translate(getPart("leftarm"), 40, "easeInOutSine", -0.35999992f, 0.3799999f, 0.33999994f)
				.translate(getPart("rightarmgun"), 40, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 40, "easeInOutSine", 0.66322523f, 0.0f, -0.94247824f)
				.translate(getPart("leftarm"), 50, "easeInOutSine", -0.3999999f, 0.3799999f, 0.33999994f)
				.translate(getPart("rightarmgun"), 50, "easeInOutSine", 0.43999985f, 0.11999998f, 0.059999995f)
				.rotate(getPart("rightarmgun"), 50, "easeInOutSine", 0.66322523f, 0.0f, 1.1519179f)
				.translate(getPart("leftarm"), 80, "easeInOutSine", -0.3999999f, 0.3799999f, 0.33999994f)
				.translate(getPart("rightarmgun"), 80, "easeInOutSine", 0.43999985f, 0.11999998f, 0.059999995f)
				.rotate(getPart("rightarmgun"), 80, "easeInOutSine", 0.66322523f, 0.0f, 1.1519179f)
				.translate(getPart("leftarm"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		HIT = new Animation("Hit")
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.80285174f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.41999987f, 0.24000004f, 0.35999992f)
				.translate(getPart("rightarmgun"), 12, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 12, "easeInOutSine", 0.80285174f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 12, "easeInOutSine", -0.41999987f, 0.24000004f, 0.35999992f)
				.translate(getPart("rightarmgun"), 16, "easeInOutBack", -0.24000004f, 0.20000002f, -0.4199999f)
				.rotate(getPart("rightarmgun"), 16, "easeInOutBack", 0.62831855f, 0.0f, -1.1175871E-8f)
				.translate(getPart("leftarm"), 16, "easeInOutBack", -0.5999997f, 0.059999995f, 0.45999983f)
				.translate(getPart("rightarmgun"), 20, "easeInOutSine", -0.24000004f, 0.20000002f, -0.4199999f)
				.rotate(getPart("rightarmgun"), 20, "easeInOutSine", 0.62831855f, 0.0f, -1.1175871E-8f)
				.translate(getPart("leftarm"), 20, "easeInOutSine", -0.5999997f, 0.059999995f, 0.45999983f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		RELOAD = new Animation("Reload")
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 20, "easeInOutSine", -0.039999984f, 0.019999985f, -0.07999999f)
				.rotate(getPart("leftarm"), 20, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 20, "easeInOutSine", 0.9399994f, -1.0599993f, -0.87999946f)
				.rotate(getPart("rightarmgun"), 20, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", -0.039999984f, 0.57999974f, -0.7399996f)
				.rotate(getPart("leftarm"), 30, "easeInOutSine", -0.8028517f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", 0.9399994f, -1.0599993f, -0.87999946f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 35, "easeInOutSine", -0.039999984f, 0.57999974f, -0.7399996f)
				.rotate(getPart("leftarm"), 35, "easeInOutSine", -0.8028517f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 45, "easeInOutSine", -0.67999965f, -0.49999982f, -0.07999998f)
				.rotate(getPart("leftarm"), 45, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 60, "easeInOutSine", -0.67999965f, -0.49999982f, -0.07999998f)
				.rotate(getPart("leftarm"), 60, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 70, "easeInOutSine", -0.039999984f, 0.019999985f, -0.07999999f)
				.rotate(getPart("leftarm"), 70, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 80, "easeInOutSine", 0.040000036f, -0.08000001f, -0.07999999f)
				.rotate(getPart("leftarm"), 80, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 85, "easeInOutSine", 0.040000036f, -0.08000001f, -0.07999999f)
				.rotate(getPart("leftarm"), 85, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 95, "easeInOutSine", 0.060000032f, -0.10000001f, -0.07999999f)
				.rotate(getPart("leftarm"), 95, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 100, "easeInOutSine", 0.060000032f, -0.10000001f, -0.07999999f)
				.rotate(getPart("leftarm"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 110, "easeInOutSine", -0.67999965f, -0.49999982f, -0.07999998f)
				.rotate(getPart("leftarm"), 110, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 130, "easeInOutSine", -0.67999965f, -0.49999982f, -0.07999998f)
				.rotate(getPart("leftarm"), 130, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 140, "easeInSine", -0.13999996f, 0.17999999f, -0.07999999f)
				.rotate(getPart("leftarm"), 140, "easeInSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 150, "easeOutBack", -0.059999935f, 0.02f, -0.07999999f)
				.rotate(getPart("leftarm"), 150, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 160, "easeInSine", -0.13999996f, 0.17999999f, -0.07999999f)
				.rotate(getPart("leftarm"), 160, "easeInSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 170, "easeOutBack", -0.059999935f, 0.02f, -0.07999999f)
				.rotate(getPart("leftarm"), 170, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 170, "easeInOutSine", 0.9399994f, -1.0599993f, -0.87999946f)
				.rotate(getPart("rightarmgun"), 170, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 180, "easeInOutSine", -0.67999965f, -0.49999982f, -0.07999998f)
				.rotate(getPart("leftarm"), 180, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 180, "easeInOutSine", 0.29999998f, 0.29999998f, 0.26000002f)
				.rotate(getPart("rightarmgun"), 180, "easeInOutSine", 0.0f, 1.1175871E-8f, 0.8726649f)
				.translate(getPart("leftarm"), 195, "easeInOutSine", -0.67999965f, -0.49999982f, -0.07999998f)
				.rotate(getPart("leftarm"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 195, "easeInOutSine", 0.29999998f, 0.29999998f, 0.26000002f)
				.rotate(getPart("rightarmgun"), 195, "easeInOutSine", 0.0f, 1.1175871E-8f, 0.8726649f)
				.translate(getPart("leftarm"), 205, "easeInOutSine", -0.22000004f, 0.31999996f, 0.3f)
				.rotate(getPart("leftarm"), 205, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 205, "easeInOutSine", 0.29999998f, 0.29999998f, 0.26000002f)
				.rotate(getPart("rightarmgun"), 205, "easeInOutSine", 0.0f, 1.1175871E-8f, 0.8726649f)
				.translate(getPart("leftarm"), 215, "easeInOutSine", -0.16000001f, 0.29999998f, 0.4199999f)
				.rotate(getPart("leftarm"), 215, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 225, "easeInOutSine", -0.16000001f, 0.3999999f, 0.4199999f)
				.rotate(getPart("leftarm"), 225, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 235, "easeInSine", -0.16000001f, 0.29999998f, 0.4199999f)
				.rotate(getPart("leftarm"), 235, "easeInSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 245, "easeInOutSine", -0.16000001f, 0.3999999f, 0.4199999f)
				.rotate(getPart("leftarm"), 245, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 255, "easeInSine", -0.16000001f, 0.29999998f, 0.4199999f)
				.rotate(getPart("leftarm"), 255, "easeInSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 255, "easeInOutSine", 0.29999998f, 0.29999998f, 0.26000002f)
				.rotate(getPart("rightarmgun"), 255, "easeInOutSine", 0.0f, 1.1175871E-8f, 0.8726649f)
				.translate(getPart("leftarm"), 265, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 265, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 265, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 265, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, BufferSource impl,
			PoseStack matrix, int light) {
		// Check if the gun has bullets
		boolean outOfBullets = NBTUtils.getBullets(stack) == 0;
		if (outOfBullets) {
			getPart("hammer").getDtransform().set(0.0f, -0.07999999f, 0.55999976f, -1.3264511f, 0.0f, 0.0f);
			//getPart("slider").getDtransform().set(0, 0, 0.12f, 0, 0, 0);
		} else {
			getPart("hammer").getDtransform().set(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
			//getPart("slider").getDtransform().set(0, 0, 0, 0, 0, 0);
			/*getPart("aim").getDtransform().set(-0.31700218f, 0.76979804f, 0.24599682f, -0.17104232f, -0.08639355f,
					0.0f);*/
		}

		matrix.pushPose();

		Easing easing = EasingHandler.INSTANCE.getEasing("easeOutBack");

		if (client.getClient().shouldRenderDefault()) {
			traslateRotate("recoil", matrix);
		}

		float aimProgress = client.getAimHandler().getProgress(item);
		
		shootAndRecoilTransformation(matrix, easing);

		float shootProgress = client.getClient().getCooldownHandler()
				.getCooldownPercent(item, 0);
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
		lerpToTransform("sprint", matrix, client.getClient().getSprintHandler()
				.getProgress(item));

		matrix.pushPose();
		
		traslateRotate("rightarmgunaroundpointvisualizer", matrix);
		
		/*matrix.pushPose();
		RenderHelper.renderSpecial(matrix, stack, buffer, light, "com.jgpg:special/debug");
		matrix.popPose();*/
		Minecraft.getInstance().getItemRenderer().render(new ItemStack(ItemRegistries.MUSKET_BULLET.get()), 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, light, 
				OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getItemRenderer().getItemModelShaper()
				.getItemModel(ItemRegistries.MUSKET_BULLET.get()));
		
		matrix.popPose();
		
		// Right Arm
		matrix.pushPose();

		//traslateRotate("rightarmgunaroundpoint", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("rightarm", matrix);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0,
				HumanoidArm.RIGHT, client.getClient().playerRenderer);

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
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0,
				HumanoidArm.LEFT, client.getClient().playerRenderer);

		matrix.popPose();

		// Gun
		matrix.pushPose();

		//traslateRotate("rightarmgunaroundpoint", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("leftarmgun", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("gun", matrix);
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

		RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.PPHAMMER);

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
		if(anim == RELOAD) {
			if(tick == 27) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
			} else if(tick == 73) {
				Utils.playSoundOnServer(SoundRegistries.INSERTING_BULLET.get());
			} else if(tick == 130) {
				renderStick = true;
			} else if(tick == 143) {
				Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
			} else if(tick == 160) {
				Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
			} else if(tick == 180) {
				renderStick = false;
			} else if(tick == 208) {
				Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
			} else if(tick == 224) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
			} else if(tick == 244) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
				
				// Remove reload materials
				if(ReloadUnloadUtils.processReloadIngredients(getGun().getAmmo())) {
					ReloadUnloadUtils.addBullet();
				}
			}
		} else if(anim == HIT) {
			if(tick == 10) {
				Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
			} else if(tick == 15) {
				MeleeHelper.hit(client.getClient(),
						Config.SERVER.piratePistol.meleeDamage.get(), 2);
			}
		}
	}

	@Override
	public List<Animation> getAnimations() {
		return Utils.of(LOOK, HIT, RELOAD);
	}

}
