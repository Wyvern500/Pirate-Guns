package com.jg.jgpg.client.handlers;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.handler.AbstractHandler;
import com.jg.jgpg.client.handler.ClientHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.InputEvent;

public class AimHandler extends AbstractHandler {

	boolean shouldStartAiming;
	boolean shouldAim;
	float aimProgress;
	float prevAimProgress;
	float maxAimProgress;
	
	public AimHandler(ClientHandler client) {
		super(client);
		maxAimProgress = 6f;
	}
	
	public void tick(LocalPlayer player) {
		if(shouldStartAiming && !player.isSprinting()) {
			if(aimProgress < maxAimProgress) {
				prevAimProgress = aimProgress;
				aimProgress += Minecraft.getInstance().getDeltaFrameTime() * 2f;
				if(aimProgress > maxAimProgress) {
					aimProgress = maxAimProgress;
				}
				
			}
		} else {
			if(aimProgress > 0) {
				prevAimProgress = aimProgress;
				aimProgress -= Minecraft.getInstance().getDeltaFrameTime() * 2f;
				if(aimProgress < 0) {
					aimProgress = 0;
				}
			}
		}
	}
	
	public void handleMouse(InputEvent.MouseButton e) {
		if(Minecraft.getInstance().screen == null) {
			if(e.getAction() == GLFW.GLFW_PRESS && e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				shouldStartAiming = true;
			} else if(e.getAction() == GLFW.GLFW_RELEASE && e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				shouldStartAiming = false;
			}
		} else {
			shouldStartAiming = false;
		}
	}
	
	public float getProgress(Item item) {
		float partialTick = Minecraft.getInstance().getPartialTick();
		//float prog = ((prevAimProgress + (aimProgress - prevAimProgress) * partialTick) / maxAimProgress);
		float prog = aimProgress / maxAimProgress;
		//LogUtils.log("AimHandler", "Prog: " + prog);
		return prog;
	}
	
}
