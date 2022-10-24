package com.jg.pirateguns.client.handlers;

import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.rendering.RenderHelper;
import com.jg.pirateguns.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Quaternion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ClientHandler {
	
	public static float partialTicks = 0.0f;
	
	private GunModel current;
	
	public int part;
	public boolean rotate;
	public boolean init;
	
	private SprintHandler sprint;
	private AimHandler aim;
	private RecoilHandler recoil;
	
	public ClientHandler() {
		sprint = new SprintHandler();
		aim = new AimHandler();
		recoil = new RecoilHandler();
	}
	
	// Methods
	
	public void tick() {
		recoil.tick();
	}
	
	public void shoot(Player player) {
		Utils.spawnParticlesOnPlayerView(player, 50, 0, 0, 0);
		current.shoot(player);
		recoil.setShoot();
		player.setXRot(player.getXRot()-(float)(Math.random() * 2));
		player.setYRot(player.getYRot()+(float)(Math.random() * 2));
	}
	
	// Rendering
	
	public void render(PoseStack matrix, MultiBufferSource buffer, int light) {
		LocalPlayer player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();
		if(current != null) {
			current.tick(player, stack);
			current.render(player, stack, buffer, matrix, light);
		}
	}
	
	public void left() {
		if(current != null) {
			part = (part - 1 + current.getParts().length) % current.getParts().length;
			GunModelPart gunPart = current.getParts()[part];
			if(gunPart != null) {
				LogUtils.getLogger().info("Left: " + part + " name: " + gunPart.getName());
			} else {
				LogUtils.getLogger().info("Left: " + part);
			}
		}
	}
	
	public void right() {
		if(current != null) {
			part = (part + 1) % current.getParts().length;
			GunModelPart gunPart = current.getParts()[part];
			if(gunPart != null) {
				LogUtils.getLogger().info("Right: " + part + " name: " + gunPart.getName());
			} else {
				LogUtils.getLogger().info("Right: " + part);
			}
		}
	}
	
	public void inc(int type) {
		float v = 0.01f;
		float vr = 1f;
		if(current != null) {
			if(!rotate) {
				GunModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					gunPart.getTransform().pos[type] += v;
					LogUtils.getLogger().info("Transform pos: x: " + current.getParts()[part].getTransform().pos[0] + " y: " + current.getParts()[part].getTransform().pos[1] + " z: " + current.getParts()[part].getTransform().pos[2]);
				}
			} else {
				GunModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					current.getParts()[part].getTransform().rot[type] += (float)Math.toRadians(vr);
					LogUtils.getLogger().info("Transform Rad x: " + current.getParts()[part].getTransform().rot[0]
							+ " y: " + 
							current.getParts()[part].getTransform().rot[1]
							+ " z: " 
							+ current.getParts()[part].getTransform().rot[2] + 
							" Deg x: " + 
							(float)Math.toDegrees(current.getParts()[part].getTransform().rot[0])
							+ " y: " + 
							(float)Math.toDegrees(current.getParts()[part].getTransform().rot[1]) 
							+ " z: " 
							+ (float)Math.toDegrees(current.getParts()[part].getTransform().rot[2]));
				}
			}
		}
	}
	
	public void dec(int type) {
		float v = 0.01f;
		float vr = 1f;
		if(current != null) {
			if(!rotate) {
				GunModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					current.getParts()[part].getTransform().pos[type] -= v;
					LogUtils.getLogger().info("Transform pos: x: " + current.getParts()[part].getTransform().pos[0] + " y: " + current.getParts()[part].getTransform().pos[1] + " z: " + current.getParts()[part].getTransform().pos[2]);
				}
			} else {
				GunModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					current.getParts()[part].getTransform().rot[type] -= (float)Math.toRadians(vr);
					LogUtils.getLogger().info("Transform Rad x: " + current.getParts()[part].getTransform().rot[0]
							+ " y: " + 
							current.getParts()[part].getTransform().rot[1]
							+ " z: " 
							+ current.getParts()[part].getTransform().rot[2] + 
							" Deg x: " + 
							(float)Math.toDegrees(current.getParts()[part].getTransform().rot[0])
							+ " y: " + 
							(float)Math.toDegrees(current.getParts()[part].getTransform().rot[1]) 
							+ " z: " 
							+ (float)Math.toDegrees(current.getParts()[part].getTransform().rot[2]));
				}
			}
		}
	}
	
	public void selectGunModel() {
		Item gun = Minecraft.getInstance().player.getMainHandItem().getItem();
		this.current = GunModelsHandler.get(gun);
	}
	
	public void switchRotationMode() {
		rotate = !rotate;
		LogUtils.getLogger().info("Rotate: " + rotate);
	}
	
	public void setGunModel(GunModel model) {
		this.current = model;
	}
	
	public GunModel getGunModel() {
		return current;
	}

	public SprintHandler getSprintHandler() {
		return sprint;
	}

	public AimHandler getAimHandler() {
		return aim;
	}
	
	public RecoilHandler getRecoilHandler(){
		return recoil;
	}
}
