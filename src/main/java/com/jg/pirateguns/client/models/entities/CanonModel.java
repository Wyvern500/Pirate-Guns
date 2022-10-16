package com.jg.pirateguns.client.models.entities;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.entities.Canon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CanonModel extends EntityModel<Canon> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(PirateGuns.MODID, "canon_model"), "main");
	private final ModelPart canon;

	public CanonModel(ModelPart root) {
		this.canon = root.getChild("canon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition canon = partdefinition.addOrReplaceChild("canon", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition base = canon.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, -6.0F, -8.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(6.0F, -6.0F, 2.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -6.0F, -8.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -6.0F, 2.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -6.0F, -8.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -16.0F, -8.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -11.0F, 2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -9.0F, 5.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -9.0F, 5.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -11.0F, 2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -16.0F, -8.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition barrel = canon.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(0, 37).addBox(-2.0F, -2.2273F, -15.4091F, 4.0F, 1.0F, 21.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-1.0F, -3.2273F, 2.5909F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-2.0F, 1.7727F, -15.4091F, 4.0F, 1.0F, 21.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(2.0F, -2.2273F, -15.4091F, 1.0F, 5.0F, 21.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-3.0F, -2.2273F, -15.4091F, 1.0F, 5.0F, 21.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-4.0F, -3.2273F, -16.4091F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(3.0F, -3.2273F, -16.4091F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-3.0F, 2.7727F, -16.4091F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-3.0F, -3.2273F, -16.4091F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-2.0F, -1.2273F, 5.5909F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-1.0F, -0.2273F, 6.5909F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.7727F, 0.4091F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Canon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//(float)Math.toRadians(
		this.canon.yRot = entity.getYRot() * ((float)Math.PI / 180F);
	    this.canon.getChild("barrel").xRot = entity.getXRot() * ((float)Math.PI / 180F);
		//canon.setRotation(0, entity.xDir, 0);
		//canon.getChild("barrel").setRotation((float)Math.toRadians(entity.yDir), 0, 0);
		//System.out.println("X Rot: " + entity.getXRot() + " Y Rot: " + entity.getYRot());
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		canon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}