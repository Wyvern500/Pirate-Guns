package com.jg.jgpg.client.handlers;

import java.util.UUID;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.capabilities.IPlayerCapability;
import com.jg.jgpg.capabilities.PlayerCapability;
import com.jg.jgpg.capabilities.PlayerCapabilityImplementation;
import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.events.RegisterEasingsEvent;
import com.jg.jgpg.client.events.RegisterGunModelsEvent;
import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.gui.GunAndAmmoCraftingGui;
import com.jg.jgpg.client.gui.widget.widgets.JgEditBox;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractGunModel;
import com.jg.jgpg.client.model.loader.JgModelLoader;
import com.jg.jgpg.client.model.models.PiratePistolModel;
import com.jg.jgpg.client.model.models.PirateRifleModel;
import com.jg.jgpg.client.model.models.TrabucoModel;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.item.items.PirateRifle;
import com.jg.jgpg.registries.ContainerRegistries;
import com.jg.jgpg.registries.EntityRegistries;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.NBTUtils;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientEventHandler {

	private static Minecraft mc = Minecraft.getInstance();
	private static ClientHandler client = new ClientHandler();
	
	public static KeyMapping RELOAD = new KeyMapping("key.jgpg.reload", GLFW.GLFW_KEY_R, 
			PirateGuns.MODID);
	public static KeyMapping LOOKANIM = new KeyMapping("key.jgpg.look", GLFW.GLFW_KEY_LEFT_ALT, 
			PirateGuns.MODID);
	public static KeyMapping KICKBACK = new KeyMapping("key.jgpg.kickback", GLFW.GLFW_KEY_V, 
			PirateGuns.MODID);
	
	private static float trStep = 0.01f;
	private static float rtStep = (float) Math.toRadians(0.01D);
	private static boolean isRotationMode = false;
	private static boolean isDisplayMode = false;
	public static boolean[] keys = new boolean[500];
	
	public static ResourceLocation SCOPE = new ResourceLocation(PirateGuns.MODID,
			"textures/misc/scope.png");
	
	public static void registerModEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::registerShaders);
		bus.addListener(ClientEventHandler::registerSpecialModels);
		bus.addListener(ClientEventHandler::registerKeyBindings);
		bus.addListener(ClientEventHandler::registerModelLoaders);
	}

	public static void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::renderPlayerHand);
		bus.addListener(ClientEventHandler::tickClient);
		bus.addListener(ClientEventHandler::handleMouse);
		bus.addListener(ClientEventHandler::handleKeyboard);
		bus.addListener(ClientEventHandler::registerGunModels);
		bus.addListener(ClientEventHandler::registerEasings);
		bus.addListener(ClientEventHandler::renderOverlayPre);
		bus.addListener(ClientEventHandler::renderOverlayPost);
		bus.addListener(ClientEventHandler::scopeModifier);
		bus.addListener(ClientEventHandler::renderPlayer);
		//bus.addGenericListener(Entity.class, ClientEventHandler::attachCapabilities);
		bus.addListener(ClientEventHandler::onPlayerClone);
	}
	
	public static void setup() {
		EntityRenderers.register(EntityRegistries.BULLET.get(), ThrownItemRenderer::new);
		
		MenuScreens.register(ContainerRegistries.CRAFTING_CONTAINER.get(), GunAndAmmoCraftingGui::new);
	}
	
	// Capabilites
	
	/*private static void attachCapabilities(AttachCapabilitiesEvent<Entity> e) {
		if(e.getObject() instanceof Player) {
			PlayerCapabilityProvider provider = new PlayerCapabilityProvider();
			
			e.addCapability(PlayerCapabilityProvider.ID, provider);
		}
	}*/
	
	private static void onPlayerClone(PlayerEvent.Clone e) {
		if(e.isWasDeath()) {
			Player old = e.getOriginal();
			Player newPlayer = e.getEntity();
			
			IPlayerCapability oldCap = old.getCapability(PlayerCapability.INSTANCE).orElseGet(() -> 
					new PlayerCapabilityImplementation());
			newPlayer.getCapability(PlayerCapability.INSTANCE).ifPresent(cap -> cap.deserializeNBT(oldCap
					.serializeNBT()));
			
			old.invalidateCaps();
		}
	}
	
	// Registering
	
	private static void registerKeyBindings(RegisterKeyMappingsEvent e) {
		if(!ModList.get().isLoaded("oldguns")) {
			e.register(RELOAD);
			e.register(LOOKANIM);
			e.register(KICKBACK);
		} else {
			for(KeyMapping key : mc.options.keyMappings) {
				switch(key.getKey().getName()) {
				case "key.oldguns.reload":
					RELOAD = key;
					break;
				case "key.oldguns.look":
					LOOKANIM = key;
					break;
				case "key.oldguns.kickback":
					KICKBACK = key;
					break;
				}
			}
		}
	}
	
	private static void registerModelLoaders(RegisterGeometryLoaders e) {
		e.register("jgmodels", JgModelLoader.INSTANCE);
	}
	
	private static void registerSpecialModels(ModelEvent.RegisterAdditional e) {
		e.register(new ModelResourceLocation(new ResourceLocation(Constants.PPHAMMER),
				"inventory"));
		e.register(new ModelResourceLocation(new ResourceLocation(Constants.PRHAMMER),
				"inventory"));
		e.register(new ModelResourceLocation(new ResourceLocation(Constants.THAMMER),
				"inventory"));
		e.register(new ModelResourceLocation(new ResourceLocation("jgpg:special/pirate_pistol_test"),
				"inventory"));
		
		LogUtils.log("ClientEventHandler", "Registering special models");
	}
	
	private static void registerShaders(RegisterShadersEvent e) {
		MinecraftForge.EVENT_BUS.start();
		MinecraftForge.EVENT_BUS.post(new RegisterGunModelsEvent());
		MinecraftForge.EVENT_BUS.post(new RegisterEasingsEvent());
		client.initPlayerRenderer();
	}
	
	private static void registerGunModels(RegisterGunModelsEvent e) {
		e.register(ItemRegistries.PIRATEPISTOL.get(), new PiratePistolModel(client));
		e.register(ItemRegistries.PIRATERIFLE.get(), new PirateRifleModel(client));
		e.register(ItemRegistries.TRABUCO.get(), new TrabucoModel(client));
		LogUtils.log("ClientEventHandler", "Registering Gun Models :)");
	}
	
	private static void registerEasings(RegisterEasingsEvent e) {
		EasingHandler.registerEasings(e);
	}
	
	// Rendering
	
	private static void renderPlayerHand(RenderHandEvent e) {
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
	
	private static void renderPlayer(RenderPlayerEvent.Pre e) {
		ItemStack stack = e.getEntity().getMainHandItem();
		if(stack.getItem() instanceof JgGunItem) {
			e.setCanceled(true);
			
			client.renderPlayer(e.getPoseStack(), e.getMultiBufferSource(), e.getPartialTick(), 
					e.getPackedLight());
		}
	}
	
	// Screen
	
	public static void scopeModifier(ViewportEvent.ComputeFov e) {
		Player player = mc.player;
		if (player == null)
			return;
		Item item = player.getMainHandItem().getItem();
		if (item instanceof JgGunItem) {
			if(item instanceof PirateRifle) {
				if(client.getAimHandler().getProgress(item) > 0.5f) {
					e.setFOV((float) Mth.lerp((client.getAimHandler().getProgress(item) - 0.5f) / 0.5f, 
							Minecraft.getInstance().options.fov().get(), 20));
				}
			}
		}
	}

	// Rendering Overlay

	public static void renderOverlayPre(RenderGuiOverlayEvent.Pre e) {
		Player player = mc.player;

		if (player == null)
			return;

		boolean ifi = false;
		if(ifi){
			int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
			int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

			float f = (float) Math.min(screenWidth, screenHeight);
			float f1 = Math.min((float) screenWidth / f, (float) screenHeight / f) *
					Mth.lerp((0 - 0.5f) / 0.5f, 0.01f, 1.125f);
			int i = Mth.floor(f * f1);
			int j = Mth.floor(f * f1);
			int k = (screenWidth - i) / 2;
			int l = (screenHeight - j) / 2;
			int i1 = k + i;
			int j1 = l + j;

			RenderSystem.enableDepthTest();
			GL11.glEnable(GL11.GL_BLEND);
			RenderHelper.blit(SCOPE, e.getGuiGraphics().pose(), k, l, -90, 0.0F, 0.0F, i, j, i, j);
			GL11.glDisable(GL11.GL_BLEND);
			RenderSystem.disableDepthTest();
		}

		if (player.getMainHandItem().getItem() instanceof JgGunItem) {
			if (e.getOverlay().overlay() == VanillaGuiOverlay.CROSSHAIR.type()
					.overlay()) {
				if (mc.options.getCameraType().isFirstPerson()) {
					//e.setCanceled(true);
				}
			}

		}
	}

	public static void renderOverlayPost(RenderGuiOverlayEvent.Post e) {
		Player player = mc.player;
		if (player == null)
			return;

		Item item = player.getMainHandItem().getItem();
		if (item instanceof JgGunItem) {
			if(item instanceof PirateRifle) {
				if (e.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
					if(client.getAimHandler().getProgress(item) > 0.5f) {
						RenderHelper.renderScopeOverlay(client.getAimHandler().getProgress(item));
					}
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
				client.tick(player, stack, id);
			}
			
			// Handle keys
			// Model Manipulation
			boolean shouldMove = true;
			Screen screen = Minecraft.getInstance().screen;
			if(screen != null) {
				if(screen instanceof AnimationGui) {
					if(screen.getFocused() instanceof JgEditBox) {
						shouldMove = false;
					}
				}
			}
			trStep = 0.01f;
			rtStep = (float) Math.toRadians(1f);
			if(shouldMove && Config.CLIENT.debugMode.get()) {
				float toUse = isRotationMode ? rtStep : trStep; 
				if(keys[263]) { // Left arrow
					client.addToModel(-toUse, 0, 0, isRotationMode, isDisplayMode);
				} else if(keys[262]) { // Right arrow
					client.addToModel(toUse, 0, 0, isRotationMode, isDisplayMode);
				} else if(keys[265]) { // Up arrow
					client.addToModel(0, toUse, 0, isRotationMode, isDisplayMode);
				} else if(keys[264]) { // Down arrow
					client.addToModel(0, -toUse, 0, isRotationMode, isDisplayMode);
				} else if(keys[78]) { // N arrow
					client.addToModel(0, 0, -toUse, isRotationMode, isDisplayMode);
				} else if(keys[77]) { // M arrow
					client.addToModel(0, 0, toUse, isRotationMode, isDisplayMode);
				}
			}
		}
	}
	
	// Mouse
	
	private static void handleMouse(InputEvent.MouseButton.Pre e) {
		client.handleMouse(e);
	}
	
	// Keyboard
	
	private static void handleKeyboard(InputEvent.Key e) {
		
		if(e.getAction() == GLFW.GLFW_PRESS) {
			LocalPlayer player = mc.player;
			
			Screen screen = mc.screen;
			if(screen instanceof AnimationGui) {
				GuiEventListener comp = screen.getFocused();
				if(comp instanceof JgEditBox) {
					return;
				}
			}
			
			keys[e.getKey()] = true;
			
			// LogUtils.log("ClientEventHandler:handleKeyboard", "Key: " + e.getKey());
			
			AbstractGunModel model = client.getModel();
			Animation currentAnimation = model.getAnimator().getCurrent();
			
			if(model != null) {
				// Gun related stuff
				if(e.getKey() == RELOAD.getKey().getValue()) {
					model.tryToReload(player);
				} else if(e.getKey() == LOOKANIM.getKey().getValue() && currentAnimation == null) {
					model.getAnimator().setCurrent(model, model.getLookAnimAnimation(player));
					model.getAnimator().play();
				} else if(e.getKey() == KICKBACK.getKey().getValue() && currentAnimation == null) {
					model.getAnimator().setCurrent(model, model.getKickbackAnimAnimation(player));
					model.getAnimator().play();
				}
			}
			
			boolean debugMode = Config.CLIENT.debugMode.get();
			
			// Debug Stuff
			if(debugMode) {
				// Model Manipulation
				// Left 263 Up 265 Down 264 Right 262 N 78 M 77 C 67 Intro (numpad) 335 J 74 K 75
				switch(e.getKey()) {
				case 261: // Supr
					// Open Animation gui
					if(player != null) {
						ItemStack stack = player.getMainHandItem();
						if(stack.getItem() instanceof JgGunItem) {
							AbstractGunModel gunModel = GunModelsHandler.get(ForgeRegistries.ITEMS
									.getKey(stack.getItem()).toString());
							if(gunModel != null) {
								AnimationGui gui = new AnimationGui(client);
								client.setModel(gunModel);
								gui.setModel(gunModel);
								Minecraft.getInstance().setScreen(gui);
							}
						}
					}
					break;
				case 67: // C
					isRotationMode = !isRotationMode;
					LogUtils.log("handleKeyboard", "isRotationMode: " + isRotationMode);
					break;
				case 92: // }
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
				case 39: // {
					client.switchRenderDefault();
					break;
				}
			}
		} else if(e.getAction() == GLFW.GLFW_RELEASE) {
			keys[e.getKey()] = false;
		}
	}
	
}
