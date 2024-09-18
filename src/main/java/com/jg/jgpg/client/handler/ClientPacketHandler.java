package com.jg.jgpg.client.handler;

import com.jg.jgpg.client.handlers.ClientEventHandler;
import com.jg.jgpg.client.handlers.JgModelsHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.network.HandleAnimationChangedMessage;
import com.jg.jgpg.network.HandleJgModelUpdateMessage;
import com.jg.jgpg.network.HandlePlayerPoseChangedMessage;
import com.jg.jgpg.network.SynchronizePlayerDataMessage;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handleSynchronizePlayerData(SynchronizePlayerDataMessage msg,
        Supplier<NetworkEvent.Context> ctx) {
        PlayerData playerData = ClientHandler.getPlayerData(msg.playerUUID);
        if(msg.playerUUID == Minecraft.getInstance().player.getUUID()) {
            playerData.setModel(JgModelsHandler.get(msg.itemId));
        } else {
            playerData.setModel(JgModelsHandler.getSupplier(msg.itemId).get());
        }
        playerData.setPose(ClientEventHandler.getClientHandler().getPlayerPoses()
                .getPlayerPose(new ResourceLocation(msg.pose)));
    }


    public static void handlePlayerChangedPose(HandlePlayerPoseChangedMessage msg,
                                               Supplier<NetworkEvent.Context> ctx) {
        ClientHandler.getPlayerData(msg.playerUUID).setPose(ClientEventHandler
                .getClientHandler().getPlayerPoses().getPlayerPose(new ResourceLocation(msg
                        .pose)));
    }

    public static void handlePlayerChangedJgModel(HandleJgModelUpdateMessage msg,
                                                  Supplier<NetworkEvent.Context> ctx) {
        if(msg.playerUUID == Minecraft.getInstance().player.getUUID()) {
            ClientHandler.getPlayerData(msg.playerUUID).setModel(JgModelsHandler.get(msg
                    .itemId));
        } else {
            ClientHandler.getPlayerData(msg.playerUUID).setModel(JgModelsHandler.getSupplier(msg
                    .itemId).get());
        }
        LogUtils.log("ClientPacketHandler", "Syncrhonized: " +
                msg.playerUUID.toString() + " ItemId: " + msg.itemId);
    }

    public static void handleAnimationChanged(HandleAnimationChangedMessage msg,
        Supplier<NetworkEvent.Context> ctx) {
        if(msg.animationIndex != -1) {
            JgModel<?> model = ClientHandler.getPlayerData(msg.playerUUID).getModel();
            model.getAnimator().setAnimation(model.getAnimations().get(msg.animationIndex));
        }
    }

    public static void handleHitmarker() {
        Player player = Minecraft.getInstance().player;
        ClientEventHandler.getCustomClientHandler().getHitmarker().reset();
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(
                SoundRegistries.HITMARKER.get(), SoundSource.PLAYERS, 1.0F,
                1.0F, player.level().random, player.blockPosition()));
    }

}
