package com.jg.jgpg.client.jgmodel.itemmodel.itemmodels;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.animation.RepetitiveAnimation;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handler.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.AbstractGunModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.model.item.PrimitiveRevolverModModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoseHandlers;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.common.network.Networking;
import com.jg.jgpg.common.network.SetLoadedMessage;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.*;
import com.jg.jgpg.utils.InventoryUtils.InvData;
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

public class PrimitiveRevolverModel extends AbstractGunModel {

	Animation LOOK;
	Animation HIT;
	Animation AFTERSHOOT;
	RepetitiveAnimation RELOAD;
	boolean renderStick;

	public PrimitiveRevolverModel(AbstractClientHandler client, Item item) {
		super(client, item);
	}

	@Override
	public boolean canReload(Player player) {
		int bullets = NBTUtils.getBullets(player.getMainHandItem());
		int toReload = 6 - bullets;
		if(toReload <= 0) {
			return false;
		}
		InvData data = InventoryUtils.getTotalCountAndIndexForItem(player, ItemRegistries
				.MUSKET_BULLET.get(), toReload);
		int gunpowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
		if(data.getTotal() > 0 && gunpowder > 0) {
			RELOAD.setCycles(Math.min(data.getTotal(), gunpowder) - 1);
			return true;
		}
		return false;
	}

	@Override
	public void reload(Player player) {
		RELOAD.build();
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
	public void afterUse(Player player) {
		// Updating loaded
		Networking.sendToServer(new SetLoadedMessage(false));

		if(NBTUtils.getBullets(Utils.handStack()) > 1) {
			animator.setAnimation(AFTERSHOOT);
			animator.play();
		}
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
		return new PrimitiveRevolverModModel(origin);
	}

	@Override
	public void initParts() {
		addPart(new JgModelPart("aim", -0.5966027f, 0.8397974f, 0.3779959f, -0.13613571f, -0.08988424f, 0.0f));
		addPart(new JgModelPart("all", -0.7599996f, 0.29999998f, 0.69999963f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("breathing", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("chamber", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("gun", 1.399999f, -1.2999991f, -2.4199982f, 0.06981317f, 0.06981318f, 0.0f));
		addPart(new JgModelPart("hammer", 0.0f, 0.02f, 0.65999967f, -1.4660776f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarm", 0.4199998f, -0.5199998f, -1.359999f, 0.0f, -1.2217312f, 0.0f));
		addPart(new JgModelPart("leftarmchamber", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarmstick", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("muzzle", 1.0999993f, -0.8199995f, -3.0199976f, 0.0f, 0.0f, 0.0f));
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
				.translate(getPart("all"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("all"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("all"), 10, "easeInOutSine", -0.35999992f, -0.33999994f, 0.6399997f)
				.rotate(getPart("all"), 10, "easeInOutSine", -3.7252903E-9f, 1.1175871E-8f, 0.41887894f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.11973468f, -1.5157461E-4f, 0.19977263f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 12, "easeInOutSine", -0.35999992f, -0.33999994f, 0.6399997f)
				.rotate(getPart("all"), 12, "easeInOutSine", -3.7252903E-9f, 1.1175871E-8f, 0.41887894f)
				.translate(getPart("leftarm"), 12, "easeInOutSine", -0.11973468f, -1.5157461E-4f, 0.19977263f)
				.translate(getPart("rightarmgun"), 12, "easeInOutSine", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 12, "easeInOutSine", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 17, "easeInOutBack", -0.65999967f, -0.55999976f, 0.22000007f)
				.rotate(getPart("all"), 17, "easeInOutBack", 0.2443461f, 1.1175871E-8f, 0.41887894f)
				.translate(getPart("leftarm"), 17, "easeInOutSine", -0.11973468f, -1.5157461E-4f, 0.19977263f)
				.translate(getPart("rightarmgun"), 17, "easeInOutBack", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 17, "easeInOutBack", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 22, "easeInOutSine", -0.65999967f, -0.55999976f, 0.22000007f)
				.rotate(getPart("all"), 22, "easeInOutSine", 0.2443461f, 1.1175871E-8f, 0.41887894f)
				.translate(getPart("leftarm"), 22, "easeInOutSine", -0.11973468f, -1.5157461E-4f, 0.19977263f)
				.translate(getPart("rightarmgun"), 22, "easeInOutSine", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 22, "easeInOutSine", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("all"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		AFTERSHOOT = new Animation("AfterShoot")
				.translate(getPart("rightarmgunpump"), 0, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.20000002f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 15, "easeInOutSine", -0.09999999f, -0.02f, 0.0f)
				.translate(getPart("rightarmgunpump"), 20, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 20, "easeInOutSine", -0.09999999f, -0.02f, 0.0f)
				.translate(getPart("rightarmgunpump"), 23, "empty", -0.02f, 0.02f, 0.0f)
				.translate(getPart("rightarmgunpump"), 30, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", -0.29999998f, 0.09999999f, 0.0f)
				.translate(getPart("leftarm"), 40, "easeInOutSine", -0.17999999f, 0.22000003f, 0.35999992f)
				.translate(getPart("leftarm"), 50, "easeInOutSine", -0.019999985f, 0.22000003f, 0.35999992f)
				.translate(getPart("rightarmgunpump"), 53, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgunpump"), 57, "empty", 0.0f, 0.0f, 0.04f)
				.translate(getPart("leftarm"), 60, "easeInOutBack", -0.019999985f, 0.22000003f, 0.4799998f)
				.translate(getPart("rightarmgunpump"), 62, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 65, "easeInOutSine", -0.019999985f, 0.22000003f, 0.4799998f)
				.translate(getPart("leftarm"), 75, "easeInOutSine", -0.24000002f, 0.22000003f, 0.4799998f)
				.translate(getPart("leftarm"), 85, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		RELOAD = new RepetitiveAnimation("Reload")
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmchamber"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.13999999f, -0.3999999f, 0.85999954f)
				.rotate(getPart("leftarm"), 10, "easeInOutSine", 0.593412f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", -0.039999984f, -0.4999998f, -0.4199999f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", 1.5009842f, 0.24434613f, -1.1170113f)
				.translate(getPart("leftarmchamber"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 20, "easeInOutSine", -0.13999999f, -0.3999999f, 0.85999954f)
				.rotate(getPart("leftarm"), 20, "easeInOutSine", 0.593412f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 20, "easeInOutSine", -0.039999984f, -0.4999998f, -0.4199999f)
				.rotate(getPart("rightarmgun"), 20, "easeInOutSine", 1.5009842f, 0.24434613f, -1.1170113f)
				.translate(getPart("leftarmchamber"), 20, "easeInOutSine", -0.02f, 0.0f, 0.059999995f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", -0.4999998f, -0.7399996f, 0.8999995f)
				.rotate(getPart("leftarm"), 30, "easeInOutSine", 0.593412f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", -0.039999984f, -0.4999998f, -0.4199999f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", 1.5009842f, 0.24434613f, -1.1170113f)
				.translate(getPart("leftarm"), 50, "easeInOutSine", -0.4999998f, -0.7399996f, 0.8999995f)
				.rotate(getPart("leftarm"), 50, "easeInOutSine", 0.593412f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 50, "easeInOutSine", -0.039999984f, -0.4999998f, -0.4199999f)
				.rotate(getPart("rightarmgun"), 50, "easeInOutSine", 1.5009842f, 0.24434613f, -1.1170113f)
				.translate(getPart("leftarm"), 60, "easeInOutSine", -0.13999999f, -0.3999999f, 0.85999954f, true)
				.rotate(getPart("leftarm"), 60, "easeInOutSine", 0.593412f, 0.0f, 0.0f, true)
				.translate(getPart("rightarmgun"), 60, "easeInOutSine", -0.039999984f, -0.4999998f, -0.4199999f, true)
				.rotate(getPart("rightarmgun"), 60, "easeInOutSine", 1.5009842f, 0.24434613f, -1.1170113f, true)
				.translate(getPart("leftarm"), 70, "easeInOutSine", -0.20000002f, 0.9399994f, 0.2200001f)
				.rotate(getPart("leftarm"), 70, "easeInOutSine", -0.76794505f, 0.0f, -1.1175871E-8f)
				.translate(getPart("leftarm"), 80, "easeInOutSine", -0.20000002f, 0.9399994f, 0.2200001f)
				.rotate(getPart("leftarm"), 80, "easeInOutSine", -0.76794505f, 0.0f, -1.1175871E-8f)
				.translate(getPart("leftarm"), 90, "easeInOutSine", -0.4999998f, 1.4901161E-8f, 0.8799995f)
				.rotate(getPart("leftarm"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 115, "easeInOutSine", -0.4999998f, 1.4901161E-8f, 0.8799995f)
				.rotate(getPart("leftarm"), 115, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 125, "easeInOutSine", -0.15999998f, 0.45999986f, 0.8799995f)
				.rotate(getPart("leftarm"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 135, "easeInCubic", -0.15999998f, 0.35999995f, 0.8799995f)
				.rotate(getPart("leftarm"), 135, "easeInCubic", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 140, "easeInOutSine", -0.11999997f, 0.32f, 0.83999956f)
				.rotate(getPart("leftarm"), 140, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 140, "easeInOutSine", -0.039999984f, -0.4999998f, -0.4199999f)
				.rotate(getPart("rightarmgun"), 140, "easeInOutSine", 1.5009842f, 0.24434613f, -1.1170113f)
				.translate(getPart("leftarm"), 150, "easeInOutSine", 0.0f, 0.28000003f, 0.8799995f)
				.rotate(getPart("leftarm"), 150, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 160, "easeOutBack", -0.28f, 0.28000003f, 0.8799995f, true)
				.rotate(getPart("leftarm"), 160, "easeOutBack", 0.0f, 0.0f, 0.0f, true)
				.translate(getPart("rightarmgun"), 160, "easeInOutSine", -0.039999984f, -0.4999998f, -0.4199999f, true)
				.rotate(getPart("rightarmgun"), 160, "easeInOutSine", 1.5009842f, 0.24434613f, -1.1170113f, true)
				.translate(getPart("leftarm"), 170, "easeInOutSine", -0.26000002f, 0.09999999f, 0.29999998f)
				.rotate(getPart("leftarm"), 170, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 170, "easeInOutSine", 0.18f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 170, "easeInOutSine", 0.0f, 0.0f, 0.9424782f)
				.translate(getPart("leftarm"), 180, "easeOutBack", -0.26000002f, 0.09999999f, 0.45999983f)
				.rotate(getPart("leftarm"), 180, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 185, "easeInOutSine", -0.26000002f, 0.09999999f, 0.45999983f)
				.rotate(getPart("leftarm"), 185, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 185, "easeInOutSine", 0.18f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 185, "easeInOutSine", 0.0f, 0.0f, 0.9424782f)
				.translate(getPart("leftarm"), 195, "easeInOutSine", -0.26000002f, 0.24000002f, 0.45999983f)
				.rotate(getPart("leftarm"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 205, "easeInOutQuad", -0.26000002f, 0.09999997f, 0.45999983f)
				.rotate(getPart("leftarm"), 205, "easeInOutQuad", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 215, "easeInOutSine", -0.26000002f, 0.24000002f, 0.45999983f)
				.rotate(getPart("leftarm"), 215, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 215, "easeInOutSine", 0.18f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 215, "easeInOutSine", 0.0f, 0.0f, 0.9424782f)
				.translate(getPart("leftarm"), 225, "easeInOutQuad", -0.26000002f, 0.09999997f, 0.45999983f)
				.rotate(getPart("leftarm"), 225, "easeInOutQuad", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 234, "easeInOutSine", -0.28982782f, 0.03454914f, 0.15892601f)
				.translate(getPart("leftarm"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, BufferSource impl,
			PoseStack matrix, int light) {
		// Check if the gun has bullets
		boolean isLoaded = NBTUtils.isLoaded(stack);
		if (!isLoaded) {
			getPart("hammer").getDtransform().set(0.0f, 0.02f, 0.65999967f, -1.4660776f, 0.0f, 0.0f);
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

		traslateRotate("all", matrix);
		lerpToTransform("aim", matrix, aimProgress);
		lerpToTransform("sprint", matrix, client.getClient().getSprintHandler()
				.getProgress(stack.getItem()));

		matrix.pushPose();
		
		traslateRotate("rightarmgunaroundpointvisualizer", matrix);
		
		Minecraft.getInstance().getItemRenderer().render(new ItemStack(ItemRegistries.MUSKET_BULLET.get()), 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, light, 
				OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getItemRenderer().getItemModelShaper()
				.getItemModel(ItemRegistries.MUSKET_BULLET.get()));
		
		matrix.popPose();
		
		// Right Arm
		matrix.pushPose();

		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("rightarm", matrix);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT, client
				.getClient().playerRenderer);

		matrix.popPose();
		
		// Left Arm
		matrix.pushPose();

		traslateRotate("leftarmgun", matrix);
		traslateRotate("leftarmstick", matrix);
		traslateRotate("leftarmchamber", matrix);
		traslateRotate("leftarm", matrix);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT, client
				.getClient().playerRenderer);

		matrix.popPose();

		// Gun
		matrix.pushPose();

		traslateRotate("leftarmgun", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("gun", matrix);
		RenderHelper.renderItem(matrix, stack, buffer, light);

		matrix.popPose();

		// Hammer
		matrix.pushPose();

		// Shooting

		traslateRotate("leftarmgun", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("gun", matrix);
		traslateRotate("hammer", matrix);

		RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.PRVHAMMER);

		matrix.popPose();
		
		// Chamber
		matrix.pushPose();

		// Shooting

		traslateRotate("leftarmgun", matrix);
		traslateRotate("leftarmchamber", matrix);
		traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
		traslateRotate("rightarmgunpump", matrix);
		traslateRotate("gun", matrix);
		traslateRotate("chamber", matrix);

		RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.PRVCHAMBER);

		matrix.popPose();

		matrix.popPose();
		
	}

	@Override
	public void onAnimationTick(Animation anim, float prevTick, float tick) {
		if(anim == RELOAD) {
			if(isRepTick(60, 70, tick, 110, RELOAD.getCycles() + 1)) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
			} else if(isRepTick(60, 140, tick, 110, RELOAD.getCycles() + 1)) {
				Utils.playSoundOnServer(SoundRegistries.INSERTING_BULLET.get());
			} else if(isRepTick(60, 151, tick, 110, RELOAD.getCycles() + 1)) {
				Utils.playSoundOnServer(SoundRegistries.REVOLVER_SINGLE_SPIN.get());
				
				// Remove reload materials
				if(ReloadUnloadUtils.processReloadIngredients(getGun().getAmmo())) {
					ReloadUnloadUtils.addBullet();
				}
			} else if(tick == RELOAD.getDuration() - 67) {
				Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
				
				// Updating loaded
				Networking.sendToServer(new SetLoadedMessage(true));
			} else if(tick == RELOAD.getDuration() - 40) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
			} else if(tick == RELOAD.getDuration() - 20) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
			}
		} else if(anim == HIT) {
			if(tick == 12) {
				Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
			} else if(tick == 15) {
				MeleeHelper.hit(client.getClient(), Config.SERVER.primitiveRevolver
						.meleeDamage.get(), 2);
			}
		} else if(anim == AFTERSHOOT) {
			if(tick == 24) {
				Utils.playSoundOnServer(SoundRegistries.REVOLVER_SINGLE_SPIN.get());
			} else if(tick == 56) {
				Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
				
				// Updating loaded
				Networking.sendToServer(new SetLoadedMessage(true));
				
				if(Config.SERVER.shouldPrvConsumeGunpowder.get()) {
					int amount = InventoryUtils.getCountForItem(Minecraft.getInstance().player, 
							Items.GUNPOWDER);
					if(amount > 0) {
						InventoryUtils.consumeItem(Items.GUNPOWDER);
					}
				}
			}
		}
	}
	
	@Override
	public List<Animation> getAnimations() {
		return Utils.of(LOOK, HIT, RELOAD, AFTERSHOOT);
	}
	
}
