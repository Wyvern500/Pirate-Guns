package com.jg.jgpg.client.player.model.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class JgCustomHeadLayer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel>
		extends RenderLayer<T, M> {

	private final float scaleX;
	private final float scaleY;
	private final float scaleZ;
	private final Map<SkullBlock.Type, SkullModelBase> skullModels;
	private final ItemInHandRenderer itemInHandRenderer;

	public JgCustomHeadLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, ItemInHandRenderer itemInHandRenderer) {
		this(renderer, modelSet, 1.0F, 1.0F, 1.0F, itemInHandRenderer);
	}

	public JgCustomHeadLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, float scaleX, float scaleY, float scaleZ, ItemInHandRenderer itemInHandRenderer) {
		super(renderer);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		this.skullModels = SkullBlockRenderer.createSkullRenderers(modelSet);
		this.itemInHandRenderer = itemInHandRenderer;
	}

	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
		if (!itemstack.isEmpty()) {
			Item item = itemstack.getItem();
			poseStack.pushPose();
			poseStack.scale(this.scaleX, this.scaleY, this.scaleZ);
			boolean flag = livingEntity instanceof Villager || livingEntity instanceof ZombieVillager;
			float f2;
			if (livingEntity.isBaby() && !(livingEntity instanceof Villager)) {
				float f = 2.0F;
				f2 = 1.4F;
				poseStack.translate(0.0F, 0.03125F, 0.0F);
				poseStack.scale(0.7F, 0.7F, 0.7F);
				poseStack.translate(0.0F, 1.0F, 0.0F);
			}

			((HeadedModel)this.getParentModel()).getHead().translateAndRotate(poseStack);
			if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock) {
				f2 = 1.1875F;
				poseStack.scale(1.1875F, -1.1875F, -1.1875F);
				if (flag) {
					poseStack.translate(0.0F, 0.0625F, 0.0F);
				}

				ResolvableProfile resolvableprofile = (ResolvableProfile)itemstack.get(DataComponents.PROFILE);
				poseStack.translate(-0.5, 0.0, -0.5);
				SkullBlock.Type skullblock$type = ((AbstractSkullBlock)((BlockItem)item).getBlock()).getType();
				SkullModelBase skullmodelbase = (SkullModelBase)this.skullModels.get(skullblock$type);
				RenderType rendertype = SkullBlockRenderer.getRenderType(skullblock$type, resolvableprofile);
				Entity var22 = livingEntity.getVehicle();
				WalkAnimationState walkanimationstate;
				if (var22 instanceof LivingEntity) {
					LivingEntity livingentity = (LivingEntity)var22;
					walkanimationstate = livingentity.walkAnimation;
				} else {
					walkanimationstate = livingEntity.walkAnimation;
				}

				float f3 = walkanimationstate.position(partialTicks);
				SkullBlockRenderer.renderSkull((Direction)null, 180.0F, f3, poseStack, buffer, packedLight, skullmodelbase, rendertype);
			} else {
				label52: {
					if (item instanceof ArmorItem) {
						ArmorItem armoritem = (ArmorItem)item;
						if (armoritem.getEquipmentSlot() == EquipmentSlot.HEAD) {
							break label52;
						}
					}

					translateToHead(poseStack, flag);
					this.itemInHandRenderer.renderItem(livingEntity, itemstack, ItemDisplayContext.HEAD, false, poseStack, buffer, packedLight);
				}
			}

			poseStack.popPose();
		}

	}

	public static void translateToHead(PoseStack p_174484_, boolean p_174485_) {
		float f = 0.625F;
		p_174484_.translate(0.0D, -0.25D, 0.0D);
		p_174484_.mulPose(Axis.YP.rotationDegrees(180.0F));
		p_174484_.scale(0.625F, -0.625F, -0.625F);
		if (p_174485_) {
			p_174484_.translate(0.0D, 0.1875D, 0.0D);
		}

	}

}
