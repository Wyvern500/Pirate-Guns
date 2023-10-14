package com.jg.jgpg.client.model.player;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface JgIArmPoseTransformer
{

    /**
     * This method should be used to apply all wanted transformations to the player when the ArmPose is active.
     * You can use {@link LivingEntity#getTicksUsingItem()} and {@link LivingEntity#getUseItemRemainingTicks()} for moving animations.
     *
     * @param model   The humanoid model
     * @param entity  The humanoid entity
     * @param arm Arm to pose
     */
    void applyTransform(JgHumanoidModel<?> model, LivingEntity entity, HumanoidArm arm);

}