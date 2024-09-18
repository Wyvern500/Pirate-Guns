package com.jg.jgpg.network;

import com.jg.jgpg.client.handler.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HandleJgModelUpdateMessage {

    public UUID playerUUID;
    public String itemId;

    public HandleJgModelUpdateMessage(UUID uuid, String itemId) {
        this.playerUUID = uuid;
        this.itemId = itemId;
    }

    public static void encode(HandleJgModelUpdateMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeUtf(msg.itemId, 32727);
    }

    public static HandleJgModelUpdateMessage decode(FriendlyByteBuf buf) {
        return new HandleJgModelUpdateMessage(buf.readUUID(), buf.readUtf());
    }

    public static void handle(HandleJgModelUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler
                    .handlePlayerChangedJgModel(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
    
}
