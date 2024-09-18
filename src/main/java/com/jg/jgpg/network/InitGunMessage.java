package com.jg.jgpg.network;

import java.util.function.Supplier;

import com.jg.jgpg.utils.NBTUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class InitGunMessage {
	
	public String id;
	
	public InitGunMessage(String id) {
		this.id = id;
	}

	public static void encode(InitGunMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.id, 32727);
	}

	public static InitGunMessage decode(FriendlyByteBuf buf) {
		return new InitGunMessage(buf.readUtf());
	}

	public static void handle(InitGunMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			ItemStack stack = player.getMainHandItem();
			NBTUtils.setId(stack, msg.id);
			NBTUtils.setExtraData(stack, new CompoundTag());
		});
		context.setPacketHandled(true);
	}
	
}
