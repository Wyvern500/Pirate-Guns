package com.jg.jgpg.client.jgmodel.itemmodel.itemmodels;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.AbstractGunModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.model.item.HeavyHandCannonModModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.client.player.model.pose.PlayerPoseHandlers;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class HeavyHandCannonModel extends AbstractGunModel {

    Animation SHOOT;
    Animation LOOK;
    Animation HIT;
    Animation RELOAD;
    Animation RELOAD2;
    boolean renderFire;
    boolean renderItem;
    boolean shake;
    Item item;

    public HeavyHandCannonModel(AbstractClientHandler client) {
        super(client);
    }

    @Override
    public boolean canReload(Player player) {
        int bullets = InventoryUtils.getCountForItem(player, ItemRegistries
                .HEAVYHANDCANNONBULLET.get());
        int gunpowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
        return NBTUtils.getBullets(player.getMainHandItem()) <= 0 && gunpowder >= 5
                && bullets >= 1;
    }

    @Override
    public void reload(Player player) {
        boolean reload1 = client.getClient().getRandom().nextBoolean();
        animator.setAnimation(reload1 ? RELOAD : RELOAD2);
        animator.play();
    }

    @Override
    public Animation getLookAnimAnimation(Player player) {
        return LOOK;
    }

    @Override
    public Animation getKickbackAnimAnimation(Player player) {
        return HIT;
    }

    @Override
    public boolean canAim(Player player) {
        return animator.getAnimation() == null || animator.getAnimation() ==
                SHOOT;
    }

    @Override
    public void onUse(Player player, ItemStack stack) {
        if (canUse(player)) {
            rebuildShootAnimation(90);
            animator.setAnimation(SHOOT);
            animator.play();
        }
    }

    @Override
    public void cooldownFinished(Player player) {

    }

    protected void rebuildShootAnimation(int duration) {
        SHOOT = new Animation("Shoot")
                .translate(getPart("leftarm"), 0, "empty", 0.0f, 0.0f, 0.0f)
                .translate(getPart("fire"), 0, "empty", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgunpump"), 0, "empty", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 10, "empty", 0.059999995f, 0.43999985f, 0.13999999f)
                .translate(getPart("leftarm"), 20, "easeOutBack", -0.02f, 0.31999996f, 0.11999998f)
                .translate(getPart("leftarm"), 25, "empty", -0.02f, 0.31999996f, 0.11999998f)
                .translate(getPart("leftarm"), 35, "easeInOutBack", -0.28000003f, 0.26000002f, 0.11999998f)
                .translate(getPart("leftarm"), 45, "empty", 0.059999995f, 0.43999985f, 0.13999999f)
                .translate(getPart("leftarm"), 55, "easeOutBack", -0.02f, 0.31999996f, 0.11999998f)
                .translate(getPart("leftarm"), 60, "empty", -0.02f, 0.31999996f, 0.11999998f)
                .translate(getPart("leftarm"), 70, "easeInOutBack", -0.28000003f, 0.26000002f, 0.11999998f)
                .translate(getPart("fire"), 70, "empty", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 80, "empty", 0.0f, 0.0f, 0.0f)
                .translate(getPart("fire"), 70 + duration, "empty", 0.036000002f, -0.8199995f, -0.036000006f)
                .end();
    }

    @Override
    public JgPlayerPoseHandler getPlayerPoseHandler() {
        return PlayerPoseHandlers.CANNON;
    }

    @Override
    public JgPlayerPose getPose(Player player) {
        if(client.getAimHandler().getProgress(item) > 0.5) {
            return PlayerPoses.AIMING;
        } else if(player.isSprinting()){
            return PlayerPoses.RUNNING;
        } else if(animator.getAnimation() == RELOAD || animator.getAnimation() ==
            RELOAD2) {
            return PlayerPoses.RELOADING;
        }
        return PlayerPoses.HOLDINGGUN;
    }

    @Override
    public ToShowModModel getModModel(BakedModel origin) {
        return new HeavyHandCannonModModel(origin);
    }

    @Override
    public void initParts() {
        addPart(new JgModelPart("aim", -0.35060298f, 0.40579772f, 0.16999607f, -0.17104232f, -0.08988424f, 0.0f));
        addPart(new JgModelPart("all", -0.7599996f, 0.29999998f, 0.69999963f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("breathing", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("fire", -4.399953f, 3.040008f, -3.6599178f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("gun", 1.2799991f, -0.8999995f, -1.6399989f, 0.10471975f, 0.06981318f, 0.0f));
        addPart(new JgModelPart("hammer", 0.0f, 0.3799999f, 0.09999999f, -1.431171f, 0.0f, 0.0f));
        addPart(new JgModelPart("item", 1.1199993f, -1.0999993f, -2.0999985f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("leftarm", 0.23999995f, -0.55999976f, -0.91999936f, 0.0f, -1.2217312f, 0.0f));
        addPart(new JgModelPart("leftarm_item", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("leftarmgun", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("leftarmstick", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("muzzle", 1.1399993f, -0.8199995f, -3.0199976f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("recoil", 0.0f, 0.007999802f, 0.14000022f, 1.8626451E-8f, 0.0f, 0.0f));
        addPart(new JgModelPart("recoilaccumulation", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("rightarm", 0.5399998f, -0.19999996f, -0.51999974f, -0.52359873f, 0.17453296f, 0.6981318f));
        addPart(new JgModelPart("rightarmgun", 0.0f, 0.0f, 0.0f, 0.034906626f, 0.0f, 0.0f));
        addPart(new JgModelPart("rightarmgunaroundpoint", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("rightarmgunaroundpointvisualizer", 1.1f, -1.4f, -1.2600005f, 3.7252903E-9f, 0.0f, 0.0f));
        addPart(new JgModelPart("rightarmgunpump", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        addPart(new JgModelPart("sprint", -0.27999994f, -0.38f, 0.3f, 0.418879f, -0.13962635f, 0.0f));
        addPart(new JgModelPart("stick", 0.85999954f, -0.93999946f, -1.0799997f, 0.2094395f, -0.1047197f, 0.27925268f));
    }

    @Override
    public void initAnimations() {
        rebuildShootAnimation(30);
        LOOK = new Animation("Look")
                .translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.66322523f, 0.0f, -0.94247824f)
                .translate(getPart("leftarm"), 10, "easeInOutSine", 0.32f, 0.28f, 0.22000004f)
                .translate(getPart("rightarmgun"), 40, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 40, "easeInOutSine", 0.66322523f, 0.0f, -0.94247824f)
                .translate(getPart("leftarm"), 40, "easeInOutSine", 0.32f, 0.28f, 0.22000004f)
                .translate(getPart("rightarmgun"), 50, "easeInOutSine", 0.43999985f, 0.11999998f, 0.059999995f)
                .rotate(getPart("rightarmgun"), 50, "easeInOutSine", 0.66322523f, 0.0f, 1.1519179f)
                .translate(getPart("leftarm"), 50, "easeInOutSine", -0.080000006f, 0.29999998f, 0.18000002f)
                .translate(getPart("rightarmgun"), 80, "easeInOutSine", 0.43999985f, 0.11999998f, 0.059999995f)
                .rotate(getPart("rightarmgun"), 80, "easeInOutSine", 0.66322523f, 0.0f, 1.1519179f)
                .translate(getPart("leftarm"), 80, "easeInOutSine", -0.080000006f, 0.29999998f, 0.18000002f)
                .translate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 90, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .end();
        HIT = new Animation("Hit")
                .translate(getPart("all"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("all"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("all"), 10, "easeInBack", -0.33999994f, -0.57999974f, 0.43999985f)
                .rotate(getPart("all"), 10, "easeInBack", 0.0f, 3.7252903E-9f, 0.48869208f)
                .translate(getPart("all"), 15, "easeInOutSine", -0.33999994f, -0.57999974f, 0.43999985f)
                .rotate(getPart("all"), 15, "easeInOutSine", 0.0f, 3.7252903E-9f, 0.48869208f)
                .translate(getPart("rightarm"), 18, "easeInOutSine", 0.07999999f, 0.16f, 0.0f)
                .rotate(getPart("rightarm"), 18, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("all"), 20, "easeInOutBack", -0.55999976f, -0.67999965f, 0.079999976f)
                .rotate(getPart("all"), 20, "easeInOutBack", 0.20943953f, 3.7252903E-9f, 0.48869208f)
                .translate(getPart("rightarm"), 20, "easeInOutSine", 0.07999999f, 0.11999998f, 0.04f)
                .rotate(getPart("rightarm"), 20, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("all"), 25, "easeInOutSine", -0.55999976f, -0.67999965f, 0.079999976f)
                .rotate(getPart("all"), 25, "easeInOutSine", 0.20943953f, 3.7252903E-9f, 0.48869208f)
                .translate(getPart("all"), 35, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("all"), 35, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarm"), 35, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 35, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .end();
        RELOAD = new Animation("Reload")
                .translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm_item"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("item"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("item"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 15, "easeInOutSine", 0.7399996f, -0.059999995f, -0.8599995f)
                .rotate(getPart("rightarmgun"), 15, "easeInOutSine", 1.5009842f, 0.80285174f, 0.0f)
                .translate(getPart("leftarm_item"), 15, "easeInOutSine", 0.24000002f, -1.5599989f, 0.5999997f)
                .rotate(getPart("leftarm_item"), 15, "easeInOutSine", 0.69813186f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 20, "easeInOutSine", 0.7399996f, -0.059999995f, -0.8599995f)
                .rotate(getPart("rightarmgun"), 20, "easeInOutSine", 1.5009842f, 0.80285174f, 0.0f)
                .translate(getPart("leftarm_item"), 20, "easeInOutSine", 0.24000002f, -1.5599989f, 0.5999997f)
                .rotate(getPart("leftarm_item"), 20, "easeInOutSine", 0.69813186f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 30, "easeInOutSine", -0.31999996f, -1.4999989f, 0.9399994f)
                .rotate(getPart("leftarm_item"), 30, "easeInOutSine", 0.69813186f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 45, "easeInOutSine", -0.31999996f, -1.4999989f, 0.9399994f)
                .rotate(getPart("leftarm_item"), 45, "easeInOutSine", 0.69813186f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 55, "easeInOutSine", 0.7399996f, -0.059999995f, -0.8599995f)
                .rotate(getPart("rightarmgun"), 55, "easeInOutSine", 1.5009842f, 0.80285174f, 0.0f)
                .translate(getPart("leftarm_item"), 55, "easeInOutSine", 0.1f, -0.8199997f, 0.47999984f)
                .rotate(getPart("leftarm_item"), 55, "easeInOutSine", 0.48869208f, 0.0f, 0.0f)
                .translate(getPart("item"), 55, "easeInOutSine", -0.16f, 0.0f, 0.0f)
                .rotate(getPart("item"), 55, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 65, "easeInOutSine", 1.2999991f, -0.53999996f, 0.69999963f)
                .rotate(getPart("leftarm_item"), 65, "easeInOutSine", 0.48869208f, 0.0f, -0.872665f)
                .translate(getPart("item"), 65, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("item"), 65, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("item"), 75, "easeInOutSine", 0.09999999f, -0.11999998f, 0.0f)
                .rotate(getPart("item"), 75, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 85, "easeInOutSine", 0.83999956f, -0.7399998f, 1.1999992f)
                .rotate(getPart("leftarm_item"), 85, "easeInOutSine", 0.48869208f, 0.0f, -0.872665f)
                .translate(getPart("item"), 85, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("item"), 85, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 95, "easeInOutSine", 0.1f, -0.8199997f, 0.47999984f)
                .rotate(getPart("leftarm_item"), 95, "easeInOutSine", 0.48869208f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 105, "easeInOutSine", 1.2999991f, -0.53999996f, 0.69999963f)
                .rotate(getPart("leftarm_item"), 105, "easeInOutSine", 0.48869208f, 0.0f, -0.872665f)
                .translate(getPart("item"), 105, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 115, "easeInOutSine", 1.2999991f, -0.53999996f, 0.69999963f)
                .rotate(getPart("leftarm_item"), 115, "easeInOutSine", 0.48869208f, 0.0f, -0.872665f)
                .translate(getPart("item"), 115, "easeInOutSine", 0.20000002f, -0.18f, 0.0f)
                .translate(getPart("leftarm_item"), 125, "easeInOutSine", 0.87999964f, -0.46000004f, 1.439999f)
                .rotate(getPart("leftarm_item"), 125, "easeInOutSine", 0.48869208f, 0.0f, -0.872665f)
                .translate(getPart("item"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("item"), 125, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 135, "easeInOutSine", 1.559999f, -0.080000184f, 0.5799998f)
                .rotate(getPart("leftarm_item"), 135, "easeInOutSine", 0.48869208f, 0.52359873f, -0.52359873f)
                .translate(getPart("item"), 135, "easeInOutSine", -0.24000002f, 0.11999998f, 0.15999998f)
                .rotate(getPart("item"), 135, "easeInOutSine", 1.9896768f, 0.38397238f, -0.52359873f)
                .translate(getPart("leftarm_item"), 145, "easeOutBack", 1.8399987f, -0.42000008f, 0.5599998f)
                .rotate(getPart("leftarm_item"), 145, "easeOutBack", 0.48869208f, 0.52359873f, -0.52359873f)
                .translate(getPart("leftarm_item"), 150, "easeInOutSine", 1.8399987f, -0.42000008f, 0.5599998f)
                .rotate(getPart("leftarm_item"), 150, "easeInOutSine", 0.48869208f, 0.52359873f, -0.52359873f)
                .translate(getPart("leftarm_item"), 160, "easeInOutSine", 1.559999f, -0.080000184f, 0.5799998f)
                .rotate(getPart("leftarm_item"), 160, "easeInOutSine", 0.48869208f, 0.52359873f, -0.52359873f)
                .translate(getPart("leftarm_item"), 170, "easeOutBack", 1.8399987f, -0.42000008f, 0.5599998f)
                .rotate(getPart("leftarm_item"), 170, "easeOutBack", 0.48869208f, 0.52359873f, -0.52359873f)
                .translate(getPart("leftarm_item"), 175, "easeInOutSine", 1.8399987f, -0.42000008f, 0.5599998f)
                .rotate(getPart("leftarm_item"), 175, "easeInOutSine", 0.48869208f, 0.52359873f, -0.52359873f)
                .translate(getPart("leftarm_item"), 180, "easeInOutSine", 1.539999f, -0.080000184f, 0.5599998f)
                .translate(getPart("rightarmgun"), 185, "easeInOutSine", 0.7399996f, -0.059999995f, -0.8599995f)
                .rotate(getPart("rightarmgun"), 185, "easeInOutSine", 1.5009842f, 0.80285174f, 0.0f)
                .translate(getPart("leftarm_item"), 185, "easeInOutSine", 1.2799993f, -0.48000002f, 1.419999f)
                .rotate(getPart("leftarm_item"), 185, "easeInOutSine", 0.48869208f, 0.52359873f, -0.52359873f)
                .translate(getPart("item"), 185, "easeInOutSine", -0.24000002f, 0.11999998f, 0.15999998f)
                .rotate(getPart("item"), 185, "easeInOutSine", 1.9896768f, 0.38397238f, -0.52359873f)
                .translate(getPart("rightarmgun"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm_item"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm_item"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("item"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("item"), 195, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .end();
        RELOAD2 = new Animation("Reload2")
                .translate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("gun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("gun"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("item"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("item"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 0, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 10, "easeInOutSine", 0.31415927f, 0.24434613f, 0.0f)
                .translate(getPart("leftarm"), 10, "easeInOutSine", -0.5199998f, 0.3999999f, 0.57999974f)
                .rotate(getPart("leftarm"), 10, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 20, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 20, "easeInOutSine", -0.24434611f, 0.24434613f, 0.0f)
                .translate(getPart("rightarmgun"), 25, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 25, "easeInOutSine", -0.24434611f, 0.24434613f, 0.0f)
                .translate(getPart("gun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("gun"), 30, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarmgun"), 35, "easeInOutBack", 0.0f, 0.09999999f, 0.0f)
                .rotate(getPart("rightarmgun"), 35, "easeInOutBack", 0.4886921f, 0.24434613f, 0.0f)
                .translate(getPart("gun"), 40, "easeOutBack", -0.2f, -0.24000005f, -0.09999997f)
                .rotate(getPart("gun"), 40, "easeOutBack", 1.1868246f, -0.1570845f, -0.10471973f)
                .translate(getPart("item"), 40, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("item"), 50, "easeInOutSine", -1.2199992f, -0.65999967f, 0.0f)
                .translate(getPart("item"), 60, "easeOutBack", -1.2199992f, 0.99999934f, 0.0f)
                .translate(getPart("item"), 70, "easeInBack", -0.39999998f, 0.99999934f, 0.0f)
                .translate(getPart("item"), 80, "easeInOutBack", -0.36f, 0.33999997f, 0.35999995f)
                .translate(getPart("item"), 90, "easeInOutSine", -1.2199992f, -0.65999967f, 0.0f)
                .translate(getPart("item"), 100, "easeOutBack", -1.2199992f, 0.99999934f, 0.0f)
                .translate(getPart("item"), 110, "easeInBack", -0.20000015f, 0.99999934f, 0.0f)
                .translate(getPart("item"), 120, "easeInOutBack", -0.24000011f, 0.33999997f, 0.35999995f)
                .translate(getPart("item"), 130, "easeInOutSine", -1.2199992f, -0.65999967f, 0.0f)
                .translate(getPart("item"), 140, "easeOutBack", -1.2199992f, 0.99999934f, 0.0f)
                .translate(getPart("item"), 150, "easeInBack", -0.3400001f, 1.0199994f, 0.6199997f)
                .translate(getPart("item"), 160, "easeInOutBack", -0.36f, 0.33999997f, 0.39999992f)
                .translate(getPart("item"), 165, "easeInOutSine", -0.36f, 0.33999997f, 0.39999992f)
                .translate(getPart("item"), 175, "easeOutBack", -0.36f, 0.69999963f, 0.6399997f)
                .translate(getPart("item"), 185, "easeInOutBack", -0.36f, 0.33999997f, 0.39999992f)
                .translate(getPart("rightarmgun"), 190, "easeInOutSine", 0.0f, 0.100004f, 0.0f)
                .rotate(getPart("rightarmgun"), 190, "easeInOutSine", 0.48872152f, 0.24434613f, 0.0f)
                .translate(getPart("leftarm"), 190, "easeInOutSine", -0.5177115f, 0.39823967f, 0.5774474f)
                .rotate(getPart("leftarm"), 190, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("gun"), 190, "easeInOutSine", -0.20000437f, -0.24000528f, -0.100002155f)
                .rotate(getPart("gun"), 190, "easeInOutSine", 1.1868504f, -0.15708792f, -0.104722016f)
                .translate(getPart("item"), 190, "easeInOutSine", -0.36f, 0.33999997f, 0.39999992f)
                .translate(getPart("rightarm"), 190, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 190, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("item"), 200, "easeInOutSine", -0.36f, 1.0599993f, 0.57999974f)
                .rotate(getPart("item"), 200, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarm"), 200, "easeInOutBack", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 200, "easeInOutBack", 0.31415927f, 0.0f, 0.0f)
                .translate(getPart("item"), 210, "easeInOutSine", -0.36f, 1.0599993f, 0.57999974f)
                .rotate(getPart("item"), 210, "easeInOutSine", -1.1868246f, 0.2443461f, -1.7802372f)
                .translate(getPart("rightarm"), 210, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 210, "easeInOutSine", 0.31415927f, 0.0f, 0.0f)
                .translate(getPart("item"), 220, "easeInOutSine", -0.20000009f, 1.0599993f, 0.57999974f)
                .rotate(getPart("item"), 220, "easeInOutSine", -1.1868246f, 0.2443461f, -1.7802372f)
                .translate(getPart("rightarm"), 220, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 220, "easeInOutSine", 0.31415927f, -0.27925268f, 0.0f)
                .translate(getPart("rightarmgun"), 230, "easeInOutSine", 0.0f, 0.100004f, 0.0f)
                .rotate(getPart("rightarmgun"), 230, "easeInOutSine", 0.48872152f, 0.24434613f, 0.0f)
                .translate(getPart("leftarm"), 230, "easeInOutSine", -0.5177115f, 0.39823967f, 0.5774474f)
                .rotate(getPart("leftarm"), 230, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("gun"), 230, "easeInOutSine", -0.20000437f, -0.24000528f, -0.100002155f)
                .rotate(getPart("gun"), 230, "easeInOutSine", 1.1868504f, -0.15708792f, -0.104722016f)
                .translate(getPart("item"), 230, "easeInOutBack", -1.8199986f, 1.0599993f, 0.57999974f)
                .rotate(getPart("item"), 230, "easeInOutBack", -1.1868246f, 0.2443461f, -1.7802372f)
                .translate(getPart("rightarm"), 230, "easeInOutBack", 0.16f, -0.07999999f, 0.05999998f)
                .rotate(getPart("rightarm"), 230, "easeInOutBack", 0.31415927f, 0.48869213f, 0.0f)
                .translate(getPart("rightarmgun"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarmgun"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("leftarm"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("leftarm"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("gun"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("gun"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("item"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("item"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .translate(getPart("rightarm"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .rotate(getPart("rightarm"), 240, "easeInOutSine", 0.0f, 0.0f, 0.0f)
                .end();
    }

    @Override
    public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer,
                       MultiBufferSource.BufferSource impl, PoseStack matrix, int light) {
        // Check if the gun has bullets
        boolean outOfBullets = NBTUtils.getBullets(stack) == 0;
        if (outOfBullets) {
            getPart("hammer").getDtransform().set(0.0f, 0.3799999f, 0.09999999f, -1.431171f, 0.0f, 0.0f);
        } else {
            getPart("hammer").getDtransform().set(0, 0, 0, 0, 0, 0);
        }

        matrix.pushPose();

        float f = 0.01f;

        Easing easing = EasingHandler.INSTANCE.getEasing("easeOutBack");

        if (client.getClient().shouldRenderDefault()) {
            traslateRotate("recoil", matrix);
        }

        float aimProgress = client.getAimHandler().getProgress(stack.getItem());

        shootAndRecoilTransformation(matrix, easing);

        float shootProgress = client.getClient().getCooldownHandler()
                .getCooldownPercent(item,0);
        float compensator = 1f;
        if(aimProgress > 0.5f) {
            compensator = 0.5f;
        }
        lerpToTransform("recoil", matrix, shootProgress * compensator);

        // Breathing simulation
        breath(matrix, 0.01f);

        // LogUtils.log("Colt1911Model", "P: " +
        // client.getBreathingHandler().getProgress(null));

        traslateRotate("all", matrix);
        lerpToTransform("aim", matrix, aimProgress);
        lerpToTransform("sprint", matrix, client.getClient().getSprintHandler().getProgress(stack.getItem()));

        // Right Arm
        matrix.pushPose();

        //traslateRotate("rightarmgunaroundpoint", "rightarmgunaroundpointvisualizer", matrix);
        traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
        traslateRotate("rightarmgunpump", matrix);
        traslateRotate("rightarm", matrix);
        if(shake) {
            matrix.translate(Math.random() * f, Math.random() * f,
                    Math.random() * f);
        }
        if(animator.getAnimation() == RELOAD2) {
            if(animator.getTick() >= 200 && animator.getTick() <= 210) {
                float progress = (animator.getTick() - 200) / 10;
                float radius = -0.1f;
                float angle = progress * (float) Math.PI * 2;
                float x = (float) Math.cos(angle) * radius;
                float y = (float) Math.sin(angle) * radius;

                matrix.translate(x, y, 0);
            }
        }
        RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT, client
                .getClient().playerRenderer);

        matrix.popPose();

        if(renderItem && item != null) {
            matrix.pushPose();

            traslateRotate("leftarm_item", matrix);
            traslateRotate("item", matrix);
            RenderHelper.renderItem(matrix, new ItemStack(item), buffer, light);

            matrix.popPose();
        }

        // Left Arm
        matrix.pushPose();

        traslateRotate("leftarmgun", matrix);
        traslateRotate("leftarmstick", matrix);
        traslateRotate("leftarm_item", matrix);
        traslateRotate("leftarm", matrix);
        if(shake) {
            matrix.translate(Math.random() * f, Math.random() * f,
                    Math.random() * f);
        }
        RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT, client
                .getClient().playerRenderer);

        matrix.popPose();
        // Gun
        matrix.pushPose();

        //traslateRotate("rightarmgunaroundpoint", "rightarmgunaroundpointvisualizer", matrix);
        traslateRotate("leftarmgun", matrix);
        traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
        traslateRotate("rightarmgunpump", matrix);
        traslateRotate("gun", matrix);
        RenderHelper.renderItem(matrix, stack, buffer, light);

        matrix.popPose();

        if(renderFire) {
            matrix.pushPose();

            float scale = 0.05f;

            traslateRotate("leftarmgun", matrix);
            traslateRotate("rightarmgun", "rightarmgunaroundpointvisualizer", matrix);
            traslateRotate("rightarmgunpump", matrix);
            traslateRotate("gun", matrix);
            matrix.scale(scale, scale, scale);
            traslateRotate("fire", matrix);
            BlockState blockState = Blocks.FIRE.defaultBlockState();
            BlockRenderDispatcher bRender = Minecraft.getInstance().getBlockRenderer();
            //BakedModel fire = bRender.getBlockModel(blockState);
            bRender.renderSingleBlock(blockState, matrix, buffer, light, OverlayTexture
                            .NO_OVERLAY, net.minecraftforge.client.model.data.ModelData.EMPTY,
                    null);

            matrix.popPose();
        }

        matrix.popPose();

    }

    @Override
    public void onAnimationEnd(Animation anim) {
        super.onAnimationEnd(anim);
        if(anim == SHOOT) {
            renderFire = false;

            Player player = Minecraft.getInstance().player;
            shoot(player, getGun());
            xDirection = (Utils.random.nextBoolean() ? 1 : -1);
            NBTUtils.setBullets(Utils.handStack(), Math.max(0, NBTUtils.getBullets(Utils
                    .handStack()) - 1));
            afterUse(player);

        } else if(anim == RELOAD) {
            renderItem = false;
        } else if(anim == RELOAD2) {
            renderItem = false;
            shake = false;
        }
    }

    @Override
    public void onAnimationStart(Animation anim) {
        super.onAnimationStart(anim);
        renderFire = false;
    }

    @Override
    public void onAnimationTick(Animation anim, float prevTick, float tick) {
        if(anim == SHOOT) {
            if(tick == 30) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 64) {
                renderFire = true;
                Utils.playSoundOnServer(SoundEvents.FLINTANDSTEEL_USE);
            } else if(tick == 65) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            }
        } else if(anim == HIT) {
            if(tick == 24) {
                Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
            } else if(tick == 27) {
                MeleeHelper.hit(client.getClient(), Config.SERVER.heavyHandCannon.meleeDamage
                        .get(), 2);
            } else if(tick == 82) {
                Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
            } else if(tick == 120) {
                Utils.playSoundOnServer(SoundRegistries.METALIC_DING.get());
            } else if(tick == 157) {
                Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
            } else if(tick == 182) {
                Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
            }
        } else if(anim == RELOAD) {
            if(tick == 40) {
                renderItem = true;
                item = Items.GUNPOWDER;
            } else if(tick == 69) {
                Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
            } else if(tick == 80) {
                renderItem = true;
                item = ItemRegistries.HEAVYHANDCANNONBULLET.get();
            } else if(tick == 113) {
                Utils.playSoundOnServer(SoundRegistries.METALIC_DING.get());
            } else if(tick == 120) {
                renderItem = true;
                item = Items.STICK;
            } else if(tick == 143) {
                Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
            } else if(tick == 164) {
                Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());

                // Remove reload materials
                if(ReloadUnloadUtils.processReloadIngredients(getGun().getAmmo())) {
                    ReloadUnloadUtils.addBullet();
                }

            } else if(tick == 187) {
                renderItem = false;
            }
        } else if(anim == RELOAD2) {
            if(tick == 32) {
                shake = true;
                Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
            } else if(tick == 50) {
                item = Items.GUNPOWDER;
                renderItem = true;
            } else if(tick == 52) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 68) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 77) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 80) {
                renderItem = false;
            } else if(tick == 90) {
                item = ItemRegistries.HEAVYHANDCANNONBULLET.get();
                renderItem = true;
            } else if(tick == 92) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 108) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 117) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 120) {
                renderItem = false;
            } else if(tick == 130) {
                item = Items.STICK;
                renderItem = true;
            } else if(tick == 132) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 150) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 159) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 171) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 181) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());

                // Remove reload materials
                if(ReloadUnloadUtils.processReloadIngredients(getGun().getAmmo())) {
                    ReloadUnloadUtils.addBullet();
                }

            } else if(tick == 198) {
                Utils.playSoundOnServer(SoundRegistries.SHORTROTATE.get());
            } else if(tick == 220) {
                Utils.playSoundOnServer(SoundRegistries.SWING_WHOOSH_3.get());
            } else if(tick == 228) {
                renderItem = false;
            }
        }
    }

    @Override
    public List<Animation> getAnimations() {
        return Utils.of(SHOOT, LOOK, HIT, RELOAD, RELOAD2);
    }

}
