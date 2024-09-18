package com.jg.jgpg.client.jgmodel.itemmodel;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handlers.PirateGunsClientHandler;
import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.network.SynchronizeAnimationMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractMeleeModel extends JgModel<PirateGunsClientHandler>
        implements IAimableModel {

    public AbstractMeleeModel(AbstractClientHandler client) {
        super(client);
    }

    @Override
    public void modifyModelByPose(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount,
                                  float pAgeInTicks, float pNetHeadYaw, float pHeadPitch,
                                  float attackTime, float swimAmount, boolean crouching,
                                  boolean riding, JgPlayerPose pose, JgHumanoidModel<?> model) {
        super.modifyModelByPose(pEntity, pLimbSwing, pLimbSwingAmount,
                pAgeInTicks, pNetHeadYaw, pHeadPitch, attackTime, swimAmount, crouching,
                riding, pose, model);
    }

    @Override
    public void onSetupPlayerModel(LivingEntity entity, JgHumanoidModel<?> model,
                                   float pLimbSwing, float pLimbSwingAmount,
                                   float pAgeInTicks, float pNetHeadYaw,
                                   float pHeadPitch) {
        doAttackProgress(entity, model);
    }

    @Override
    public void onUse(Player player, ItemStack stack) {
        if(canUse(player)) {
            animator.setAnimation(getAttackAnimAnimation(player));
            animator.play();
        }
    }

    @Override
    public boolean canUse(Player player) {
        return animator.getAnimation() == null;
    }

    public boolean canAim(Player player) {
        return animator.getAnimation() == null;
    }

    @Override
    public void onAnimationStart(Animation anim) {
        PirateGuns.channel.sendToServer(new SynchronizeAnimationMessage(Minecraft.getInstance()
                .player.getUUID(), getAnimationIndex(anim)));
    }

    @Override
    public void onAnimationEnd(Animation anim) {
        PirateGuns.channel.sendToServer(new SynchronizeAnimationMessage(Minecraft.getInstance()
                .player.getUUID(), -1));
    }

    public abstract void doAttackProgress(LivingEntity entity, JgHumanoidModel<?> model);

    public abstract Animation getLookAnimAnimation(Player player);

    public abstract Animation getAttackAnimAnimation(Player player);

}
