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

public class InitGunMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<InitGunMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("init_gun"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			InitGunMessage> CODEC = StreamCodec.ofMember(InitGunMessage::toBytes,
			InitGunMessage::new);

	public String id;
	
	public InitGunMessage(String id) {
		this.id = id;
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeUtf(id, 32727);
	}

	public InitGunMessage(FriendlyByteBuf buf) {
		this(buf.readUtf());
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			ItemStack stack = player.getMainHandItem();
			NBTUtils.setId(stack, id);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
