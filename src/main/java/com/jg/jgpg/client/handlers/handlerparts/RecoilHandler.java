package com.jg.jgpg.client.handlers.handlerparts;

import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handler.AbstractClientHandlerPart;
import com.jg.jgpg.client.handlers.PirateGunsClientHandler;
import com.jg.jgpg.item.JgGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RecoilHandler extends AbstractClientHandlerPart<PirateGunsClientHandler> {
	
	Map<JgGunItem, RecoilData> recoils;
	Map<JgGunItem, Float> recoilRecoveries;
	float recoilRecoveryTimer;
	float oldRecoilRecoveryTimer;
	
	public RecoilHandler(AbstractClientHandler client) {
		super(client);
		recoils = new HashMap<>();
		recoilRecoveries = new HashMap<>();
	}
	
	@Override
	public void onTickPre(LocalPlayer player, ItemStack stack, String stackId) {
		
	}

	@Override
	public void onTickPost(LocalPlayer player, ItemStack stack, String stackId) {
		float multiplier = 0.2f;
		// Recoils
		if(!recoils.isEmpty()) {
			Iterator<Entry<JgGunItem, RecoilData>> iterator = recoils.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<JgGunItem, RecoilData> entry = iterator.next();
				RecoilData data = entry.getValue();

				if(!Minecraft.getInstance().options.keyAttack.isDown()) {
					if(data.timer > 0) {
						data.timer -= Minecraft.getInstance().getDeltaFrameTime() * multiplier;
					} else {
						iterator.remove();
					}
				}
			}
		}

		// Recoil Recoveries
		if(!recoilRecoveries.isEmpty()) {
			Iterator<Entry<JgGunItem, Float>> iterator = recoilRecoveries.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<JgGunItem, Float> entry = iterator.next();
				float time = entry.getValue();
				if (recoilRecoveryTimer >= time) {
					iterator.remove();
					recoilRecoveryTimer = 0;
				} else {
					oldRecoilRecoveryTimer = recoilRecoveryTimer;
					recoilRecoveryTimer += Minecraft.getInstance().getDeltaFrameTime()
							* multiplier;
					if (recoilRecoveryTimer > time) {
						recoilRecoveryTimer = time;
					}
				}
			}
		}
	}

	@Override
	public float getProgress(Item item) {
		return 0;
	}

	@Override
	public boolean shouldWork(ItemStack stack, LocalPlayer player) {
		return stack.getItem() instanceof JgGunItem;
	}

	public void pushRecoil(JgGunItem gun, ItemStack stack) {
		recoils.putIfAbsent(gun, new RecoilData(gun.getRecoilTime(stack)));
		RecoilData data = recoils.get(gun);
		data.timer += (gun.getRecoilTime(stack) * gun.getRecoilWeight(stack))
				* Minecraft.getInstance().getDeltaFrameTime() * 0.2f;
		if (data.timer > data.time) {
			data.timer = data.time;
		}
	}
	
	public void addRecoil(JgGunItem gun, float time) {
		if(!recoils.containsKey(gun)) {
			recoils.put(gun, new RecoilData(time));
		}
	}
	
	public float getProgress(JgGunItem gun) {
		if(recoils.containsKey(gun)) {
			RecoilData data = recoils.get(gun);
			float progress = data.timer / data.time;
			return progress;
		}
		return 0;
	}
	
	public static class RecoilData {
		
		public float time;
		public float timer;
		
		public RecoilData(float time) {
			this.time = time;
		}
		
	}
	
}
