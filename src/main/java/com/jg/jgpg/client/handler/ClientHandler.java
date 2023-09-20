package com.jg.jgpg.client.handler;

import java.util.ArrayList;
import java.util.List;

import com.jg.jgpg.client.handlers.AnimationDataHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.utils.LogUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ClientHandler {

	EasingHandler easingHandler;
	AnimationDataHandler animationDataHandler;
	
	AbstractJgModel model;
	List<JgModelPart> parts;
	
	private int index;
	
	public static float partialTicks = 0.0f;
	
	public ClientHandler() {
		easingHandler = new EasingHandler();
		animationDataHandler = new AnimationDataHandler();
		parts = new ArrayList<>();
	}

	public void render(LocalPlayer player, ItemStack stack, PoseStack matrix, MultiBufferSource buffer, 
			int packedLight) {
		if(model != null) {
			model.render(player, stack, buffer, matrix, packedLight);
		}
	}
	
	public void tick() {
		if(model != null) {
			model.tick();
		}
	}
	
	// Model Manipulation
	
	public void nextPartIndex() {
		index++;
		if(index >= parts.size()) {
			index = 0;
		}
		LogUtils.log("ClientHandler", "Index: " + index + " part: " + parts.get(index).getName());
	}
	
	public void prevPartIndex() {
		index--;
		if(index <= -1) {
			index = parts.size() - 1;
		}
		LogUtils.log("ClientHandler", "Index: " + index + " part: " + parts.get(index).getName());
	}
	
	public void addToModel(float x, float y, float z, boolean isRot, boolean display) {
		if(display) {
			if(isRot) {
				parts.get(index).getDtransform().add(x, y, z, true);
			} else {
				parts.get(index).getDtransform().add(x, y, z, false);
			}
		} else {
			if(isRot) {
				parts.get(index).getTransform().add(x, y, z, true);
			} else {
				parts.get(index).getTransform().add(x, y, z, false);
			}
		}
	}
	
	// Getters and setters
	
	public EasingHandler getEasingHandler() {
		return easingHandler;
	}
	
	public AnimationDataHandler getAnimationDataHandler() {
		return animationDataHandler;
	}
	
	public void setModel(AbstractJgModel model) {
		this.model = model;
		parts = new ArrayList<>(model.getParts().values());
	}
	
	public AbstractJgModel getModel() {
		return model;
	}
	
}
