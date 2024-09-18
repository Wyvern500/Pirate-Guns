package com.jg.jgpg.client.jgmodel.itemmodel.itemmodels;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.BakedModelHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.AbstractGunModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.model.item.PirateRifleModModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoseHandlers;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.network.AddItemMessage;
import com.jg.jgpg.network.UpdateExtraDataMessage;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class PirateRifleModel extends AbstractGunModel {

	public static final String QUITSCOPENOTIFICATION = "quitScope";
	
	Animation LOOK;
	Animation HIT;
	Animation RELOAD;
	Animation QUITSCOPE;
	Animation PUTSCOPE;
	boolean renderStick;

	public PirateRifleModel(AbstractClientHandler client) {
		super(client);
	}

	@Override
	public boolean canReload(Player player) {
		int bullets = InventoryUtils.getCountForItem(player, ItemRegistries.MUSKET_BULLET.get());
		int gunpowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
		LogUtils.log("PirateRifleModel", "Gunpowder: " + gunpowder + " bullets: " + bullets);
		return NBTUtils.getBullets(player.getMainHandItem()) <= 0 && gunpowder >= 2 && bullets > 0;
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
	public void processNbt(CompoundTag nbt) {
		if(nbt.contains(QUITSCOPENOTIFICATION)) {
			if(nbt.getBoolean(QUITSCOPENOTIFICATION)) {
				animator.setAnimation(QUITSCOPE);
				animator.play();
			} else {
				if(InventoryUtils.getCountForItem(Minecraft.getInstance().player, Items.SPYGLASS)
						>= 0) {
					animator.setAnimation(PUTSCOPE);
					animator.play();
				}
			}
		}
	}

	@Override
	public ToShowModModel getModModel(BakedModel origin) {
		return new PirateRifleModModel(origin);
	}

	@Override
	public void initParts() {
		addPart(new JgModelPart("aim", -0.6906022f, 0.7857981f, 0.24599682f, -0.17104232f, -0.09337489f, 0.0f));
		addPart(new JgModelPart("all", -0.7599996f, 0.29999998f, 0.69999963f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("breathing", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("gun", 1.3399991f, -1.1199993f, -1.8799987f, 0.06981317f, 0.06981318f, 0.0f));
		addPart(new JgModelPart("hammer", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("scope", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarmscope", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarm", 0.4199998f, -0.57999974f, -1.4999988f, 0.0f, -1.2217312f, 0.0f));
		addPart(new JgModelPart("leftarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("leftarmstick", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("muzzle", 1.1999992f, -0.7199996f, -4.680012f, 0.0f, 0.0f, 0.0f));
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
				.translate(getPart("leftarm"), 10, "easeInOutSine", 0.0202653f, 0.13984841f, 0.19977263f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 12, "easeInOutSine", -0.35999992f, -0.33999994f, 0.6399997f)
				.rotate(getPart("all"), 12, "easeInOutSine", -3.7252903E-9f, 1.1175871E-8f, 0.41887894f)
				.translate(getPart("leftarm"), 12, "easeInOutSine", 0.0202653f, 0.13984841f, 0.19977263f)
				.translate(getPart("rightarmgun"), 12, "easeInOutSine", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 12, "easeInOutSine", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 17, "easeInOutBack", -0.57999974f, -0.6199997f, 0.1f)
				.rotate(getPart("all"), 17, "easeInOutBack", 0.13962632f, 1.1175871E-8f, 0.41887894f)
				.translate(getPart("leftarm"), 17, "easeInOutBack", 0.0202653f, 0.13984841f, 0.19977263f)
				.translate(getPart("rightarmgun"), 17, "easeInOutBack", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 17, "easeInOutBack", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 22, "easeInOutSine", -0.57999974f, -0.6199997f, 0.1f)
				.rotate(getPart("all"), 22, "easeInOutSine", 0.13962632f, 1.1175871E-8f, 0.41887894f)
				.translate(getPart("leftarm"), 22, "easeInOutSine", 0.0202653f, 0.13984841f, 0.19977263f)
				.translate(getPart("rightarmgun"), 22, "easeInOutSine", 0.02f, -0.11999998f, 0.4999998f)
				.rotate(getPart("rightarmgun"), 22, "easeInOutSine", -5.0701573E-4f, 3.7252903E-9f, 0.0f)
				.translate(getPart("all"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("all"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		RELOAD = new Animation("Reload")
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 10, "empty", -0.3999999f, -0.28f, 0.69999963f)
				.rotate(getPart("leftarm"), 10, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 20, "easeInOutSine", 1.459999f, -1.6799988f, -0.6399997f)
				.rotate(getPart("rightarmgun"), 20, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 20, "easeInOutSine", -0.3799999f, 0.17999999f, 0.57999974f)
				.rotate(getPart("leftarm"), 20, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", 1.459999f, -1.6799988f, -0.6399997f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", -0.31999996f, 0.6399997f, 0.28000003f)
				.rotate(getPart("leftarm"), 30, "easeInOutSine", -0.90757155f, 3.7252903E-9f, -0.6283186f)
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
				.translate(getPart("leftarm"), 120, "easeInOutSine", -0.39999986f, 0.3799999f, 0.5399998f)
				.rotate(getPart("leftarm"), 120, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 130, "easeInOutBack", -0.3399999f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 130, "easeInOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 135, "easeInOutSine", -0.3399999f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 135, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 145, "easeInOutSine", -0.39999986f, 0.3799999f, 0.5399998f)
				.rotate(getPart("leftarm"), 145, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 155, "easeInOutBack", -0.3399999f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 155, "easeInOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 160, "easeInOutSine", 1.459999f, -1.6799988f, -0.6399997f)
				.rotate(getPart("rightarmgun"), 160, "easeInOutSine", 1.3613577f, 0.83775836f, -0.94247824f)
				.translate(getPart("leftarm"), 160, "easeInOutSine", -0.3399999f, 0.24000002f, 0.5399998f)
				.rotate(getPart("leftarm"), 160, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 170, "easeInOutSine", 0.039999984f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 170, "easeInOutSine", 0.0f, 0.0f, 0.9424782f)
				.translate(getPart("leftarm"), 170, "easeInOutSine", -0.29999995f, 0.24000002f, 0.57999974f)
				.rotate(getPart("leftarm"), 170, "easeInOutSine", 0.0f, 0.0f, 0.0f)
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
				.translate(getPart("rightarmgun"), 245, "easeInOutSine", 0.039999984f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 245, "easeInOutSine", 0.0f, 0.0f, 0.9424782f)
				.translate(getPart("leftarm"), 245, "easeInOutCubic", -0.29999995f, 0.24000002f, 0.57999974f)
				.rotate(getPart("leftarm"), 245, "easeInOutCubic", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 250, "empty", -0.20499994f, 0.18000002f, 0.71499956f)
				.rotate(getPart("leftarm"), 250, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 260, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		QUITSCOPE = new Animation("QuitScope")
				.translate(getPart("leftarmscope"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarmscope"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarmscope"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.4799998f, -0.09999999f, 0.0f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.0f, 0.0f, 0.9773848f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.13999999f, 0.09999999f, 0.22000003f)
				.rotate(getPart("leftarm"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 15, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarmscope"), 15, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 15, "easeInOutSine", 0.4799998f, -0.09999999f, 0.0f)
				.rotate(getPart("rightarmgun"), 15, "easeInOutSine", 0.0f, 0.0f, 0.9773848f)
				.translate(getPart("leftarm"), 15, "easeInOutSine", -0.13999999f, 0.09999999f, 0.22000003f)
				.rotate(getPart("leftarm"), 15, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 25, "easeOutBack", -0.3799999f, 0.07999999f, 0.0f)
				.rotate(getPart("leftarmscope"), 25, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 35, "easeInOutSine", -0.3799999f, -0.45999983f, 0.77999955f)
				.rotate(getPart("leftarmscope"), 35, "easeInOutSine", 0.34906584f, 0.0f, 0.0f)
				.translate(getPart("scope"), 35, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 35, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 60, "easeInOutSine", -0.3799999f, -0.45999983f, 0.77999955f)
				.rotate(getPart("leftarmscope"), 60, "easeInOutSine", 0.34906584f, 0.0f, 0.0f)
				.translate(getPart("scope"), 60, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 60, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 70, "easeInOutSine", -0.3799999f, 0.39999992f, 0.55999976f)
				.rotate(getPart("leftarmscope"), 70, "easeInOutSine", 4.0978193E-8f, 0.0f, 0.0f)
				.translate(getPart("scope"), 70, "easeInOutSine", 0.13999999f, 0.059999995f, -0.11999998f)
				.rotate(getPart("scope"), 70, "easeInOutSine", -0.034906596f, 0.41887897f, 0.0f)
				.translate(getPart("leftarmscope"), 95, "easeInOutSine", -0.3799999f, 0.39999992f, 0.55999976f)
				.rotate(getPart("leftarmscope"), 95, "easeInOutSine", 4.0978193E-8f, 0.0f, 0.0f)
				.translate(getPart("scope"), 95, "easeInOutSine", 0.13999999f, 0.059999995f, -0.11999998f)
				.rotate(getPart("scope"), 95, "easeInOutSine", -0.034906596f, 0.41887897f, 0.0f)
				.translate(getPart("leftarmscope"), 105, "easeInOutSine", -0.3799999f, -0.57999974f, 0.55999976f)
				.rotate(getPart("leftarmscope"), 105, "easeInOutSine", 4.0978193E-8f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 110, "easeInOutSine", -0.3799999f, -0.57999974f, 0.55999976f)
				.rotate(getPart("leftarmscope"), 110, "easeInOutSine", 4.0978193E-8f, 0.0f, 0.0f)
				.translate(getPart("scope"), 110, "easeInOutSine", 0.13999999f, 0.059999995f, -0.11999998f)
				.rotate(getPart("scope"), 110, "easeInOutSine", -0.034906596f, 0.41887897f, 0.0f)
				.translate(getPart("rightarmgun"), 110, "easeInOutSine", 0.4799998f, -0.09999999f, 0.0f)
				.rotate(getPart("rightarmgun"), 110, "easeInOutSine", 0.0f, 0.0f, 0.9773848f)
				.translate(getPart("leftarm"), 110, "easeInOutSine", -0.13999999f, 0.09999999f, 0.22000003f)
				.rotate(getPart("leftarm"), 110, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarmscope"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarmscope"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
		PUTSCOPE = new Animation("PutScope")
				.translate(getPart("scope"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgunpump"), 0, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 10, "easeInOutSine", -1.0199994f, 0.11999998f, 0.0f)
				.rotate(getPart("scope"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.35999992f, -0.09999999f, 0.0f)
				.rotate(getPart("rightarmgun"), 10, "easeInOutSine", -3.7252903E-9f, 1.1175871E-8f, 1.0821047f)
				.translate(getPart("leftarm"), 10, "easeInOutSine", -0.69999963f, -0.55999976f, 0.22000003f)
				.rotate(getPart("leftarm"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 30, "easeInOutSine", -1.0199994f, 0.11999998f, 0.0f)
				.rotate(getPart("scope"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 30, "easeInOutSine", 0.35999992f, -0.09999999f, 0.0f)
				.rotate(getPart("rightarmgun"), 30, "easeInOutSine", -3.7252903E-9f, 1.1175871E-8f, 1.0821047f)
				.translate(getPart("leftarm"), 30, "easeInOutSine", -0.69999963f, -0.55999976f, 0.22000003f)
				.rotate(getPart("leftarm"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 40, "easeInOutSine", 0.0f, 0.0f, 0.07999999f)
				.rotate(getPart("scope"), 40, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 40, "easeInOutSine", -0.28f, 0.04000002f, 0.28f)
				.rotate(getPart("leftarm"), 40, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 45, "easeInOutSine", 0.0f, 0.0f, 0.07999999f)
				.rotate(getPart("scope"), 45, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 45, "easeInOutSine", -0.28f, 0.04000002f, 0.28f)
				.rotate(getPart("leftarm"), 45, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 55, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 55, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 55, "easeOutBack", -0.28f, 0.04000002f, 0.15999998f)
				.rotate(getPart("leftarm"), 55, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 60, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 60, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 60, "easeOutBack", -0.28f, 0.04000002f, 0.15999998f)
				.rotate(getPart("leftarm"), 60, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 70, "easeInOutSine", -0.6399997f, 0.18000004f, 0.28f)
				.rotate(getPart("leftarm"), 70, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 75, "easeInOutSine", -0.6399997f, 0.18000004f, 0.28f)
				.rotate(getPart("leftarm"), 75, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgunpump"), 78, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgunpump"), 82, "empty", 0.0f, -0.04f, 0.0f)
				.translate(getPart("rightarmgun"), 85, "easeInOutSine", 0.35999992f, -0.09999999f, 0.0f)
				.rotate(getPart("rightarmgun"), 85, "easeInOutSine", -3.7252903E-9f, 1.1175871E-8f, 1.0821047f)
				.translate(getPart("leftarm"), 85, "easeOutBack", -0.28000003f, 0.14000002f, 0.28f)
				.rotate(getPart("leftarm"), 85, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgunpump"), 87, "empty", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 90, "easeInOutSine", 0.35999992f, -0.09999999f, 0.0f)
				.rotate(getPart("rightarmgun"), 90, "easeInOutSine", -3.7252903E-9f, 1.1175871E-8f, 1.0821047f)
				.translate(getPart("leftarm"), 90, "easeOutBack", -0.28000003f, 0.14000002f, 0.28f)
				.rotate(getPart("leftarm"), 90, "easeOutBack", 0.0f, 0.0f, 0.0f)
				.translate(getPart("scope"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("scope"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("rightarmgun"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("rightarmgun"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.translate(getPart("leftarm"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.rotate(getPart("leftarm"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f)
				.end();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, BufferSource impl,
			PoseStack matrix, int light) {
		boolean hasScope = NBTUtils.getExtraData(stack).getBoolean(Constants.HASSCOPE);
		
		if(client.getAimHandler().getProgress(item) > 0.9f && hasScope) {
			return;
		}
		// Check if the gun has bullets
		boolean outOfBullets = NBTUtils.getBullets(stack) == 0;
		if (outOfBullets) {
			getPart("hammer").getDtransform().set(0.0f, 0.20000002f, 0.09999999f, -1.5009842f, 0.0f, 0.0f);
		} else {
			getPart("hammer").getDtransform().set(0, 0, 0, 0, 0, 0);
		}
		
		if(hasScope) {
			getPart("aim").getDtransform().set(-0.6906022f, 0.7857981f, 0.24599682f, -0.17104232f, 
					-0.09337489f, 0.0f);
		} else {
			getPart("aim").getDtransform().set(-0.68460226f, 0.85779786f, 0.36599672f, -0.17104232f,
					-0.09337489f, 0.0f);
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

		traslateRotate("leftarmscope", matrix);
		traslateRotate("leftarmgun", matrix);
		traslateRotate("leftarmstick", matrix);
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

		RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.PRHAMMER);

		matrix.popPose();
		
		if(hasScope) {
			// Scope
			matrix.pushPose();
	
			// Shooting
	
			traslateRotate("leftarmscope", matrix);
			traslateRotate("leftarmgun", matrix);
			traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
			traslateRotate("rightarmgunpump", matrix);
			traslateRotate("gun", matrix);
			traslateRotate("scope", matrix);
	
			RenderHelper.renderSpecial(matrix, stack, buffer, light, Constants.PRSCOPE);
	
			matrix.popPose();
		}

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
			if (tick == 14) {
				Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
			} else if(tick == 17) {
				MeleeHelper.hit(client.getClient(),
						Config.SERVER.pirateRifle.meleeDamage.get(), 2);
			}
		} else if(anim == RELOAD) {
			if(tick == 30) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
			} else if(tick == 85) {
				Utils.playSoundOnServer(SoundRegistries.INSERTING_BULLET.get());
			} else if(tick == 105) {
				renderStick = true;
			} else if(tick == 126) {
				Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
			} else if(tick == 151) {
				Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
			} else if(tick == 175) {
				Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
			} else if(tick == 195) {
				renderStick = false;
			} else if(tick == 220) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
			} else if(tick == 242) {
				Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
				
				// Remove reload materials
				if(ReloadUnloadUtils.processReloadIngredients(getGun().getAmmo())) {
					ReloadUnloadUtils.addBullet();
				}
			}
		} else if(anim == QUITSCOPE) {
			if(tick == 15) {
				Utils.playSoundOnServer(SoundRegistries.REMOVE_SOMETHING_METALIC.get());
			} else if(tick == 110) {
				CompoundTag nbt = NBTUtils.getExtraData(Utils.handStack());
				nbt.putBoolean(Constants.HASSCOPE, false);
				PirateGuns.channel.sendToServer(new UpdateExtraDataMessage(nbt));
				BakedModelHandler.INSTANCE.update(Utils.handStack());
				
				PirateGuns.channel.sendToServer(new AddItemMessage(Utils.item(Items.SPYGLASS), 
						InventoryUtils.distributeItem(Items.SPYGLASS, 1)));
			}
		} else if(anim == PUTSCOPE) {
			if(tick == 20) {
				CompoundTag nbt = NBTUtils.getExtraData(Utils.handStack());
				nbt.putBoolean(Constants.HASSCOPE, true);
				PirateGuns.channel.sendToServer(new UpdateExtraDataMessage(nbt));
				BakedModelHandler.INSTANCE.update(Utils.handStack());
				
				InventoryUtils.consumeItem(Items.SPYGLASS);
			} else if(tick == 46) {
				Utils.playSoundOnServer(SoundRegistries.REMOVE_SOMETHING_METALIC_2.get());
			} else if(tick == 80) {
				Utils.playSoundOnServer(SoundRegistries.HITTING_GUN.get());
			}
		}
	}

	@Override
	public List<Animation> getAnimations() {
		return Utils.of(LOOK, HIT, RELOAD, QUITSCOPE, PUTSCOPE);
	}
	
}
