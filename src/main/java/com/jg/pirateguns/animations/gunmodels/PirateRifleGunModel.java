package com.jg.pirateguns.animations.gunmodels;

import java.util.List;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.handlers.SoundHandler;
import com.jg.pirateguns.network.LoadBulletMessage;
import com.jg.pirateguns.network.ShootMessage;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.registries.SoundRegistries;
import com.jg.pirateguns.utils.InventoryUtils;
import com.jg.pirateguns.utils.NBTUtils;
import com.jg.pirateguns.utils.Paths;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import com.mojang.math.Quaternion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;

public class PirateRifleGunModel extends GunModel {

	private final ModelResourceLocation HAMMER = new ModelResourceLocation(Paths.PRHAMMER, "inventory");
	public Animation look;
	public Animation reload;
	
	private static String LOOK = "pirate_rifle/lookAnim.jg";
	private static String RELOAD = "pirate_rifle/reloadAnim.jg";
	
	public PirateRifleGunModel(ClientHandler client) {
		super(new GunModelPart[] {
				new GunModelPart("rightarm", -0.306f, 0, -0.1f, -0.6f, 0, 0.6f), 
				new GunModelPart("leftarm", -0.16f, 0.09f, -0.52f, 0, -1.082094f, 0), 
				new GunModelPart("gun", 0.62f, -0.5f, -0.87f, 0.030186f, 0.034906f, 0),
				new GunModelPart("hammer", 0.62f, -0.5f, -0.87f, 0.030186f, 0.034906f, 0),
				new GunModelPart("all"),
				new GunModelPart("alllessleftarm"),
				new GunModelPart("aim", -0.554f, 0.28f, 0, -0.069813f, 0, 0), 
				new GunModelPart("sprint", 0.8f, -0.64f, 0.209f, 0.506144f, 1.047198f, 0),
				new GunModelPart("recoil", 0.02f, -0.25f, 0.1f, 0.139626f, -0.069813f, -0.069813f)
		}, ItemRegistries.PIRATERIFLE.get(), client, new String[] { LOOK, RELOAD });
		
		look = new Animation("pirate_rifle/lookAnim.jg", "jgpg:pirate_rifle")
				.startKeyframe(16, "easeOutQuint")
				.translate(parts[1], -0.8599995f, 0.0f, 0.0f)
				.translate(parts[5], -0.42999986f, -0.7299996f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 1.1519179f)
				.startKeyframe(28)
				.translate(parts[1], -0.8599995f, 0.0f, 0.0f)
				.translate(parts[5], -0.42999986f, -0.7299996f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 1.1519179f)
				.startKeyframe(8, "easeOutQuint")
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 0.0f)
				.startKeyframe(16)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(28)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(12, "easeInOutQuart")
				.translate(parts[1], 0.039999984f, -0.33999994f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.55850536f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(12)
				.translate(parts[1], 0.039999984f, -0.33999994f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.55850536f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(2, "easeInOutQuint")
				.translate(parts[1], 0.13999997f, -0.059999995f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.24434613f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(2)
				.translate(parts[1], 0.13999997f, -0.059999995f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.24434613f, 0.0f, 0.0f)
				.rotate(parts[5], -0.034906585f, -0.034906585f, -0.7504918f)
				.startKeyframe(8)
				.translate(parts[1], 0.13999997f, -0.059999995f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.24434613f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(16)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 0.0f)
				.end();
		
		reload = new Animation("pirate_rifle/reloadAnim.jg", "jgpg:pirate_rifle")
				.startKeyframe(16, "easeInQuart")
				.translate(parts[3], 0.0f, -0.17f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.11999998f, -0.29999998f)
				.translate(parts[1], -0.04f, 0.30999997f, 0.44999984f)
				.translate(parts[2], 0.0f, -0.17f, -0.30999997f)
				.rotate(parts[3], 1.431171f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 1.431171f, 0.0f, 0.0f)
				.startKeyframe(4)
				.translate(parts[3], 0.0f, -0.17f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.11999998f, -0.29999998f)
				.translate(parts[1], -0.04f, 0.30999997f, 0.44999984f)
				.translate(parts[2], 0.0f, -0.17f, -0.30999997f)
				.rotate(parts[3], 1.431171f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 1.431171f, 0.0f, 0.0f)
				.startKeyframe(16, "easeInQuart")
				.translate(parts[3], 0.0f, -0.87f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.82f, -0.29999998f)
				.translate(parts[1], -0.5499998f, 0.30999997f, 0.44999984f)
				.translate(parts[2], 0.0f, -0.87f, -0.30999997f)
				.rotate(parts[3], 1.4660776f, 0.47123885f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 1.4660776f, 0.47123885f, 0.0f)
				.startKeyframe(14)
				.translate(parts[3], 0.0f, -0.87f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.82f, -0.29999998f)
				.translate(parts[1], -0.5499998f, 0.30999997f, 0.44999984f)
				.translate(parts[2], 0.0f, -0.87f, -0.30999997f)
				.rotate(parts[3], 1.4660776f, 0.47123885f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 1.4660776f, 0.47123885f, 0.0f)
				.startKeyframe(14)
				.translate(parts[3], 0.01f, -0.87f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.82f, -0.29999998f)
				.translate(parts[1], -0.5499998f, 0.10999997f, 0.44999984f)
				.translate(parts[2], 0.2f, -1.07f, -0.30999997f)
				.rotate(parts[3], 1.4660776f, 0.47123885f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 1.4660776f, 0.47123885f, 0.0f)
				.startKeyframe(14)
				.translate(parts[3], 0.01f, -0.87f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.82f, -0.29999998f)
				.translate(parts[1], -0.5499998f, 0.10999997f, 0.44999984f)
				.translate(parts[2], 0.2f, -1.07f, -0.30999997f)
				.rotate(parts[3], 1.4660776f, 0.47123885f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 1.4660776f, 0.47123885f, 0.0f)
				.startKeyframe(14)
				.translate(parts[3], 0.01f, -0.87f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.82f, -0.29999998f)
				.translate(parts[1], -0.76999956f, 0.2f, 0.6499997f)
				.translate(parts[2], 0.2f, -1.07f, -0.30999997f)
				.rotate(parts[3], 1.4660776f, 0.47123885f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.20943953f, 0.0f, 0.0f)
				.rotate(parts[2], 1.4660776f, 0.47123885f, 0.0f)
				.startKeyframe(14, "easeOutQuint")
				.translate(parts[3], 0.01f, -0.87f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.82f, -0.29999998f)
				.translate(parts[1], -0.7299996f, 0.2f, 0.6499997f)
				.translate(parts[2], 0.2f, -1.07f, -0.30999997f)
				.rotate(parts[3], 1.4660776f, 0.47123885f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.20943953f, 0.0f, 0.0f)
				.rotate(parts[2], 1.4660776f, 0.47123885f, 0.0f)
				.startKeyframe(14, "easeOutQuart")
				.translate(parts[3], 0.01f, -0.87f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.82f, -0.29999998f)
				.translate(parts[1], -0.7099996f, 0.17999999f, 0.6499997f)
				.translate(parts[2], 0.2f, -1.07f, -0.30999997f)
				.rotate(parts[3], 1.4660776f, 0.47123885f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.20943953f, 0.0f, 0.0f)
				.rotate(parts[2], 1.4660776f, 0.47123885f, 0.0f)
				.startKeyframe(16)
				.translate(parts[3], 0.0f, -0.17f, -0.30999997f)
				.translate(parts[0], 0.0f, -0.11999998f, -0.29999998f)
				.translate(parts[1], -0.04f, 0.30999997f, 0.44999984f)
				.translate(parts[2], 0.0f, -0.17f, -0.30999997f)
				.rotate(parts[3], 1.431171f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 1.431171f, 0.0f, 0.0f)
				.startKeyframe(14, "easeOutQuint")
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12, "easeInQuint")
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.13999999f, -1.2799991f, -0.11999998f)
				.translate(parts[1], -0.04f, 0.13999999f, 0.7399996f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.80285174f)
				.rotate(parts[0], 0.7155852f, -0.20943953f, 0.8552117f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.80285174f)
				.startKeyframe(14, "easeInOutQuint")
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.13999999f, -1.2799991f, -0.11999998f)
				.translate(parts[1], -0.04f, 0.13999999f, 0.77999955f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.80285174f)
				.rotate(parts[0], 0.7155852f, -0.20943953f, 0.8552117f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.80285174f)
				.startKeyframe(14, "easeInQuint")
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.13999999f, -1.2799991f, -0.11999998f)
				.translate(parts[1], -0.04f, 0.18f, 0.7399996f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.80285174f)
				.rotate(parts[0], 0.7155852f, -0.20943953f, 0.8552117f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.80285174f)
				.startKeyframe(14, "easeOutQuint")
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.13999999f, -1.2799991f, -0.11999998f)
				.translate(parts[1], -0.04f, 0.09999999f, 0.7399996f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.80285174f)
				.rotate(parts[0], 0.7155852f, -0.20943953f, 0.8552117f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.80285174f)
				.startKeyframe(14, "easeInQuint")
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.13999999f, -1.2799991f, -0.11999998f)
				.translate(parts[1], -0.04f, 0.16f, 0.7399996f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.80285174f)
				.rotate(parts[0], 0.7155852f, -0.20943953f, 0.8552117f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.80285174f)
				.startKeyframe(14, "easeOutQuint")
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.13999999f, -1.2799991f, -0.11999998f)
				.translate(parts[1], -0.04f, 0.09999999f, 0.7399996f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.80285174f)
				.rotate(parts[0], 0.7155852f, -0.20943953f, 0.8552117f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.80285174f)
				.startKeyframe(14)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.13999999f, -1.2799991f, -0.11999998f)
				.translate(parts[1], -0.36999992f, 0.09999999f, 0.7399996f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.80285174f)
				.rotate(parts[0], 0.7155852f, -0.20943953f, 0.8552117f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.80285174f)
				.startKeyframe(16)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.end();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		//LogUtils.getLogger().info("Progress: " + client.getAimHandler().getProgress());
		if(client.getAimHandler().getProgress() < 0.5f) {
			matrix.pushPose();
			lerpTransform(matrix, client.getAimHandler().getProgress(), parts[6].getDTransform());
			lerpTransform(matrix, client.getSprintHandler().getProgress(), parts[7].getDTransform());
			lerpTransform(matrix, client.getRecoilHandler().getProgress(), parts[8].getDTransform());// client.getRecoilHandler().getProgress()
			//LogUtils.getLogger().info("Progress: " + client.getRecoilHandler().getProgress());
			matrix.pushPose();
			matrix.translate(parts[4].getTransform().pos[0], parts[4].getTransform().pos[1],
					parts[4].getTransform().pos[2]);
			matrix.mulPose(new Quaternion(parts[4].getTransform().rot[0],
					parts[4].getTransform().rot[1], parts[4].getTransform().rot[2], false));
			//translateAndRotate(parts[4].getCombined(), matrix);
			matrix.pushPose();
			matrix.scale(1f, 1.5f, 1f);
			renderArm(player, buffer, matrix, light, HumanoidArm.LEFT, 
					parts[1].getCombined());
			matrix.popPose();
			matrix.pushPose();
			// All less leftarm
			translateAndRotate(parts[5].getTransform(), matrix);
			matrix.pushPose();
			renderArm(player, buffer, matrix, light, HumanoidArm.RIGHT, 
					parts[0].getCombined());
			matrix.popPose();
			matrix.pushPose();
			renderItem(player, stack, buffer, matrix, light, 
					parts[2].getCombined());
			matrix.popPose();
			matrix.pushPose();
			renderModel(player, stack, buffer, matrix, light, Minecraft.getInstance()
					.getModelManager().getModel(HAMMER), parts[3].getCombined());
			matrix.popPose();
			matrix.popPose();
			matrix.popPose();
			matrix.popPose();
		}
		
		float tick = getAnimator().getTick();
		if(getAnimation() == reload) {
			if(tick == 18 || tick == 136 || tick == 150) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUN_MOVING.get());
			} else if(tick == 99) {
				SoundHandler.playSoundOnServer(SoundRegistries.INSERTING_BULLET.get());
			} else if(tick == 176){
				SoundHandler.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
				parts[3].getDTransform().setPos(0.62f, -0.5f, -0.87f);
				parts[3].getDTransform().setRot(0.030186f, 0.034906f, 0f);
				PirateGuns.channel.sendToServer(new LoadBulletMessage(true));
			} else if(tick == 196) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
			}
		} else if(getAnimation() == look) {
			if(tick == 13 || tick == 61) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUN_MOVING.get());
			} else if(tick == 120) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUN_HIT.get());
			}
		}
	}
	
	@Override
	public void shoot(Player player, ItemStack stack) {
		PirateGuns.channel.sendToServer(new ShootMessage(player.getYRot(), 
				player.getXRot(), gun.getShootSound().getRegistryName().toString()));
		// Hammer down
		parts[3].getDTransform().setPos(0.62f, -0.27f, -0.76f);
		parts[3].getDTransform().setRot(-1.627878f, 0.034906f, 0f);
		markChanges();
	}

	@Override
	public void reload(Player player, ItemStack stack) {
		if(!NBTUtils.isLoaded(stack)) {
			int bullet = InventoryUtils.getIndexForItem(player, ItemRegistries.MUSKET_BULLET.get());
			int[] gunpowder = InventoryUtils.getCountAndIndexForItem(player, Items.GUNPOWDER);
			if(bullet != -1 && gunpowder[1] >= 2) {
				InventoryUtils.consumeItems(player, new int[] {bullet, gunpowder[0]}, 
						new int[] {1, 2});
				setAnimation(reload);
			}
		}
	}

	@Override
	public Animation getLookAnimation() {
		return look;
	}

	@Override
	public List<GunModelPart> getGunParts() {
		return List.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
	}

	@Override
	public GunModelPart getGunModelPart() {
		return parts[2];
	}

	/*@Override
	public SoundEvent getShootSound() {
		return SoundRegistries.FLINTLOCK_RIFLE_SHOOT.get();
	}*/

	@Override
	public float getKnockback() {
		return 2;
	}

}
