package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import com.jg.jgpg.client.handler.handlers.JgModelsHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.registries.AttachmentRegistries;
import com.jg.jgpg.server.JgPlayerData;
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

public class JgModelSynchronizationMessage extends AbstractPacket {

    public static final CustomPacketPayload.Type<JgModelSynchronizationMessage> TYPE =
            new CustomPacketPayload.Type<>(PirateGuns.prefix("jgmodel_synchronization"));
    public static final StreamCodec<RegistryFriendlyByteBuf,
            JgModelSynchronizationMessage> CODEC = StreamCodec.ofMember(JgModelSynchronizationMessage::toBytes,
            JgModelSynchronizationMessage::new);

    public UUID playerUUID;
    public String itemId;

    public JgModelSynchronizationMessage(UUID uuid, String itemId) {
        this.playerUUID = uuid;
        this.itemId = itemId;
    }

    @Override
    void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeUtf(itemId, 32727);
    }

    public JgModelSynchronizationMessage(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf());
    }

    @Override
    public void onClientReceived(Minecraft minecraft, IPayloadContext ctx, Player player) {
        ctx.enqueueWork(() -> {
            if(!Minecraft.getInstance().player.getUUID().equals(playerUUID)) {
                JgModel<?> model = JgModelsHandler.get(itemId).get();
                model.setIsEmulatedModel(true);
                ClientHandler.getPlayerData(playerUUID).setModel(model);
            } else {
                ClientHandler.getPlayerData(playerUUID).setModel(ClientEventHandler
                        .getClientHandler().getModel());
            }
        });
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
        ctx.enqueueWork(() -> {
            JgPlayerData playerData = player.getData(AttachmentRegistries
                    .PLAYER_DATA_ATTACHMENT.get());
            playerData.setModel(itemId);
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
