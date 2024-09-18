package com.jg.jgpg.client.handlers;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handler.IClientInputHandler;
import com.jg.jgpg.client.handlers.handlerparts.AimHandler;
import com.jg.jgpg.client.handlers.handlerparts.HitmarkerHandler;
import com.jg.jgpg.client.handlers.handlerparts.RecoilHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.AbstractGunModel;
import com.jg.jgpg.client.jgmodel.itemmodel.IAimable;
import com.jg.jgpg.client.jgmodel.itemmodel.itemmodels.PirateRifleModel;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.NBTUtils;
import com.jg.jgpg.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;

public class PirateGunsClientHandler extends AbstractClientHandler  implements
        IClientInputHandler, ResourceManagerReloadListener {

    AimHandler aim;
    HitmarkerHandler hitmarker;
    RecoilHandler recoils;
    float sensitivity;

    public PirateGunsClientHandler(ClientHandler client) {
        super(client);

        hitmarker = new HitmarkerHandler();

        addClientHandlerPart(aim = new AimHandler(this));
        addClientHandlerPart(recoils = new RecoilHandler(this));

        sensitivity = -1f;
    }

    @Override
    public void handleMouse(InputEvent.MouseButton e) {
        Item item = Utils.handStack().getItem();
        if(item instanceof IAimable) {
            aim.handleMouse(e);
        }
    }

    @Override
    public void handleKeyboard(InputEvent.Key e) {
        ItemStack stack = Utils.handStack();
        LocalPlayer player = Minecraft.getInstance().player;

        if(stack.getItem() instanceof JgGunItem) {

            AbstractGunModel model = (AbstractGunModel) getClient().getModel();

            if (model != null) {
                Animation currentAnimation = model.getAnimator().getAnimation();
                if (currentAnimation == null) {
                    // Gun related stuff
                    if (e.getKey() == ClientEventHandler.RELOAD.getKey().getValue()) {
                        model.tryToReload(player);
                    } else if (e.getKey() == ClientEventHandler.LOOKANIM.getKey().getValue()) {
                        model.getAnimator().setAnimation(model.getLookAnimAnimation(player));
                        model.getAnimator().play();
                    } else if (e.getKey() == ClientEventHandler.KICKBACK.getKey().getValue()) {
                        model.getAnimator().setAnimation(model.getKickbackAnimAnimation(player));
                        model.getAnimator().play();
                    } else if (ClientEventHandler.SWITCHPIRATERIFLESCOPE.consumeClick()) {
                        if (stack.getItem() == ItemRegistries.PIRATERIFLE.get()) {
                            CompoundTag notification = new CompoundTag();
                            boolean hasScope = NBTUtils.getExtraData(stack)
                                    .getBoolean(Constants.HASSCOPE);
                            notification.putBoolean(PirateRifleModel
                                    .QUITSCOPENOTIFICATION, hasScope);
                            model.processNbt(notification);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onTickPre(LocalPlayer player, ItemStack stack, String stackId) {
        super.onTickPre(player, stack, stackId);
    }

    @Override
    public void onTickPost(LocalPlayer player, ItemStack stack, String stackId) {
        Item item = stack.getItem();

        super.onTickPost(player, stack, stackId);

        // Mouse sensitivy change when aiming
        float aimProgress = aim.getProgress(item);
        if(aimProgress > 0.5f) {
            if (sensitivity == -1) {
                sensitivity = Minecraft.getInstance().options.sensitivity().get()
                        .floatValue();
            }
            float scale = 0.7f;

            if(item instanceof JgGunItem) {
                scale = ((JgGunItem) item).getAimSensitivity(stack);
            }

            Minecraft.getInstance().options.sensitivity().set((double) (sensitivity
                    * scale));
        } else {
            if(sensitivity != -1) {
                Minecraft.getInstance().options.sensitivity().set((double) sensitivity);
            }
            sensitivity = -1;
        }
    }

    @Override
    public float getProgress(Item item) {
        return 0;
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_10758_) {

    }

    public AimHandler getAimHandler() {
        return aim;
    }

    public HitmarkerHandler getHitmarker() {
        return hitmarker;
    }

    public RecoilHandler getRecoilsHandler() {
        return recoils;
    }

    public boolean canRenderHitmarker() {
        return hitmarker.hitmarkerTime > 0;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

}
