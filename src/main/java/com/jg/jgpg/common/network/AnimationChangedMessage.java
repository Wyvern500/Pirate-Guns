package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.ClientPacketHandler;
import com.jg.jgpg.registries.AttachmentRegistries;
import com.jg.jgpg.server.JgPlayerData;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class AnimationChangedMessage extends AbstractPacket {

    public static final CustomPacketPayload.Type<AnimationChangedMessage> TYPE =
            new CustomPacketPayload.Type<>(PirateGuns.prefix("animation_changed"));
    public static final StreamCodec<RegistryFriendlyByteBuf,
            AnimationChangedMessage> CODEC = StreamCodec
            .ofMember(AnimationChangedMessage::toBytes,
                    AnimationChangedMessage::new);

    public UUID playerUUID;
    public int animationIndex;
    public int animationCycles;

    public AnimationChangedMessage(UUID uuid, int animationIndex, int animationCycles) {
        this.playerUUID = uuid;
        this.animationIndex = animationIndex;
        this.animationCycles = animationCycles;
    }

    @Override
    void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeInt(animationIndex);
        buf.writeInt(animationCycles);
    }

    public AnimationChangedMessage(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readInt(), buf.readInt());
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
        ctx.enqueueWork(() -> {
            JgPlayerData playerData = player.getData(AttachmentRegistries
                    .PLAYER_DATA_ATTACHMENT.get());
            playerData.setAnimationIndex(animationIndex);
            player.setData(AttachmentRegistries.PLAYER_DATA_ATTACHMENT.get(),
                    playerData);
            LogUtils.log("AnimationChangedMessage", "AnimationChanged Xd");
            Networking.sendToPlayersInChunk(this, player.chunkPosition(),
                    player);
        });
    }

    @Override
    public void onClientReceived(Minecraft minecraft, IPayloadContext ctx, Player player) {
        ClientPacketHandler.handleAnimationChanged(ctx, player, this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
