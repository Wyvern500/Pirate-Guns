package com.jg.jgpg.network;

import java.util.function.Supplier;

import com.jg.jgpg.utils.NBTUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class SetScopeMessage {

	boolean hasScope;
	
	public SetScopeMessage(boolean hasScope) {
		this.hasScope = hasScope;
	}
	
	public static void encode(SetScopeMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.hasScope);
	}
	
	public static SetScopeMessage decode(FriendlyByteBuf buf) {
		return new SetScopeMessage(buf.readBoolean());
	}
	
	public static void handle(SetScopeMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			
			NBTUtils.setHasScope(player.getMainHandItem(), msg.hasScope);
		});
		context.setPacketHandled(true);
	}
	
}
