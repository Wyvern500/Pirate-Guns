package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.RepetitiveAnimation;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handler.PlayerData;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import com.jg.jgpg.client.handler.handlers.JgModelsHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class PlayerDataSynchronizationMessage extends AbstractPacket {

    public static final CustomPacketPayload.Type<PlayerDataSynchronizationMessage> TYPE =
            new CustomPacketPayload.Type<>(PirateGuns.prefix("player_data_synchronization"));
    public static final StreamCodec<RegistryFriendlyByteBuf,
            PlayerDataSynchronizationMessage> CODEC = StreamCodec.ofMember(PlayerDataSynchronizationMessage::toBytes,
            PlayerDataSynchronizationMessage::new);

    public UUID requesterUUID;
    public String itemId;
    public String pose;
    public int animationIndex;
    public int animationCycles;
    public int animationTick;

    public PlayerDataSynchronizationMessage(UUID requesterUUID, String itemId, String pose,
                                        int animationIndex, int animationCycles,
                                        int animationTick) {
        this.requesterUUID = requesterUUID;
        this.itemId = itemId;
        this.pose = pose;
        this.animationIndex = animationIndex;
        this.animationCycles = animationCycles;
        this.animationTick = animationTick;
    }

    @Override
    void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(requesterUUID);
        buf.writeUtf(itemId, 32727);
        buf.writeUtf(pose, 32727);
        buf.writeInt(animationIndex);
        buf.writeInt(animationCycles);
        buf.writeInt(animationTick);
    }

    public PlayerDataSynchronizationMessage(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf(),
                buf.readUtf(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void onClientReceived(Minecraft minecraft, IPayloadContext ctx, Player player) {
        ctx.enqueueWork(() -> {
            PlayerData playerData = ClientHandler.getPlayerData(requesterUUID);
            if(!Minecraft.getInstance().player.getUUID().equals(requesterUUID)) {
                // This is a emulated model, it means that it wont update the model parts as
                // we only need to update the animation tick so the progress can be reflected
                JgModel<?> model = JgModelsHandler.get(itemId).get();
                model.setIsEmulatedModel(true);
                playerData.setModel(model);
            } else {
                // Setting the model to a non emulated model, it means this model is for
                // this client
                playerData.setModel(ClientEventHandler.getClientHandler().getModel());
            }
            // Setting the pose
            playerData.setPose(ClientEventHandler.getClientHandler().getPlayerPoses()
                    .getPlayerPose(ResourceLocation.parse(pose)));
            // Getting the animation
            Animation anim = playerData.getModel().getAnimationByIndex(animationIndex);
            if(anim instanceof RepetitiveAnimation repetitiveAnimation) {
                // The animation is a repetitive one so we need to set the cycles and
                // build it to keep the sync
                if(!requesterUUID.equals(Minecraft.getInstance().player.getUUID())) {
                    // Only set the animation if the client is not the local one as
                    // the animation is already set on the local client, otherwise it would
                    // lead to some bugs
                    repetitiveAnimation.setCycles(animationCycles);
                    repetitiveAnimation.build();
                    playerData.getModel().getAnimator().setAnimation(repetitiveAnimation);
                    playerData.getModel().getAnimator().play();
                }
                playerData.setAnimation(repetitiveAnimation);
            } else {
                playerData.setAnimation(anim);
                if(!requesterUUID.equals(Minecraft.getInstance().player.getUUID())) {
                    // Only set the animation if the client is not the local one as
                    // the animation is already set on the local client, otherwise it would
                    // lead to some bugs
                    playerData.getModel().getAnimator().setAnimation(anim);
                    playerData.getModel().getAnimator().play();
                }
            }
            // Synchronizing the animation tick so that they are not out of sync
            playerData.getModel().getAnimator().setTick(animationTick);
        });
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer,
                                 IPayloadContext ctx, ServerPlayer player) {
        ctx.enqueueWork(() -> {
            ServerPlayer toPlayer = (ServerPlayer) player.level()
                    .getPlayerByUUID(requesterUUID);
            Networking.sendToPlayerClient(this, toPlayer);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
