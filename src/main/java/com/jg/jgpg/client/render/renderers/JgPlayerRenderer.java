package com.jg.jgpg.client.render.renderers;

import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.player.JgHumanoidArmorModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel.ArmPose;
import com.jg.jgpg.client.model.player.JgPlayerModel;
import com.jg.jgpg.client.model.player.layers.JgArrowLayer;
import com.jg.jgpg.client.model.player.layers.JgBeeStingerLayer;
import com.jg.jgpg.client.model.player.layers.JgCapeLayer;
import com.jg.jgpg.client.model.player.layers.JgDeadmau5EarsLayer;
import com.jg.jgpg.client.model.player.layers.JgHumanoidArmorLayer;
import com.jg.jgpg.client.model.player.layers.JgItemInHandLayer;
import com.jg.jgpg.client.model.player.layers.JgParrotOnShoulderLayer;
import com.jg.jgpg.client.model.player.layers.JgSpinAttackEffectLayer;
import com.jg.jgpg.client.render.JgAbstractPlayerRenderer;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

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
		this.addLayer(new JgItemInHandLayer<>(this));
		this.addLayer(new JgArrowLayer<>(context, this));
		this.addLayer(new JgDeadmau5EarsLayer(this));
		this.addLayer(new JgCapeLayer(this));
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
		this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
		this.addLayer(new JgParrotOnShoulderLayer<>(this, context.getModelSet()));
		this.addLayer(new JgSpinAttackEffectLayer<>(this, context.getModelSet()));
		this.addLayer(new JgBeeStingerLayer<>(this));
	}

	public void render(AbstractClientPlayer p_117788_, float p_117789_, float p_117790_, PoseStack p_117791_,
			MultiBufferSource p_117792_, int p_117793_) {
		this.setModelProperties(p_117788_);
		super.render(p_117788_, p_117789_, p_117790_, p_117791_, p_117792_, p_117793_);
	}

	public Vec3 getRenderOffset(AbstractClientPlayer p_117785_, float p_117786_) {
		return p_117785_.isCrouching() ? new Vec3(0.0D, -0.125D, 0.0D) : super.getRenderOffset(p_117785_, p_117786_);
	}

	private void setModelProperties(AbstractClientPlayer p_117819_) {
		JgPlayerModel<AbstractClientPlayer> playermodel = this.getModel();
		if (p_117819_.isSpectator()) {
			playermodel.setAllVisible(false);
			playermodel.head.visible = true;
			playermodel.hat.visible = true;
		} else {
			playermodel.setAllVisible(true);
			playermodel.hat.visible = p_117819_.isModelPartShown(PlayerModelPart.HAT);
			playermodel.jacket.visible = p_117819_.isModelPartShown(PlayerModelPart.JACKET);
			playermodel.leftPants.visible = p_117819_.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
			playermodel.rightPants.visible = p_117819_.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
			playermodel.leftSleeve.visible = p_117819_.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
			playermodel.rightSleeve.visible = p_117819_.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
			playermodel.crouching = p_117819_.isCrouching();
			JgHumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(p_117819_, 
					InteractionHand.MAIN_HAND);
			JgHumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(p_117819_, 
					InteractionHand.OFF_HAND);
			if (humanoidmodel$armpose.isTwoHanded()) {
				humanoidmodel$armpose1 = p_117819_.getOffhandItem().isEmpty() ? JgHumanoidModel.ArmPose.EMPTY
						: JgHumanoidModel.ArmPose.ITEM;
			}

			if (p_117819_.getMainArm() == HumanoidArm.RIGHT) {
				playermodel.rightArmPose = humanoidmodel$armpose;
				playermodel.leftArmPose = humanoidmodel$armpose1;
			} else {
				playermodel.rightArmPose = humanoidmodel$armpose1;
				playermodel.leftArmPose = humanoidmodel$armpose;
			}
		}

	}

	private static JgHumanoidModel.ArmPose getArmPose(AbstractClientPlayer p_117795_, 
			InteractionHand p_117796_) {
		ItemStack stack = p_117795_.getItemInHand(p_117796_);
		if(JgPlayerRenderer.client.getModel() == null) {
			return ArmPose.SPYGLASS;
		}
		return JgPlayerRenderer.client.getModel().getArmPose(stack);
		/*if (itemstack.isEmpty()) {
			return HumanoidModel.ArmPose.CROSSBOW_HOLD;
		} else {
			if (p_117795_.getUsedItemHand() == p_117796_ && p_117795_.getUseItemRemainingTicks() > 0) {
				UseAnim useanim = itemstack.getUseAnimation();
				if (useanim == UseAnim.BLOCK) {
					return HumanoidModel.ArmPose.BLOCK;
				}

				if (useanim == UseAnim.BOW) {
					return HumanoidModel.ArmPose.BOW_AND_ARROW;
				}

				if (useanim == UseAnim.SPEAR) {
					return HumanoidModel.ArmPose.THROW_SPEAR;
				}

				if (useanim == UseAnim.CROSSBOW && p_117796_ == p_117795_.getUsedItemHand()) {
					return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
				}

				if (useanim == UseAnim.SPYGLASS) {
					return HumanoidModel.ArmPose.SPYGLASS;
				}
			} else if (!p_117795_.swinging && itemstack.getItem() instanceof CrossbowItem
					&& CrossbowItem.isCharged(itemstack)) {
				return HumanoidModel.ArmPose.CROSSBOW_HOLD;
			}

			return HumanoidModel.ArmPose.ITEM;
		}*/
		//return JgHumanoidModel.ArmPose.SPYGLASS;
	}

	public ResourceLocation getTextureLocation(AbstractClientPlayer p_117783_) {
		return p_117783_.getSkinTextureLocation();
	}

	protected void scale(AbstractClientPlayer p_117798_, PoseStack p_117799_, float p_117800_) {
		float f = 0.9375F;
		p_117799_.scale(f, f, f);
	}

	protected void renderNameTag(AbstractClientPlayer p_117808_, Component p_117809_, PoseStack p_117810_,
			MultiBufferSource p_117811_, int p_117812_) {
		double d0 = this.entityRenderDispatcher.distanceToSqr(p_117808_);
		p_117810_.pushPose();
		if (d0 < 100.0D) {
			Scoreboard scoreboard = p_117808_.getScoreboard();
			Objective objective = scoreboard.getDisplayObjective(2);
			if (objective != null) {
				Score score = scoreboard.getOrCreatePlayerScore(p_117808_.getScoreboardName(), objective);
				super.renderNameTag(p_117808_, (Component.translatable(Integer.toString(score.getScore()))).append(" ")
						.append(objective.getDisplayName()), p_117810_, p_117811_, p_117812_);
				p_117810_.translate(0.0D, (double) (9.0F * 1.15F * 0.025F), 0.0D);
			}
		}

		super.renderNameTag(p_117808_, p_117809_, p_117810_, p_117811_, p_117812_);
		p_117810_.popPose();
	}

	public void renderRightHand(PoseStack p_117771_, MultiBufferSource p_117772_, int p_117773_,
			AbstractClientPlayer p_117774_) {
		if (!net.minecraftforge.client.ForgeHooksClient.renderSpecificFirstPersonArm(p_117771_, 
				p_117772_, p_117773_, p_117774_, HumanoidArm.RIGHT))
			this.renderHand(p_117771_, p_117772_, p_117773_, p_117774_, (this.model).rightArm,
					(this.model).rightSleeve);
	}

	public void renderLeftHand(PoseStack p_117814_, MultiBufferSource p_117815_, int p_117816_,
			AbstractClientPlayer p_117817_) {
		if (!net.minecraftforge.client.ForgeHooksClient.renderSpecificFirstPersonArm(p_117814_, 
				p_117815_, p_117816_, p_117817_, HumanoidArm.LEFT))
			this.renderHand(p_117814_, p_117815_, p_117816_, p_117817_, (this.model).leftArm, (this.model).leftSleeve);
	}

	private void renderHand(PoseStack p_117776_, MultiBufferSource p_117777_, int p_117778_,
			AbstractClientPlayer p_117779_, ModelPart p_117780_, ModelPart p_117781_) {
		JgPlayerModel<AbstractClientPlayer> playermodel = this.getModel();
		this.setModelProperties(p_117779_);
		playermodel.attackTime = 0.0F;
		playermodel.crouching = false;
		playermodel.swimAmount = 0.0F;
		playermodel.setupAnim(p_117779_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		p_117780_.xRot = 0.0F;
		p_117780_.render(p_117776_, p_117777_.getBuffer(RenderType.entitySolid(p_117779_
				.getSkinTextureLocation())), p_117778_, OverlayTexture.NO_OVERLAY);
		p_117781_.xRot = 0.0F;
		p_117781_.render(p_117776_,
				p_117777_.getBuffer(RenderType.entityTranslucent(p_117779_.getSkinTextureLocation())), p_117778_,
				OverlayTexture.NO_OVERLAY);
	}

	protected void setupRotations(AbstractClientPlayer p_117802_, PoseStack p_117803_, float p_117804_, float p_117805_,
			float p_117806_) {
		float f = p_117802_.getSwimAmount(p_117806_);
		if (p_117802_.isFallFlying()) {
			super.setupRotations(p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
			float f1 = (float) p_117802_.getFallFlyingTicks() + p_117806_;
			float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
			if (!p_117802_.isAutoSpinAttack()) {
				p_117803_.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - p_117802_.getXRot())));
			}

			Vec3 vec3 = p_117802_.getViewVector(p_117806_);
			Vec3 vec31 = p_117802_.getDeltaMovement();
			double d0 = vec31.horizontalDistanceSqr();
			double d1 = vec3.horizontalDistanceSqr();
			if (d0 > 0.0D && d1 > 0.0D) {
				double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
				double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
				p_117803_.mulPose(Axis.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
			}
		} else if (f > 0.0F) {
			super.setupRotations(p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
			float f3 = p_117802_.isInWater() ? -90.0F - p_117802_.getXRot() : -90.0F;
			float f4 = Mth.lerp(f, 0.0F, f3);
			p_117803_.mulPose(Axis.XP.rotationDegrees(f4));
			if (p_117802_.isVisuallySwimming()) {
				p_117803_.translate(0.0D, -1.0D, (double) 0.3F);
			}
		} else {
			super.setupRotations(p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
		}

	}

}