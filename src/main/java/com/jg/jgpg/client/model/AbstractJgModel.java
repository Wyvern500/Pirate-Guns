package com.jg.jgpg.client.model;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joml.Quaternionf;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.animations.Animator;
import com.jg.jgpg.client.animations.Keyframe;
import com.jg.jgpg.client.animations.Transform;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.utils.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractJgModel {

	protected TreeMap<String, JgModelPart> parts;
	protected Animator animator;
	protected ClientHandler client;
	protected boolean prevShouldUpdate;
	protected boolean shouldUpdate;
	
	public AbstractJgModel(ClientHandler client) {
		parts = new TreeMap<String, JgModelPart>();
		animator = new Animator();
		this.client = client;
		prevShouldUpdate = false;
		shouldUpdate = true;
		initParts();
		initAnimations();
	}
	
	public JgModelPart getPart(String part) {
		return parts.get(part);
	}
	
	public void addPart(JgModelPart part) {
		parts.put(part.name, part);
	}
	
	// Matrix stuff
	
	public void lerpToTraslation(PoseStack matrix, float x, float y, float z, float prog) {
		lerpToTraslation(matrix, x, y, z, prog, "empty");
	}
	
	public void lerpToTraslation(PoseStack matrix, float x, float y, float z, float prog, String easing) {
		float p = EasingHandler.INSTANCE.getEasing(easing).get(prog);
		float xt = MathUtils.lerp(0, x, p);
		float yt = MathUtils.lerp(0, y, p);
		float zt = MathUtils.lerp(0, z, p);
		
		matrix.translate(xt, yt, zt);
	}
	
	public void lerpToRotation(PoseStack matrix, float rx, float ry, float rz, float prog) {
		lerpToRotation(matrix, rx, ry, rz, prog, "empty");
	}
	
	public void lerpToRotation(PoseStack matrix, float rx, float ry, float rz, float prog, String easing) {
		float p = EasingHandler.INSTANCE.getEasing(easing).get(prog);
		float xr = MathUtils.rotLerp(0, rx, p);
		float yr = MathUtils.rotLerp(0, ry, p);
		float zr = MathUtils.rotLerp(0, rz, p);
		
		matrix.mulPose(new Quaternionf(xr, yr, zr, 1.0f));
	}
	
	public void lerpTo(PoseStack matrix, float x, float y, float z, float rx, float ry, float rz, float prog) {
		lerpTo(matrix, x, y, z, rx, ry, rz, prog, "empty");
	}
	
	public void lerpTo(PoseStack matrix, float x, float y, float z, float rx, float ry, float rz, float prog, 
			String easing) {
		float p = EasingHandler.INSTANCE.getEasing(easing).get(prog);
		float xt = MathUtils.lerp(0, x, p);
		float yt = MathUtils.lerp(0, y, p);
		float zt = MathUtils.lerp(0, z, p);
		float xr = MathUtils.rotLerp(0, rx, p);
		float yr = MathUtils.rotLerp(0, ry, p);
		float zr = MathUtils.rotLerp(0, rz, p);
		
		matrix.mulPose(new Quaternionf(xr, yr, zr, 1.0f));
		matrix.translate(xt, yt, zt);
	}
	
	public void traslateRotate(String part, PoseStack matrix) {
		Transform transform = parts.get(part).getCombined();
		matrix.translate(transform.pos[0], transform.pos[1], transform.pos[2]);
		matrix.mulPose(new Quaternionf().rotationXYZ(transform.rot[0], transform.rot[1], transform.rot[2]));
	}
	
	public void traslateRotateD(String part, PoseStack matrix) {
		Transform transform = parts.get(part).getDtransform();
		matrix.translate(transform.pos[0], transform.pos[1], transform.pos[2]);
		matrix.mulPose(new Quaternionf().rotationXYZ(transform.rot[0], transform.rot[1], transform.rot[2]));
	}
	
	public void lerpToTransform(String part, PoseStack matrix, float prog) {
		Transform combined = parts.get(part).getCombined();
		matrix.translate(MathUtils.lerp(0, combined.pos[0], prog), MathUtils.lerp(0, combined.pos[1], prog), 
				MathUtils.lerp(0, combined.pos[2], prog));
		matrix.mulPose(new Quaternionf().rotationXYZ(MathUtils.lerp(0, combined.rot[0], prog), 
				MathUtils.lerp(0, combined.rot[1], prog), MathUtils.lerp(0, combined.rot[2], prog)));
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
	
	// Model stuff
	
	public void setupModelPartsForAnimation(Keyframe first) {
		if(first != null) {
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
	}
	
	public abstract void initParts();
	
	public abstract void initAnimations();
	
	public abstract void tick();
	
	public abstract void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, 
			PoseStack matrix, int light);
	
	public abstract void renderDefault(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, 
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

	public ClientHandler getClient() {
		return client;
	}
	
	public boolean shouldUpdate() { 
		return shouldUpdate;
	}
	
	public void setShouldUpdate(boolean shouldUpdate) {
		this.shouldUpdate = shouldUpdate;
	}
	
	public boolean getPrevShouldUpdate() { 
		return prevShouldUpdate;
	}
	
	public void setPrevShouldUpdate(boolean prevShouldUpdate) {
		this.prevShouldUpdate = prevShouldUpdate;
	}
	
}
