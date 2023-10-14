package com.jg.jgpg.utils;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.network.BodyHitMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MeleeHelper {

	public static void hit(ClientHandler client, double damage) {
		Player player = Minecraft.getInstance().player;
		Entity entity = rayTraceEntities(player.level(), player,
				new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ()), Entity.class);
		if (entity instanceof LivingEntity) {
			client.getHitmarker().reset();
			PirateGuns.channel.sendToServer(new BodyHitMessage(entity.getId(), (float) damage));
		}
	}
	
	public static <T extends Entity> T rayTraceEntities(Level w, Player player, Vec3 pos, Class<T> entityClazz) {
		Vec3 dir = player.getLookAngle().scale(2);
		Vec3 end = pos.add(dir);
		AABB aabb = new AABB(pos.x, pos.y, pos.z, end.x, end.y, end.z).expandTowards(dir.x, dir.y, dir.z);
		Vec3 checkVec = pos.add(dir);
		for (T t : w.getEntitiesOfClass(entityClazz, aabb)) {
			AABB entityBB = t.getBoundingBox();
			if (entityBB == null) {
				continue;
			}

			if (entityBB.intersects(Math.min(pos.x, checkVec.x), Math.min(pos.y, checkVec.y),
					Math.min(pos.z, checkVec.z), Math.max(pos.x, checkVec.x), Math.max(pos.y, checkVec.y),
					Math.max(pos.z, checkVec.z))) {
				if (t != player) {
					return t;
				}
			}
		}

		return null;
	}
	
}
