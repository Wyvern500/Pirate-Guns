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

public class SetScopeMessage extends AbstractPacket {

    public static final CustomPacketPayload.Type<SetScopeMessage> TYPE =
            new CustomPacketPayload.Type<>(PirateGuns.prefix("set_scope"));
    public static final StreamCodec<RegistryFriendlyByteBuf,
            SetScopeMessage> CODEC = StreamCodec.ofMember(SetScopeMessage::toBytes,
            SetScopeMessage::new);

    public boolean hasScope;

    public SetScopeMessage(boolean hasScope) {
        this.hasScope = hasScope;
    }

    @Override
    void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(hasScope);
    }

    public SetScopeMessage(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
        ctx.enqueueWork(() -> {
            ItemStack stack = player.getMainHandItem();
            NBTUtils.setScope(stack, hasScope);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}