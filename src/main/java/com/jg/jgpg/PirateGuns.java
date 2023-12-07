package com.jg.jgpg;

import com.jg.jgpg.capabilities.IPlayerCapability;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handlers.ClientEventHandler;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.network.AddCooldownMessage;
import com.jg.jgpg.network.BodyHitMessage;
import com.jg.jgpg.network.CraftItemMessage;
import com.jg.jgpg.network.LoadBulletMessage;
import com.jg.jgpg.network.PlaySoundMessage;
import com.jg.jgpg.network.SetBulletsMessage;
import com.jg.jgpg.network.SetScopeMessage;
import com.jg.jgpg.network.ShootMessage;
import com.jg.jgpg.proxy.ClientProxy;
import com.jg.jgpg.proxy.IProxy;
import com.jg.jgpg.proxy.ServerProxy;
import com.jg.jgpg.registries.BlockRegistries;
import com.jg.jgpg.registries.ContainerRegistries;
import com.jg.jgpg.registries.EntityRegistries;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.NBTUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(PirateGuns.MODID)
public class PirateGuns {

	public static final String MODID = "jgpg";
	private static IProxy proxy;
	public static SimpleChannel channel;
    private static int packetsRegistered = 0;
	public static final String PROTOCOL_VERSION = "1";
	
	public PirateGuns() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
	
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.server_config);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.client_config);
	
		// Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemRegistries::creativeModeTabRegister);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemRegistries::buildCreativeModeTab);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerCapabilites);
		
        PirateGuns.proxy.registerModEventListeners(bus);
        
        //Register mod stuff (Items, Entities, Containers, etc.)
        SoundRegistries.SOUNDS.register(bus);
        ItemRegistries.ITEMS.register(bus);
        BlockRegistries.BLOCKS.register(bus);
        EntityRegistries.ENTITIES.register(bus);
        ContainerRegistries.CONTAINERS.register(bus);
        
        bus = MinecraftForge.EVENT_BUS;
        PirateGuns.proxy.registerForgeEventListeners(bus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		PirateGuns.channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), 
        		() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

		channel.registerMessage(packetsRegistered++, LoadBulletMessage.class, 
				LoadBulletMessage::encode, LoadBulletMessage::decode, 
				LoadBulletMessage::handle);
		channel.registerMessage(packetsRegistered++, ShootMessage.class, 
				ShootMessage::encode, ShootMessage::decode, 
				ShootMessage::handle);
		channel.registerMessage(packetsRegistered++, PlaySoundMessage.class, 
				PlaySoundMessage::encode, PlaySoundMessage::decode, 
				PlaySoundMessage::handle);
		channel.registerMessage(packetsRegistered++, BodyHitMessage.class, 
				BodyHitMessage::encode, BodyHitMessage::decode, 
				BodyHitMessage::handle);
		channel.registerMessage(packetsRegistered++, AddCooldownMessage.class, 
				AddCooldownMessage::encode, AddCooldownMessage::decode, 
				AddCooldownMessage::handle);
		channel.registerMessage(packetsRegistered++, CraftItemMessage.class, 
				CraftItemMessage::encode, CraftItemMessage::decode, 
				CraftItemMessage::handle);
		channel.registerMessage(packetsRegistered++, SetBulletsMessage.class, 
				SetBulletsMessage::encode, SetBulletsMessage::decode, 
				SetBulletsMessage::handle);
		channel.registerMessage(packetsRegistered++, SetScopeMessage.class, 
				SetScopeMessage::encode, SetScopeMessage::decode, 
				SetScopeMessage::handle);
	}
	
	private void registerCapabilites(RegisterCapabilitiesEvent e) {
		e.register(IPlayerCapability.class);
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		event.enqueueWork(() ->
		{
			ClientEventHandler.setup();
			ClientHandler.registerItemProperties();
		});
	}
	
}