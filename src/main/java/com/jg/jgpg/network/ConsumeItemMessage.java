package com.jg.jgpg.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ConsumeItemMessage {

	int[] consumeData;
	
	public ConsumeItemMessage(int[] consumeData) {
		this.consumeData = consumeData;
	}
	
	public static void encode(ConsumeItemMessage msg, FriendlyByteBuf buf) {
		buf.writeVarIntArray(msg.consumeData);
	}
	
	public static ConsumeItemMessage decode(FriendlyByteBuf buf) {
		return new ConsumeItemMessage(buf.readVarIntArray());
	}
	
	public static void handle(ConsumeItemMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			for(int i = 0; i < msg.consumeData.length; i += 2) {
				player.getInventory().removeItem(msg.consumeData[i], msg.consumeData[i + 1]);
			}
		});
		context.setPacketHandled(true);
	}
	
}
