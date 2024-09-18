package com.jg.jgpg.client.jgmodel.itemmodel;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Animator;
import com.jg.jgpg.client.animation.Transform;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoseHandler;
import com.jg.jgpg.utils.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.joml.Quaternionf;

import java.lang.reflect.Field;
import java.util.List;
import java.util.TreeMap;

public abstract class JgModel<T extends AbstractClientHandler> {

	protected Item item;
	protected T client;
	protected TreeMap<String, JgModelPart> parts;
	protected Animator animator;
	protected boolean shouldQuitAnimation;
	
	public JgModel(AbstractClientHandler client) {
		this.client = (T) client;
		parts = new TreeMap<>();
		animator = new Animator(this);
	}
	
	public void tick() {
		animator.tick();
	}
	
	protected void addPart(String name, float x, float y, float z, float rx, float ry, float rz) {
		parts.put(name, new JgModelPart(name, x, y, z, rx, ry, rz));
	}
	
	protected void addPart(JgModelPart part) {
		parts.put(part.name, part);
	}
	
	protected JgModelPart getPart(String name) {
		return parts.get(name);
	}

	// Matrix methods

	public void breath(PoseStack matrix, float scalar) {
		float bp = client.getClient().getBreathingHandler().getProgress(null);
		matrix.translate(0, Math.sin(bp) * scalar, 0);
	}
	
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

	public void traslateRotate(String part, String around, PoseStack matrix) {
		Transform transform = parts.get(part).getCombined();
		Transform aroundPart = parts.get(around).getCombined();
		matrix.translate(transform.pos[0], transform.pos[1], transform.pos[2]);
		matrix.rotateAround(new Quaternionf().rotationXYZ(transform.rot[0], transform.rot[1],
				transform.rot[2]), aroundPart.pos[0], aroundPart.pos[1], aroundPart.pos[2]);
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
		Transform combined = parts.get(part).getCombined();
		matrix.translate(combined.pos[0], combined.pos[1], combined.pos[2]);
	}

	public void rotate(String part, PoseStack matrix) {
		Transform combined = parts.get(part).getCombined();
		matrix.mulPose(new Quaternionf(combined.rot[0], combined.rot[1], combined.rot[2], 1.0f));
	}

	public void traslate(JgModelPart part, PoseStack matrix) {
		Transform combined = part.getCombined();
		matrix.translate(combined.pos[0], combined.pos[1], combined.pos[2]);
	}

	public void rotate(JgModelPart part, PoseStack matrix) {
		Transform combined = part.getCombined();
		matrix.mulPose(new Quaternionf(combined.rot[0], combined.rot[1], combined.rot[2], 1.0f));
	}
	
	protected void resetToZero() {
		for(JgModelPart part : getParts().values()) {
			part.getTransform().set(0, 0, 0, 0, 0, 0);
		}
	}
	
	// Misc
	
	public void quitAnimation() {
		shouldQuitAnimation = true;
	}
	
	public void resetQuitAnimation() {
		shouldQuitAnimation = false;
		initAnimations();
		resetToZero();
	}
	
	public boolean shouldQuitAnimation() {
		return shouldQuitAnimation;
	}
	
	public void reset() {
		shouldQuitAnimation = false;
	}
	
	public boolean isRepTick(int start, float target, float tick, int cycleDur, int times) {
		float t = tick - start;
		int current = (int) Math.floor(t / cycleDur);
		target = target - start;
		return tick > start && current < times && t - (current * cycleDur) == target && tick < start 
				+ (cycleDur * times);
	}

	public void modifyModelByPose(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount,
						 float pAgeInTicks, float pNetHeadYaw, float pHeadPitch,
						 float attackTime, float swimAmount, boolean crouching, boolean riding,
						 JgPlayerPose pose, JgHumanoidModel<?> model) {
		JgPlayerPoseHandler poseHandler = getPlayerPoseHandler();
		if(poseHandler != null) {
			poseHandler.handlePlayerPose(pEntity, pLimbSwing, pLimbSwingAmount,
					pAgeInTicks, pNetHeadYaw, pHeadPitch, attackTime, swimAmount, crouching,
					riding, pose, model);
		}
	}

	public void onSetupPlayerModel(LivingEntity entity, JgHumanoidModel<?> model,
				float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks,
				 float pNetHeadYaw, float pHeadPitch) {

	}

	public void onReset() {
		
	}

	public void processNbt(CompoundTag nbt) {

	}

	public void dispose() {
		parts.clear();
		makeAnimationsNull();
	}

	protected void makeAnimationsNull() {
		Field[] fields = getClass().getDeclaredFields();

		for(Field field : fields) {
            try {
				field.setAccessible(true);
                Object object = field.get(this);
				if(object instanceof Animation) {
					field.set(this, null);
				}
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
	}

	public void onPick() {
		initParts();
		initAnimations();
	}

	public boolean animationIsNull() {
		return animator.getAnimation() == null;
	}
	
	public void onSetModel(Player player, ItemStack stack, String stackId) {
		
	}

	public void afterUse(Player player) {

	}
	
	public abstract ToShowModModel getModModel(BakedModel origin);
	
	public abstract void initParts();
	
	public abstract void initAnimations();

	public abstract boolean canUse(Player player);

	public abstract void onUse(Player player, ItemStack stack);

	public abstract void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, 
			BufferSource impl, PoseStack matrix, int light);
	
	public abstract void renderOverlayPre(RenderGuiOverlayEvent.Pre e, LocalPlayer player, 
			Font font);
	
	public abstract void onAnimationEnd(Animation anim);
	
	public abstract void onAnimationStart(Animation anim);
	
	public abstract void onAnimationTick(Animation anim, float prevTick, float tick);

	public abstract void cooldownFinished(Player player);

	public abstract JgPlayerPoseHandler getPlayerPoseHandler();

	public abstract JgPlayerPose getPose(Player player);

	public abstract List<Animation> getAnimations();

	public int getAnimationIndex(Animation animation) {
		return getAnimations().indexOf(animation);
	}

	public Animation getAnimationByName(String name) {
		return getAnimations().stream().filter(anim -> anim.getName().equals(name))
				.findFirst().orElse(null);
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public TreeMap<String, JgModelPart> getParts() {
		return parts;
	}

	public Animator getAnimator() {
		return animator;
	}
	
}
