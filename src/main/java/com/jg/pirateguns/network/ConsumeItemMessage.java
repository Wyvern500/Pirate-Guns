package com.jg.pirateguns.network;

import java.util.function.Supplier;

import com.jg.pirateguns.utils.NBTUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ConsumeItemMessage {

	int[] indexes;
	int[] amounts;
	
	public ConsumeItemMessage(int index, int amount) {
		this(new int[] { index }, new int[] { amount });
	}
	
	public ConsumeItemMessage(int[] indexes, int[] amounts) {
		this.indexes = indexes;
		this.amounts = amounts;
	}
	
	public static void encode(ConsumeItemMessage msg, FriendlyByteBuf buf) {
		buf.writeVarIntArray(msg.indexes);
		buf.writeVarIntArray(msg.amounts);
	}
	
	public static ConsumeItemMessage decode(FriendlyByteBuf buf) {
		return new ConsumeItemMessage(buf.readVarIntArray(), buf.readVarIntArray());
	}
	
	public static void handle(ConsumeItemMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			for(int i = 0; i < msg.indexes.length; i++) {
				player.getInventory().removeItem(msg.indexes[i], msg.amounts[i]);
			}
		});
		context.setPacketHandled(true);
	}

}
