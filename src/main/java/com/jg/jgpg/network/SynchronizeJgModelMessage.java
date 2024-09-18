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

public class SynchronizeJgModelMessage {

    public UUID playerUUID;
    public String itemId;

    public SynchronizeJgModelMessage(UUID uuid, String itemId) {
        this.playerUUID = uuid;
        this.itemId = itemId;
    }

    public static void encode(SynchronizeJgModelMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.itemId, 32727);
    }

    public static SynchronizeJgModelMessage decode(FriendlyByteBuf buf) {
        return new SynchronizeJgModelMessage(buf.readUUID(), buf.readUtf());
    }

    public static void handle(SynchronizeJgModelMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            player.getCapability(JgPlayerDataProvider.PLAYER_DATA)
                .ifPresent(cap -> {
                    cap.setModel(msg.itemId);
                });

            LevelChunk chunk = player.serverLevel().getChunkAt(player.chunkPosition()
                    .getWorldPosition());
            PirateGuns.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new HandleJgModelUpdateMessage(msg.playerUUID, msg.itemId));
        });
        ctx.get().setPacketHandled(true);
    }
    
}
