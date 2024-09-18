package com.jg.jgpg.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.server.capabilities.JgPlayerDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class SynchronizeAnimationMessage {

    public UUID playerUUID;
    public int animationIndex;

    public SynchronizeAnimationMessage(UUID uuid, int animationIndex) {
        this.playerUUID = uuid;
        this.animationIndex = animationIndex;
    }

    public static void encode(SynchronizeAnimationMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeInt(msg.animationIndex);
    }

    public static SynchronizeAnimationMessage decode(FriendlyByteBuf buf) {
        return new SynchronizeAnimationMessage(buf.readUUID(), buf.readInt());
    }

    public static void handle(SynchronizeAnimationMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            player.getCapability(JgPlayerDataProvider.PLAYER_DATA)
                    .ifPresent(cap -> cap.setAnimationIndex(msg.animationIndex));

            LevelChunk chunk = player.serverLevel().getChunkAt(player.chunkPosition()
                    .getWorldPosition());
            PirateGuns.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new HandleAnimationChangedMessage(msg.playerUUID, msg.animationIndex));
        });
        ctx.get().setPacketHandled(true);
    }
    
}
