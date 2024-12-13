package com.jg.jgpg.client.handler;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.RepetitiveAnimation;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.common.network.AnimationChangedMessage;
import com.jg.jgpg.common.network.ShowHitmarkerMessage;
import com.jg.jgpg.registries.SoundRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandler {

    public static void handleAnimationChanged(IPayloadContext ctx, Player player,
                                              AnimationChangedMessage msg) {
        ctx.enqueueWork(() -> {
            JgModel<?> model = ClientHandler.getPlayerData(msg.playerUUID).getModel();
            PlayerData playerData = ClientHandler.getPlayerData(msg.playerUUID);

            // Getting the animation
            Animation anim = playerData.getModel()
                    .getAnimationByIndex(msg.animationIndex);
            if(anim instanceof RepetitiveAnimation repetitiveAnimation) {
                if(!msg.playerUUID.equals(Minecraft.getInstance().player.getUUID())) {
                    // Only set the animation if the client is not the local one as
                    // the animation is already set on the local client, otherwise it would
                    // lead to some bugs
                    repetitiveAnimation.setCycles(msg.animationCycles);
                    repetitiveAnimation.build();
                    model.getAnimator().setAnimation(repetitiveAnimation);
                    model.getAnimator().play();
                }
                playerData.setAnimation(repetitiveAnimation);
            } else {
                playerData.setAnimation(anim);
                if(!msg.playerUUID.equals(Minecraft.getInstance().player.getUUID())) {
                    // Only set the animation if the client is not the local one as
                    // the animation is already set on the local client, otherwise it would
                    // lead to some bugs
                    model.getAnimator().setAnimation(anim);
                    model.getAnimator().play();
                }
            }
        });
    }

    public static void handleHitmarker(IPayloadContext ctx, Player player,
                                              ShowHitmarkerMessage msg) {
        ClientEventHandler.getCustomClientHandler().getHitmarker().reset();
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(
                SoundRegistries.HITMARKER.get(), SoundSource.PLAYERS,
                1.0F, 1.0F, player.level().random,
                player.blockPosition()));
    }

}
