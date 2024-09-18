package com.jg.jgpg.proxy;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.item.JgItem;
import com.jg.jgpg.network.SynchronizePlayerDataMessage;
import com.jg.jgpg.registries.DamageTypeRegistries;
import com.jg.jgpg.server.capabilities.IJgPlayerData;
import com.jg.jgpg.server.capabilities.JgPlayerDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

public class ServerProxy implements IProxy {

	@Override
	public void registerModEventListeners(IEventBus bus) {
		bus.addListener(this::registerCapabilities);
	}

	@Override
	public void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(this::onPlayerAttack);
		bus.addListener(this::onTryToBreakBlock);
		bus.addListener(this::onStartTrackingPlayer);
		bus.addGenericListener(Entity.class, this::attachCapabilities);
		bus.addListener(this::onPlayerClone);
	}

	private void registerCapabilities(RegisterCapabilitiesEvent e) {
		e.register(IJgPlayerData.class);
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

	private void onPlayerAttack(LivingAttackEvent e) {
		if(!e.getSource().is(DamageTypeRegistries.MELEE)) {
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
			player.getCapability(JgPlayerDataProvider.PLAYER_DATA).ifPresent(cap -> {
				PirateGuns.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)
						e.getEntity()), new SynchronizePlayerDataMessage(player.getUUID(),
						cap.getModel(),cap.getPose()));
			});
		}
	}

	private void attachCapabilities(AttachCapabilitiesEvent<Entity> e) {
		if(e.getObject() instanceof Player) {
			if(!e.getObject().getCapability(JgPlayerDataProvider.PLAYER_DATA).isPresent()) {
				e.addCapability(new ResourceLocation(PirateGuns.MODID, "player_data"),
						new JgPlayerDataProvider());
			}
		}
	}

	private void onPlayerClone(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			event.getOriginal().getCapability(JgPlayerDataProvider.PLAYER_DATA).ifPresent(
					oldStore -> {
						event.getOriginal().getCapability(JgPlayerDataProvider.PLAYER_DATA)
								.ifPresent(newStore -> {
									newStore.copyFrom(oldStore);
								});
					});
		}
	}

	@Override
	public Player getPlayerFromContext(Context context) {
		return context.getSender();
	}

}
