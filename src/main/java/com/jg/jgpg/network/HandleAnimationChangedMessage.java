package com.jg.jgpg.network;

import com.jg.jgpg.client.handler.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class HandleAnimationChangedMessage {

    public UUID playerUUID;
    public int animationIndex;

    public HandleAnimationChangedMessage(UUID uuid, int animationIndex) {
        this.playerUUID = uuid;
        this.animationIndex = animationIndex;
    }

    public static void encode(HandleAnimationChangedMessage msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerUUID);
        buf.writeInt(msg.animationIndex);
    }

    public static HandleAnimationChangedMessage decode(FriendlyByteBuf buf) {
        return new HandleAnimationChangedMessage(buf.readUUID(), buf.readInt());
    }

    public static void handle(HandleAnimationChangedMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler
                    .handleAnimationChanged(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
    
}
