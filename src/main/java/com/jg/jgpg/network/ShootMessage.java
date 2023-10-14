package com.jg.jgpg.network;

import java.util.function.Supplier;

import com.jg.jgpg.entities.GunBullet;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.utils.NBTUtils;
import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

public class ShootMessage {
	
	private final String sound;
	private final String item;
	double f;
	double f1;
	double f2;
	
	public ShootMessage(String sound, String item, double f, double f1, double f2) {
		this.sound = sound;
		this.item = item;
		this.f = f;
		this.f1 = f1;
		this.f2 = f2;
	}
	
	public static void encode(ShootMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.sound);
		buf.writeUtf(msg.item);
		buf.writeDouble(msg.f);
		buf.writeDouble(msg.f1);
		buf.writeDouble(msg.f2);
	}
	
	public static ShootMessage decode(FriendlyByteBuf buf) {
		return new ShootMessage(buf.readUtf(32767), buf.readUtf(32767), buf.readDouble(), 
				buf.readDouble(), buf.readDouble());
	}
	
	public static void handle(ShootMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();

			if (player != null) {
				/*
				player.setXRot(msg.pitch);
				player.setYRot(msg.yaw);
				*/
				SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(msg.sound));
				if (sound != null) {
					player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), sound,
							SoundSource.NEUTRAL, 1.0f, 1.0f);
				}
				JgGunItem gun = (JgGunItem) player.getMainHandItem().getItem();
				for (int i = 0; i < gun.getBulletsPerShoot(); i++) {
					GunBullet bullet = new GunBullet(player, player.level());
					bullet.setPos(player.position().add(0, player.getEyeHeight(), 0));
					bullet.setDamage(gun.getDamage()).setRange(gun.getRange()).setGravity(0.9f)
							.setDamageReduction(gun.getRangeDamageReduction());
					bullet.shoot(msg.f, msg.f1, msg.f2, gun.getPower(), gun.getInaccuracy());
					Vec3 vec3 = player.getDeltaMovement();
					bullet.setDeltaMovement(
							bullet.getDeltaMovement().add(vec3.x, player.onGround() ? 0.0D : vec3.y, vec3.z));
					player.level().addFreshEntity(bullet);
				}
				player.getCooldowns().addCooldown(ForgeRegistries.ITEMS.getValue(new ResourceLocation(msg.item)), 
						gun.getShootCooldown());
				
				NBTUtils.setLoaded(player.getMainHandItem(), false);
				
				LogUtils.getLogger().info("Shooting Server");
			}
		});
		context.setPacketHandled(true);
	}
}
