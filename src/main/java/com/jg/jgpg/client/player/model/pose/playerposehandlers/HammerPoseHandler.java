package com.jg.jgpg.client.player.model.pose.playerposehandlers;

import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class HammerPoseHandler extends JgPlayerPoseHandler {

    public HammerPoseHandler() {

    }

    @Override
    public void handlePlayerPose(LivingEntity pEntity, float pLimbSwing,
                                 float pLimbSwingAmount, float pAgeInTicks,
                                 float pNetHeadYaw, float pHeadPitch, float attackTime,
                                 float swimAmount, boolean crouching, boolean riding,
                                 JgPlayerPose pose, JgHumanoidModel<?> model) {
        if(pose == PlayerPoses.MELEE) {
            LogUtils.log("HammerPoseHandler", "AttackTime: " + attackTime);
        } else if(pose == PlayerPoses.AIMING) {
            // Walking
            float f = 0.2F;
            float f2 = 0.5F;

            model.rightArm.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI)
                * f;
            model.rightArm.yRot = -Mth.sin(pLimbSwing * 0.6662F + (float)Math.PI)
                * f * f2;
            model.leftArm.xRot = -Mth.cos(pLimbSwing * 0.6662F) * f;
            model.leftArm.yRot = -Mth.sin(pLimbSwing * 0.6662F + (float)Math.PI)
                    * f * f2;

            model.rightArm.zRot = 0.0F;
            model.leftArm.zRot = 0.0F;

            if (riding) {
                model.rightArm.xRot += (-(float)Math.PI / 5F);
                model.leftArm.xRot += (-(float)Math.PI / 5F);
            }

            // Holding weapon
            model.leftArm.xRot += (float) -(Math.PI / 2f);
            model.leftArm.yRot += (float) (Math.PI / 2f) - 0.7f;

            model.rightArm.xRot += (float) -(Math.PI / 2f);
            model.rightArm.yRot -= 0.4f;
        }
    }
}
