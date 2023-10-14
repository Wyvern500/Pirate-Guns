package com.jg.jgpg.network;

import java.util.function.Supplier;

import com.jg.jgpg.registries.SoundRegistries;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

public class AddCooldownMessage {

	String item;
	int cooldown;
	
	public AddCooldownMessage(String item, int cooldown) {
		this.item = item;
		this.cooldown = cooldown;
	}
	
	public static void encode(AddCooldownMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.item);
		buf.writeInt(msg.cooldown);
	}

	public static AddCooldownMessage decode(FriendlyByteBuf buf) {
		return new AddCooldownMessage(buf.readUtf(32727), buf.readInt());
	}

	public static void handle(AddCooldownMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			player.getCooldowns().addCooldown(ForgeRegistries.ITEMS.getValue(new ResourceLocation(msg.item)), 
					msg.cooldown);
			// Test code
			player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), 
            		SoundRegistries.FLINTLOCK_PISTOL_SHOOT.get(), SoundSource.NEUTRAL, 1.0f, player.getXRot());
			// End of test code
		});
		context.setPacketHandled(true);
	}
	
}
