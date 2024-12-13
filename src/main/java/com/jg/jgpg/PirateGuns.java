package com.jg.jgpg;

import com.jg.jgpg.common.network.Networking;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.proxy.ClientProxy;
import com.jg.jgpg.proxy.IProxy;
import com.jg.jgpg.proxy.ServerProxy;
import com.jg.jgpg.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(PirateGuns.MODID)
public class PirateGuns {

	public static final String MODID = "jgpg";
	public static IProxy proxy;

	public PirateGuns(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.SERVER, Config.server_config);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Config.client_config);

		//modEventBus.addListener(this::setup);
		modEventBus.addListener(ItemRegistries::creativeModeTabRegister);

		if(FMLEnvironment.dist == Dist.CLIENT) {
			proxy = new ClientProxy();
		} else {
			proxy = new ServerProxy();
		}

        PirateGuns.proxy.registerModEventListeners(modEventBus);

		//Register mod stuff (Items, Entities, Containers, etc.)
        ItemRegistries.ITEMS.register(modEventBus);
        SoundRegistries.SOUNDS.register(modEventBus);
		EntityRegistries.ENTITIES.register(modEventBus);
		ContainerRegistries.CONTAINERS.register(modEventBus);
		BlockRegistries.BLOCKS.register(modEventBus);
		AttachmentRegistries.ATTACHMENT_TYPES.register(modEventBus);
		DataComponentRegistries.REGISTRAR.register(modEventBus);

        PirateGuns.proxy.registerNeoForgeEventListeners(NeoForge.EVENT_BUS);

		modEventBus.addListener(Networking::register);
	}

	public static ResourceLocation prefix(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
	
}
