package com.jg.jgpg.client.player.model.pose.playerposehandlers;

import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class CannonPoseHandler extends JgPlayerPoseHandler {

    public CannonPoseHandler() {

    }

    @Override
    public void handlePlayerPose(LivingEntity pEntity, float pLimbSwing,
                                 float pLimbSwingAmount, float pAgeInTicks,
                                 float pNetHeadYaw, float pHeadPitch, float attackTime,
                                 float swimAmount, boolean crouching, boolean riding,
                                 JgPlayerPose pose, JgHumanoidModel<?> model) {
        if (pose == PlayerPoses.HOLDINGGUN) {
            model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1);
            model.leftArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

            model.rightArm.yRot = -0.1F + model.head.yRot;
            model.rightArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

            model.head.x = 0;
            model.head.zRot = 0;
            model.hat.copyFrom(model.head);
        } else if (pose == PlayerPoses.AIMING) {
            boolean isCrouching = pEntity.isCrouching();

            model.leftArm.x = 4;
            if (!isCrouching) {
                model.leftArm.y = 1f;
            } else {
                model.leftArm.y = 5f;
            }
            model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1);
            model.leftArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

            if (!isCrouching) {
                model.rightArm.y = 1f;
            } else {
                model.rightArm.y = 5f;
            }
            model.rightArm.yRot = -0.1F + model.head.yRot;
            model.rightArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

            model.head.x = -1;
            model.head.xRot = (pHeadPitch * ((float) Math.PI / 180F)) + 0.2f;
            model.head.zRot = -0.2f;

            model.hat.copyFrom(model.head);
        } else if (pose == PlayerPoses.RELOADING) {
            model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1);
            model.leftArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot;

            model.rightArm.yRot = -0.1F + model.head.yRot;
            model.rightArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot;

            model.head.x = 0;
            model.head.xRot = (pHeadPitch * ((float) Math.PI / 180F)) + 0.6f;
            model.head.zRot = 0;
            model.hat.copyFrom(model.head);
        } else if (pose == PlayerPoses.RUNNING) {
            model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1) +
                    (Mth.sin(pLimbSwing) * 0.06f);
            model.leftArm.xRot = -(((float) Math.PI / 2F) + 0.4f) + model.head.xRot +
                    (Mth.cos(pLimbSwing) * 0.09f);

            model.rightArm.yRot = -0.1F + model.head.yRot + (Mth.sin(pLimbSwing) * 0.06f);
            model.rightArm.xRot = -(((float) Math.PI / 2F) + 0.4f) + model.head.xRot +
                    (Mth.cos(pLimbSwing) * 0.09f);

			/*float f = 1.0f;

			model.rightArm.xRot = (Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 2.0F
					* pLimbSwingAmount * 0.5F / f);
			model.leftArm.xRot = (Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount
					* 0.5F / f) * 0.6f;
			model.rightArm.zRot = 0.0F;
			model.leftArm.zRot = 0.0F;
			model.rightLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F
					* pLimbSwingAmount / f;
			model.leftLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F
					* pLimbSwingAmount / f;
			model.rightLeg.yRot = 0.0F;
			model.leftLeg.yRot = 0.0F;
			model.rightLeg.zRot = 0.0F;
			model.leftLeg.zRot = 0.0F;*/

            model.head.x = 0;
            model.head.zRot = 0;
            model.hat.copyFrom(model.head);
        }/* else if(armPose == JgHumanoidModel.ArmPose.MAGOUT) {
			model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1);
			model.leftArm.xRot = ((-(float)Math.PI / 2F) + 0.4f) + model.head.xRot;

			model.rightArm.yRot = -0.3F + model.head.yRot;
			model.rightArm.xRot = ((-(float)Math.PI / 2F) - 0.3f) + model.head.xRot;

			model.head.x = 0;
			model.head.xRot = (pHeadPitch * ((float)Math.PI / 180F)) + 0.4f;
			model.head.zRot = 0;
			model.hat.copyFrom(model.head);
		    }*/
    }

}
