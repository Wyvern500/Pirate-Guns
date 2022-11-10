package com.jg.pirateguns.animations.gunmodels;

import java.util.List;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.handlers.SoundHandler;
import com.jg.pirateguns.network.LoadBulletMessage;
import com.jg.pirateguns.network.ShootMessage;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.registries.SoundRegistries;
import com.jg.pirateguns.utils.InventoryUtils;
import com.jg.pirateguns.utils.NBTUtils;
import com.jg.pirateguns.utils.Paths;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TrabucoGunModel extends GunModel {

	private final ModelResourceLocation HAMMER = new ModelResourceLocation(Paths.THAMMER, "inventory");
	public Animation look;
	public Animation reload;
	
	private static String LOOK = "trabuco/lookAnim.jg";
	private static String RELOAD = "trabuco/reloadAnim.jg";
	
	public TrabucoGunModel(ClientHandler client) {
		super(new GunModelPart[] { 
				new GunModelPart("rightarm", -0.306f, 0, -0.1f, -0.6f, 0, 0.6f), 
				new GunModelPart("leftarm", -0.16f, 0.09f, -0.52f, 0, -1.082094f, 0), 
				new GunModelPart("gun", 0.62f, -0.58f, -0.66f, 0.030186f, 0.034906f, 0),
				new GunModelPart("hammer", 0.62f, -0.58f, -0.66f, 0.030186f, 0.034906f, 0),
				new GunModelPart("all"),
				new GunModelPart("alllessleftarm"),
				new GunModelPart("aim", -0.562f, 0.308f, 0, -0.069813f, -0.034906f, 0), 
				new GunModelPart("sprint", 0.8f, -0.64f, 0.209f, 0.506144f, 1.047198f, 0),
				new GunModelPart("recoil", 0f, -0.37f, 0.26f, 0.244345f, -0.034906f, 
						-0.069813f) }, ItemRegistries.PIRATEPISTOL.get(), 
				client, new String[] { LOOK, RELOAD });
		
		reload = new Animation("trabuco/reloadAnim.jg", "jgpg:trabuco")
				.startKeyframe(12)
				.translate(parts[1], -0.5199998f, 0.059999995f, 0.27f)
				.translate(parts[5], 1.1599993f, -1.1299993f, 0.29f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.54105204f, 0.7504918f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], -0.35999995f, 0.059999995f, 0.27f)
				.translate(parts[5], 1.1599993f, -1.1299993f, 0.29f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.54105204f, 0.7504918f, 0.0f)
				.startKeyframe(12)
				.translate(parts[1], -0.35999995f, 0.059999995f, 0.27f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12)
				.translate(parts[1], -0.35999995f, 0.20000002f, 0.7199996f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12)
				.translate(parts[1], -0.26000005f, 0.20000002f, 0.7199996f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12, "easeOutQuint")
				.translate(parts[1], -0.26000005f, 0.20000002f, 0.8399995f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12, "easeOutQuint")
				.translate(parts[1], -0.26000005f, 0.22000003f, 0.8399995f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12, "easeInQuint")
				.translate(parts[1], -0.26000005f, 0.20000002f, 0.8399995f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12, "easeOutQuint")
				.translate(parts[1], -0.26000005f, 0.22000003f, 0.8399995f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12, "easeOutQuint")
				.translate(parts[1], -0.26000005f, 0.20000002f, 0.8399995f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12)
				.translate(parts[1], -0.39999992f, 0.20000002f, 0.8399995f)
				.translate(parts[5], -0.27f, -0.7599996f, 0.0f)
				.rotate(parts[1], 3.7252903E-9f, 3.7252903E-9f, 0.06981317f)
				.rotate(parts[5], 0.0f, 0.0f, 1.0122914f)
				.startKeyframe(12)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 0.0f)
				.end();
		
		look = new Animation("trabuco/lookAnim.jg", "jgpg:trabuco")
				.startKeyframe(16, "easeOutQuint")
				.translate(parts[1], -0.8599995f, 0.0f, 0.0f)
				.translate(parts[5], -0.42999986f, -0.7299996f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 1.1519179f)
				.startKeyframe(28)
				.translate(parts[1], -0.8599995f, 0.0f, 0.0f)
				.translate(parts[5], -0.42999986f, -0.7299996f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 1.1519179f)
				.startKeyframe(8, "easeOutQuint")
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 0.0f)
				.startKeyframe(16)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(28)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(12, "easeInOutQuart")
				.translate(parts[1], 0.039999984f, -0.33999994f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.55850536f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(12)
				.translate(parts[1], 0.039999984f, -0.33999994f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.55850536f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(2, "easeInOutQuint")
				.translate(parts[1], 0.13999997f, -0.059999995f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.24434613f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(2)
				.translate(parts[1], 0.13999997f, -0.059999995f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.24434613f, 0.0f, 0.0f)
				.rotate(parts[5], -0.034906585f, -0.034906585f, -0.7504918f)
				.startKeyframe(8)
				.translate(parts[1], 0.13999997f, -0.059999995f, 0.28f)
				.translate(parts[5], 0.66999966f, 0.37999994f, 0.17999999f)
				.rotate(parts[1], 0.24434613f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, -0.7504918f)
				.startKeyframe(16)
				.translate(parts[1], 0.0f, 0.0f, 0.0f)
				.translate(parts[5], 0.0f, 0.0f, 0.0f)
				.rotate(parts[1], 0.0f, 0.0f, 0.0f)
				.rotate(parts[5], 0.0f, 0.0f, 0.0f)
				.end();
	}

	@Override
	public float getKnockback() {
		return 8;
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		matrix.pushPose();
		lerpTransform(matrix, client.getAimHandler().getProgress(), parts[6].getDTransform());
		lerpTransform(matrix, client.getSprintHandler().getProgress(), parts[7].getDTransform());
		lerpTransform(matrix, client.getRecoilHandler().getProgress(), parts[8].getDTransform());// client.getRecoilHandler().getProgress()
		//LogUtils.getLogger().info("Progress: " + client.getRecoilHandler().getProgress());
		matrix.pushPose();
		matrix.translate(parts[4].getTransform().pos[0], parts[4].getTransform().pos[1],
				parts[4].getTransform().pos[2]);
		matrix.mulPose(new Quaternion(parts[4].getTransform().rot[0],
				parts[4].getTransform().rot[1], parts[4].getTransform().rot[2], false));
		//translateAndRotate(parts[4].getCombined(), matrix);
		matrix.pushPose();
		matrix.scale(1f, 1.5f, 1f);
		renderArm(player, buffer, matrix, light, HumanoidArm.LEFT, 
				parts[1].getCombined());
		matrix.popPose();
		matrix.pushPose();
		// All less leftarm
		translateAndRotate(parts[5].getTransform(), matrix);
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
		matrix.popPose();
		matrix.popPose();
		
		float tick = animator.getTick();
		if(getAnimation() == look) {
			if(tick == 13 || tick == 61) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUN_MOVING.get());
			} else if(tick == 120) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUN_HIT.get());
			}
		} else if(getAnimation() == reload) {
			if(tick == 12) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUN_MOVING.get());
			} else if(tick == 24) {
				SoundHandler.playSoundOnServer(SoundRegistries.INSERTING_BULLET.get());
			} else if(tick == 25) {
				SoundHandler.playSoundOnServer(SoundRegistries.MULTIPLE_BULLETS_HITTING_METAL.get());
			} else if(tick == 70) {
				SoundHandler.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
				parts[3].getDTransform().setPos(0.62f, -0.58f, -0.66f);
				parts[3].getDTransform().setRot(0.030186f, 0.034906f, 0f);
				PirateGuns.channel.sendToServer(new LoadBulletMessage(true));
			} else if(tick == 90) {
				SoundHandler.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
			}
		}
	}

	@Override
	public void reload(Player player, ItemStack stack) {
		if(!NBTUtils.isLoaded(stack)) {
			int[] bullet = InventoryUtils.getCountAndIndexForItem(player, 
					ItemRegistries.TRABUCO_BULLET.get());
			int[] gunpowder = InventoryUtils.getCountAndIndexForItem(player, Items.GUNPOWDER);
			if(bullet[1] >= 8 && gunpowder[1] >= 2) {
				InventoryUtils.consumeItems(player, new int[] {bullet[0], gunpowder[0]}, 
						new int[] {8, 2});
				setAnimation(reload);
			}
		}
	}
	
	@Override
	public void shoot(Player player, ItemStack stack) {
		PirateGuns.channel.sendToServer(new ShootMessage(player.getYRot(), 
				player.getXRot(), gun.getShootSound().getRegistryName().toString()));
		// Hammer down
		parts[3].getDTransform().setPos(0.62f, -0.12f, -0.579f);
		parts[3].getDTransform().setRot(-1.575518f, 0.034906f, 0f);
		markChanges();
	}

	@Override
	public Animation getLookAnimation() {
		return look;
	}

	@Override
	public List<GunModelPart> getGunParts() {
		return List.of(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
	}

	@Override
	public GunModelPart getGunModelPart() {
		return parts[2];
	}

	/*@Override
	public SoundEvent getShootSound() {
		return null;
	}*/

}
