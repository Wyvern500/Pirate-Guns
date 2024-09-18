package com.jg.jgpg.network;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

public class AddItemMessage {

	public String itemPath;
	public int[] distributionData;
	public CompoundTag nbt;
	
	public AddItemMessage(String itemPath, int[] distributionData) {
		this.itemPath = itemPath;
		this.distributionData = distributionData;
		this.nbt = new CompoundTag();
	}
	
	public AddItemMessage(String itemPath, int[] distributionData, CompoundTag nbt) {
		this.itemPath = itemPath;
		this.distributionData = distributionData;
		this.nbt = nbt;
	}
	
	public static void encode(AddItemMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.itemPath, 32727);
		buf.writeVarIntArray(msg.distributionData);
		buf.writeNbt(msg.nbt);
	}
	
	public static AddItemMessage decode(FriendlyByteBuf buf) {
		return new AddItemMessage(buf.readUtf(), buf.readVarIntArray(), buf.readNbt());
	}
	
	public static void handle(AddItemMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(msg
					.itemPath));
			for(int i = 0; i < msg.distributionData.length - 1; i += 2) {
				player.getInventory().add(new ItemStack(item, msg.distributionData[i + 1]));
			}
			int amount = msg.distributionData[msg.distributionData.length - 1];
			if(amount > 0) {
				player.drop(new ItemStack(item, amount), false);
			}
		});
		context.setPacketHandled(true);
	}
	
}
