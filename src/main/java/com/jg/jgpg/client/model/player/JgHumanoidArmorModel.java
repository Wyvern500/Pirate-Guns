package com.jg.jgpg.client.model.player;

import com.jg.jgpg.client.handler.ClientHandler;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class JgHumanoidArmorModel<T extends LivingEntity> extends JgHumanoidModel<T>{
	
	public JgHumanoidArmorModel(ModelPart p_270765_, ClientHandler client) {
		super(p_270765_, client);
	}

	public static MeshDefinition createBodyLayer(CubeDeformation p_270527_) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(p_270527_, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F,
				4.0F, 12.0F, 4.0F, p_270527_.extend(-0.1F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F,
				0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_270527_.extend(-0.1F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		return meshdefinition;
	}
}
