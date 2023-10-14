package com.jg.jgpg.client.handlers;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.AbstractHandler;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.network.AddCooldownMessage;
import com.jg.jgpg.network.ShootMessage;
import com.jg.jgpg.utils.NBTUtils;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ShootHandler extends AbstractHandler {

	public ShootHandler(ClientHandler client) {
		super(client);
	}
	
	public void shoot(LocalPlayer player, JgGunItem gun) {
		PirateGuns.channel.sendToServer(new AddCooldownMessage(ForgeRegistries.ITEMS.getKey(gun).toString(), 
				gun.getShootCooldown()));
		// This is needed for shooting :)
		float f = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F)) * Mth.cos(player.getXRot() 
				* ((float) Math.PI / 180F));
		float f1 = -Mth.sin((player.getXRot()) * ((float) Math.PI / 180F));
		float f2 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F)) * Mth.cos(player.getXRot() 
				* ((float) Math.PI / 180F));
		// Sending the message to the server to shoot
		PirateGuns.channel.sendToServer(new ShootMessage(Utils.sound(gun.getSound()), Utils.item(gun), f, f1, f2));
		// The vertical recoil has to be negative for the shot to be upward
		float newXRot = player.getXRot() + (gun.getRecoilWeight() * 
				-gun.getVerticalRecoilMultiplier());
		float newYRot = player.getYRot() + (gun.getRecoilWeight() * 
				new Random().nextFloat(-gun.getHorizontalRecoilMultiplier(), 
						gun.getHorizontalRecoilMultiplier()));
		player.setXRot(newXRot);
		player.setYRot(newYRot);
	}
	
	public void handleMouse(InputEvent.MouseButton e) {
		LocalPlayer player = Minecraft.getInstance().player;
		if(player != null && Minecraft.getInstance().screen == null) {
			if(e.getAction() == GLFW.GLFW_PRESS) {
				if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					Item item = player.getMainHandItem().getItem();
					if(item instanceof JgGunItem ) {
						if(!player.getCooldowns().isOnCooldown(item) && NBTUtils
								.isLoaded(player.getMainHandItem())) {
							JgGunItem gun = ((JgGunItem) item);
							shoot(player, gun);
						}
					}
				}
			}
		}
	}
	
	public void tick(LocalPlayer player) {
		
	}
	
	public float getProgress(Item item) {
		return Minecraft.getInstance().player.getCooldowns().getCooldownPercent(item, Minecraft.getInstance()
				.getPartialTick());
	}

}
