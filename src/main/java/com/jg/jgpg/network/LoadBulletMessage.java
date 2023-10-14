package com.jg.jgpg.network;

import java.util.function.Supplier;

import com.jg.jgpg.utils.NBTUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class LoadBulletMessage {

	boolean loaded;
	int[] data;
	
	public LoadBulletMessage(boolean loaded, int[] data) {
		this.loaded = loaded;
		this.data = data;
	}
	
	public static void encode(LoadBulletMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.loaded);
		buf.writeVarIntArray(msg.data);
	}
	
	public static LoadBulletMessage decode(FriendlyByteBuf buf) {
		return new LoadBulletMessage(buf.readBoolean(), buf.readVarIntArray());
	}
	
	public static void handle(LoadBulletMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			
			NBTUtils.setLoaded(player.getMainHandItem(), msg.loaded);
			
			if(msg.loaded) {
				for(int i = 0; i < msg.data.length; i += 2) {
					player.getInventory().removeItem(msg.data[i], msg.data[i+1]);
				}
			}
		});
		context.setPacketHandled(true);
	}
	
}
