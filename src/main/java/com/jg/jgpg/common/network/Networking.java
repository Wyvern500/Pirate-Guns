package com.jg.jgpg.common.network;

import com.jg.jgpg.utils.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

// Code from: Ars-Nouveau, of baileyholl Bailey Hollingsworth
public class Networking {

    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar reg = event.registrar("1");
        // Server
        reg.playToServer(AddItemMessage.TYPE, AddItemMessage.CODEC,
                Networking::handle);
        reg.playToServer(BodyHitMessage.TYPE, BodyHitMessage.CODEC,
                Networking::handle);
        reg.playToServer(ConsumeItemMessage.TYPE, ConsumeItemMessage.CODEC,
                Networking::handle);
        reg.playToServer(CraftItemMessage.TYPE, CraftItemMessage.CODEC,
                Networking::handle);
        reg.playToServer(InitGunMessage.TYPE, InitGunMessage.CODEC,
                Networking::handle);
        reg.playToServer(PlaySoundMessage.TYPE, PlaySoundMessage.CODEC,
                Networking::handle);
        reg.playToServer(SetBulletsMessage.TYPE,
                SetBulletsMessage.CODEC,
                Networking::handle);
        reg.playToServer(ShootMessage.TYPE,
                ShootMessage.CODEC,
                Networking::handle);
        reg.playToServer((SetScopeMessage.TYPE),
                SetScopeMessage.CODEC,
                Networking::handle);
        reg.playToServer((SetLoadedMessage.TYPE),
                SetLoadedMessage.CODEC,
                Networking::handle);
        // Client
        reg.playToClient(RequestPlayerDataSynchronizationMessage.TYPE,
                RequestPlayerDataSynchronizationMessage.CODEC,
                Networking::handle);
        LogUtils.log("Networking", "Show hitmarker");
        reg.playToClient(ShowHitmarkerMessage.TYPE,
                ShowHitmarkerMessage.CODEC,
                Networking::handle);
        // Bidirectional
        LogUtils.log("Networking", "Starting bidirectional");
        reg.playBidirectional(AnimationChangedMessage.TYPE,
                AnimationChangedMessage.CODEC, Networking::handle);
        LogUtils.log("Networking", "Next Xd");
        reg.playBidirectional(PlayerPoseChangedMessage.TYPE,
                PlayerPoseChangedMessage.CODEC,
                Networking::handle);
        reg.playBidirectional(PlayerDataSynchronizationMessage.TYPE,
                PlayerDataSynchronizationMessage.CODEC,
                Networking::handle);
        reg.playBidirectional(JgModelSynchronizationMessage.TYPE,
                JgModelSynchronizationMessage.CODEC,
                Networking::handle);
    }

    private static <T extends AbstractPacket> void handle(T message,
                                                          IPayloadContext ctx) {
        if (ctx.flow().getReceptionSide() == LogicalSide.SERVER) {
            handleServer(message, ctx);
        } else {
            ClientMessageHandler.handleClient(message, ctx);
        }
    }

    private static <T extends AbstractPacket> void handleServer(T message,
                                                                IPayloadContext ctx) {
        MinecraftServer server = ctx.player().getServer();
        message.onServerReceived(server, ctx, (ServerPlayer) ctx.player());
    }

    private static class ClientMessageHandler {

        public static <T extends AbstractPacket> void handleClient(T message,
                                                                   IPayloadContext ctx) {
            net.minecraft.client.Minecraft minecraft = net.minecraft.client
                    .Minecraft.getInstance();
            message.onClientReceived(minecraft, ctx, minecraft.player);
        }
    }

    public static void sendToNearbyClient(Level world, BlockPos pos, CustomPacketPayload toSend) {
        if (world instanceof ServerLevel ws) {
            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> Networking.sendToPlayerClient(toSend, p));
        }
    }

    public static void sendToNearbyClient(Level world, Entity e, CustomPacketPayload toSend) {
        sendToNearbyClient(world, e.blockPosition(), toSend);
    }

    public static void sendToPlayerClient(CustomPacketPayload msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, msg);
    }

    public static void sendToPlayersInChunk(CustomPacketPayload msg,
                                            ChunkPos chunkPos, ServerPlayer player) {
        PacketDistributor.sendToPlayersTrackingChunk(player.serverLevel(),
                chunkPos, msg);
    }

    public static void sendToServer(CustomPacketPayload msg) {
        PacketDistributor.sendToServer(msg);
    }

}
