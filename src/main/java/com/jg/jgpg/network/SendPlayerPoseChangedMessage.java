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

public class SendPlayerPoseChangedMessage {

    public UUID playerUUID;
    public String pose;

    public SendPlayerPoseChangedMessage(UUID uuid, String pose) {
        this.playerUUID = uuid;
        this.pose = pose;
    }

    public static void encode(SendPlayerPoseChangedMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.pose, 32727);
    }

    public static SendPlayerPoseChangedMessage decode(FriendlyByteBuf buf) {
        return new SendPlayerPoseChangedMessage(buf.readUUID(), buf.readUtf());
    }

    public static void handle(SendPlayerPoseChangedMessage msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            player.getCapability(JgPlayerDataProvider.PLAYER_DATA)
                    .ifPresent(cap -> {
                        cap.setPose(msg.pose);
                    });

            LevelChunk chunk = player.serverLevel().getChunkAt(player.chunkPosition()
                            .getWorldPosition());
            PirateGuns.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new HandlePlayerPoseChangedMessage(msg.playerUUID, msg.pose));
        });
        ctx.get().setPacketHandled(true);
    }
    
}
