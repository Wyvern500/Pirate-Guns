package com.jg.pirateguns.network;

import java.util.function.Supplier;

import com.jg.pirateguns.entities.GunBullet;
import com.jg.pirateguns.utils.NBTUtils;
import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ShootMessage {
	
	float yaw;
	float pitch;
	
	public ShootMessage(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public static void encode(ShootMessage msg, FriendlyByteBuf buf) {
		buf.writeFloat(msg.yaw);
		buf.writeFloat(msg.pitch);
	}
	
	public static ShootMessage decode(FriendlyByteBuf buf) {
		return new ShootMessage(buf.readFloat(), buf.readFloat());
	}
	
	public static void handle(ShootMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			
			if(player != null) {
				player.setXRot(msg.pitch);
				player.setYRot(msg.yaw);
				GunBullet bullet = new GunBullet(player, player.level);
				bullet.setPos(player.position().add(0, player.getEyeHeight(), 0));
				bullet.setDamage(8.0f).setRange(20).setGravity(0.9f).setDamageReduction(0.9f);
				bullet.shootFromRotation(player, msg.pitch, msg.yaw, 0, 5f, 0.1f);
				player.level.addFreshEntity(bullet);
				LogUtils.getLogger().info("Shooting Server");
			}
		});
		context.setPacketHandled(true);
	}
}
