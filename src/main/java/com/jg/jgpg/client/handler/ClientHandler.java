package com.jg.jgpg.client.handler;

import java.util.ArrayList;
import java.util.List;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handlers.AimHandler;
import com.jg.jgpg.client.handlers.AnimationDataHandler;
import com.jg.jgpg.client.handlers.GunModelsHandler;
import com.jg.jgpg.client.handlers.HitmarkerHandler;
import com.jg.jgpg.client.handlers.ShootHandler;
import com.jg.jgpg.client.handlers.SprintHandler;
import com.jg.jgpg.client.model.AbstractGunModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.render.renderers.JgPlayerRenderer;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.NBTUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientHandler {

	AnimationDataHandler animationDataHandler;
	AimHandler aim;
	ShootHandler shoot;
	HitmarkerHandler hitmarker;
	SprintHandler sprint;
	JgPlayerRenderer playerRenderer;
	
	AbstractGunModel model;
	List<JgModelPart> parts;
	
	private int index;
	private String prevId;
	
	private boolean renderDefault;
	
	public static float partialTicks = 0.0f;
	
	public ClientHandler() {
		animationDataHandler = new AnimationDataHandler();
		aim = new AimHandler(this);
		shoot = new ShootHandler(this);
		sprint = new SprintHandler(this);
		hitmarker = new HitmarkerHandler();
		parts = new ArrayList<>();
		prevId = "";
	}
	
	public void initPlayerRenderer() {
		playerRenderer = new JgPlayerRenderer(this);
	}
	
	public void render(LocalPlayer player, ItemStack stack, PoseStack matrix, MultiBufferSource buffer, 
			int packedLight) {
		if(model != null) {
			model.render(player, stack, buffer, matrix, packedLight);
		}
	}
	
	public void renderPlayer(PoseStack matrix, MultiBufferSource bufferSource, float partialTicks, 
			int packedLight) {
		if(playerRenderer != null) {
			playerRenderer.render(Minecraft.getInstance().player, 0, partialTicks, matrix, 
					bufferSource, packedLight);
		}
	}
	
	public void tick(LocalPlayer player, ItemStack stack, String stackId) {
		if(!prevId.equals(stackId)) {
			AbstractGunModel model = GunModelsHandler.get(ForgeRegistries.ITEMS.getKey(stack.getItem())
					.toString());
			if(model != null) {
				setModel(model);
				//LogUtils.log("ClientHandler", "Picking a new Model");
			}
		}
		if(model != null) {
			aim.tick(player);
			model.tick();
			hitmarker.tick();
			sprint.tick(player);
		} else {
			AbstractGunModel model = GunModelsHandler.get(ForgeRegistries.ITEMS.getKey(stack.getItem())
					.toString());
			if(model != null) {
				setModel(model);
			}
		}
		prevId = stackId;
	}
	
	public void handleMouse(InputEvent.MouseButton e) {
		aim.handleMouse(e);
		shoot.handleMouse(e);
	}
	
	public static void registerItemProperties() {
		ItemProperties.register(ItemRegistries.PIRATERIFLE.get(), 
			      new ResourceLocation(PirateGuns.MODID, "has_scope"), (stack, level, living, id) -> {
			    	  return NBTUtils.hasScope(stack) ? 0.0f : 1.0f;
		});
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
	
	public AnimationDataHandler getAnimationDataHandler() {
		return animationDataHandler;
	}
	
	public AimHandler getAimHandler() {
		return aim;
	}
	
	public SprintHandler getSprintHandler() {
		return sprint;
	}
	
	public HitmarkerHandler getHitmarker() {
		return hitmarker;
	}
	
	public ShootHandler getShoothandler() {
		return shoot;
	}
	
	public boolean shouldRenderDefault() {
		return renderDefault;
	}
	
	public void switchRenderDefault() {
		renderDefault = !renderDefault;
		LogUtils.log("ClientHandler", "renderDefault: " + renderDefault);
	}
	
	public void setModel(AbstractGunModel model) {
		this.model = model;
		index = MathUtils.clamp(index, 0, model.getParts().values().size() - 1);
		parts = new ArrayList<>(model.getParts().values());
	}
	
	public AbstractGunModel getModel() {
		return model;
	}
	
}
