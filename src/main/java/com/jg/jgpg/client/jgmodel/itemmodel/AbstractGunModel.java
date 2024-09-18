package com.jg.jgpg.client.jgmodel.itemmodel;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.handler.AbstractClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.handlers.PirateGunsClientHandler;
import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.item.JgGunItem;
import com.jg.jgpg.network.ShootMessage;
import com.jg.jgpg.network.SynchronizeAnimationMessage;
import com.jg.jgpg.utils.NBTUtils;
import com.jg.jgpg.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

import java.util.Random;

public abstract class AbstractGunModel extends JgModel<PirateGunsClientHandler>
	implements IAimableModel {

	protected int xDirection;
	
	public AbstractGunModel(AbstractClientHandler client) {
		super(client);
	}
	
	// Progress stuff

	public float getShootProgress(ItemStack stack, float anchor, String inEasing, String outEasing) {
		// anchor = 0.2f works good
		Easing in = EasingHandler.INSTANCE.getEasing(inEasing);
		Easing out = EasingHandler.INSTANCE.getEasing(outEasing);

		float p = (1 - client.getClient().getCooldownHandler().getProgress(stack.getItem()));
		if (p > anchor) {
			p = client.getClient().getCooldownHandler().getProgress(stack.getItem());
			return out.get(p / 1);
		}
		return in.get(p / anchor);
	}

	// Gun methods

	public void tryToReload(Player player) {
		if (canReload(player) && animator.getAnimation() == null) {
			reload(player);
		}
	}

	public boolean cooldown(Player player) {
		return client.getClient().getCooldownHandler().isOnCooldown(item);
	}
	
	public boolean enoughBullets() {
		return NBTUtils.getBullets(Utils.handStack()) > 0;
	}
	
	public void shootAndRecoilTransformation(PoseStack matrix, Easing easing) {
		float p = client.getRecoilsHandler().getProgress(item);

		float aiming = 1f;
		if (client.getAimHandler().getProgress(item) > 0) {
			aiming = 0.4f;
		}
		p = easing.get(p);
		lerpToTransform("recoilaccumulation", matrix, p * aiming);
	}

	@Override
	public boolean canUse(Player player) {
		return (animationIsNull() && enoughBullets() && !cooldown(player))
				|| (player.isCreative() && !cooldown(player));
	}

	@Override
	public void onUse(Player player, ItemStack stack) {
		if (canUse(player)) {
			shoot(player, getGun());
			xDirection = (Utils.random.nextBoolean() ? 1 : -1);
			NBTUtils.setBullets(Utils.handStack(), Math.max(0, NBTUtils.getBullets(Utils
					.handStack()) - 1));
			afterUse(player);
		}
	}

	public void shoot(Player player, JgGunItem gun) {
		ItemStack stack = Utils.handStack();

		// This is needed for shooting :)
		float f = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F)) * Mth.cos(player.getXRot()
				* ((float) Math.PI / 180F));
		float f1 = -Mth.sin((player.getXRot()) * ((float) Math.PI / 180F));
		float f2 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F)) * Mth.cos(player.getXRot()
				* ((float) Math.PI / 180F));

		// Sending the message to the server to shoot
		SoundEvent sound = gun.getSound();
		if(sound == null) {
			sound = SoundType.EMPTY.getPlaceSound();
		}
		PirateGuns.channel.sendToServer(new ShootMessage(Utils.sound(sound),
				Utils.item(gun), f, f1, f2, gun.getShootCooldown(stack)));

		// Setting cooldown
		client.getClient().getCooldownHandler().addCooldown(gun, gun.getShootCooldown(stack));

		// Adding recoil
		client.getRecoilsHandler().addRecoil(gun, gun.getRecoilTime(stack));

		client.getRecoilsHandler().pushRecoil(gun, stack);

		// The vertical recoil has to be negative for the shot to be upward
		float ver = gun.getVerticalRecoilMultiplier(stack);
		if(ver == 0) {
			ver = 0.2f;
		}
		float hor = gun.getHorizontalRecoilMultiplier(stack);
		if(hor == 0) {
			hor = 0.2f;
		}
		float newXRot = player.getXRot() + ((gun.getRecoilWeight(stack) *
				-ver));
		float newYRot = player.getYRot() + (gun.getRecoilWeight(stack) *
				new Random().nextFloat(-hor, hor));
		player.setXRot(newXRot);
		player.setYRot(newYRot);
	}

	public void renderOverlayPre(RenderGuiOverlayEvent.Pre e, LocalPlayer player, Font font) {
		/*if(e.getOverlay().overlay() == VanillaGuiOverlay.HOTBAR.type().overlay()) {
			
			String magPath = NBTUtils.getMagPath(player.getMainHandItem());
			MagItem mag = null;
			
			if(!magPath.equals("")) {
				mag = (MagItem) ForgeRegistries.ITEMS.getValue(new ResourceLocation(magPath));
			}
			
			int ammo = NBTUtils.getBullets(player.getMainHandItem());
			int maxAmmo = mag == null ? 0 : mag.getAmmo();
			e.getGuiGraphics().drawString(font, String.valueOf(ammo) + "/" + String.valueOf(maxAmmo), 
					e.getWindow().getGuiScaledWidth() / 5, e.getWindow().getGuiScaledHeight() - 40, 0xFFFFFF);
			
			int max = 20;
			float width = MathUtils.lerp(max, 0, client.getCooldownsHandler()
					.getCooldownPercent(gun, Minecraft.getInstance()
							.getPartialTick()));
			
			// Background
			RenderHelper.rect(e.getGuiGraphics(), e.getWindow()
					.getGuiScaledWidth() / 5, e.getWindow().getGuiScaledHeight() - 54, 
					max, 10, Color.rgba(80, 80, 80, 255));
			// Progress
			RenderHelper.rect(e.getGuiGraphics(), e.getWindow()
					.getGuiScaledWidth() / 5, e.getWindow().getGuiScaledHeight() - 54, 
					(int) width, 10, Color.rgba(20, 20, 20, 255));
		}*/
	}
	
	public boolean canAim(Player player) {
		return animator.getAnimation() == null;
	}

	public JgGunItem getGun() {
		return (JgGunItem) item;
	}

	@Override
	public void modifyModelByPose(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount,
								  float pAgeInTicks, float pNetHeadYaw, float pHeadPitch,
								  float attackTime, float swimAmount, boolean crouching,
								  boolean riding, JgPlayerPose pose, JgHumanoidModel<?> model) {
		super.modifyModelByPose(pEntity, pLimbSwing, pLimbSwingAmount,
				pAgeInTicks, pNetHeadYaw, pHeadPitch, attackTime, swimAmount, crouching,
				riding, pose, model);
	}

	@Override
	public void onSetupPlayerModel(LivingEntity entity, JgHumanoidModel<?> model,
								   float pLimbSwing, float pLimbSwingAmount,
								   float pAgeInTicks, float pNetHeadYaw,
								   float pHeadPitch) {
		doAttackProgress(entity, model);
	}

	public void doAttackProgress(LivingEntity entity, JgHumanoidModel<?> model) {
		if(animator.getAnimation() != null) {
			if (animator.getAnimation() == getKickbackAnimAnimation((Player) entity)) {
				model.attackTime = animator.getTick() / animator.getAnimation()
						.getDuration();
			}
		}
	}

	@Override
	public void onAnimationStart(Animation anim) {
		PirateGuns.channel.sendToServer(new SynchronizeAnimationMessage(Minecraft.getInstance()
				.player.getUUID(), getAnimationIndex(anim)));
	}

	@Override
	public void onAnimationEnd(Animation anim) {
		PirateGuns.channel.sendToServer(new SynchronizeAnimationMessage(Minecraft.getInstance()
				.player.getUUID(), -1));
	}

	// Abstract methods
	
	public abstract boolean canReload(Player player);

	public abstract void reload(Player player);

	public abstract Animation getLookAnimAnimation(Player player);

	public abstract Animation getKickbackAnimAnimation(Player player);

}
