package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.common.damagetypes.DamageType;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BodyHitMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<BodyHitMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("body_hit"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			BodyHitMessage> CODEC = StreamCodec.ofMember(BodyHitMessage::toBytes,
			BodyHitMessage::new);

	public int id;
	public float damage;

	public BodyHitMessage(int id, float damage) {
		this.id = id;
		this.damage = damage;
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeInt(id);
		buf.writeFloat(damage);
	}

	public BodyHitMessage(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readFloat());
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer,
								 IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			LogUtils.log("BodyHitMessage", "Starting OnServerReceived");
			LivingEntity entity = (LivingEntity) player.level().getEntity(id);
			entity.hurt(DamageType.melee(entity.level()), damage);
			player.level().playSound(null, player.getX(), player.getY(),
					player.getZ(), SoundEvents.PLAYER_HURT, SoundSource.NEUTRAL,
					1.0f, player.getXRot());
			Networking.sendToPlayerClient(new ShowHitmarkerMessage(), player);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
