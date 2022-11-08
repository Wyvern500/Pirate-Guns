package com.jg.pirateguns.animations.gunmodels;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.Animator;
import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.animations.serializers.AnimationSerializer;
import com.jg.pirateguns.animations.serializers.KeyframeSerializer;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.handlers.SoundHandler;
import com.jg.pirateguns.network.LoadBulletMessage;
import com.jg.pirateguns.network.ShootMessage;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.registries.SoundRegistries;
import com.jg.pirateguns.utils.FileUtils;
import com.jg.pirateguns.utils.NBTUtils;
import com.jg.pirateguns.utils.Paths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Quaternion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PiratePistolGunModel extends GunModel {

	private final ModelResourceLocation HAMMER = new ModelResourceLocation(Paths.PPHAMMER, "inventory");
	public Animation look;
	public Animation reload;
	
	private static String LOOK = "pirate_gun/lookAnim.jg";
	private static String RELOAD = "pirate_gun/reloadAnim.jg";
	
	public PiratePistolGunModel(ClientHandler client) {
		super(new GunModelPart[] { 
				new GunModelPart("rightarm", -0.3f, 0, -0.1f, -0.6f, 0, 0.6f), 
				new GunModelPart("leftarm", -1.12f, -2.4f, 0, 0.034906f, -1.117001f, 0), 
				new GunModelPart("gun", 0.8f, -0.6f, -1.1f, 0.1f, 0, 0),
				new GunModelPart("hammer", 0.8f, -0.6f, -1.1f, 0.1f, 0, 0),
				new GunModelPart("all"),
				new GunModelPart("aim", -0.534f, 0.32f, 0, -0.069813f, 0, 0), 
				new GunModelPart("sprint", 0, 0, 0.209f, 0.575958f, 0, 0),
				new GunModelPart("recoil", 0.02f, -0.18f, 0.02f, 0.139626f, -0.069813f, 0) }, ItemRegistries.PIRATEPISTOL.get(), 
				client, new String[] { LOOK, RELOAD });
		
		look = new Animation("pirate_gun/lookAnim.jg", "jgpg:pirate_pistol")
				 .startKeyframe(4)
				 .translate(parts[4], 0.0f, 0.0f, 0.0f)
				 .translate(parts[1], 0.0f, 0.0f, 0.0f)
				 .translate(parts[3], -0.40999988f, -0.20000002f, -0.3899999f)
				 .translate(parts[0], 0.7599996f, 0.0f, 0.0f)
				 .translate(parts[2], -0.40999988f, -0.20000002f, -0.3899999f)
				 .rotate(parts[4], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[1], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[3], 0.087266445f, 0.57595867f, -0.2792527f)
				 .rotate(parts[0], 0.0f, 0.8552117f, 0.0f)
				 .rotate(parts[2], 0.087266445f, 0.57595867f, -0.2792527f)
				 .startKeyframe(16)
				 .translate(parts[4], 0.0f, 0.0f, 0.0f)
				 .translate(parts[1], 0.0f, 0.0f, 0.0f)
				 .translate(parts[3], -0.40999988f, -0.20000002f, -0.3899999f)
				 .translate(parts[0], 0.7599996f, 0.0f, 0.0f)
				 .translate(parts[2], -0.40999988f, -0.20000002f, -0.3899999f)
				 .rotate(parts[4], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[1], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[3], 0.087266445f, 0.57595867f, -0.2792527f)
				 .rotate(parts[0], 0.0f, 0.8552117f, 0.0f)
				 .rotate(parts[2], 0.087266445f, 0.57595867f, -0.2792527f)
				 .startKeyframe(4)
				 .translate(parts[4], 0.0f, 0.0f, 0.0f)
				 .translate(parts[1], 0.0f, 0.0f, 0.0f)
				 .translate(parts[3], -0.5299998f, -0.049999997f, -0.44999987f)
				 .translate(parts[0], 0.7399996f, -0.5099998f, 0.21000002f)
				 .translate(parts[2], -0.5299998f, -0.049999997f, -0.44999987f)
				 .rotate(parts[4], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[1], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[3], 0.034906585f, 0.8552117f, 0.4537855f)
				 .rotate(parts[0], -0.017453311f, 0.7330385f, 0.7330385f)
				 .rotate(parts[2], 0.034906585f, 0.8552117f, 0.4537855f)
				 .startKeyframe(16)
				 .translate(parts[4], 0.0f, 0.0f, 0.0f)
				 .translate(parts[1], 0.0f, 0.0f, 0.0f)
				 .translate(parts[3], -0.5299998f, -0.049999997f, -0.44999987f)
				 .translate(parts[0], 0.7399996f, -0.5099998f, 0.21000002f)
				 .translate(parts[2], -0.5299998f, -0.049999997f, -0.44999987f)
				 .rotate(parts[4], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[1], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[3], 0.034906585f, 0.8552117f, 0.4537855f)
				 .rotate(parts[0], -0.017453311f, 0.7330385f, 0.7330385f)
				 .rotate(parts[2], 0.034906585f, 0.8552117f, 0.4537855f)
				 .startKeyframe(8)
				 .translate(parts[4], 0.0f, 0.0f, 0.0f)
				 .translate(parts[1], 0.0f, 0.0f, 0.0f)
				 .translate(parts[3], 0.0f, 0.0f, 0.0f)
				 .translate(parts[0], 0.0f, 0.0f, 0.0f)
				 .translate(parts[2], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[4], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[1], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[3], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[0], 0.0f, 0.0f, 0.0f)
				 .rotate(parts[2], 0.0f, 0.0f, 0.0f)
				 .startKeyframe(8)
				 .end();
		reload = new Animation("pirate_gun/reloadAnim.jg", "jgpg:pirate_pistol")
				.startKeyframe(8)
				.translate(parts[1], 0.73999953f, 1.9199994f, -0.8299995f)
				.translate(parts[2], -0.13999999f, -0.20000002f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], -0.13999999f, -0.20000002f, 0.0f)
				.translate(parts[0], -0.16f, 0.17999999f, -0.40999988f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], -0.45378554f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], -0.45378554f, 0.0f, 0.0f)
				.rotate(parts[0], -0.45378554f, 0.0f, 0.0f)
				.startKeyframe(14)
				.translate(parts[1], 0.73999953f, 1.9199994f, -0.8299995f)
				.translate(parts[2], -0.13999999f, -0.20000002f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], -0.13999999f, -0.20000002f, 0.0f)
				.translate(parts[0], -0.16f, 0.17999999f, -0.40999988f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], -0.45378554f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], -0.45378554f, 0.0f, 0.0f)
				.rotate(parts[0], -0.45378554f, 0.0f, 0.0f)
				.startKeyframe(14)
				.translate(parts[1], 0.7999995f, 2.02f, -0.76999956f)
				.translate(parts[2], -0.13999999f, -0.20000002f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], -0.13999999f, -0.20000002f, 0.0f)
				.translate(parts[0], -0.16f, 0.17999999f, -0.40999988f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], -0.45378554f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], -0.45378554f, 0.0f, 0.0f)
				.rotate(parts[0], -0.45378554f, 0.0f, 0.0f)
				.startKeyframe(14)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[0], -0.13999999f, -0.059999995f, 0.12999998f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[0], 0.20943952f, 0.0f, 0.0f)
				.startKeyframe(6)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[0], -0.13999999f, -0.059999995f, 0.12999998f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[0], 0.20943952f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], 0.0f, -0.16f, 0.07999999f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(4)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], 0.0f, -0.16f, 0.07999999f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(8)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[0], -0.13999999f, -0.059999995f, 0.12999998f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[0], 0.20943952f, 0.0f, 0.0f)
				.startKeyframe(4)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], -0.13999999f, 0.18f, 0.0f)
				.translate(parts[0], -0.13999999f, -0.059999995f, 0.12999998f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.20943952f, 0.0f, 0.0f)
				.rotate(parts[0], 0.20943952f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.0f, 1.6899989f, 0.0f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.34999996f, 2.329999f, 0.0f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.67999965f, 2.569f, 0.34999993f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.67999965f, 2.569f, 0.44999984f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.02f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.67999965f, 2.61f, 0.44999984f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.67999965f, 2.569f, 0.44999984f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.67999965f, 2.61f, 0.44999984f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], 0.67999965f, 2.569f, 0.44999984f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], -0.29f, -0.6199997f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.8901183f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.startKeyframe(16)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[2], 0.0f, 0.0f, 0.0f)
				.translate(parts[4], 0.0f, 0.0f, 0.0f)
				.translate(parts[3], 0.0f, 0.0f, 0.0f)
				.translate(parts[0], 0.0f, 0.0f, 0.0f)
				.rotate(parts[2], 0.0f, 0.0f, 0.0f)
				.rotate(parts[4], 0.0f, 0.0f, 0.0f)
				.rotate(parts[3], 0.0f, 0.0f, 0.0f)
				.rotate(parts[0], 0.0f, 0.0f, 0.0f)
				.end();
		/*look = AnimationSerializer.deserialize(FileUtils.readFile
				("pirate_gun/lookAnim.jg"));*/
	}
	
	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		matrix.pushPose();
		lerpTransform(matrix, client.getAimHandler().getProgress(), parts[5].getDTransform());
		lerpTransform(matrix, client.getSprintHandler().getProgress(), parts[6].getDTransform());
		lerpTransform(matrix, client.getRecoilHandler().getProgress(), parts[7].getDTransform());// client.getRecoilHandler().getProgress()
		//LogUtils.getLogger().info("Progress: " + client.getRecoilHandler().getProgress());
		matrix.pushPose();
		matrix.translate(parts[4].getTransform().pos[0], parts[4].getTransform().pos[1],
				parts[4].getTransform().pos[2]);
		matrix.mulPose(new Quaternion(parts[4].getTransform().rot[0],
				parts[4].getTransform().rot[1], parts[4].getTransform().rot[2], false));
		//translateAndRotate(parts[4].getCombined(), matrix);
		matrix.pushPose();
		renderArm(player, buffer, matrix, light, HumanoidArm.RIGHT, 
				parts[0].getCombined());
		matrix.popPose();
		matrix.pushPose();
		renderItem(player, stack, buffer, matrix, light, 
				parts[2].getCombined());
		matrix.popPose();
		matrix.pushPose();
		renderModel(player, stack, buffer, matrix, light, Minecraft.getInstance()
				.getModelManager().getModel(HAMMER), parts[3].getCombined());
		matrix.popPose();
		matrix.popPose();
		matrix.pushPose();
		renderArm(player, buffer, matrix, light, HumanoidArm.LEFT, 
				parts[1].getCombined());
		matrix.popPose();
		matrix.popPose();
	}
	
	@Override
	public GunModelPart getGunModelPart() {
		return parts[2];
	}

	@Override
	public void tick(Player player, ItemStack stack) {
		super.tick(player, stack);
		// leftarm pos -1.12f, -1.5f, 0
		// leftarm rot 0.034906f, -1.117001f, 0 
		if(shouldUpdateAnimation) {
			shouldUpdateAnimation = false;
			for(Keyframe kf : look.getKeyframes()) {
				for(Entry<GunModelPart, float[]> entry : kf.translations.entrySet()) {
					LogUtils.getLogger().info("Key: " + entry.getKey().getName() + " value: " + 
							Arrays.toString(entry.getValue()));
				}
			}
		}
		
		//LogUtils.getLogger().info("Tick: " + animator.getTick());
		float tick = animator.getTick();
		if(tick == 35) {
			SoundHandler.playSoundOnServer(SoundRegistries.INSERTING_BULLET.get());
		} else if(tick == 68) {
			// Bullet touching the start of the barrel
			SoundHandler.playSoundOnServer(SoundRegistries.SMALL_BULLET_HITTING_METAL
					.get());
		} else if(tick == 132){
			// Hammer back
			SoundHandler.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK
					.get());
			// Set loaded message
			PirateGuns.channel.sendToServer(new LoadBulletMessage(true));
			// Hammer back transform
			parts[3].getDTransform().setPos(0.8f, -0.6f, -1.1f);
			parts[3].getDTransform().setRot(0.1f, 0f, 0f);
		} else if(tick == 156) {
			// Dropping gunpowder
			SoundHandler.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2
					.get());
		}
	}

	@Override
	public void shoot(Player player, ItemStack stack) {
		PirateGuns.channel.sendToServer(new ShootMessage(player.getYRot(), 
				player.getXRot(), getShootSound().getRegistryName().toString()));
		// Hammer down
		parts[3].getDTransform().setPos(0f, -0.523f, 0.6113f);
		parts[3].getDTransform().setRot(-1.576943f, 0f, 0f);
		markChanges();
	}
	
	@Override
	public void reload(Player player, ItemStack stack) {
		markChanges();
		if(!NBTUtils.isLoaded(stack)) {
			setAnimation(reload);
		}
	}

	@Override
	public List<GunModelPart> getGunParts() {
		return List.of(parts[0], parts[1], parts[2], parts[3], parts[4]);
	}

	@Override
	public SoundEvent getShootSound() {
		return SoundRegistries.FLINTLOCK_PISTOL_SHOOT.get();
	}

	@Override
	public Animation getLookAnimation() {
		return look;
	}
	
}
