package com.jg.jgpg.client.model;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.animations.Easing;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.model.player.JgHumanoidModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel.ArmPose;
import com.jg.jgpg.network.LoadBulletMessage;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.InventoryUtils;
import com.jg.jgpg.utils.InventoryUtils.InvData;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public abstract class AbstractGunModel extends AbstractJgModel {

	public AbstractGunModel(ClientHandler client) {
		super(client);
	}
	
	// Progress stuff
	
	public float getShootProgress(ItemStack stack, float anchor, String inEasing, String outEasing) {
		// anchor = 0.2f works good
		Easing in = EasingHandler.INSTANCE.getEasing(inEasing);
		Easing out = EasingHandler.INSTANCE.getEasing(outEasing);

		float p = (1 - client.getShootHandler().getProgress(stack.getItem()));
		if (p > anchor) {
			p = client.getShootHandler().getProgress(stack.getItem());
			return out.get(p / 1);
		}
		return in.get(p / anchor);
	}
	
	// Gun methods
	
	public void tryToReload(LocalPlayer player) {
		if(canReload(player) && animator.getCurrent() == null) {
			reload(player);
		}
	}
	
	public void consumeItems(int gunpowder, int bullets, boolean isShotgun) {
		Player player = Minecraft.getInstance().player;
		
		InvData gunpowderData = InventoryUtils.getTotalCountAndIndexForItem(player, 
				Items.GUNPOWDER, gunpowder);
		
		int[] gunPowderIndexes = gunpowderData.getData();
		
		int[] bulletsData = new int[] { InventoryUtils.getIndexForItem(player, 
				isShotgun ? ItemRegistries.TRABUCO_BULLET.get() : ItemRegistries.MUSKET_BULLET.get()), 
				bullets };
		
		int[] data = Utils.mixIntArrays(gunPowderIndexes, bulletsData);
		
		PirateGuns.channel.sendToServer(new LoadBulletMessage(true, data));
	}
	
	// Abstract methods
	
	public abstract ArmPose getArmPose(ItemStack stack);
	
	public abstract <T extends LivingEntity> void handleArmPose(ArmPose pose, ItemStack stack, 
			JgHumanoidModel<T> model, HumanoidArm arm);
	
	public abstract boolean canReload(LocalPlayer player);
	
	public abstract void reload(LocalPlayer player);
	
	public abstract Animation getLookAnimAnimation(LocalPlayer player);
	
	public abstract Animation getKickbackAnimAnimation(LocalPlayer player);

}
