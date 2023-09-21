package com.jg.jgpg.client.handlers;

import java.util.Map.Entry;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.events.RegisterEasingsEvent;
import com.jg.jgpg.client.events.RegisterGunModelsEvent;
import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.model.models.PiratePistolModel;
import com.jg.jgpg.client.model.models.PirateRifleModel;
import com.jg.jgpg.client.model.models.TrabucoModel;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.NBTUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientEventHandler {

	private static Minecraft mc = Minecraft.getInstance();
	private static ClientHandler client = new ClientHandler();
	
	private static float trStep = 0.1f;
	private static float rtStep = (float) Math.toRadians(0.1D);
	private static boolean isRotationMode = false;
	private static boolean isDisplayMode = false;
	
	public static void registerModEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::registerShaders);
	}

	public static void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::renderPlayerHand);
		bus.addListener(ClientEventHandler::tickClient);
		bus.addListener(ClientEventHandler::handleMouse);
		bus.addListener(ClientEventHandler::handleKeyboard);
		bus.addListener(ClientEventHandler::registerGunModels);
		bus.addListener(ClientEventHandler::registerEasings);
	}
	
	public static void setup() {
		
	}
	
	private static void registerShaders(RegisterShadersEvent e) {
		MinecraftForge.EVENT_BUS.start();
		MinecraftForge.EVENT_BUS.post(new RegisterGunModelsEvent());
		MinecraftForge.EVENT_BUS.post(new RegisterEasingsEvent());
		LogUtils.log("ClientEventHandler", "Shaders Event");
	}
	
	private static void registerGunModels(RegisterGunModelsEvent e) {
		e.register(ItemRegistries.PIRATEPISTOL.get(), new PiratePistolModel(client));
		e.register(ItemRegistries.PIRATERIFLE.get(), new PirateRifleModel(client));
		e.register(ItemRegistries.TRABUCO.get(), new TrabucoModel(client));
		LogUtils.log("ClientEventHandler", "Registering Gun Models :)");
		for(AbstractJgModel model : GunModelsHandler.getModels().values()) {
			for(Entry<String, JgModelPart> entry : model.getParts().entrySet()) {
				LogUtils.log("ClientEventHandler", "Part: " + entry.getValue().getName() + " dytr: " + 
						entry.getValue().getDtransform().toString() + " dftr: " + entry.getValue()
						.getTransform().toString());
			}
		}
	}
	
	private static void registerEasings(RegisterEasingsEvent e) {
		EasingHandler.registerEasings(e);
	}
	
	// Rendering
	
	private static void renderPlayerHand(RenderHandEvent e) {
		trStep = 0.01f;
		rtStep = (float) Math.toRadians(0.01f);
		LocalPlayer player = mc.player;
		if (player != null) {
			ClientHandler.partialTicks = e.getPartialTick();
			if(player.getMainHandItem().getItem() instanceof JgGunItem) {
				e.setCanceled(true);
				if(e.getHand() == InteractionHand.MAIN_HAND) {
					client.tick(player, player.getMainHandItem(), NBTUtils.getId(player.getMainHandItem()));
					client.render(player, player.getMainHandItem(), e.getPoseStack(), e.getMultiBufferSource(), 
							e.getPackedLight());
				}
			}
		}
	}
	
	// Misc
	
	private static void tickClient(ClientTickEvent e) {
		LocalPlayer player = Minecraft.getInstance().player;
		if(player != null) {
			ItemStack stack = player.getMainHandItem();
			
			// When initializing the stack id, I use it to differentiate each stack and for other purposes
			String id = NBTUtils.getId(stack);
			if(id.equals("") || id.isEmpty()) {
				// Giving a random uuid
				String newId = UUID.randomUUID().toString();
				NBTUtils.setId(stack, newId);
				id = newId;
			}
			
			if(stack.getItem() instanceof JgGunItem) {
				//client.tick(player, stack, id);
			}
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
						
			// Model Manipulation
			// Left 263 Up 265 Down 264 Right 262 N 78 M 77 C 67 Intro (numpad) 335 J 74 K 75
			switch(e.getKey()) {
			case 261: // Supr
				// Open Animation gui
				LocalPlayer player = Minecraft.getInstance().player;
				if(player != null) {
					ItemStack stack = player.getMainHandItem();
					if(stack.getItem() instanceof JgGunItem) {
						AbstractJgModel gunModel = GunModelsHandler.get(ForgeRegistries.ITEMS.getKey(stack
								.getItem()).toString());
						if(gunModel != null) {
							AnimationGui gui = new AnimationGui(client);
							client.setModel(gunModel);
							gui.setModel(gunModel);
							Minecraft.getInstance().setScreen(gui);
						}
					}
				}
				break;
			case 263: // Left arrow
				client.addToModel(-trStep, 0, 0, isRotationMode, isDisplayMode);
				break;
			case 262: // Right arrow
				client.addToModel(trStep, 0, 0, isRotationMode, isDisplayMode);
				break;
			case 265: // Up arrow
				client.addToModel(0, trStep, 0, isRotationMode, isDisplayMode);
				break;
			case 264: // Down arrow
				client.addToModel(0, -trStep, 0, isRotationMode, isDisplayMode);
				break;
			case 78: // N
				client.addToModel(0, 0, -trStep, isRotationMode, isDisplayMode);
				break;
			case 77: // M
				client.addToModel(0, 0, trStep, isRotationMode, isDisplayMode);
				break;
			case 67: // C
				isRotationMode = !isRotationMode;
				LogUtils.log("handleKeyboard", "isRotationMode: " + isRotationMode);
				break;
			case 335: // Intro (numpad)
				isDisplayMode = !isDisplayMode;
				LogUtils.log("handleKeyboard", "displayMode: " + isDisplayMode);
				break;
			case 72: // J
				client.prevPartIndex();
				break;
			case 74: // K
				client.nextPartIndex();
				break;
			case 80: // P
				client.getModel().getAnimator().play();
				break;
			case 93: // +
				MinecraftForge.EVENT_BUS.start();
				MinecraftForge.EVENT_BUS.post(new RegisterGunModelsEvent());
				MinecraftForge.EVENT_BUS.post(new RegisterEasingsEvent());
				LogUtils.log("ClientEventHandler", "Updating models");
				break;
			}
		}
	}
	
}
