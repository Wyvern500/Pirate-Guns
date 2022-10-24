package com.jg.pirateguns.client.handlers;

import org.lwjgl.glfw.GLFW;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.animations.gunmodels.PiratePistolGunModel;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.client.events.RegisterGunModelsEvent;
import com.jg.pirateguns.client.models.entities.CanonModel;
import com.jg.pirateguns.client.rendering.entities.CanonRenderer;
import com.jg.pirateguns.client.screens.AnimationScreen;
import com.jg.pirateguns.client.screens.GunPartsScreen;
import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.network.LoadBulletMessage;
import com.jg.pirateguns.registries.EntityRegistries;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.utils.Paths;
import com.mojang.logging.LogUtils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientEventHandler {
	
	private static Minecraft mc = Minecraft.getInstance();
	private static ClientHandler client = new ClientHandler();
	
	public static final KeyMapping SWITCH = new KeyMapping("key.jgpg.switch", 
			GLFW.GLFW_KEY_C, PirateGuns.MODID);
	public static final KeyMapping RELOAD = new KeyMapping("key.jgpg.reload", 
			GLFW.GLFW_KEY_R, PirateGuns.MODID);
	public static final KeyMapping LEFT = new KeyMapping("key.jgpg.left", 
			GLFW.GLFW_KEY_LEFT, PirateGuns.MODID);
	public static final KeyMapping UP = new KeyMapping("key.jgpg.up", 
			GLFW.GLFW_KEY_UP, PirateGuns.MODID);
	public static final KeyMapping DOWN = new KeyMapping("key.jgpg.down", 
			GLFW.GLFW_KEY_DOWN, PirateGuns.MODID);
	public static final KeyMapping RIGHT = new KeyMapping("key.jgpg.right", 
			GLFW.GLFW_KEY_RIGHT, PirateGuns.MODID);
	public static final KeyMapping N = new KeyMapping("key.jgpg.n", 
			GLFW.GLFW_KEY_N, PirateGuns.MODID);
	public static final KeyMapping M = new KeyMapping("key.jgpg.m", 
			GLFW.GLFW_KEY_M, PirateGuns.MODID);
	public static final KeyMapping H = new KeyMapping("key.jgpg.z", 
			GLFW.GLFW_KEY_H, PirateGuns.MODID);
	public static final KeyMapping J = new KeyMapping("key.jgpg.x", 
			GLFW.GLFW_KEY_J, PirateGuns.MODID);
	public static final KeyMapping MINUS = new KeyMapping("key.jgpg.-", 
			GLFW.GLFW_KEY_MINUS, PirateGuns.MODID);
	
	public static void setup() {
		EntityRenderers.register(EntityRegistries.CANON.get(), 
				(context) -> new CanonRenderer(context));
		EntityRenderers.register(EntityRegistries.BULLET.get(), 
				ThrownItemRenderer::new);
		
		ClientRegistry.registerKeyBinding(SWITCH);
		ClientRegistry.registerKeyBinding(RELOAD);
		
	}
	
	public static void registerModEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::modelRegistry);
		bus.addListener(ClientEventHandler::registerLayerDefinitions);
	}

	public static void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::renderHand);
		bus.addListener(ClientEventHandler::clientTick);
		bus.addListener(ClientEventHandler::onClientLogIn);
		bus.addListener(ClientEventHandler::onClientLogOut);
		bus.addListener(ClientEventHandler::handleKeyboard);
		bus.addListener(ClientEventHandler::handleMouse);
		bus.addListener(ClientEventHandler::handleRawMouse);
		bus.addListener(ClientEventHandler::registerGunModels);
	}
	
	// Models
	
	private static void modelRegistry(ModelRegistryEvent e) {
		ForgeModelBakery.addSpecialModel(new ModelResourceLocation(Paths.PPHAMMER , "inventory"));
	}
	
	private static void registerLayerDefinitions(RegisterLayerDefinitions e) {
		e.registerLayerDefinition(CanonModel.LAYER_LOCATION, () -> CanonModel.createBodyLayer());
	}
	
	// Rendering
	
	private static void renderHand(RenderHandEvent e) {
		Player player = mc.player;
		if(player != null) {
			ClientHandler.partialTicks = e.getPartialTicks();
			if(e.getHand() == InteractionHand.MAIN_HAND) {
				ItemStack stack = player.getMainHandItem();
				if(stack.getItem() instanceof GunItem) {
					e.setCanceled(true);
					client.render(e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight());
				}
			}
		}
	}

	// Client
	
	private static void onClientLogIn(PlayerLoggedInEvent e) {
		LogUtils.getLogger().info("PlayerLoggedIn");
	}
	
	private static void onClientLogOut(PlayerLoggedOutEvent e) {
		LogUtils.getLogger().info("PlayerLoggedOut");
	}
	
	private static void registerGunModels(RegisterGunModelsEvent e) {
		LogUtils.getLogger().info("Registering models");
		// Registering GunModels
		GunModelsHandler.register(ItemRegistries.PIRATEPISTOL.get(), new PiratePistolGunModel(client));
	}
	
	private static void clientTick(ClientTickEvent e) {
		if(e.phase == Phase.START) {
			Player player = Minecraft.getInstance().player;
			if(!client.init) {
				MinecraftForge.EVENT_BUS.post(new RegisterGunModelsEvent());
				client.init = true;
			}
			if(player != null) {
				ItemStack stack = player.getMainHandItem();
				if(stack.getItem() instanceof GunItem) {
					client.tick();
					client.selectGunModel();
					if(client.getGunModel() != null) {
						// Sprint handler
						client.getSprintHandler().tick(player.isSprinting());
						// Aim handler
						if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(),
								GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS && !player.isSprinting()
								&& Minecraft.getInstance().screen == null) {
							client.getAimHandler().tick(true);
						} else {
							client.getAimHandler().tick(false);
						}
					}
				}
			}
		}
	}
	
	// Input
	
	private static void handleKeyboard(InputEvent.KeyInputEvent e) {
		Screen screen = Minecraft.getInstance().screen;
		if(screen == null || screen instanceof AnimationScreen) {
			boolean animEditFocused = false;
			if(screen instanceof AnimationScreen) {
				animEditFocused = ((AnimationScreen)screen).getEditBoxes().get(6).isFocused(); 
			}
			if(animEditFocused) return;
			if(e.getAction() == GLFW.GLFW_PRESS) {
				Player player = Minecraft.getInstance().player;
				if(player != null) {
					ItemStack stack = player.getMainHandItem();
					if(stack.getItem() instanceof GunItem) {
						if(client.getGunModel() == null) return;
						if(RELOAD.getKey().getValue() == e.getKey()) {
							PirateGuns.channel.sendToServer(new LoadBulletMessage(true));
							client.getGunModel().reload(player, stack);
						}else if(LEFT.getKey().getValue() == e.getKey()) {
							client.dec(0);
						} else if(RIGHT.getKey().getValue() == e.getKey()) {
							client.inc(0);
						} else if(UP.getKey().getValue() == e.getKey()) {
							client.inc(1);
						} else if(DOWN.getKey().getValue() == e.getKey()) {
							client.dec(1);
						} else if(M.getKey().getValue() == e.getKey()) {
							client.dec(2);
						} else if(N.getKey().getValue() == e.getKey()) {
							client.inc(2);
						} else if(H.getKey().getValue() == e.getKey()) {
							client.left();
						} else if(J.getKey().getValue() == e.getKey()) {
							client.right();
						} else if(47 == e.getKey() && Minecraft.getInstance().screen == null) {
							Minecraft.getInstance().setScreen(new GunPartsScreen(client.getGunModel())
									.setAnimScreen());
						} else if(SWITCH.getKey().getValue() == e.getKey()) {
							client.switchRotationMode();
						} 
					}
				}
			}
		}
	}
	
	private static void handleMouse(InputEvent.MouseInputEvent e) {
		
	}
	
	private static void handleRawMouse(InputEvent.RawMouseEvent e) {
		if(Minecraft.getInstance().screen == null) {
			Player player = Minecraft.getInstance().player;
			if(player != null) {
				if(player.getMainHandItem().getItem() instanceof GunItem) {
					if(e.getAction() == GLFW.GLFW_PRESS) {
						if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
							if(client.getGunModel() != null) {
								if(client.getRecoilHandler().getProgress() == 0) {
									client.shoot(player);
								}
								LogUtils.getLogger().info("Shooting");
							}
							PirateGuns.channel.sendToServer(new LoadBulletMessage(false));
						}
					}
				}
			}
		}
	}
	
}
