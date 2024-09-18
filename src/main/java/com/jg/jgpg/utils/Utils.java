package com.jg.jgpg.utils;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.Easing;
import com.jg.jgpg.client.animation.Keyframe;
import com.jg.jgpg.client.animation.ModelPartKeyframesHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.player.model.JgHumanoidModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.PlayerPoses;
import com.jg.jgpg.network.PlaySoundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Utils {

	public static Random random = new Random();

	public static <T> List<T> of(T... objects) {
		return new ArrayList<T>(Arrays.stream(objects).toList());
	}

	public static ItemStack handStack() {
		return Minecraft.getInstance().player.getMainHandItem();
	}
	
	public static void tryToAddPrevKeyframes(Keyframe addedKf, JgModel model) {
		if(addedKf != null) {
			Map<JgModelPart, ModelPartKeyframesHandler> handlers = model.getAnimator().getAnimation()
					.getPartHandlers();
			int lastTick = -1;
			for(ModelPartKeyframesHandler handler : handlers.values()) {
				for(Keyframe kf : handler.getTranslations()) {
					if(kf.getTick() < addedKf.getTick() && kf.getTick() > lastTick) {
						lastTick = kf.getTick();
					}
				}
				for(Keyframe kf : handler.getRotations()) {
					if(kf.getTick() < addedKf.getTick() && kf.getTick() > lastTick) {
						lastTick = kf.getTick();
					}
				}
			}
			if(lastTick != -1) {
				for(ModelPartKeyframesHandler handler : handlers.values()) {
					
					if(!handler.getTranslations().isEmpty()) {
						Keyframe prevKfToLastTick = Utils.searchPrevKfToTick(handler.getTranslations(), 
								lastTick);
						if(prevKfToLastTick != null) {
							// Checking if the keyframe already exists
							boolean canAdd = true;
							for(Keyframe kf : handler.getTranslations()) {
								if(kf.getTick() == lastTick) {
									canAdd = false;
								}
							}
							if(canAdd) {
								handler.getTranslations().add(new Keyframe(lastTick, Easing.DEFAULT, 
										prevKfToLastTick.getVec().copy(), true));
							}
						}
					}
					if(!handler.getRotations().isEmpty()) {
						Keyframe prevKfToLastTick = Utils.searchPrevKfToTick(handler.getRotations(), 
								lastTick);
						if(prevKfToLastTick != null) {
							// Checking if the keyframe already exists
							boolean canAdd = true;
							for(Keyframe kf : handler.getRotations()) {
								if(kf.getTick() == lastTick) {
									canAdd = false;
								}
							}
							if(canAdd) {
							handler.getRotations().add(new Keyframe(lastTick, Easing.DEFAULT, 
									prevKfToLastTick.getVec().copy(), false));
							}
						}
					}
				}
			}
		}
		
		model.getAnimator().getAnimation().sort();
	}
	
	public static Keyframe searchPrevKfToTick(List<Keyframe> keyframes, int tick) {
		for(int i = keyframes.size() - 1; i > -1; i--) {
			Keyframe kf = keyframes.get(i);
			if(tick > kf.getTick()) {
				return kf;
			}
		}
		return null;
	}
	
	public static void tryToRemoveKeyframe(ModelPartKeyframesHandler handler, int tick, boolean translate) {
		List<Keyframe> keyframes = translate ? handler.getTranslations() 
				: handler.getRotations();
		for(int i = 0; i < keyframes.size(); i++) {
			Keyframe kf = keyframes.get(i);
			if(kf.getTick() == tick) {
				keyframes.remove(kf);
				break;
			} else if(kf.getTick() > tick) {
				break;
			}
		}
	}
	
	public static List<Keyframe> getAndRemoveShadowKeyframes(List<Keyframe> keyframes) {
		List<Keyframe> shadows = new ArrayList<>();
		for(int i = 0; i < keyframes.size(); i++) {
			Keyframe kf = keyframes.get(i);
			if(kf.isShadow()) {
				shadows.add(kf);
				LogUtils.log("RepetitiveAnimation", "Removing tick: " + kf.getTick());
			}
		}
		keyframes.removeAll(shadows);
		return shadows;
	}
	
	public static int getBackwardOffset(int cycleDuration, List<Keyframe> cycle, List<Keyframe> shadows) {
		if(cycle.size() == 0) {
			return 0;
		}
		int times = shadows.size() / cycle.size();
		return times * cycleDuration;
	}
	
	public static int[] getCycleStartAndEnd(List<Keyframe> keyframes) {
		int start = -1;
		int end = -1;
		for(int i = 0; i < keyframes.size(); i++) {
			Keyframe kf = keyframes.get(i);
			if(kf.isRepetitive()) {
				// Start
				if(start == -1) {
					start = i;
				} else if(end == -1) {
					end = i;
				}
				
				if(start != -1 && end != -1) {
					break;
				}
			}
		}
		return new int[] { start, end };
	}
	
	public static Pair<List<Keyframe>, Integer> getCycleData(List<Keyframe> keyframes) {
		List<Keyframe> cycleKeyframes = new ArrayList<>();
		int duration = 0;
		int[] startAndEnd = getCycleStartAndEnd(keyframes);
		if(startAndEnd[1] != -1) {
			// We have to add 1 to the end to also include the last cycle keyframe
			for(int i = startAndEnd[0]; i < startAndEnd[1] + 1; i++) {
				if(i - 1 >= 0) {
					int prevTick = keyframes.get(i - 1).getTick();
					duration += keyframes.get(i).getTick() - prevTick;
				}
				cycleKeyframes.add(keyframes.get(i));
			}
		}
		return new Pair<List<Keyframe>, Integer>(cycleKeyframes, duration);
	}
	
	/*public static <T extends LivingEntity> void handleRightArmPose(LocalPlayer player,
			JgHumanoidModel<T> model, ModelPart rightArm, JgHumanoidModel
			.ArmPose pose) {
		if(pose == JgHumanoidModel
				.ArmPose.SPYGLASS) {
			model.rightArm.yRot = -0.1F + model.head.yRot;
			model.rightArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot;
	    }
	}
	
	public static <T extends LivingEntity> void handleLeftArmPose(LocalPlayer player, 
			JgHumanoidModel<T> model, ModelPart leftArm, JgHumanoidModel
			.ArmPose pose) {
		if(pose == JgHumanoidModel
				.ArmPose.SPYGLASS) {
			leftArm.xRot = 0.6f;
			leftArm.yRot = -0.8f;
	    }
	}*/

	public static void handlePlayerPose(LivingEntity pEntity, float pLimbSwing,
								 float pLimbSwingAmount, float pAgeInTicks,
								 float pNetHeadYaw, float pHeadPitch, float attackTime,
								 float swimAmount, boolean crouching, boolean riding,
								 JgPlayerPose pose, JgHumanoidModel<?> model) {
		if (pose == PlayerPoses.HOLDINGGUN) {
			model.leftArm.xRot = 0.6f;
			model.leftArm.yRot = -0.8f;

			model.rightArm.yRot = -0.1F + model.head.yRot;
			model.rightArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

			model.head.x = 0;
			model.head.zRot = 0;
			model.hat.copyFrom(model.head);
		} else if (pose == PlayerPoses.AIMING) {
			boolean isCrouching = pEntity.isCrouching();

			model.leftArm.x = 4;
			if (!isCrouching) {
				model.leftArm.y = 1f;
			} else {
				model.leftArm.y = 5f;
			}
			model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1);
			model.leftArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

			if (!isCrouching) {
				model.rightArm.y = 1f;
			} else {
				model.rightArm.y = 5f;
			}
			model.rightArm.yRot = -0.1F + model.head.yRot;
			model.rightArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;

			model.head.x = -1;
			model.head.xRot = (pHeadPitch * ((float) Math.PI / 180F)) + 0.2f;
			model.head.zRot = -0.2f;

			model.hat.copyFrom(model.head);
		} else if (pose == PlayerPoses.RELOADING) {
			model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1);
			model.leftArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot;

			model.rightArm.yRot = -0.1F + model.head.yRot;
			model.rightArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot;

			model.head.x = 0;
			model.head.xRot = (pHeadPitch * ((float) Math.PI / 180F)) + 0.6f;
			model.head.zRot = 0;
			model.hat.copyFrom(model.head);
		} else if (pose == PlayerPoses.RUNNING) {
			model.leftArm.yRot = Mth.clamp(0.8F + model.head.yRot, -1, 1) +
					(Mth.sin(pLimbSwing) * 0.06f);
			model.leftArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot +
					(Mth.cos(pLimbSwing) * 0.09f);

			model.rightArm.yRot = -0.1F + model.head.yRot + (Mth.sin(pLimbSwing) * 0.06f);
			model.rightArm.xRot = ((-(float) Math.PI / 2F) + 0.4f) + model.head.xRot +
					(Mth.cos(pLimbSwing) * 0.09f);

			/*float f = 1.0f;

			model.rightArm.xRot = (Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 2.0F
					* pLimbSwingAmount * 0.5F / f);
			model.leftArm.xRot = (Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount
					* 0.5F / f) * 0.6f;
			model.rightArm.zRot = 0.0F;
			model.leftArm.zRot = 0.0F;
			model.rightLeg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F
					* pLimbSwingAmount / f;
			model.leftLeg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F
					* pLimbSwingAmount / f;
			model.rightLeg.yRot = 0.0F;
			model.leftLeg.yRot = 0.0F;
			model.rightLeg.zRot = 0.0F;
			model.leftLeg.zRot = 0.0F;*/

			model.head.x = 0;
			model.head.zRot = 0;
			model.hat.copyFrom(model.head);
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
		List<Integer> data = Arrays.stream(arr1)
			      .boxed()
			      .collect(Collectors.toList());
		data.addAll(Arrays.stream(arr2)
			      .boxed()
			      .collect(Collectors.toList()));
		
		/*int[] data = new int[arr1.length + 2];

		for (int i = 0; i < arr1.length; i++) {
			data[i] = arr1[i];
		}
		for (int i = 0; i < arr2.length; i++) {
			data[arr1.length + i] = arr2[i];
		}*/
		return data.stream().mapToInt(Integer::intValue).toArray();
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
		PirateGuns.channel.sendToServer(new PlaySoundMessage(e, player.getX(),
				player.getY(), player.getZ(), 0.4f, 1.2F / (player.getRandom().nextFloat() * 0.2F 
						+ 0.9F)));
	}

	public static <T, R> String mapToString(Map<T, R> map) {
		String all = "";
		for (Entry<T, R> e : map.entrySet()) {
			all += e.getKey().toString();
			all += e.getValue().toString() + "\n";
		}
		return all;
	}

	public static ResourceLocation loc(String path) {
		return new ResourceLocation(PirateGuns.MODID, path);
	}
	
	public static boolean collides(int x, int y, int w, int h, int x2, int y2) {
		return x2 > x && x2 < x + w && y2 > y && y2 < y + h;
	}

	public static boolean collides(double x, double y, double w, double h, double x2, double y2) {
		return x2 > x && x2 < x + w && y2 > y && y2 < y + h;
	}

}
