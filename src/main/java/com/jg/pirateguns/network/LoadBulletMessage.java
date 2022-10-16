package com.jg.pirateguns.network;

import java.util.function.Supplier;

import com.jg.pirateguns.utils.NBTUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class LoadBulletMessage {

	boolean loaded;
	
	public LoadBulletMessage(boolean loaded) {
		this.loaded = loaded;
	}
	
	public static void encode(LoadBulletMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.loaded);
	}
	
	public static LoadBulletMessage decode(FriendlyByteBuf buf) {
		return new LoadBulletMessage(buf.readBoolean());
	}
	
	public static void handle(LoadBulletMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			
			NBTUtils.setLoaded(player.getMainHandItem(), msg.loaded);
		});
		context.setPacketHandled(true);
	}
	
}
