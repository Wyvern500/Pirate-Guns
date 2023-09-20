package com.jg.jgpg.client.handlers;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.models.PiratePistolModel;
import com.jg.jgpg.utils.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientEventHandler {

	private static Minecraft mc = Minecraft.getInstance();
	private static ClientHandler client = new ClientHandler();
	
	private static float trStep = 0.1f;
	private static float rtStep = 0.1f;
	private static boolean isRotationMode = false;
	private static boolean isDisplayMode = false;
	
	public static void registerModEventListeners(IEventBus bus) {
		
	}

	public static void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::renderPlayerHand);
		bus.addListener(ClientEventHandler::tickClient);
		bus.addListener(ClientEventHandler::handleMouse);
		bus.addListener(ClientEventHandler::handleKeyboard);
	}
	
	public static void setup() {
		client.setModel(new PiratePistolModel(client));
	}
	
	// Rendering
	
	private static void renderPlayerHand(RenderHandEvent e) {
		rtStep = (float) Math.toRadians(1);
		LocalPlayer player = mc.player;
		if (player != null) {
			ClientHandler.partialTicks = e.getPartialTick();
			if(player.getMainHandItem().getItem() == Items.IRON_SWORD) {
				e.setCanceled(true);
				if(e.getHand() == InteractionHand.MAIN_HAND) {
					client.render(player, player.getMainHandItem(), e.getPoseStack(), e.getMultiBufferSource(), 
							e.getPackedLight());
				}
			}
		}
	}
	
	// Misc
	
	private static void tickClient(ClientTickEvent e) {
		Player player = Minecraft.getInstance().player;
		if(player != null) {
			client.tick();
		}
	}
	
	// Mouse
	
	private static void handleMouse(InputEvent.MouseButton e) {
		
	}
	
	// Keyboard
	
	private static void handleKeyboard(InputEvent.Key e) {
		if(e.getAction() == GLFW.GLFW_PRESS) {
			
			// LogUtils.log("ClientEventHandler:handleKeyboard", "Key: " + e.getKey());
			
			// Debug Stuff
			// Open Animation gui
			if(e.getKey() == GLFW.GLFW_KEY_DELETE) {
				AnimationGui gui = new AnimationGui(client);
				gui.setModel(client.getModel());
				Minecraft.getInstance().setScreen(gui);
			}
			// Model Manipulation
			// Left 263 Up 265 Down 264 Right 262 N 78 M 77 C 67 Intro (numpad) 335 J 74 K 75
			switch(e.getKey()) {
			// Left arrow
			case 263:
				client.addToModel(-trStep, 0, 0, isRotationMode, isDisplayMode);
				break;
			case 262:
				client.addToModel(trStep, 0, 0, isRotationMode, isDisplayMode);
				break;
			case 265:
				client.addToModel(0, trStep, 0, isRotationMode, isDisplayMode);
				break;
			case 264:
				client.addToModel(0, -trStep, 0, isRotationMode, isDisplayMode);
				break;
			case 78:
				client.addToModel(0, 0, -trStep, isRotationMode, isDisplayMode);
				break;
			case 77:
				client.addToModel(0, 0, trStep, isRotationMode, isDisplayMode);
				break;
			case 67:
				isRotationMode = !isRotationMode;
				LogUtils.log("handleKeyboard", "isRotationMode: " + isRotationMode);
				break;
			case 335:
				isDisplayMode = !isDisplayMode;
				LogUtils.log("handleKeyboard", "displayMode: " + isDisplayMode);
				break;
			case 74:
				client.prevPartIndex();
				break;
			case 75:
				client.nextPartIndex();
				break;
			case 80: // P
				client.getModel().getAnimator().play();
				break;
			}
		}
	}
	
}
