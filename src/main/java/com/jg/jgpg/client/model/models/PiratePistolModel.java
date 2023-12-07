package com.jg.jgpg.client.model.models;

import java.util.List;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractGunModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.model.player.JgHumanoidModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel.ArmPose;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.InventoryUtils;
import com.jg.jgpg.utils.MeleeHelper;
import com.jg.jgpg.utils.NBTUtils;
import com.jg.jgpg.utils.Utils;
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
import net.minecraft.world.item.Items;
import net.minecraftforge.client.RenderTypeHelper;

public class PiratePistolModel extends AbstractGunModel {

	public Animation LOOK;
	public Animation RELOAD;
	public Animation HIT;
	
	public PiratePistolModel(ClientHandler handler) {
		super(handler);
	}
	
	@Override
	public void initParts() {
		addPart(new JgModelPart("aim", -1.1059934f, 0.75199604f, 0.32799998f, -0.08726639f, -0.13264497f, 0.0f));
		addPart(new JgModelPart("all", 0.41999987f, -0.28f, -0.7119996f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("gun", 0.776f, -0.7079999f, -1.4219983f, 0.17004529f, 0.14f, 0.0f));
		addPart(new JgModelPart("gunwithhammer", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("hammer", 0.83599997f, -0.84799975f, -0.82199883f, -1.3658453f, 0.035280257f, 0.06981317f));
		addPart(new JgModelPart("leftarm", 0.0f, -0.56f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("recoil", -0.09999999f, -0.97999936f, 0.039999984f, 0.6283214f, 0.0f, 0.15707956f));
		addPart(new JgModelPart("rightarm", 0.3f, 0.24000005f, 1.8199986f, -0.25830868f, 0.20594876f, 0.5096381f));
		addPart(new JgModelPart("rightarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
		addPart(new JgModelPart("sprint", 0.16f, -0.85999954f, 1.4901161E-8f, 0.9424782f, -0.20943952f, 0.0f));
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
				
				// Original
				RELOAD = new Animation("ReloadAnimation")
						.addKeyframe(5)
						.traslate(getPart("rightarmgun"), 0.45999992f, 0.32f, -0.18000002f, "empty")
						.rotate(getPart("rightarmgun"), -0.49999982f, 0.6599997f, 0.0f, "empty")
						.addKeyframe(14)
						.traslate(getPart("leftarm"), -0.04f, 1.1399993f, -0.41999987f, "empty")
						.rotate(getPart("leftarm"), -0.72019994f, -0.7199996f, 0.0f, "empty")
						.addKeyframe(16)
						.traslate(getPart("leftarm"), -0.04f, 1.1399993f, -0.41999987f, "empty")
						.rotate(getPart("leftarm"), -0.72019994f, -0.7199996f, 0.0f, "empty")
						.addKeyframe(20)
						.traslate(getPart("leftarm"), 0.04f, 1.1822838f, -0.32080153f, "easeOutQuint")
						.rotate(getPart("leftarm"), -0.681521f, -0.72132033f, 0.04f, "easeOutQuint")
						.addKeyframe(22)
						.traslate(getPart("leftarm"), 0.04f, 1.1822838f, -0.32080153f, "easeOutQuint")
						.rotate(getPart("leftarm"), -0.681521f, -0.72132033f, 0.04f, "easeOutQuint")
						.addKeyframe(28)
						.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
						.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
						.addKeyframe(34)
						.traslate(getPart("leftarm"), -0.04f, 1.1399993f, -0.41999987f, "empty")
						.rotate(getPart("leftarm"), -0.7199996f, -0.7199996f, 0.0f, "empty")
						.addKeyframe(41)
						.traslate(getPart("leftarm"), -0.04f, 1.1399993f, -0.41999987f, "empty")
						.rotate(getPart("leftarm"), -0.7199996f, -0.7199996f, 0.0f, "empty")
						.addKeyframe(47)
						.traslate(getPart("leftarm"), -0.04f, 1.1399993f, -0.41999987f, "empty")
						.rotate(getPart("leftarm"), -0.67999965f, -0.7199996f, 0.0f, "empty")
						.addKeyframe(53)
						.traslate(getPart("leftarm"), -0.04f, 1.1399993f, -0.41999987f, "empty")
						.rotate(getPart("leftarm"), -0.7199996f, -0.7199996f, 0.0f, "empty")
						.addKeyframe(59)
						.traslate(getPart("leftarm"), -0.04f, 1.1399993f, -0.41999987f, "empty")
						.rotate(getPart("leftarm"), -0.67999965f, -0.7199996f, 0.0f, "empty")
						.addKeyframe(65)
						.traslate(getPart("rightarmgun"), 0.45999992f, 0.32f, -0.18000002f, "empty")
						.rotate(getPart("rightarmgun"), -0.49999982f, 0.6599997f, 0.0f, "empty")
						.addKeyframe(75)
						.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
						.traslate(getPart("rightarmgun"), 1.5199989f, -0.87999946f, -0.080000006f, "easeOutCirc")
						.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
						.rotate(getPart("rightarmgun"), 0.55999976f, 1.1399993f, 0.0f, "easeOutCirc")
						.addKeyframe(81)
						.traslate(getPart("leftarm"), -0.060400777f, 0.55999976f, -0.15986636f, "empty")
						.rotate(getPart("leftarm"), 0.0f, -0.35999992f, 0.0f, "empty")
						.addKeyframe(86)
						.traslate(getPart("leftarm"), 0.059599213f, 0.55999976f, -0.15986636f, "easeInExpo")
						.addKeyframe(91)
						.traslate(getPart("leftarm"), -0.060400777f, 0.55999976f, -0.15986636f, "empty")
						.rotate(getPart("leftarm"), 0.0f, -0.35999992f, 0.0f, "empty")
						.addKeyframe(96)
						.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
						.addKeyframe(101)
						.traslate(getPart("leftarm"), -0.060400777f, 0.55999976f, -0.15986636f, "empty")
						.rotate(getPart("leftarm"), 0.0f, -0.35999992f, 0.0f, "empty")
						.addKeyframe(107)
						.traslate(getPart("leftarm"), 0.059599213f, 0.55999976f, -0.15986636f, "easeOutBack")
						.addKeyframe(108)
						.traslate(getPart("leftarm"), 0.059599213f, 0.55999976f, -0.15986636f, "easeOutBack")
						.addKeyframe(113)
						.traslate(getPart("leftarm"), -0.060400777f, 0.55999976f, -0.15986636f, "empty")
						.rotate(getPart("leftarm"), 0.0f, -0.35999992f, 0.0f, "empty")
						.addKeyframe(119)
						.traslate(getPart("leftarm"), 0.059599213f, 0.55999976f, -0.15986636f, "easeOutBack")
						.addKeyframe(120)
						.traslate(getPart("leftarm"), 0.059599213f, 0.55999976f, -0.15986636f, "easeOutBack")
						.addKeyframe(126)
						.traslate(getPart("leftarm"), -0.060400777f, 0.55999976f, -0.15986636f, "empty")
						.rotate(getPart("leftarm"), 0.0f, -0.35999992f, 0.0f, "empty")
						.addKeyframe(130)
						.traslate(getPart("rightarmgun"), 1.5199989f, -0.87999946f, -0.080000006f, "empty")
						.rotate(getPart("rightarmgun"), 0.55999976f, 1.1399993f, 0.0f, "empty")
						.addKeyframe(140)
						.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
						.traslate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
						.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
						.rotate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
						.end();
				
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
	public boolean canReload(LocalPlayer player) {
		int gunPowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
		int bullets = InventoryUtils.getCountForItem(player, ItemRegistries.MUSKET_BULLET.get());
		return gunPowder >= Config.SERVER.ppGunpowderToReload.get() && bullets >= 1;
	}
	
	@Override
	public void reload(LocalPlayer player) {
		animator.setCurrent(this, RELOAD);
		animator.play();
	}
	
	@Override
	public void tick() {
		animator.tick();
		
		prevShouldUpdate = shouldUpdate;
		shouldUpdate = NBTUtils.isLoaded(Minecraft.getInstance().player.getMainHandItem());
		
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, 
			int light) {
		if(NBTUtils.isLoaded(stack)) {
			getPart("hammer").getDtransform().set(0.776f, -0.7079999f, -1.4219983f, 0.17004529f, 
					0.14f, 0);
		} else {
			getPart("hammer").getDtransform().set(0.83599997f, -0.84799975f, -0.82199883f, 
					-1.3658453f, 0.035280257f, 0.06981317f);
		}
		
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
		BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, model, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		RenderType type = RenderTypeHelper.getFallbackItemRenderType(stack, model, false);
		VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(buffer, type, true, false);
        matrix.translate(-0.5F, -0.5F, -0.5F);
		Minecraft.getInstance().getItemRenderer().renderModelLists(model, stack, light, 
				OverlayTexture.NO_OVERLAY, matrix, vertexConsumer);
		
		/*Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, 
				matrix, buffer, light, OverlayTexture.NO_OVERLAY, model);*/
		matrix.popPose();
		
		matrix.pushPose();
		traslateRotate("rightarmgun", matrix);
		traslateRotate("gunwithhammer", matrix);
		traslateRotate("hammer", matrix);
		ModelManager man = Minecraft.getInstance().getModelManager();
		
		// Hammer
		BakedModel hammer = man.getModel(new ModelResourceLocation(new ResourceLocation(Constants
				.PPHAMMER), "inventory"));
		hammer = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, hammer, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext
				.NONE, false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, hammer);
		matrix.popPose();
		
		matrix.popPose();
	}

	@Override
	public void renderDefault(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light) {
		matrix.pushPose();
		traslateRotateD("rightarmgun", matrix);
		traslateRotateD("rightarm", matrix);
		matrix.scale(1f, 1f, 3f);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT);
		matrix.popPose();
		if(animator.getCurrent() != null) {
			if(animator.getCurrent() == RELOAD) {
				matrix.pushPose();
				traslateRotateD("leftarm", matrix);
				RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT);
				matrix.popPose();
			}
		}
		matrix.pushPose();
		traslateRotateD("rightarmgun", matrix);
		// Gun
		traslateRotateD("gun", matrix);
		BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, model, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		RenderType type = RenderTypeHelper.getFallbackItemRenderType(stack, model, false);
		VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(buffer, type, true, false);
        matrix.translate(-0.5F, -0.5F, -0.5F);
		Minecraft.getInstance().getItemRenderer().renderModelLists(model, stack, light, 
				OverlayTexture.NO_OVERLAY, matrix, vertexConsumer);
		matrix.popPose();
	}
	
	@Override
	public ArmPose getArmPose(ItemStack stack) {
		return ArmPose.SPYGLASS;
	}
	
	@Override
	public <T extends LivingEntity> void handleArmPose(ArmPose pose, ItemStack stack, JgHumanoidModel<T> model,
			HumanoidArm arm) {
		if(arm == HumanoidArm.RIGHT) {
			if(pose == ArmPose.SPYGLASS) {
				model.rightArm.yRot = -0.1F + model.head.yRot;
				model.rightArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot;
		    }
		} else {
			if(pose == ArmPose.SPYGLASS) {
				model.leftArm.xRot = 0.6f;
				model.leftArm.yRot = -0.8f;
		    }
		}
	}
	
	@Override
	public void onAnimationEnd(Animation anim) {
		
	}

	@Override
	public void onAnimationStart(Animation anim) {
		
	}

	@Override
	public void onAnimationTick(Animation anim, float prevTick, float tick) {
		Animation current = animator.getCurrent();
		
		prevTick = (float) Math.floor(prevTick);
		tick = (float) Math.floor(tick);
		
		if(Math.abs(tick - prevTick) > 0) {
			if(current == HIT) {
				if(tick == 11) {
					Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
				} else if(tick == 13) {
					MeleeHelper.hit(client, Config.SERVER.ppMeleeHitDamage.get());
				}
			} else if(current == RELOAD) {
				if(tick == 19) {
					Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
					
					consumeItems(Config.SERVER.ppGunpowderToReload.get(), 1, false);
					
				} else if(tick == 40) {
					Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
				} else if(tick == 50) {
					Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
				} else if(tick  == 85) {
					Utils.playSoundOnServer(SoundRegistries.SMALL_BULLET_HITTING_METAL.get());
				} else if(tick == 103) {
					Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
				} else if(tick == 108) {
					Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
				}
			}
		}
	}

	@Override
	public Animation getLookAnimAnimation(LocalPlayer player) {
		return LOOK;
	}
	
	@Override
	public Animation getKickbackAnimAnimation(LocalPlayer player) {
		return HIT;
	}
	
	@Override
	public List<Animation> getAnimations() {
		return List.of(LOOK, RELOAD, HIT);
	}

}
