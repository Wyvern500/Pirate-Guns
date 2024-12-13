package com.jg.jgpg.client.player.render;

import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.player.model.JgHumanoidArmorModel;
import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.JgPlayerModel;
import com.jg.jgpg.client.player.model.layers.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.neoforge.client.ClientHooks;

public class JgPlayerRenderer extends JgAbstractPlayerRenderer<AbstractClientPlayer, 
			JgPlayerModel<AbstractClientPlayer>> {
	
	private static ClientHandler client;
	
	public JgPlayerRenderer(ClientHandler client) {
		super(new JgPlayerModel<>(Minecraft.getInstance().getEntityModels()
				.bakeLayer(ModelLayers.PLAYER_SLIM), false, client), 0.5F);
		JgPlayerRenderer.client = client;
		this.addLayer(new JgHumanoidArmorLayer<>(this, new JgHumanoidArmorModel<>(context
				.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR), client), 
				new JgHumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR), 
						client), 
				context.getModelManager()));
		this.addLayer(new JgItemInHandLayer<>(this, Minecraft.getInstance()
				.gameRenderer.itemInHandRenderer));
		this.addLayer(new JgArrowLayer<>(context, this));
		this.addLayer(new JgDeadmau5EarsLayer(this));
		this.addLayer(new JgCapeLayer(this));
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
		this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
		this.addLayer(new JgParrotOnShoulderLayer<>(this, context.getModelSet()));
		this.addLayer(new JgSpinAttackEffectLayer<>(this, context.getModelSet()));
		this.addLayer(new JgBeeStingerLayer<>(this));
	}

	public void render(AbstractClientPlayer entity, float entityYaw,
					   float partialTicks, PoseStack poseStack,
					   MultiBufferSource buffer, int packedLight,
					   PlayerRenderer originalRenderer) {
		this.setModelProperties(entity);
		super.render(entity, entityYaw, partialTicks, poseStack, buffer,
				packedLight);
		/*if (!((RenderPlayerEvent.Pre) NeoForge.EVENT_BUS
				.post(new RenderPlayerEvent.Pre(entity,
						originalRenderer, partialTicks,
						poseStack, buffer, packedLight))).isCanceled()) {
			super.render(entity, entityYaw, partialTicks, poseStack, buffer,
					packedLight);
			NeoForge.EVENT_BUS.post(new RenderPlayerEvent.Post(entity,
					originalRenderer, partialTicks, poseStack, buffer, packedLight));
		}*/
	}

	public Vec3 getRenderOffset(AbstractClientPlayer entity, float partialTicks) {
		return entity.isCrouching() ? new Vec3(0.0, (double)(entity.getScale() * -2.0F) / 16.0, 0.0) : super.getRenderOffset(entity, partialTicks);
	}

	private void setModelProperties(AbstractClientPlayer clientPlayer) {
		JgPlayerModel<AbstractClientPlayer> playermodel = this.getModel();
		if (clientPlayer.isSpectator()) {
			playermodel.setAllVisible(false);
			playermodel.head.visible = true;
			playermodel.hat.visible = true;
		} else {
			playermodel.setAllVisible(true);
			playermodel.hat.visible = clientPlayer.isModelPartShown(PlayerModelPart.HAT);
			playermodel.jacket.visible = clientPlayer.isModelPartShown(PlayerModelPart.JACKET);
			playermodel.leftPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
			playermodel.rightPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
			playermodel.leftSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
			playermodel.rightSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
			playermodel.crouching = clientPlayer.isCrouching();
			JgHumanoidModel.ArmPose humanoidmodel$armpose =
					getArmPose(clientPlayer, InteractionHand.MAIN_HAND);
			JgHumanoidModel.ArmPose humanoidmodel$armpose1 =
					getArmPose(clientPlayer, InteractionHand.OFF_HAND);
			if (humanoidmodel$armpose.isTwoHanded()) {
				humanoidmodel$armpose1 = clientPlayer.getOffhandItem().isEmpty()
						? JgHumanoidModel.ArmPose.EMPTY : JgHumanoidModel.ArmPose.ITEM;
			}

			if (clientPlayer.getMainArm() == HumanoidArm.RIGHT) {
				playermodel.rightArmPose = humanoidmodel$armpose;
				playermodel.leftArmPose = humanoidmodel$armpose1;
			} else {
				playermodel.rightArmPose = humanoidmodel$armpose1;
				playermodel.leftArmPose = humanoidmodel$armpose;
			}
		}

	}

	private static JgHumanoidModel.ArmPose getArmPose(AbstractClientPlayer player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.isEmpty()) {
			return JgHumanoidModel.ArmPose.EMPTY;
		} else {
			if (player.getUsedItemHand() == hand && player.getUseItemRemainingTicks() > 0) {
				UseAnim useanim = itemstack.getUseAnimation();
				if (useanim == UseAnim.BLOCK) {
					return JgHumanoidModel.ArmPose.BLOCK;
				}

				if (useanim == UseAnim.BOW) {
					return JgHumanoidModel.ArmPose.BOW_AND_ARROW;
				}

				if (useanim == UseAnim.SPEAR) {
					return JgHumanoidModel.ArmPose.THROW_SPEAR;
				}

				if (useanim == UseAnim.CROSSBOW && hand == player.getUsedItemHand()) {
					return JgHumanoidModel.ArmPose.CROSSBOW_CHARGE;
				}

				if (useanim == UseAnim.SPYGLASS) {
					return JgHumanoidModel.ArmPose.SPYGLASS;
				}

				if (useanim == UseAnim.TOOT_HORN) {
					return JgHumanoidModel.ArmPose.TOOT_HORN;
				}

				if (useanim == UseAnim.BRUSH) {
					return JgHumanoidModel.ArmPose.BRUSH;
				}
			} else if (!player.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
				return JgHumanoidModel.ArmPose.CROSSBOW_HOLD;
			}

			return JgHumanoidModel.ArmPose.ITEM;
		}
	}

	public ResourceLocation getTextureLocation(AbstractClientPlayer entity) {
		return entity.getSkin().texture();
	}

	protected void scale(AbstractClientPlayer livingEntity, PoseStack poseStack, float partialTickTime) {
		float f = 0.9375F;
		poseStack.scale(0.9375F, 0.9375F, 0.9375F);
	}

	protected void renderNameTag(AbstractClientPlayer entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick) {
		double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
		poseStack.pushPose();
		if (d0 < 100.0) {
			Scoreboard scoreboard = entity.getScoreboard();
			Objective objective = scoreboard.getDisplayObjective(DisplaySlot.BELOW_NAME);
			if (objective != null) {
				ReadOnlyScoreInfo readonlyscoreinfo = scoreboard.getPlayerScoreInfo(entity, objective);
				Component component = ReadOnlyScoreInfo.safeFormatValue(readonlyscoreinfo, objective.numberFormatOrDefault(StyledFormat.NO_STYLE));
				super.renderNameTag(entity, Component.empty().append(component).append(CommonComponents.SPACE).append(objective.getDisplayName()), poseStack, bufferSource, packedLight, partialTick);
				poseStack.translate(0.0F, 0.25875F, 0.0F);
			}
		}

		super.renderNameTag(entity, displayName, poseStack, bufferSource, packedLight, partialTick);
		poseStack.popPose();
	}

	public void renderRightHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player) {
		if (!ClientHooks.renderSpecificFirstPersonArm(poseStack, buffer, combinedLight, player, HumanoidArm.RIGHT)) {
			this.renderHand(poseStack, buffer, combinedLight, player,
					this.model.rightArm, this.model.rightSleeve);
		}

	}

	public void renderLeftHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player) {
		if (!ClientHooks.renderSpecificFirstPersonArm(poseStack, buffer, combinedLight, player, HumanoidArm.LEFT)) {
			this.renderHand(poseStack, buffer, combinedLight, player,
					this.model.leftArm, this.model.leftSleeve);
		}

	}

	private void renderHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear) {
		JgPlayerModel<AbstractClientPlayer> playermodel = this.getModel();
		this.setModelProperties(player);
		playermodel.attackTime = 0.0F;
		playermodel.crouching = false;
		playermodel.swimAmount = 0.0F;
		playermodel.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		rendererArm.xRot = 0.0F;
		ResourceLocation resourcelocation = player.getSkin().texture();
		rendererArm.render(poseStack, buffer.getBuffer(RenderType.entitySolid(resourcelocation)), combinedLight, OverlayTexture.NO_OVERLAY);
		rendererArmwear.xRot = 0.0F;
		rendererArmwear.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(resourcelocation)), combinedLight, OverlayTexture.NO_OVERLAY);
	}

	protected void setupRotations(AbstractClientPlayer entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
		float f = entity.getSwimAmount(partialTick);
		float f1 = entity.getViewXRot(partialTick);
		float f4;
		float f3;
		if (entity.isFallFlying()) {
			super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
			f4 = (float)entity.getFallFlyingTicks() + partialTick;
			f3 = Mth.clamp(f4 * f4 / 100.0F, 0.0F, 1.0F);
			if (!entity.isAutoSpinAttack()) {
				poseStack.mulPose(Axis.XP.rotationDegrees(f3 * (-90.0F - f1)));
			}

			Vec3 vec3 = entity.getViewVector(partialTick);
			Vec3 vec31 = entity.getDeltaMovementLerped(partialTick);
			double d0 = vec31.horizontalDistanceSqr();
			double d1 = vec3.horizontalDistanceSqr();
			if (d0 > 0.0 && d1 > 0.0) {
				double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
				double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
				poseStack.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
			}
		} else if (f > 0.0F) {
			super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
			f4 = !entity.isInWater() && !entity.isInFluidType((fluidType, height) -> {
				return entity.canSwimInFluidType(fluidType);
			}) ? -90.0F : -90.0F - entity.getXRot();
			f3 = Mth.lerp(f, 0.0F, f4);
			poseStack.mulPose(Axis.XP.rotationDegrees(f3));
			if (entity.isVisuallySwimming()) {
				poseStack.translate(0.0F, -1.0F, 0.3F);
			}
		} else {
			super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
		}

	}

}