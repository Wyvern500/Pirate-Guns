package com.jg.jgpg.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.model.player.JgHumanoidModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel.ArmPose;
import com.jg.jgpg.network.PlaySoundMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class Utils {

	public static <T extends LivingEntity> void handleRightArmPose(LocalPlayer player, 
			JgHumanoidModel<T> model, ModelPart rightArm, ArmPose pose) {
		if(pose == ArmPose.SPYGLASS) {
			model.rightArm.yRot = -0.1F + model.head.yRot;
			model.rightArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot;
	    }
	}
	
	public static <T extends LivingEntity> void handleLeftArmPose(LocalPlayer player, 
			JgHumanoidModel<T> model, ModelPart leftArm, ArmPose pose) {
		if(pose == ArmPose.SPYGLASS) {
			leftArm.xRot = 0.6f;
			leftArm.yRot = -0.8f;
	    }
	}
	
	public static <T extends LivingEntity, M extends JgHumanoidModel> Model 
		getArmorModel(T entity, ItemStack itemStack,
			EquipmentSlot slot, M model) {
		return getGenericArmorModel(entity, itemStack, slot, model);
	}

	public static Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack,
			EquipmentSlot equipmentSlot, JgHumanoidModel<?> original) {
		JgHumanoidModel<?> replacement = getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
		if (replacement != original) {
			copyModelProperties(original, replacement);
			return replacement;
		}
		return original;
	}

	public static JgHumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
			EquipmentSlot equipmentSlot, JgHumanoidModel<?> original) {
		return original;
	}

	public static <T extends LivingEntity> void copyModelProperties(JgHumanoidModel<T> original,
			JgHumanoidModel<?> replacement) {
		// this function does not make use of the <T> generic, so the unchecked cast
		// should be safe
		original.copyPropertiesTo((JgHumanoidModel<T>) replacement);
		replacement.head.visible = original.head.visible;
		replacement.hat.visible = original.hat.visible;
		replacement.body.visible = original.body.visible;
		replacement.rightArm.visible = original.rightArm.visible;
		replacement.leftArm.visible = original.leftArm.visible;
		replacement.rightLeg.visible = original.rightLeg.visible;
		replacement.leftLeg.visible = original.leftLeg.visible;
	}

	public static Context getEntityRendererContext() {
		Minecraft mc = Minecraft.getInstance();
		return new Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderer(),
				mc.gameRenderer.itemInHandRenderer, mc.getResourceManager(), mc.getEntityModels(), mc.font);
	}

	public static int[] mixIntArrays(int[] arr1, int[] arr2) {
		int[] data = new int[arr1.length + 2];

		for (int i = 0; i < arr1.length; i++) {
			data[i] = arr1[i];
		}
		for (int i = 0; i < arr2.length; i++) {
			data[arr1.length + i] = arr2[i];
		}

		return data;
	}

	public static List<Item> getItemsFromTag(TagKey<Item> tag) {
		return ForgeRegistries.ITEMS.tags().getTag(tag).stream().toList();
	}

	public static String item(Item item) {
		return ForgeRegistries.ITEMS.getKey(item).toString();
	}

	public static String sound(SoundEvent sound) {
		return ForgeRegistries.SOUND_EVENTS.getKey(sound).toString();
	}

	public static void playSoundOnServer(SoundEvent e) {
		Player player = Minecraft.getInstance().player;
		PirateGuns.channel.sendToServer(new PlaySoundMessage(e, player.getX(), player.getY(), player.getZ(), 0.4f,
				1.2F / (player.getRandom().nextFloat() * 0.2F + 0.9F)));
	}

	public static <T, R> String mapToString(Map<T, R> map) {
		String all = "";
		for (Entry<T, R> e : map.entrySet()) {
			all += e.getKey().toString();
			all += e.getValue().toString() + "\n";
		}
		return all;
	}

	public static boolean collides(int x, int y, int w, int h, int x2, int y2) {
		return x2 > x && x2 < x + w && y2 > y && y2 < y + h;
	}

	public static boolean collides(double x, double y, double w, double h, double x2, double y2) {
		return x2 > x && x2 < x + w && y2 > y && y2 < y + h;
	}

}
