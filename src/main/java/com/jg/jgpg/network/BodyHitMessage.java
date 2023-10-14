package com.jg.jgpg.network;

import java.util.function.Supplier;

import com.jg.jgpg.registries.SoundRegistries;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

public class BodyHitMessage {
	public int id;
	public float damage;

	public BodyHitMessage(int id, float damage) {
		this.id = id;
		this.damage = damage;
	}

	public static void encode(BodyHitMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.id);
		buf.writeFloat(msg.damage);
	}

	public static BodyHitMessage decode(FriendlyByteBuf buf) {
		return new BodyHitMessage(buf.readInt(), buf.readFloat());
	}

	public static void handle(BodyHitMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			LivingEntity entity = (LivingEntity) player.level().getEntity(msg.id);
			DamageSource src = player.level().damageSources().mobAttack(player);
            entity.hurt(src, msg.damage);
            player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), 
            		SoundRegistries.GUN_MELEE_HIT.get(), SoundSource.NEUTRAL, 1.0f, player.getXRot());
		});
		context.setPacketHandled(true);
	}
}
