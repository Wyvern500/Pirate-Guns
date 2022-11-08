package com.jg.pirateguns.network;

import java.util.function.Supplier;

import com.jg.pirateguns.entities.GunBullet;
import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.utils.NBTUtils;
import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;

public class ShootMessage {
	
	private final String sound;
	float yaw;
	float pitch;
	
	public ShootMessage(float yaw, float pitch, String sound) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.sound = sound;
	}
	
	public static void encode(ShootMessage msg, FriendlyByteBuf buf) {
		buf.writeFloat(msg.yaw);
		buf.writeFloat(msg.pitch);
		buf.writeUtf(msg.sound);
	}
	
	public static ShootMessage decode(FriendlyByteBuf buf) {
		return new ShootMessage(buf.readFloat(), buf.readFloat(), buf.readUtf(32767));
	}
	
	public static void handle(ShootMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			
			if(player != null) {
				player.setXRot(msg.pitch);
				player.setYRot(msg.yaw);
				SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(msg.sound));
				if (sound != null) {
					System.out.println(sound.getRegistryName().toString());
					player.level.playSound((Player) null, player.getX(), 
							player.getY(), player.getZ(), sound, SoundSource.NEUTRAL, 
							1.0f, 1.0f);
				}
				GunItem gun = (GunItem)player.getMainHandItem().getItem();
				GunBullet bullet = new GunBullet(player, player.level);
				bullet.setPos(player.position().add(0, player.getEyeHeight(), 0));
				bullet.setDamage(gun.getDamage()).setRange(gun.getRange())
				.setGravity(0.9f)
				.setDamageReduction(gun.getRangeDamageReduction());
				bullet.shootFromRotation(player, msg.pitch, msg.yaw, 0, gun.getPower(), 
						gun.getInnacuracy());
				player.level.addFreshEntity(bullet);
				NBTUtils.setLoaded(player.getMainHandItem(), false);
				LogUtils.getLogger().info("Shooting Server");
			}
		});
		context.setPacketHandled(true);
	}
}
