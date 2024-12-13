package com.jg.jgpg.proxy;

import com.jg.jgpg.common.damagetypes.DamageType;
import com.jg.jgpg.common.item.JgItem;
import com.jg.jgpg.common.network.Networking;
import com.jg.jgpg.common.network.RequestPlayerDataSynchronizationMessage;
import com.jg.jgpg.server.CapabilitiesHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ServerProxy implements IProxy {

	@Override
	public void registerModEventListeners(IEventBus bus) {
		bus.addListener(CapabilitiesHandler::attachCapabilities);
	}

	@Override
	public void registerNeoForgeEventListeners(IEventBus bus) {
		bus.addListener(this::onPlayerAttack);
		bus.addListener(this::onTryToBreakBlock);
		bus.addListener(this::onStartTrackingPlayer);
	}

	private void onTryToBreakBlock(PlayerEvent.BreakSpeed e) {
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

	private void onPlayerAttack(LivingIncomingDamageEvent e) {
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

	private void onStartTrackingPlayer(PlayerEvent.StartTracking e) {
		Entity entity = e.getTarget();
		if(entity instanceof ServerPlayer player) {
			if(player.getMainHandItem().getItem() instanceof JgItem) {
				Networking.sendToPlayerClient(new RequestPlayerDataSynchronizationMessage(e.getEntity()
						.getUUID()), player);
			}
		}
	}

}
