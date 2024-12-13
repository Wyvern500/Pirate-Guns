package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AddItemMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<AddItemMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("add_item"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			AddItemMessage> CODEC = StreamCodec.ofMember(AddItemMessage::toBytes,
			AddItemMessage::new);

	public String itemPath;
	public int[] distributionData;
	public CompoundTag nbt;

	public AddItemMessage(String itemPath, int[] distributionData) {
		this(itemPath, distributionData, new CompoundTag());
	}
	
	public AddItemMessage(String itemPath, int[] distributionData, CompoundTag nbt) {
		this.itemPath = itemPath;
		this.distributionData = distributionData;
		this.nbt = nbt;
	}

	public AddItemMessage(RegistryFriendlyByteBuf buf) {
		this(buf.readUtf(), buf.readVarIntArray(), buf.readNbt());
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeUtf(itemPath, 32727);
		buf.writeVarIntArray(distributionData);
		buf.writeNbt(nbt);
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer,
								 IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemPath));
			for(int i = 0; i < distributionData.length - 1; i += 2) {
				player.getInventory().add(new ItemStack(item,
						distributionData[i + 1]));
			}
			int amount = distributionData[distributionData.length - 1];
			if(amount > 0) {
				player.drop(new ItemStack(item, amount), false);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
