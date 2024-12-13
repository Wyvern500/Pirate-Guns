package com.jg.jgpg.client.player.model.pose;

import com.jg.jgpg.client.player.model.JgHumanoidModel;
import net.minecraft.world.entity.LivingEntity;

public abstract class JgPlayerPoseHandler {

    public JgPlayerPoseHandler() {

    }

    public abstract void handlePlayerPose(LivingEntity pEntity, float pLimbSwing,
                                  float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw,
                                  float pHeadPitch, float attackTime, float swimAmount,
                                  boolean crouching, boolean riding, JgPlayerPose pose,
                                  JgHumanoidModel<?> model);

}
