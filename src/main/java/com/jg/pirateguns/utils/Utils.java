package com.jg.pirateguns.utils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class Utils {
	
	public static void spawnParticlesOnPlayerView(Player player, int amount, float ox, float oy, float oz) {
		Vec3 view = player.getViewVector(1.0f);
		if(amount <= 0) {
			amount = 50;
		}
		for(int i = 0; i < amount; i++) {
			player.level.addParticle(ParticleTypes.SMOKE, (player.getX()+view.x)+ox, 
					(player.getEyeY())+oy, 
					(player.getZ()+view.z)+oz, view.x*0.1f, 0, 0);
		}
	}
	
}
