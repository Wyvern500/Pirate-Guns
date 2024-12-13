package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.utils.NBTUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SetLoadedMessage extends AbstractPacket {

    public static final CustomPacketPayload.Type<SetLoadedMessage> TYPE =
            new CustomPacketPayload.Type<>(PirateGuns.prefix("set_loaded"));
    public static final StreamCodec<RegistryFriendlyByteBuf,
            SetLoadedMessage> CODEC = StreamCodec.ofMember(SetLoadedMessage::toBytes,
            SetLoadedMessage::new);

    public boolean isLoaded;

    public SetLoadedMessage(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    @Override
    void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(isLoaded);
    }

    public SetLoadedMessage(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer,
                                 IPayloadContext ctx, ServerPlayer player) {
        ctx.enqueueWork(() -> {
            ItemStack stack = player.getMainHandItem();
            NBTUtils.setLoaded(stack, isLoaded);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}