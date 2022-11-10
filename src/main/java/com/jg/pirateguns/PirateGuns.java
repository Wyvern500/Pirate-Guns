package com.jg.pirateguns;

import com.jg.pirateguns.client.handlers.ClientEventHandler;
import com.jg.pirateguns.config.Config;
import com.jg.pirateguns.entities.Canon;
import com.jg.pirateguns.network.ConsumeItemMessage;
import com.jg.pirateguns.network.LoadBulletMessage;
import com.jg.pirateguns.network.PlaySoundMessage;
import com.jg.pirateguns.network.ShootCanonBallMessage;
import com.jg.pirateguns.network.ShootMessage;
import com.jg.pirateguns.proxy.ClientProxy;
import com.jg.pirateguns.proxy.IProxy;
import com.jg.pirateguns.proxy.ServerProxy;
import com.jg.pirateguns.registries.EntityRegistries;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.registries.SoundRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
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
	private static final CreativeModeTab tab = new CreativeModeTab("JG Pirate Guns") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.GUNPOWDER);
		}
	};
	
	public PirateGuns() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
	
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.server_config);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.client_config);
	
		// Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerAttributes);
        
        PirateGuns.proxy.registerModEventListeners(bus);
        
        //Register mod stuff (Items, Entities, Containers, etc.)
        SoundRegistries.SOUNDS.register(bus);
        ItemRegistries.ITEMS.register(bus);
        EntityRegistries.ENTITIES.register(bus);
        
        bus = MinecraftForge.EVENT_BUS;
        PirateGuns.proxy.registerForgeEventListeners(bus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		PirateGuns.channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), 
        		() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
		channel.registerMessage(packetsRegistered++, ShootCanonBallMessage.class, 
				ShootCanonBallMessage::encode, ShootCanonBallMessage::decode, 
				ShootCanonBallMessage::handle);
		channel.registerMessage(packetsRegistered++, LoadBulletMessage.class, 
				LoadBulletMessage::encode, LoadBulletMessage::decode, 
				LoadBulletMessage::handle);
		channel.registerMessage(packetsRegistered++, ShootMessage.class, 
				ShootMessage::encode, ShootMessage::decode, 
				ShootMessage::handle);
		channel.registerMessage(packetsRegistered++, PlaySoundMessage.class, 
				PlaySoundMessage::encode, PlaySoundMessage::decode, 
				PlaySoundMessage::handle);
		channel.registerMessage(packetsRegistered++, ConsumeItemMessage.class, 
				ConsumeItemMessage::encode, ConsumeItemMessage::decode, 
				ConsumeItemMessage::handle);
	}
	
	private void registerAttributes(EntityAttributeCreationEvent e) {
		e.put(EntityRegistries.CANON.get(), Canon.bakeAttributes());
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		ClientEventHandler.setup();
	}
	
	public static CreativeModeTab getTab() {
		return tab;
	}
	
}
