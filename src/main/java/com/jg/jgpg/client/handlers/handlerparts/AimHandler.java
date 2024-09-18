package com.jg.jgpg.client.handlers.handlerparts;

import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handler.AbstractClientHandlerPart;
import com.jg.jgpg.client.handler.IClientInputHandler;
import com.jg.jgpg.client.handlers.PirateGunsClientHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.IAimable;
import com.jg.jgpg.client.jgmodel.itemmodel.IAimableModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.Key;
import org.lwjgl.glfw.GLFW;

public class AimHandler extends AbstractClientHandlerPart<PirateGunsClientHandler>
		implements IClientInputHandler {

	boolean shouldStartAiming;
	float aimProgress;
	float prevAimProgress;
	float maxAimProgress;
	
	public AimHandler(AbstractClientHandler parent) {
		super(parent);
		maxAimProgress = 6f;
	}

	@Override
	public void onTickPre(LocalPlayer player, ItemStack stack, String stackId) {
		IAimableModel aimable = (IAimableModel) parent.getClient().getModel();

		float multiplier = 0.2f;

		if(shouldStartAiming && !player.isSprinting() && aimable.canAim(player)) {
			if(aimProgress < maxAimProgress) {
				prevAimProgress = aimProgress;
				aimProgress += Minecraft.getInstance().getDeltaFrameTime() * multiplier;
				if(aimProgress > maxAimProgress) {
					aimProgress = maxAimProgress;
				}
				
			}
		} else {
			if(aimProgress > 0) {
				prevAimProgress = aimProgress;
				aimProgress -= Minecraft.getInstance().getDeltaFrameTime() * multiplier;
				if(aimProgress < 0) {
					aimProgress = 0;
				}
			}
		}
	}

	@Override
	public void onTickPost(LocalPlayer player, ItemStack stack, String stackId) {
		
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
	
	@Override
	public void handleKeyboard(Key e) {
		
	}
	
	public float getProgress(Item item) {
		return aimProgress / maxAimProgress;
	}

	@Override
	public boolean shouldWork(ItemStack stack, LocalPlayer player) {
		return stack.getItem() instanceof IAimable;
	}

}
