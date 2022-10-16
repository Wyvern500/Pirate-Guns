package com.jg.pirateguns.animations.gunmodels;

import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.utils.NBTUtils;
import com.jg.pirateguns.utils.Paths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PiratePistolGunModel extends GunModel {
	
	// Display
	
	// Right arm
	// Pos: -0.3, 0, -0.1
	// Rot: -0.6, 0, 0.6
	
	// Gun
	// Pos: 0.8, -0.6, -1.1
	// Rot: 0.1, 0, 0
	
	// Hammer
	// Up
	// Pos: 0.8, -0.6, -1.1
	// Rot: 0.1, 0, 0
	// Down
	// Pos: 0, -0.523, 0.6113
	// Rot: -90.3522, 0, 0
	
	// Aim -0.534f, 0.32f, 0, -0.069813f, 0, 0
	// Pos: -0.534, 0.32, 0
	// Rot: -0.069813, 0, 0
	
	// Sprint
	// Pos: 0, 0, 0.209
	// Rot: 0.575958, 0, 0
	
	private final ModelResourceLocation HAMMER = new ModelResourceLocation(Paths.PPHAMMER, "inventory");
	
	public PiratePistolGunModel(ClientHandler client) {
		super(new GunModelPart[] { 
				new GunModelPart("rightarm", -0.3f, 0, -0.1f, -0.6f, 0, 0.6f), 
				new GunModelPart("gun", 0.8f, -0.6f, -1.1f, 0.1f, 0, 0),
				new GunModelPart("hammer", 0.8f, -0.6f, -1.1f, 0.1f, 0, 0),
				new GunModelPart("aim", -0.534f, 0.32f, 0, -0.069813f, 0, 0), 
				new GunModelPart("sprint", 0, 0, 0.209f, 0.575958f, 0, 0),
				new GunModelPart("recoil") }, ItemRegistries.PIRATEPISTOL.get(), client);
	}
	
	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		matrix.pushPose();
		lerpTransform(matrix, client.getAimHandler().getProgress(), parts[3].getDTransform());
		lerpTransform(matrix, client.getSprintHandler().getProgress(), parts[4].getDTransform());
		lerpTransform(matrix, client.getRecoilHandler().getProgress(), parts[5].getTransform());// client.getRecoilHandler().getProgress()
		//LogUtils.getLogger().info("Progress: " + client.getRecoilHandler().getProgress());
		matrix.pushPose();
		renderArm(player, buffer, matrix, light, HumanoidArm.RIGHT, parts[0].getCombined());
		matrix.popPose();
		matrix.pushPose();
		renderItem(player, stack, buffer, matrix, light, parts[1].getCombined());
		matrix.popPose();
		matrix.pushPose();
		renderModel(player, stack, buffer, matrix, light, Minecraft.getInstance()
				.getModelManager().getModel(HAMMER), parts[2].getCombined());
		matrix.popPose();
		matrix.popPose();
	}
	
	@Override
	public GunModelPart getGunModelPart() {
		return parts[1];
	}

	@Override
	public void tick(Player player, ItemStack stack) {
		if(hasChanges) {
			if(NBTUtils.getLoaded(stack)) {
				parts[2].getDTransform().setPos(0.8f, -0.6f, -1.1f);
				parts[2].getDTransform().setRot(0.1f, 0f, 0f);
			} else {
				parts[2].getDTransform().setPos(0f, -0.523f, 0.6113f);
				parts[2].getDTransform().setRot(-1.576943f, 0f, 0f);
			}
			hasChanges = false;
		}
	}

	@Override
	public void reload(Player player, ItemStack stack) {
		markChanges();
		LogUtils.getLogger().info("reload");
	}
	
}
