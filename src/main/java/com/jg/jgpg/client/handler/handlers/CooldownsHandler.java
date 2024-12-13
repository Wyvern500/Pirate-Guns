package com.jg.jgpg.client.handler.handlers;

import com.google.common.collect.Maps;
import com.jg.jgpg.client.handler.AbstractClientHandlerPart;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.common.item.JgGunItem;
import com.jg.jgpg.common.item.JgItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.Map;

public class CooldownsHandler extends AbstractClientHandlerPart<ClientHandler> {
	
	private final Map<JgGunItem, CooldownInstance> cooldowns = Maps.newHashMap();
	private float tickCount;

	public CooldownsHandler(ClientHandler client) {
		super(client);
	}
	
	@Override
	public void onTickPre(LocalPlayer player, ItemStack stack, String stackId) {
		
	}

	@Override
	public void onTickPost(LocalPlayer player, ItemStack stack, String stackId) {
		float multiplier = 0.2f;
		this.tickCount += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()
				* multiplier;

		if (!this.cooldowns.isEmpty()) {
			Iterator<Map.Entry<JgGunItem, CooldownInstance>> iterator = this.cooldowns.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<JgGunItem, CooldownInstance> entry = iterator.next();
				if ((entry.getValue()).endTime <= this.tickCount) {
					iterator.remove();
					this.onCooldownEnded(entry.getKey());
				}
			}
		}
		
	}

	@Override
	public float getProgress(Item item) {
		return getCooldownPercent(item, Minecraft.getInstance().getTimer()
				.getGameTimeDeltaTicks());
	}

	@Override
	public boolean shouldWork(ItemStack stack, LocalPlayer player) {
		return stack.getItem() instanceof JgItem;
	}

	public boolean isOnCooldown(Item p_41520_) {
		return this.getCooldownPercent(p_41520_, 0.0F) > 0.0F;
	}

	public float getCooldownPercent(Item id, float p_41523_) {
		CooldownInstance itemcooldowns$cooldowninstance = this.cooldowns.get(id);
		if (itemcooldowns$cooldowninstance != null) {
			float f = itemcooldowns$cooldowninstance.endTime - itemcooldowns$cooldowninstance
					.startTime;
			float f1 = itemcooldowns$cooldowninstance.endTime - (this.tickCount + p_41523_);
			return Mth.clamp(f1 / f, 0.0F, 1.0F);
		} else {
			return 0.0F;
		}
	}

	public void addCooldown(JgGunItem id, int p_41526_) {
		this.cooldowns.put(id, new CooldownInstance(this.tickCount, this.tickCount + p_41526_));
		this.onCooldownStarted(id, p_41526_);
	}

	public void removeCooldown(JgGunItem id) {
		this.cooldowns.remove(id);
		this.onCooldownEnded(id);
	}

	protected void onCooldownStarted(JgGunItem id, int duration) {
		
	}

	protected void onCooldownEnded(JgGunItem id) {
		JgModel<?> model = parent.getModel();
		if(model != null) {
			parent.getModel().cooldownFinished(Minecraft.getInstance().player);
		}
	}

	record CooldownInstance(float startTime, float endTime) {}
	
}
