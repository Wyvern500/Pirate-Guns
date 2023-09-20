package com.jg.jgpg.client.model.models;

import java.util.List;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public class TrabucoModel extends AbstractJgModel {

	public static Animation TEST = new Animation("Test");
	
	public TrabucoModel(ClientHandler handler) {
		super(handler);
	}

	@Override
	public void initParts() {
		
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		
	}

	@Override
	public void onAnimationEnd(Animation anim) {
		
	}

	@Override
	public void onAnimationStart(Animation anim) {
		
	}

	@Override
	public void onAnimationTick(Animation anim, float prevTick, float tick) {
		
	}

	@Override
	public List<Animation> getAnimations() {
		return List.of(TEST);
	}

}
