package com.jg.pirateguns.client.handlers;

import org.lwjgl.glfw.GLFW;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.animations.gunmodels.PiratePistolGunModel;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.client.models.entities.CanonModel;
import com.jg.pirateguns.client.rendering.entities.CanonRenderer;
import com.jg.pirateguns.guns.GunItem;
import com.jg.pirateguns.network.LoadBulletMessage;
import com.jg.pirateguns.registries.EntityRegistries;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.utils.Paths;
import com.mojang.logging.LogUtils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
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
	public static final KeyMapping Z = new KeyMapping("key.jgpg.z", 
			GLFW.GLFW_KEY_Z, PirateGuns.MODID);
	public static final KeyMapping X = new KeyMapping("key.jgpg.x", 
			GLFW.GLFW_KEY_X, PirateGuns.MODID);
	
	public static void setup() {
		EntityRenderers.register(EntityRegistries.CANON.get(), 
				(context) -> new CanonRenderer(context));
		EntityRenderers.register(EntityRegistries.BULLET.get(), 
				ThrownItemRenderer::new);
		
		ClientRegistry.registerKeyBinding(SWITCH);
		ClientRegistry.registerKeyBinding(RELOAD);
		
		// Registering GunModels
		GunModelsHandler.register(ItemRegistries.PIRATEPISTOL.get(), new PiratePistolGunModel(client));
	}
	
	public static void registerModEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::modelRegistry);
		bus.addListener(ClientEventHandler::registerLayerDefinitions);
	}

	public static void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::renderHand);
		bus.addListener(ClientEventHandler::clientTick);
		bus.addListener(ClientEventHandler::handleKeyboard);
		bus.addListener(ClientEventHandler::handleMouse);
		bus.addListener(ClientEventHandler::handleRawMouse);
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
	
	private static void clientTick(ClientTickEvent e) {
		if(e.phase == Phase.START) {
			Player player = Minecraft.getInstance().player;
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
					} else if(Z.getKey().getValue() == e.getKey()) {
						client.left();
					} else if(X.getKey().getValue() == e.getKey()) {
						client.right();
					} else if(SWITCH.getKey().getValue() == e.getKey()) {
						client.switchRotationMode();
					} 
				}
			}
		}
	}
	
	private static void handleMouse(InputEvent.MouseInputEvent e) {
		
	}
	
	private static void handleRawMouse(InputEvent.RawMouseEvent e) {
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
