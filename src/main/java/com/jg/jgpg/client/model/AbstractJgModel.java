package com.jg.jgpg.client.model;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joml.Quaternionf;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.animations.Animator;
import com.jg.jgpg.client.animations.Keyframe;
import com.jg.jgpg.client.animations.KeyframeTransformData;
import com.jg.jgpg.client.animations.Transform;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.utils.LogUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractJgModel {

	protected TreeMap<String, JgModelPart> parts;
	protected Animator animator;
	protected ClientHandler handler;
	
	public AbstractJgModel(ClientHandler handler) {
		parts = new TreeMap<String, JgModelPart>();
		animator = new Animator();
		this.handler = handler;
		initParts();
	}
	
	public JgModelPart getPart(String part) {
		return parts.get(part);
	}
	
	public void addPart(JgModelPart part) {
		parts.put(part.name, part);
	}
	
	public void traslateRotate(String part, PoseStack matrix) {
		Transform combined = parts.get(part).getCombined();
		matrix.translate(combined.pos[0], combined.pos[1], combined.pos[2]);
		matrix.mulPose(new Quaternionf(combined.rot[0], combined.rot[1], combined.rot[2], 1.0f));
	}
	
	public void traslateRotate(JgModelPart part, PoseStack matrix) {
		Transform combined = part.getCombined();
		matrix.translate(combined.pos[0], combined.pos[1], combined.pos[2]);
		matrix.mulPose(new Quaternionf(combined.rot[0], combined.rot[1], combined.rot[2], 1.0f));
	}
	
	public void traslate(String part, PoseStack matrix) {
		traslate(parts.get(part), matrix);
	}
	
	public void rotate(String part, PoseStack matrix) {
		rotate(parts.get(part), matrix);
	}
	
	public void traslate(JgModelPart part, PoseStack matrix) {
		Transform combined = part.getCombined();
		matrix.translate(combined.pos[0], combined.pos[1], combined.pos[2]);
	}
	
	public void rotate(JgModelPart part, PoseStack matrix) {
		Transform combined = part.getCombined();
		matrix.mulPose(new Quaternionf(combined.rot[0], combined.rot[1], combined.rot[2], 1.0f));
	}
	
	public void setupModelPartsForAnimation(Keyframe first) {
		// Setting transforms to the first keyframe when setting an animation
		for(Entry<JgModelPart, KeyframeTransformData> e : first.getTraslations().entrySet()) {
			/*LogUtils.log("AbstractJgModel", "before " + " part: " + e.getKey().getName() + " x: " + e.getKey()
				.getTransform().pos[0] + " y: " + e.getKey().getTransform().pos[1]);*/
			e.getKey().getTransform().pos[0] = e.getValue().getVal()[0];
			e.getKey().getTransform().pos[1] = e.getValue().getVal()[1];
			//LogUtils.log("AbstractJgModel", "x: " + e.getValue()[0] + " y: " + e.getValue()[1]);
			//e.getKey().getTransform().pos[2] = e.getValue()[2];
			/*LogUtils.log("AbstractJgModel", "after " + " part: " + e.getKey().getName() + " x: " + e.getKey()
			.getTransform().pos[0] + " y: " + e.getKey().getTransform().pos[1]);*/
		}
		// Setting transforms to the first keyframe when setting an animation
		for(Entry<JgModelPart, KeyframeTransformData> e : first.getRotations().entrySet()) {
			e.getKey().getTransform().rot[0] = e.getValue().getVal()[0];
			e.getKey().getTransform().rot[1] = e.getValue().getVal()[1];
			//e.getKey().getTransform().rot[2] = e.getValue()[2];
		}
		// This is becuase if the first kf doesnt have transform data for a part, then i have to create it
		// So it wont start in a strange position
		for(Entry<String, JgModelPart> e : parts.entrySet()) {
			if(!first.getTraslations().containsKey(e.getValue())) {
				e.getValue().getTransform().pos[0] = 0;
				e.getValue().getTransform().pos[1] = 0;
			}
			if(!first.getRotations().containsKey(e.getValue())) {
				e.getValue().getTransform().rot[0] = 0;
				e.getValue().getTransform().rot[1] = 0;
			}
		}
	}
	
	public abstract void initParts();
	
	public abstract void tick();
	
	public abstract void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, 
			PoseStack matrix, int light);
	
	public abstract void onAnimationEnd(Animation anim);
	
	public abstract void onAnimationStart(Animation anim);
	
	public abstract void onAnimationTick(Animation anim, float prevTick, float tick);
	
	public abstract List<Animation> getAnimations();
	
	public TreeMap<String, JgModelPart> getParts() {
		return parts;
	}

	public Animator getAnimator() {
		return animator;
	}

	public ClientHandler getHandler() {
		return handler;
	}
	
}
