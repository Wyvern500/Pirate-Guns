package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.registries.SoundRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class CraftItemMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<CraftItemMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("craft_item"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			CraftItemMessage> CODEC = StreamCodec.ofMember(CraftItemMessage::toBytes,
			CraftItemMessage::new);

	String path;
	int[] consumeData;
	int[] addData;
	
	public CraftItemMessage(String path, int[] consumeData, int[] addData) {
		this.path = path;
		this.consumeData = consumeData;
		this.addData = addData;
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeUtf(path, 32727);
		buf.writeVarIntArray(consumeData);
		buf.writeVarIntArray(addData);
	}

	public CraftItemMessage(FriendlyByteBuf buf) {
		this(buf.readUtf(), buf.readVarIntArray(), buf.readVarIntArray());
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			player.level().playSound((Player) null, player.getX(), player.getY(),
					player.getZ(), SoundRegistries.CRAFT_SOUND.get(), SoundSource.NEUTRAL,
					0.4f, 1.2F / (player.getRandom().nextFloat() * 0.2F
							+ 0.9F));

			for(int i = 0; i < consumeData.length; i += 2) {
				player.getInventory().removeItem(consumeData[i], consumeData[i+1]);
			}
			for(int i = 0; i < addData.length - 1; i += 2) {
				player.getInventory().add(new ItemStack(BuiltInRegistries.ITEM
						.get(ResourceLocation.parse(path)), addData[i + 1]));
			}
			int amount = addData[addData.length - 1];
			if(amount > 0) {
				player.drop(new ItemStack(BuiltInRegistries.ITEM
						.get(ResourceLocation.parse(path)), amount), false);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
