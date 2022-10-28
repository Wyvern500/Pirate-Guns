package com.jg.pirateguns.animations.gunmodels;

import java.util.List;

import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.animations.serializers.AnimationSerializer;
import com.jg.pirateguns.animations.serializers.KeyframeSerializer;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.utils.FileUtils;
import com.jg.pirateguns.utils.NBTUtils;
import com.jg.pirateguns.utils.Paths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PiratePistolGunModel extends GunModel {
	
	// Display
	
	// Right arm
	// Pos: -0.3, 0, -0.1
	// Rot: -0.6, 0, 0.6
	
	// Gun
	// Pos: 0.8, -0.6, -1.1
	// Rot: 0.1, 0, 0
	
	// Hammer
	// Up
	// Pos: 0.8, -0.6, -1.1
	// Rot: 0.1, 0, 0
	// Down
	// Pos: 0, -0.523, 0.6113
	// Rot: -90.3522, 0, 0
	
	// Aim -0.534f, 0.32f, 0, -0.069813f, 0, 0
	// Pos: -0.534, 0.32, 0
	// Rot: -0.069813, 0, 0
	
	// Sprint
	// Pos: 0, 0, 0.209
	// Rot: 0.575958, 0, 0
	
	private final ModelResourceLocation HAMMER = new ModelResourceLocation(Paths.PPHAMMER, "inventory");
	public Animation look;
	private AnimationSerializer animS;
	private KeyframeSerializer keyS;
	
	public PiratePistolGunModel(ClientHandler client) {
		super(new GunModelPart[] { 
				new GunModelPart("rightarm", -0.3f, 0, -0.1f, -0.6f, 0, 0.6f), 
				new GunModelPart("leftarm", 0, 0, 0, 0, 0, 0), 
				new GunModelPart("gun", 0.8f, -0.6f, -1.1f, 0.1f, 0, 0),
				new GunModelPart("hammer", 0.8f, -0.6f, -1.1f, 0.1f, 0, 0),
				new GunModelPart("all"),
				new GunModelPart("aim", -0.534f, 0.32f, 0, -0.069813f, 0, 0), 
				new GunModelPart("sprint", 0, 0, 0.209f, 0.575958f, 0, 0),
				new GunModelPart("recoil") }, ItemRegistries.PIRATEPISTOL.get(), client);
		
		keyS = new KeyframeSerializer();
		animS = new AnimationSerializer();
		
		look = new Animation(Paths.PP, "lookAnimation", new Keyframe(12)
				.addPos("gun", 0f, -0.33f, 0).addRot("gun", -0.226892f, 0, 0)
				.addPos("hammer", 0f, -0.33f, 0).addRot("hammer", -0.226892f, 0, 0)
				.addRot("rightarm", -0.226892f, 0, 0), new Keyframe(4)
				.addPos("gun", 0f, -0.13f, 0).addRot("gun", -0.026892f, 0, 0)
				.addPos("hammer", 0f, -0.13f, 0).addRot("hammer", -0.026892f, 0, 0)
				.addRot("rightarm", -0.026892f, 0, 0), new Keyframe(12)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0), new Keyframe(6)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0), new Keyframe(2)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0), new Keyframe(8)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0)) {
			
			public void onStart() {
				LogUtils.getLogger().info("On Start Animation");
			};
			
			public void onFinish() {
				LogUtils.getLogger().info("On Finish Animation");
				parts[1].getTransform().pos[0] = 0;
				parts[1].getTransform().pos[1] = 0;
				parts[1].getTransform().pos[2] = 0;
				parts[1].getTransform().rot[0] = 0;
				parts[1].getTransform().rot[1] = 0;
				parts[1].getTransform().rot[2] = 0;
			}
			
			public void onStartKeyframe() {
				LogUtils.getLogger().info("On Start Keyframe Animation");
			};
			
			public void onTick() {
				
			};
		};
	}
	
	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		matrix.pushPose();
		lerpTransform(matrix, client.getAimHandler().getProgress(), parts[5].getDTransform());
		lerpTransform(matrix, client.getSprintHandler().getProgress(), parts[6].getDTransform());
		lerpTransform(matrix, client.getRecoilHandler().getProgress(), parts[7].getTransform());// client.getRecoilHandler().getProgress()
		//LogUtils.getLogger().info("Progress: " + client.getRecoilHandler().getProgress());
		matrix.pushPose();
		matrix.pushPose();
		renderArm(player, buffer, matrix, light, HumanoidArm.LEFT, parts[1].getCombined());
		matrix.popPose();
		//translateAndRotate(parts[4].getCombined(), matrix);
		matrix.pushPose();
		renderArm(player, buffer, matrix, light, HumanoidArm.RIGHT, parts[0].getCombined());
		matrix.popPose();
		matrix.pushPose();
		renderItem(player, stack, buffer, matrix, light, parts[2].getCombined());
		matrix.popPose();
		matrix.pushPose();
		renderModel(player, stack, buffer, matrix, light, Minecraft.getInstance()
				.getModelManager().getModel(HAMMER), parts[3].getCombined());
		matrix.popPose();
		matrix.popPose();
		matrix.popPose();
	}
	
	@Override
	public GunModelPart getGunModelPart() {
		return parts[1];
	}

	@Override
	public void tick(Player player, ItemStack stack) {
		super.tick(player, stack);

		if(shouldUpdateAnimation) {
			look = AnimationSerializer.deserialize(FileUtils.readFile("pirate_gun/lookAnim.jg"));
			shouldUpdateAnimation = false;
		}
		/*new Animation(Paths.PP, "lookAnimation", new Keyframe(12)
				.addPos("gun", 0f, -0.33f, 0).addRot("gun", -0.226892f, 0, 0)
				.addPos("hammer", 0f, -0.33f, 0).addRot("hammer", -0.226892f, 0, 0)
				.addRot("rightarm", -0.226892f, 0, 0), new Keyframe(4)
				.addPos("gun", 0f, -0.13f, 0).addRot("gun", -0.026892f, 0, 0)
				.addPos("hammer", 0f, -0.13f, 0).addRot("hammer", -0.026892f, 0, 0)
				.addRot("rightarm", -0.026892f, 0, 0), new Keyframe(12)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0), new Keyframe(6)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0), new Keyframe(2)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0), new Keyframe(8)
				.addPos("gun", 0, 0, 0 ).addRot("gun", 0, 0, 0)
				.addPos("hammer", 0, 0, 0 ).addRot("hammer", 0, 0, 0)
				.addRot("rightarm", 0, 0, 0)) {
			
			public void onStart() {
				LogUtils.getLogger().info("On Start Animation");
			};
			
			public void onFinish() {
				LogUtils.getLogger().info("On Finish Animation");
				parts[1].getTransform().pos[0] = 0;
				parts[1].getTransform().pos[1] = 0;
				parts[1].getTransform().pos[2] = 0;
				parts[1].getTransform().rot[0] = 0;
				parts[1].getTransform().rot[1] = 0;
				parts[1].getTransform().rot[2] = 0;
			}
			
			public void onStartKeyframe() {
				LogUtils.getLogger().info("On Start Keyframe Animation");
			};
			
			public void onTick() {
				
			};
		};*/
		
		if(hasChanges) {
			if(NBTUtils.getLoaded(stack)) {
				parts[2].getDTransform().setPos(0.8f, -0.6f, -1.1f);
				parts[2].getDTransform().setRot(0.1f, 0f, 0f);
			} else {
				parts[2].getDTransform().setPos(0f, -0.523f, 0.6113f);
				parts[2].getDTransform().setRot(-1.576943f, 0f, 0f);
			}
			hasChanges = false;
		}
		/*if(getAnimation() != Animation.EMPTY) {
			Map<String, float[]> posi = getAnimation().getPos();
			if(!posi.isEmpty()) {
				for(Entry<String, float[]> entry : posi.entrySet()) {
					LogUtils.getLogger().info("Name: " + entry.getKey() + " Pos: x: " 
				+ entry.getValue()[0] + " y: " + entry.getValue()[1] + 
				" z: " + entry.getValue()[2]);
				}
			}
			Map<String, float[]> roti = getAnimation().getPos();
			if(!roti.isEmpty()) {
				for(Entry<String, float[]> entry : roti.entrySet()) {
					LogUtils.getLogger().info("Name: " + entry.getKey() + " Rot: rx: " 
				+ entry.getValue()[0] + " ry: " + entry.getValue()[1] + 
				" rz: " + entry.getValue()[2]);
				}
			}	
		}*/
	}

	@Override
	public void reload(Player player, ItemStack stack) {
		markChanges();
		setAnimation(look);
	}

	@Override
	public List<GunModelPart> getGunParts() {
		return List.of(parts[0], parts[1], parts[2], parts[3], parts[4]);
	}
	
}
