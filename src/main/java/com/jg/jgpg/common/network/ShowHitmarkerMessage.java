package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShowHitmarkerMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<ShowHitmarkerMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("show_hitmarker"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			ShowHitmarkerMessage> CODEC = StreamCodec.ofMember(ShowHitmarkerMessage::toBytes,
			ShowHitmarkerMessage::new);

	public ShowHitmarkerMessage() {
		
	}

	public ShowHitmarkerMessage(FriendlyByteBuf buf) {

	}

	@Override
	public void onClientReceived(Minecraft minecraft, IPayloadContext ctx, Player player) {
		ctx.enqueueWork(() -> {
			ClientPacketHandler.handleHitmarker(ctx, player, this);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
