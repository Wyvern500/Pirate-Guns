package com.jg.pirateguns.client.handlers;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.network.PlaySoundMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class SoundHandler {

	public SoundHandler() {
		
	}

	public static void playSoundOnServer(SoundEvent e) {
		Player player = Minecraft.getInstance().player;
		PirateGuns.channel.sendToServer(new PlaySoundMessage(e, player.getX(), player.getY(),
				player.getZ(), 0.4f, 1.2F / (player.getRandom().nextFloat() * 0.2F + 0.9F)));
	}
	
}
