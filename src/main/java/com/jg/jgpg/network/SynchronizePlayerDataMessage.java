package com.jg.jgpg.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.ClientPacketHandler;
import com.jg.jgpg.server.capabilities.JgPlayerDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class SynchronizePlayerDataMessage {

    public UUID playerUUID;
    public String itemId;
    public String pose;

    public SynchronizePlayerDataMessage(UUID uuid, String itemId, String pose) {
        this.playerUUID = uuid;
        this.itemId = itemId;
        this.pose = pose;
    }

    public static void encode(SynchronizePlayerDataMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.itemId, 32727);
        buf.writeUtf(msg.pose, 32727);
    }

    public static SynchronizePlayerDataMessage decode(FriendlyByteBuf buf) {
        return new SynchronizePlayerDataMessage(buf.readUUID(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(SynchronizePlayerDataMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler
                    .handleSynchronizePlayerData(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
    
}
