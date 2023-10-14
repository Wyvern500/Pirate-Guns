package com.jg.jgpg.proxy;

import com.jg.jgpg.capabilities.IPlayerCapability;
import com.jg.jgpg.capabilities.PlayerCapability;
import com.jg.jgpg.capabilities.PlayerCapabilityImplementation;
import com.jg.jgpg.capabilities.PlayerCapabilityProvider;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkEvent.Context;

public class ServerProxy implements IProxy {

	@Override
	public void registerModEventListeners(IEventBus bus) {
		
	}

	@Override
	public void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(this::attachCapabilities);
		bus.addListener(this::onPlayerClone);
	}
	
	// Capabilites
	
	private void attachCapabilities(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof Player) {
			PlayerCapabilityProvider provider = new PlayerCapabilityProvider();

			e.addCapability(PlayerCapabilityProvider.ID, provider);
		}
	}

	private void onPlayerClone(PlayerEvent.Clone e) {
		if (e.isWasDeath()) {
			Player old = e.getOriginal();
			Player newPlayer = e.getEntity();

			IPlayerCapability oldCap = old.getCapability(PlayerCapability.INSTANCE)
					.orElseGet(() -> new PlayerCapabilityImplementation());
			newPlayer.getCapability(PlayerCapability.INSTANCE)
					.ifPresent(cap -> cap.deserializeNBT(oldCap.serializeNBT()));

			old.invalidateCaps();
		}
	}

	@Override
	public Player getPlayerFromContext(Context context) {
		return context.getSender();
	}

}
