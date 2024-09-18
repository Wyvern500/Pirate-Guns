package com.jg.jgpg.network;

import com.jg.jgpg.client.handler.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class ShowHitmarkerMessage {

	public ShowHitmarkerMessage() {
		
	}

	public static void encode(ShowHitmarkerMessage msg, FriendlyByteBuf buf) {
		
	}

	public static ShowHitmarkerMessage decode(FriendlyByteBuf buf) {
		return new ShowHitmarkerMessage();
	}

	public static void handle(ShowHitmarkerMessage msg, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() ->
		    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler::handleHitmarker)
		);
	    ctx.get().setPacketHandled(true);
	}
	
}
