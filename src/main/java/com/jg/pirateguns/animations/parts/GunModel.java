package com.jg.pirateguns.animations.parts;

import java.util.List;
import java.util.Map;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.Animator;
import com.jg.pirateguns.animations.Transform;
import com.jg.pirateguns.animations.serializers.AnimationSerializer;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.rendering.RenderHelper;
import com.jg.pirateguns.client.screens.AnimationScreen;
import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.network.ShootMessage;
import com.jg.pirateguns.utils.FileUtils;
import com.jg.pirateguns.utils.NBTUtils;
import com.jg.pirateguns.utils.PGMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Quaternion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class GunModel {
	
	protected ClientHandler client;
	
	protected GunModelPart[] parts;
	
	public GunItem gun;
	
	protected Map<String, Animation> animations;
	
	protected Animator animator;
	
	protected boolean shouldUpdateAnimation;
	protected boolean hasChanges;
	protected boolean playAnimation;
	protected boolean debugMode;
	
	public GunModel(GunModelPart[] gunModelParts, Item gun, ClientHandler client
			, String[] animations) {
		this.parts = gunModelParts;
		this.gun = (GunItem)gun;
		this.client = client;
		this.animator = new Animator(this);
		this.hasChanges = true;
		this.playAnimation = true;
	}
	
	// Transform
	
	protected void translateAndRotate(Transform t, PoseStack matrix) {
		matrix.translate(t.pos[0], t.pos[1], t.pos[2]);
		matrix.mulPose(new Quaternion(t.rot[0], t.rot[1], t.rot[2], false));
	}
	
	protected void lerpTransform(PoseStack matrix, float p, Transform t) {
		matrix.translate(Mth.lerp(p, 0, t.pos[0]), Mth.lerp(p, 0, t.pos[1]), 
				Mth.lerp(p, 0, t.pos[2]));
		matrix.mulPose(new Quaternion(PGMath.rotLerp(p, 0, t.rot[0]), 
				PGMath.rotLerp(p, 0, t.rot[1]), PGMath.rotLerp(p, 0, t.rot[2]), false));
	}
	
	// Gun Methods
	
	public void shoot(Player player, ItemStack stack) {
		PirateGuns.channel.sendToServer(new ShootMessage(player.getYRot(), 
				player.getXRot(), getShootSound().getRegistryName().toString()));
		markChanges();
	}
	
	public boolean canShoot(Player player, ItemStack stack) {
		return NBTUtils.isLoaded(stack);
	}
	
	// Rendering
	
	protected void renderItem(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		Minecraft.getInstance().getItemInHandRenderer()
		.renderItem(player, stack, TransformType.FIRST_PERSON_RIGHT_HAND
				, false, matrix, buffer, light);
	}
	
	protected void renderItem(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light, Transform transform) {
		translateAndRotate(transform, matrix);
		Minecraft.getInstance().getItemInHandRenderer()
		.renderItem(player, stack, TransformType.FIRST_PERSON_RIGHT_HAND
				, false, matrix, buffer, light);
	}
	
	protected void renderModel(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light, BakedModel model) {
		Minecraft.getInstance().getItemRenderer()
		.render(stack, TransformType.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, 
				light, OverlayTexture.NO_OVERLAY, model);
	}
	
	protected void renderModel(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light, BakedModel model, Transform transform) {
		translateAndRotate(transform, matrix);
		Minecraft.getInstance().getItemRenderer()
		.render(stack, TransformType.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, 
				light, OverlayTexture.NO_OVERLAY, model);
	}
	
	protected void renderArm(LocalPlayer player, MultiBufferSource buffer, PoseStack matrix, int light, HumanoidArm arm) {
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, arm);
	}
	
	protected void renderArm(LocalPlayer player, MultiBufferSource buffer, PoseStack matrix, int light, HumanoidArm arm, Transform transform) {
		translateAndRotate(transform, matrix);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, arm);
	}
	
	// Misc
	
	public GunModelPart getPart(String name) {
		for(GunModelPart part : parts) {
			if(part.getName().equals(name)) {
				return part;
			}
		}
		return null;
	}
	
	public void addAnimation(String name) {
		animations.put(name, AnimationSerializer.deserialize(FileUtils
				.readFile(name)));
	}
	
	public void markChanges() {
		hasChanges = true;
		LogUtils.getLogger().info("Mark changes");
	}
	
	public GunModelPart[] getParts() {
		return parts;
	}
	
	// Getters and setters
	
	public void setAnimation(Animation anim) {
		this.animator.setAnimation(anim);
		if(Minecraft.getInstance().screen instanceof AnimationScreen) {
			AnimationScreen screen = (AnimationScreen)Minecraft.getInstance().screen;
			screen.initKeyframes();
		}
	}
	
	public Animation getAnimation() {
		return animator.getAnimation();
	}
	
	public boolean canPlayAnimation() {
		return playAnimation;
	}

	public void setPlayAnimation(boolean playAnimation) {
		this.playAnimation = playAnimation;
	}
	
	public boolean shouldUpdateAnimation() {
		return shouldUpdateAnimation;
	}

	public void setShouldUpdateAnimation(boolean shouldUpdateAnimation) {
		this.shouldUpdateAnimation = shouldUpdateAnimation;
	}

	public boolean isDebugModeEnabled() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	public Animator getAnimator() {
		return animator;
	}
	
	// Abstract methods

	public void tick(Player player, ItemStack stack) {
		animator.update();
	}
	
	public abstract void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light);
	
	public abstract void reload(Player player, ItemStack stack);
	
	public abstract Animation getLookAnimation(); 
	
	public abstract List<GunModelPart> getGunParts();
	
	public abstract GunModelPart getGunModelPart();
	
	public abstract SoundEvent getShootSound();
}
