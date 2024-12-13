package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ConsumeItemMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<ConsumeItemMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("consume_item"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			ConsumeItemMessage> CODEC = StreamCodec
			.ofMember(ConsumeItemMessage::toBytes, ConsumeItemMessage::new);

	int[] consumeData;
	
	public ConsumeItemMessage(int[] consumeData) {
		this.consumeData = consumeData;
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeVarIntArray(consumeData);
	}

	public ConsumeItemMessage(FriendlyByteBuf buf) {
		this(buf.readVarIntArray());
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			for(int i = 0; i < consumeData.length; i += 2) {
				player.getInventory().removeItem(consumeData[i], consumeData[i + 1]);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
