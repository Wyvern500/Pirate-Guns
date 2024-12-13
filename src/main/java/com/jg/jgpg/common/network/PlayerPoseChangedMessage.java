package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import com.jg.jgpg.registries.AttachmentRegistries;
import com.jg.jgpg.server.JgPlayerData;
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

public class PlayerPoseChangedMessage extends AbstractPacket {

    public static final CustomPacketPayload.Type<PlayerPoseChangedMessage> TYPE =
            new CustomPacketPayload.Type<>(PirateGuns.prefix("player_pose_changed"));
    public static final StreamCodec<RegistryFriendlyByteBuf,
            PlayerPoseChangedMessage> CODEC = StreamCodec
            .ofMember(PlayerPoseChangedMessage::toBytes,
                    PlayerPoseChangedMessage::new);

    public UUID playerUUID;
    public String pose;

    public PlayerPoseChangedMessage(UUID uuid, String pose) {
        this.playerUUID = uuid;
        this.pose = pose;
    }

    @Override
    void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeUtf(pose);
    }

    public PlayerPoseChangedMessage(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf());
    }

    @Override
    public void onClientReceived(Minecraft minecraft, IPayloadContext ctx, Player player) {
        ctx.enqueueWork(() -> {
            ClientHandler.getPlayerData(playerUUID).setPose(ClientEventHandler
                    .getClientHandler().getPlayerPoses()
                    .getPlayerPose(ResourceLocation.parse(pose)));
        });
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
        ctx.enqueueWork(() -> {
            JgPlayerData playerData = player.getData(AttachmentRegistries
                    .PLAYER_DATA_ATTACHMENT.get());

            playerData.setPose(pose);
            player.setData(AttachmentRegistries.PLAYER_DATA_ATTACHMENT.get(),
                    playerData);
            Networking.sendToPlayersInChunk(this, player.chunkPosition(),
                    player);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
}
