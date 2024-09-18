package com.jg.jgpg.network;

import java.util.function.Supplier;

import com.jg.jgpg.utils.NBTUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateExtraDataMessage {

	CompoundTag data;

	public UpdateExtraDataMessage(CompoundTag data) {
		this.data = data;
	}

	public static void encode(UpdateExtraDataMessage msg, FriendlyByteBuf buf) {
		buf.writeNbt(msg.data);
	}

	public static UpdateExtraDataMessage decode(FriendlyByteBuf buf) {
		return new UpdateExtraDataMessage(buf.readNbt());
	}

	public static void handle(UpdateExtraDataMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			NBTUtils.setExtraData(player.getMainHandItem(), msg.data);
		});
		context.setPacketHandled(true);
	}
	
}
