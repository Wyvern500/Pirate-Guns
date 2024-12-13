package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.utils.NBTUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SetBulletsMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<SetBulletsMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("set_bullets"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			SetBulletsMessage> CODEC = StreamCodec.ofMember(SetBulletsMessage::toBytes,
			SetBulletsMessage::new);

	public int bullets;
	
	public SetBulletsMessage(int bullets) {
		this.bullets = bullets;
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeInt(bullets);
	}

	public SetBulletsMessage(FriendlyByteBuf buf) {
		this(buf.readInt());
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			ItemStack stack = player.getMainHandItem();
			NBTUtils.setBullets(stack, bullets);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
