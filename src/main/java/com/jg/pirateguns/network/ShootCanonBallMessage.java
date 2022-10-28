package com.jg.pirateguns.network;

import java.util.function.Supplier;

import com.jg.pirateguns.entities.Canon;
import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.network.NetworkEvent.Context;

public class ShootCanonBallMessage {
	
	int id;
	public double xDir;
	public double yDir;
	public double x;
	public double y;
	public double z;
	
	
	public ShootCanonBallMessage(int id, double xDir, double yDir, double x, double y, double z) {
		this.id = id;
		this.xDir = xDir;
		this.yDir = yDir;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void encode(ShootCanonBallMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.id);
		buf.writeDouble(msg.xDir);
		buf.writeDouble(msg.yDir);
		buf.writeDouble(msg.x);
		buf.writeDouble(msg.y);
		buf.writeDouble(msg.z);
	}

	public static ShootCanonBallMessage decode(FriendlyByteBuf buf) {
		return new ShootCanonBallMessage(buf.readInt(), buf.readDouble(), buf.readDouble()
				, buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public static void handle(ShootCanonBallMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			
			LogUtils.getLogger().info("XRot: " + msg.xDir + " yRot: " + msg.yDir);
			//ball.setPos(player.position());
			Entity entity = player.level.getEntity(msg.id);
			if(entity != null) {
				if(entity instanceof Canon) {
					Canon canon = (Canon)entity;
					canon.setXRot((float)msg.xDir);
					canon.setYRot((float) msg.yDir);
					Snowball ball = new Snowball(canon.level, canon);
					ball.shootFromRotation(canon, (float)msg.xDir, (float)msg.yDir, 0.0F, 2.5F, 10.0F);
					LogUtils.getLogger().info("Non null xDir: " + (float)msg.xDir + " yDir: " + (float)msg.yDir);
					LogUtils.getLogger().info("Player xDir: " + player.getXRot() + " yDir: " + player.getYRot());
					canon.level.addFreshEntity(ball);
					/*Snowball ball = new Snowball(player.level, player);
					ball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 10.0F);
					LogUtils.getLogger().info("Non null xDir: " + (float)msg.xDir + " yDir: " + (float)msg.yDir);
					player.level.addFreshEntity(ball);*/
				}
			}
			
		});
		context.setPacketHandled(true);
	}
}
