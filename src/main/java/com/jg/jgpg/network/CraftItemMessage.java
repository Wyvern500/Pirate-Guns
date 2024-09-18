package com.jg.jgpg.network;

import com.jg.jgpg.registries.SoundRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CraftItemMessage {

	int[] consumeData;
	int[] addData;
	String path;
	
	public CraftItemMessage(String path, int[] consumeData, int[] addData) {
		this.path = path;
		this.consumeData = consumeData;
		this.addData = addData;
	}
	
	public static void encode(CraftItemMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.path, 32727);
		buf.writeVarIntArray(msg.consumeData);
		buf.writeVarIntArray(msg.addData);
	}
	
	public static CraftItemMessage decode(FriendlyByteBuf buf) {
		return new CraftItemMessage(buf.readUtf(), buf.readVarIntArray(), buf.readVarIntArray());
	}
	
	public static void handle(CraftItemMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();

			player.level().playSound((Player) null, player.getX(), player.getY(),
					player.getZ(), SoundRegistries.CRAFT_SOUND.get(), SoundSource.NEUTRAL,
					0.4f, 1.2F / (player.getRandom().nextFloat() * 0.2F
							+ 0.9F));

			for(int i = 0; i < msg.consumeData.length; i += 2) {
				player.getInventory().removeItem(msg.consumeData[i], msg.consumeData[i+1]);
			}
			for(int i = 0; i < msg.addData.length - 1; i += 2) {
				player.getInventory().add(new ItemStack(ForgeRegistries.ITEMS
						.getValue(new ResourceLocation(msg.path)), msg.addData[i + 1]));
			}
			int amount = msg.addData[msg.addData.length - 1];
			if(amount > 0) {
				player.drop(new ItemStack(ForgeRegistries.ITEMS
						.getValue(new ResourceLocation(msg.path)), amount), false);
			}
		});
		context.setPacketHandled(true);
	}
	
}
