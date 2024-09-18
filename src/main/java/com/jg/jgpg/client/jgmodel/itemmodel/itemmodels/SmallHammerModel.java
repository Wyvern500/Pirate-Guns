package com.jg.jgpg.client.jgmodel.itemmodel.itemmodels;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.RepetitiveAnimation;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.AbstractMeleeModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.model.item.SmallHammerModModel;
import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoseHandlers;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.MeleeHelper;
import com.jg.jgpg.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

import java.util.List;

public class SmallHammerModel extends AbstractMeleeModel {

    Animation look;
    RepetitiveAnimation throwPunches;

    public SmallHammerModel(AbstractClientHandler client) {
        super(client);
    }

    @Override
    public void doAttackProgress(LivingEntity entity, JgHumanoidModel<?> model) {

    }

    @Override
    public ToShowModModel getModModel(BakedModel origin) {
        return new SmallHammerModModel(origin);
    }

    @Override
    public void initParts() {
        addPart(new JgModelPart("aim", 0.69999963f, -0.2f, 0.5199998f, 0.24434613f, 0.034906574f, -0.38397238f));
        addPart(new JgModelPart("all", -0.24000002f, 0.16f, 0.41999987f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("gun", 0.35060024f, -0.69679934f, -1.2799991f, 0.10471976f, 0.034906585f, 0.6632252f));
        addPart(new JgModelPart("leftarm", -0.6799997f, 0.040000014f, -0.39999986f, 0.0f, -1.2217312f, 0.0f));
        addPart(new JgModelPart("leftarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("leftarmmodel", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("rightarm", 0.12f, -0.17999995f, -0.23999995f, -0.52359873f, 0.48869213f, 0.6981318f));
        addPart(new JgModelPart("rightarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("rightarmgunpump", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("rightarmmodel", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("sprint", 0.0f, 0.0f, 0.0f, -0.34906584f, 0.0f, 0.0f));
    }

    @Override
    public void initAnimations() {
        look = new Animation("Look")
                .translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("gun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("gun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.77999955f, -0.07999999f, 0.2f)
                .rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.24434613f, -0.034906626f, -0.6981318f)
                .translate(getPart("gun"), 10, "easeInOutSine", 0.04f, -0.04f, -0.09999999f)
                .rotate(getPart("gun"), 10, "easeInOutSine", -1.2217312f, 0.76794505f, 0.872665f)
                .translate(getPart("leftarm"), 10, "easeInOutSine", 0.31999996f, 0.22000001f, 0.24000002f)
                .translate(getPart("rightarmgun"), 40, "easeInOutSine", 0.77999955f, -0.07999999f, 0.2f)
                .rotate(getPart("rightarmgun"), 40, "easeInOutSine", 0.24434613f, -0.034906626f, -0.6981318f)
                .translate(getPart("gun"), 40, "easeInOutSine", 0.04f, -0.04f, -0.09999999f)
                .rotate(getPart("gun"), 40, "easeInOutSine", -1.2217312f, 0.76794505f, 0.872665f)
                .translate(getPart("leftarm"), 40, "easeInOutSine", 0.31999996f, 0.22000001f, 0.24000002f)
                .translate(getPart("rightarmgun"), 50, "easeInOutSine", -0.26000002f, -0.3799999f, 0.2f)
                .rotate(getPart("rightarmgun"), 50, "easeInOutSine", 0.24434613f, -0.034906626f, 0.55850536f)
                .translate(getPart("leftarm"), 50, "easeInOutSine", 0.05999998f, -0.14f, 0.2f)
                .translate(getPart("rightarmgun"), 80, "easeInOutSine", -0.26000002f, -0.3799999f, 0.2f)
                .rotate(getPart("rightarmgun"), 80, "easeInOutSine", 0.24434613f, -0.034906626f, 0.55850536f)
                .translate(getPart("gun"), 80, "easeInOutSine", 0.04f, -0.04f, -0.09999999f)
                .rotate(getPart("gun"), 80, "easeInOutSine", -1.2217312f, 0.76794505f, 0.872665f)
                .translate(getPart("leftarm"), 80, "easeInOutSine", 0.05999998f, -0.14f, 0.2f)
                .rotate(getPart("leftarm"), 80, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 82, "easeOutSine", 0.05427049f, -0.18663122f, 0.1809017f)
                .translate(getPart("leftarm"), 86, "easeOutSine", 0.027135246f, -0.21331565f, 0.09045085f)
                .translate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("gun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("gun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .end();
        throwPunches = new RepetitiveAnimation("Throw Punches")
                .translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 10, "easeInOutSine", 0.29999998f, -0.2f, 0.15999998f)
                .rotate(getPart("leftarm"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.5399998f, 0.0f, 0.28f)
                .rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.10471975f, -0.2792527f, -0.9424782f)
                .translate(getPart("leftarm"), 40, "easeInOutSine", 0.29999998f, -0.2f, 0.15999998f)
                .rotate(getPart("leftarm"), 40, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 40, "easeInOutSine", 0.5399998f, 0.0f, 0.28f)
                .rotate(getPart("rightarmgun"), 40, "easeInOutSine", 0.10471975f, -0.2792527f, -0.9424782f)
                .translate(getPart("leftarm"), 50, "easeInOutSine", -0.040000014f, -0.26000002f, 0.65999967f)
                .rotate(getPart("leftarm"), 50, "easeInOutSine", 0.48869208f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 50, "easeInOutSine", -0.67999965f, 0.0f, 0.28f)
                .rotate(getPart("rightarmgun"), 50, "easeInOutSine", 0.10471975f, -0.2792527f, 0.55850536f)
                .translate(getPart("leftarm"), 90, "easeInOutSine", -0.040000014f, -0.26000002f, 0.65999967f)
                .rotate(getPart("leftarm"), 90, "easeInOutSine", 0.48869208f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 90, "easeInOutSine", -0.67999965f, 0.0f, 0.28f)
                .rotate(getPart("rightarmgun"), 90, "easeInOutSine", 0.10471975f, -0.2792527f, 0.55850536f)
                .translate(getPart("leftarm"), 100, "easeInOutSine", 0.0f, 0.0f, 0.0f, true)
                .rotate(getPart("leftarm"), 100, "easeInOutSine", -0.41887897f, 0.0f, 0.0f, true)
                .translate(getPart("rightarmgun"), 100, "easeInOutSine", 0.7199996f, -0.41999987f, 0.5399998f, true)
                .rotate(getPart("rightarmgun"), 100, "easeInOutSine", 0.66322523f, -1.1175871E-8f, -0.4886921f, true)
                .translate(getPart("rightarmgun"), 105, "easeInOutSine", 0.7199996f, -0.41999987f, 0.5399998f)
                .rotate(getPart("rightarmgun"), 105, "easeInOutSine", 0.66322523f, -1.1175871E-8f, -0.4886921f)
                .translate(getPart("rightarmgun"), 115, "easeInOutBack", 0.37999994f, 0.67999965f, -0.55999976f)
                .rotate(getPart("rightarmgun"), 115, "easeInOutBack", -0.45378548f, -1.1175871E-8f, -0.4886921f)
                .translate(getPart("leftarm"), 120, "easeInOutSine", 0.0f, 0.0f, 0.0f, true)
                .rotate(getPart("leftarm"), 120, "easeInOutSine", -0.41887897f, 0.0f, 0.0f, true)
                .translate(getPart("rightarmgun"), 120, "easeInOutSine", 0.37999994f, 0.67999965f, -0.55999976f, true)
                .rotate(getPart("rightarmgun"), 120, "easeInOutSine", -0.45378548f, -1.1175871E-8f, -0.4886921f, true)
                .translate(getPart("leftarm"), 130, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm"), 130, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 130, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 130, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .end();
    }

    @Override
    public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer,
                       MultiBufferSource.BufferSource impl, PoseStack matrix,
                       int light) {
        matrix.pushPose();

        float aimProgress = client.getAimHandler().getProgress(item);

        traslateRotate("all", matrix);
        lerpToTransform("aim", matrix, aimProgress);
        lerpToTransform("sprint", matrix, client.getClient().getSprintHandler()
                .getProgress(stack.getItem()));

        breath(matrix, 0.03f);

        matrix.pushPose();

        traslateRotate("rightarmgun", matrix);
        traslateRotate("rightarmgunpump", matrix);
        traslateRotate("rightarm", matrix);
        RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT, client
                .getClient().playerRenderer);

        matrix.popPose();
        matrix.pushPose();

        traslateRotate("leftarmgun", matrix);
        traslateRotate("leftarm", matrix);
        RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT,
                client.getClient().playerRenderer);

        matrix.popPose();
        matrix.pushPose();

        traslateRotate("leftarmgun", matrix);
        traslateRotate("rightarmgun", matrix);
        traslateRotate("rightarmgunpump", matrix);
        // Gun
        traslateRotate("gun", matrix);

        VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, RenderHelper.itemRenderType,
                true, stack.hasFoil());

        RenderHelper.renderItem(matrix, stack, consumer, light);

        matrix.popPose();

        matrix.popPose();
    }

    @Override
    public void renderOverlayPre(RenderGuiOverlayEvent.Pre e, LocalPlayer player,
                                 Font font) {

    }

    @Override
    public Animation getLookAnimAnimation(Player player) {
        return look;
    }

    @Override
    public Animation getAttackAnimAnimation(Player player) {
        return throwPunches;
    }

    @Override
    public void onUse(Player player, ItemStack stack) {
        if (canUse(player)) {
            // Throw punches animation
            int cycles = client.getClient().getRandom().nextInt(Config.SERVER
                    .smallHammerHitsRange.get().get(0), Config.SERVER
                    .smallHammerHitsRange.get().get(1));
            throwPunches.setCycles(cycles);
            throwPunches.build();
            animator.setAnimation(throwPunches);
            animator.play();

            afterUse(player);
        }
    }

    @Override
    public void onAnimationTick(Animation anim, float prevTick, float tick) {
        if(anim == look) {
            if(tick == 16) {
                Utils.playSoundOnServer(SoundRegistries.SWING.get());
            } else if(tick == 23) {
                MeleeHelper.hit(client.getClient(), Config.SERVER.smallHammer.damage
                        .get(), 0);
            }
        } else if(anim == throwPunches) {
            if(throwPunches.isTick(106, tick)) {
                Utils.playSoundOnServer(SoundRegistries.SWING.get());
                Minecraft.getInstance().player.swing(InteractionHand.MAIN_HAND, true);
            } else if(throwPunches.isTick(112, tick)) {
                MeleeHelper.hit(client.getClient(), Config.SERVER.smallHammer.damage
                        .get(), 2);
            }
        }
    }

    @Override
    public void cooldownFinished(Player player) {

    }

    @Override
    public JgPlayerPoseHandler getPlayerPoseHandler() {
        return PlayerPoseHandlers.HAMMER;
    }

    @Override
    public JgPlayerPose getPose(Player player) {
        if(client.getAimHandler().getProgress(null) > 0) {
            return PlayerPoses.AIMING;
        } else if(player.isSprinting()) {
            return PlayerPoses.RUNNING;
        }
        return PlayerPoses.HOLDINGGUN;
    }

    @Override
    public List<Animation> getAnimations() {
        return Utils.of(look, throwPunches);
    }

}
