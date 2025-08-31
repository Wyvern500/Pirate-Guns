package com.jg.jgpg.client.handler.handlers;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.Transform;
import com.jg.jgpg.client.animation.gui.DataManagerGui;
import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import com.jg.jgpg.client.animation.gui.widget.JgEditBox;
import com.jg.jgpg.client.event.RegisterEasingsEvent;
import com.jg.jgpg.client.event.RegisterJgModelsEvent;
import com.jg.jgpg.client.gui.GunCraftingStationGui;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.jgmodel.BakedModelHandler;
import com.jg.jgpg.client.jgmodel.BaseJgModModel;
import com.jg.jgpg.client.jgmodel.JgModModels;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.IAimable;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.jgmodel.itemmodel.itemmodels.*;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.common.damagetypes.DamageType;
import com.jg.jgpg.common.item.JgGunItem;
import com.jg.jgpg.common.item.JgItem;
import com.jg.jgpg.common.network.InitGunMessage;
import com.jg.jgpg.common.network.Networking;
import com.jg.jgpg.common.network.RequestPlayerDataSynchronizationMessage;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.registries.ContainerRegistries;
import com.jg.jgpg.registries.EntityRegistries;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.server.CapabilitiesHandler;
import com.jg.jgpg.utils.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Supplier;

public class ClientEventHandler {

	private static final Minecraft mc = Minecraft.getInstance();
	
	public static boolean[] keys = new boolean[350];
	
	private static final ClientHandler client = new ClientHandler();
	private static final PirateGunsClientHandler custom =
			(PirateGunsClientHandler) client.getHandler();

	public static KeyMapping RELOAD = new KeyMapping("key.jgpg.reload", GLFW.GLFW_KEY_R,
			PirateGuns.MODID);
	public static KeyMapping SWITCHPIRATERIFLESCOPE =
			new KeyMapping("key.jgpg.switch_pirate_rifle_scope", GLFW.GLFW_KEY_INSERT,
					PirateGuns.MODID);
	public static KeyMapping LOOKANIM = new KeyMapping("key.jgpg.look", GLFW.GLFW_KEY_LEFT_ALT,
			PirateGuns.MODID);
	public static KeyMapping KICKBACK = new KeyMapping("key.jgpg.kickback", GLFW.GLFW_KEY_V,
			PirateGuns.MODID);

	public static void setup() {
		EntityRenderers.register(EntityRegistries.BULLET.get(), ThrownItemRenderer::new);
		EntityRenderers.register(EntityRegistries.HANDCANNONBULLET.get(),
				ThrownItemRenderer::new);

		PlayerPoses.addPlayerPoses(client);
	}
	
	public static void registerModEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::registerShaders);
		bus.addListener(ClientEventHandler::registerSpecialModels);
		bus.addListener(ClientEventHandler::onBakeModels);
		bus.addListener(ClientEventHandler::registerReloadListeners);
		bus.addListener(ClientEventHandler::registerKeyBindings);
		bus.addListener(ClientEventHandler::registerScreens);
		// Server
		bus.addListener(CapabilitiesHandler::attachCapabilities);
	}
	
	public static void registerNeoForgeEventListeners(IEventBus bus) {
		// Client
		bus.addListener(ClientEventHandler::registerEasings);
		bus.addListener(ClientEventHandler::renderPlayerHand);
		bus.addListener(ClientEventHandler::renderWorldLastEvent);
		bus.addListener(ClientEventHandler::handleKeyboard);
		bus.addListener(ClientEventHandler::handleMouse);
		bus.addListener(ClientEventHandler::tickClient);
		bus.addListener(ClientEventHandler::registerItemModels);
		bus.addListener(ClientEventHandler::renderPlayerPre);
		bus.addListener(ClientEventHandler::renderOverlay);
		bus.addListener(ClientEventHandler::scopeModifier);
		// Server
		bus.addListener(ClientEventHandler::onPlayerAttack);
		bus.addListener(ClientEventHandler::onTryToBreakBlock);
		bus.addListener(ClientEventHandler::onStartTrackingPlayer);
	}

	// Registering stuff

	private static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(ContainerRegistries.GUN_CRAFTING_STATION_CONTAINER.get(),
				GunCraftingStationGui::new);
	}

	private static void registerKeyBindings(RegisterKeyMappingsEvent e) {
		e.register(SWITCHPIRATERIFLESCOPE);
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

	private static void registerItemModels(RegisterJgModelsEvent e) {
		e.setCustomClient(custom);
		e.register(ItemRegistries.SMALLHAMMER.get(), SmallHammerModel::new);
		e.register(ItemRegistries.PIRATEPISTOL.get(), PiratePistolModel::new);
		e.register(ItemRegistries.PIRATERIFLE.get(), PirateRifleModel::new);
		e.register(ItemRegistries.PRIMITIVEREVOLVER.get(), PrimitiveRevolverModel::new);
		e.register(ItemRegistries.TRABUCO.get(), TrabucoModel::new);
		e.register(ItemRegistries.HEAVYHANDCANNON.get(), HeavyHandCannonModel::new);
	}

	private static void registerReloadListeners(RegisterClientReloadListenersEvent e) {
		e.registerReloadListener(client);
	}
	
	private static void registerShaders(RegisterShadersEvent e) {
		NeoForge.EVENT_BUS.post(new RegisterEasingsEvent());
		client.initPlayerRenderer();
	}
	
	private static void registerEasings(RegisterEasingsEvent e) {
		LogUtils.log("ClientEventHandler", "Registering easings");
		EasingHandler.registerEasings(e);

		LogUtils.log("ClientEventHandler", "Models: " + JgModelsHandler.getModels()
				.toString());
	}
	
	private static void registerSpecialModels(ModelEvent.RegisterAdditional e) {
		e.register(new ModelResourceLocation(ResourceLocation.parse(Constants.PPHAMMER),
				"standalone"));
		LogUtils.log("ClientEventHandler", "Hello xd");
		e.register(new ModelResourceLocation(ResourceLocation.parse(Constants.PRVHAMMER),
				"standalone"));
		e.register(new ModelResourceLocation(ResourceLocation.parse(Constants.PRVCHAMBER),
				"standalone"));
		e.register(new ModelResourceLocation(ResourceLocation.parse(Constants.PRHAMMER),
				"standalone"));
		e.register(new ModelResourceLocation(ResourceLocation.parse(Constants.PRSCOPE),
				"standalone"));
		e.register(new ModelResourceLocation(ResourceLocation.parse(Constants.THAMMER),
				"standalone"));

		NeoForge.EVENT_BUS.start();
		NeoForge.EVENT_BUS.post(new RegisterJgModelsEvent());
	}
	
	// Rendering

	public static void scopeModifier(ViewportEvent.ComputeFov e) {
		Player player = mc.player;
		if (player == null)
			return;
		ItemStack stack = player.getMainHandItem();
		Item item = stack.getItem();
		if (item instanceof IAimable) {
			if(custom.getAimHandler().getProgress(item) > 0.5f) {
				int targetFOV = 60;

				if(item == ItemRegistries.PIRATERIFLE.get()
						&& NBTUtils.hasScope(stack)) {
					targetFOV = 30;
				}

				e.setFOV(Mth.lerp((custom.getAimHandler().getProgress(item)
								- 0.5f) / 0.5f, mc.options.fov().get(), targetFOV));
			}
		}
	}

	private static void renderOverlay(RenderGuiLayerEvent.Post e) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			ItemStack stack = player.getMainHandItem();
			Item item = stack.getItem();
			if (item instanceof JgGunItem) {
				if (e.getName().equals(VanillaGuiLayers.HOTBAR)) {
					int width = Minecraft.getInstance().getWindow()
							.getGuiScaledWidth();
					int height = Minecraft.getInstance().getWindow()
							.getGuiScaledHeight();

					e.getGuiGraphics().drawString(mc.font, "Bullets: " + NBTUtils
							.getBullets(stack), (int) (width
							* 0.1f), (int) (height * 0.8f), Color.rgba(255,
							255, 255, 255));

					if(custom.getAimHandler().getProgress(item) > 0.9f && NBTUtils
							.hasScope(stack)) {
						RenderHelper.renderScopeOverlay(custom.getAimHandler()
								.getProgress(item), e.getGuiGraphics());
					}

					if(custom.getHitmarker().getHitmarkerTime() > 0) {
						RenderHelper.renderHitmarker(e.getGuiGraphics().pose(), e
								.getGuiGraphics().bufferSource(), e.getPartialTick()
										.getGameTimeDeltaTicks(), 12);

						// Decrease hitmarkerTime
						custom.getHitmarker().hitmarkerTime--;
					}
				}
			}
		}
	}

	private static void renderPlayerPre(RenderPlayerEvent.Pre e) {
		Player player = e.getEntity();
		ItemStack stack = player.getMainHandItem();
		if(stack.getItem() instanceof JgItem) {
			e.setCanceled(true);

			ClientHandler.getPlayerData(e.getEntity().getUUID());

			client.playerRenderer.render((AbstractClientPlayer) e.getEntity(),
					e.getEntity().getViewYRot(e.getPartialTick()),
					e.getPartialTick(), e.getPoseStack(), e.getMultiBufferSource(),
					e.getPackedLight(), e.getRenderer());
		}
	}

	private static void renderPlayerHand(RenderHandEvent e) {
		LocalPlayer player = mc.player;
		if (player != null) {
			Item item = player.getMainHandItem().getItem();
			if(item instanceof JgItem jgItem) {
                if(jgItem.isCustomRendered(player.getMainHandItem())) {
					e.setCanceled(true);
					if(e.getHand() == InteractionHand.MAIN_HAND) {
						MultiBufferSource.BufferSource impl = mc.renderBuffers()
								.bufferSource();
						client.render(player, player.getMainHandItem(), e.getPoseStack(), 
								e.getMultiBufferSource(), impl, e.getPackedLight());
						impl.endBatch();
					}
				}
			}
		}
	}

	private static void renderWorldLastEvent(RenderLevelStageEvent e) {
		LocalPlayer player = mc.player;
		if (player != null) {
			ItemStack stack = player.getMainHandItem();
			ClientHandler.partialTicks = e.getPartialTick().getGameTimeDeltaTicks();
			if (stack.getItem() instanceof JgItem) {
				client.tick(player, stack, NBTUtils
						.getId(stack));
			}
		}
	}

	// Models
	
	private static void onBakeModels(ModelEvent.ModifyBakingResult e) {
		Map<String, Supplier<JgModel<?>>> jgModels = JgModelsHandler.getModels();
		
		for(Entry<String, Supplier<JgModel<?>>> entry : jgModels.entrySet()) {
			JgModel<?> model = entry.getValue().get();
			ResourceLocation loc = ResourceLocation.parse(entry.getKey());
			ModelResourceLocation mLoc = new ModelResourceLocation(loc, "standalone");
			BakedModel origin = e.getModels().get(mLoc);
			e.getModels().put(mLoc, new BaseJgModModel(origin, model.getItem()));

			JgModModels.addModel(model.getItem(), () -> model.getModModel(origin));
			LogUtils.log("ClientEventHandler", "Registering toShowModModel: " +
					entry.getKey() + " MLoc: " + mLoc.toString() + " item: " +
					model.getItem().getDescriptionId());
		}
	}

	// Misc

	private static void onTryToBreakBlock(PlayerEvent.BreakSpeed e) {
		if(e.getEntity() instanceof Player) {
			Player player = e.getEntity();
			ItemStack stack = player.getMainHandItem();
			if(stack.getItem() instanceof JgItem) {
				if(!((JgItem) stack.getItem()).shouldBreakBlocks(stack)) {
					e.setCanceled(true);
				}
			}
		}
	}

	private static void onPlayerAttack(LivingIncomingDamageEvent e) {
		if(!e.getSource().equals(DamageType.melee(e.getEntity().level()))) {
			if (e.getSource().getEntity() instanceof Player player) {
				ItemStack stack = player.getMainHandItem();
				if (stack.getItem() instanceof JgItem) {
					if(e.getSource().is(DamageTypes.PLAYER_ATTACK)) {
						if (!((JgItem) stack.getItem()).shouldHurt(stack)) {
							e.setCanceled(true);
						}
					}
				}
			}
		}
	}

	private static void onStartTrackingPlayer(PlayerEvent.StartTracking e) {
		Entity entity = e.getTarget();
		if(entity instanceof ServerPlayer player) {
			if(player.getMainHandItem().getItem() instanceof JgItem) {
				Networking.sendToPlayerClient(
						new RequestPlayerDataSynchronizationMessage(e.getEntity()
						.getUUID()), player);
			}
			/*player.getCapability(JgPlayerDataProvider.PLAYER_DATA).ifPresent(cap -> {
				PirateGuns.channel.send(PacketDistributor.PLAYER.with(() -> player),
						new SynchronizePlayerDataMessage(player.getUUID(),
						cap.getModel(), cap.getPose(), cap.getAnimationIndex()));
			});*/

		}
		/*Player player = e.getEntity();
		LogUtils.log("ClientEventHandler", "OnStartTracking: " + player
				.getName() + " entity: " + entity.getName());*/
	}

	// Client
	
	private static void tickClient(ClientTickEvent.Post e) {
		LocalPlayer player = Minecraft.getInstance().player;
		if(player != null) {
			
			ItemStack stack = player.getMainHandItem();
			
			if(stack.getItem() instanceof JgItem) {
				
				// When initializing the stack id, I use it to differentiate each stack and for other purposes
				String id = NBTUtils.getId(stack);
				if(id.equals(Constants.EMPTYID) || id.isEmpty()) {
					// Giving a random uuid
					String newId = UUID.randomUUID().toString();
					Networking.sendToServer(new InitGunMessage(newId));
					id = newId;
				}
				
				//client.tick(player, stack, id);
			}
			
			// Handle keys
			// Model Manipulation
			boolean shouldMove = true;
			Screen screen = Minecraft.getInstance().screen;
			if(screen != null) {
				if(screen instanceof NewAnimationGui) {
					if(screen.getFocused() instanceof JgEditBox) {
						shouldMove = false;
					}
				}
			}
			
			if(shouldMove && Config.CLIENT.doDebugStuff.get()) {
				boolean isRotationMode = client.isRotationMode();
				boolean isDisplayMode = client.isDisplayMode();
				float toUse = isRotationMode ? client.getRotationStep() : client
						.getTranslationStep();
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
	
	// Input handling
	
	private static void handleKeyboard(InputEvent.Key e) {
		if(e.getKey() < 0 || e.getKey() >= keys.length) return;

		if(e.getAction() == GLFW.GLFW_PRESS) {
			LocalPlayer player = mc.player;
			
			if(player == null) return;
			
			Screen screen = mc.screen;
			if(screen instanceof NewAnimationGui) {
				GuiEventListener comp = screen.getFocused();
				if(comp instanceof JgEditBox) {
					return;
				}
			}
			
			keys[e.getKey()] = true;
			
			if(player.getMainHandItem().getItem() instanceof JgItem) {
				
				//LogUtils.log("ClientEventHandler:handleKeyboard", "Key: " + e.getKey());
				
				client.handleKeyboard(e);
				
				boolean debugMode = Config.CLIENT.doDebugStuff.get();

				ItemStack stack = player.getMainHandItem();

				// Debug Stuff
				if(debugMode) {
					// Model Manipulation
					if(!keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
						if(keys[GLFW.GLFW_KEY_C]) {
							client.switchRotationMode();
							LogUtils.log("handleKeyboard", "isRotationMode: " + client
									.isRotationMode());
						}
					}
					// Left 263 Up 265 Down 264 Right 262 N 78 M 77 C 67 Intro (numpad) 335
					// J 74 K 75 Supr 261 Fin 268
					switch(e.getKey()) {
					case 261: // Supr
						// Open Animation gui
						if(player != null) {
							if(stack.getItem() instanceof JgItem) {
								String itemId = BuiltInRegistries.ITEM
										.getKey(stack.getItem()).toString();
								JgModel<?> gunModel = JgModelsHandler.get(itemId).get();
								if(gunModel != null) {
									NewAnimationGui gui = new NewAnimationGui(client);
									client.setModel(gunModel, itemId);
									gui.setModel(gunModel);
									Minecraft.getInstance().setScreen(gui);
								}
							}
						}
						break;
					case 269: // Fin
						// Open Animation gui
						if (player != null) {
							if (stack.getItem() instanceof JgItem) {
								DataManagerGui gui = new DataManagerGui();
								Minecraft.getInstance().setScreen(gui);
							}
						}
						break;
					case 92: // }
						client.switchDisplayMode();
						LogUtils.log("handleKeyboard", "displayMode: " + client.isDisplayMode());
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
						NeoForge.EVENT_BUS.start();
						NeoForge.EVENT_BUS.post(new RegisterJgModelsEvent());
						NeoForge.EVENT_BUS.post(new RegisterEasingsEvent());
						LogUtils.log("ClientEventHandler", "Updating models");
						break;
					case 39: // {
						client.switchRenderDefault();
						break;
					case 88: // X
						String res = "\n";
						for(Entry<String, JgModelPart> entry : client.getModel().getParts()
								.entrySet()) {
							Transform t = entry.getValue().getDtransform();
							res += "addPart(new JgModelPart(\"" + entry.getKey() + "\", " + t.pos[0] + "f, " + 
									t.pos[1] + "f, " + t.pos[2] + "f, " + t.rot[0] + "f, " + t.rot[1] + "f, " + 
									t.rot[2] + "f));\n";
						}
						LogUtils.log("ClientEventHandler", "ModelData: " + res);
						break;
					case GLFW.GLFW_KEY_COMMA:
						ToShowModModel modModel = BakedModelHandler.INSTANCE.getModelForId(NBTUtils
								.getId(Utils.handStack()));
						
						if(modModel != null) {
							modModel.tryToRebuild();
							LogUtils.log("ClientHandler", "Rebuilding");
						}
						break;
					}
				}
			}
		} else if(e.getAction() == GLFW.GLFW_RELEASE) {
			keys[e.getKey()] = false;
		}
	}
	
	private static void handleMouse(InputEvent.MouseButton.Pre e) {
		if(mc.player != null) {
			if(mc.screen == null) {
				if (mc.player.getMainHandItem().getItem() instanceof JgItem) {
					client.handleMouse(e);
				}
			}
		}
	}
	
	public static ClientHandler getClientHandler() {
		return client;
	}

	public static PirateGunsClientHandler getCustomClientHandler() {
		return custom;
	}
	
}
