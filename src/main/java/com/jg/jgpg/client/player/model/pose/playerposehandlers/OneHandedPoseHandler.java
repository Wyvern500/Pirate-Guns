package com.jg.jgpg.client.player.model.pose.playerposehandlers;

import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.handler.handlers.EasingHandler;
import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class OneHandedPoseHandler extends JgPlayerPoseHandler {

    public OneHandedPoseHandler() {

    }

    @Override
    public void handlePlayerPose(LivingEntity pEntity, float pLimbSwing,
                                 float pLimbSwingAmount, float pAgeInTicks,
                                 float pNetHeadYaw, float pHeadPitch, float attackTime,
                                 float swimAmount, boolean crouching, boolean riding,
                                 JgPlayerPose pose, JgHumanoidModel<?> model) {
        if (pose == PlayerPoses.HOLDINGGUN) {
            if (!crouching) {
                model.leftArm.x = 4f;
                model.leftArm.xRot = 0.6f;
                model.leftArm.yRot = -0.8f;
            } else {
                model.leftArm.x = 4.4f;
                model.leftArm.z = 0.6f;
                model.leftArm.xRot = 1.2f;
                model.leftArm.yRot = -0.6f;
            }

            model.rightArm.yRot = -0.1F + model.head.yRot;
            model.rightArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

            model.head.x = 0;
            model.head.zRot = 0;
            model.hat.copyFrom(model.head);

            Easing easing = EasingHandler.INSTANCE.getEasing("easeOutBack");

            model.rightArm.xRot += (float) -Math.sin(easing.get(attackTime)
                    * ((float)Math.PI * 2F));
            //model.rightArm.yRot += (float) Math.sin(attackTime * ((float)Math.PI * 2F));
            /*model.body.yRot = Mth.sin(Mth.sqrt(attackTime)
                    * ((float)Math.PI * 2F)) * 0.2F;*/
        } else if (pose == PlayerPoses.AIMING) {

            if (!crouching) {
                model.leftArm.x = 4f;
                model.leftArm.xRot = 0.6f;
                model.leftArm.yRot = -0.8f;
            } else {
                model.leftArm.x = 4.4f;
                model.leftArm.z = 0.6f;
                model.leftArm.xRot = 1.2f;
                model.leftArm.yRot = -0.6f;
            }


            if (!crouching) {
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
            model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1
                    ,1) + (float) (Math.sin(pAgeInTicks * 0.13f) * 0.2f);
            model.leftArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot
            + (float) (Math.sin(pAgeInTicks * 0.173f) * 0.2f);

            model.rightArm.yRot = -0.1F + model.head.yRot
                    + (float) (Math.sin(pAgeInTicks * 0.13f) * 0.2f);
            model.rightArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot;

            model.head.x = 0;
            model.head.xRot = (pHeadPitch * ((float) Math.PI / 180F)) + 0.6f;
            model.head.zRot = 0;
            model.hat.copyFrom(model.head);
        } else if (pose == PlayerPoses.RUNNING) {
            model.rightArm.yRot = model.head.yRot + (Mth.sin(pLimbSwing) * 0.06f);
            model.rightArm.xRot = ((-(float) Math.PI / 2F) - 0.4f) + model.head.xRot +
                    (Mth.cos(pLimbSwing) * 0.09f);

            model.head.x = 0;
            model.head.zRot = 0;
            model.hat.copyFrom(model.head);
        }
        /* else if(armPose == JgHumanoidModel.ArmPose.MAGOUT) {
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
