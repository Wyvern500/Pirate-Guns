package com.jg.jgpg.network;

import com.jg.jgpg.client.handler.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HandlePlayerPoseChangedMessage {

    public UUID playerUUID;
    public String pose;

    public HandlePlayerPoseChangedMessage(UUID uuid, String pose) {
        this.playerUUID = uuid;
        this.pose = pose;
    }

    public static void encode(HandlePlayerPoseChangedMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.pose, 32727);
    }

    public static HandlePlayerPoseChangedMessage decode(FriendlyByteBuf buf) {
        return new HandlePlayerPoseChangedMessage(buf.readUUID(), buf.readUtf());
    }

    public static void handle(HandlePlayerPoseChangedMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler
                    .handlePlayerChangedPose(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
    
}
