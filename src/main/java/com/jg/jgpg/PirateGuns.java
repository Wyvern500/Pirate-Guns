package com.jg.jgpg;

import com.jg.jgpg.client.handlers.ClientEventHandler;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.network.*;
import com.jg.jgpg.proxy.ClientProxy;
import com.jg.jgpg.proxy.IProxy;
import com.jg.jgpg.proxy.ServerProxy;
import com.jg.jgpg.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemRegistries::creativeModeTabRegister);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemRegistries::buildCreativeModeTab);
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        PirateGuns.proxy.registerModEventListeners(bus);

		//Register mod stuff (Items, Entities, Containers, etc.)
		DamageTypeRegistries.DAMAGE_TYPE_REGISTER.register(bus);
        ItemRegistries.ITEMS.register(bus);
        SoundRegistries.SOUNDS.register(bus);
		EntityRegistries.ENTITIES.register(bus);
		ContainerRegistries.CONTAINERS.register(bus);
		BlockRegistries.BLOCKS.register(bus);

        bus = MinecraftForge.EVENT_BUS;

        PirateGuns.proxy.registerForgeEventListeners(bus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		PirateGuns.channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"),
        		() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
		
		PirateGuns.channel.registerMessage(packetsRegistered++, BodyHitMessage.class,
				BodyHitMessage::encode, BodyHitMessage::decode, BodyHitMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, PlaySoundMessage.class,
				PlaySoundMessage::encode, PlaySoundMessage::decode, PlaySoundMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, InitGunMessage.class,
				InitGunMessage::encode, InitGunMessage::decode, InitGunMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++,
				SendPlayerPoseChangedMessage.class, SendPlayerPoseChangedMessage::encode,
				SendPlayerPoseChangedMessage::decode, SendPlayerPoseChangedMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++,
				HandlePlayerPoseChangedMessage.class, HandlePlayerPoseChangedMessage::encode,
				HandlePlayerPoseChangedMessage::decode, HandlePlayerPoseChangedMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++,
				ShootMessage.class, ShootMessage::encode,
				ShootMessage::decode, ShootMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, UpdateExtraDataMessage.class,
				UpdateExtraDataMessage::encode, UpdateExtraDataMessage::decode,
				UpdateExtraDataMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, CraftItemMessage.class,
				CraftItemMessage::encode, CraftItemMessage::decode,
				CraftItemMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, AddItemMessage.class,
				AddItemMessage::encode, AddItemMessage::decode,
				AddItemMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, ConsumeItemMessage.class,
				ConsumeItemMessage::encode, ConsumeItemMessage::decode,
				ConsumeItemMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, SetBulletsMessage.class,
				SetBulletsMessage::encode, SetBulletsMessage::decode,
				SetBulletsMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, ShowHitmarkerMessage.class,
				ShowHitmarkerMessage::encode, ShowHitmarkerMessage::decode,
				ShowHitmarkerMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, SynchronizeJgModelMessage.class,
				SynchronizeJgModelMessage::encode, SynchronizeJgModelMessage::decode,
				SynchronizeJgModelMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, HandleJgModelUpdateMessage.class,
				HandleJgModelUpdateMessage::encode, HandleJgModelUpdateMessage::decode,
				HandleJgModelUpdateMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, SynchronizePlayerDataMessage.class,
				SynchronizePlayerDataMessage::encode, SynchronizePlayerDataMessage::decode,
				SynchronizePlayerDataMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, SynchronizeAnimationMessage.class,
				SynchronizeAnimationMessage::encode, SynchronizeAnimationMessage::decode,
				SynchronizeAnimationMessage::handle);
		PirateGuns.channel.registerMessage(packetsRegistered++, HandleAnimationChangedMessage.class,
				HandleAnimationChangedMessage::encode, HandleAnimationChangedMessage::decode,
				HandleAnimationChangedMessage::handle);
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		ClientEventHandler.setup();
	}
	
}
