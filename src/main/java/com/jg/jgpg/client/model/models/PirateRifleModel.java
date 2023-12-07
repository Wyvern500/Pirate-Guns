package com.jg.jgpg.client.model.models;

import java.util.List;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractGunModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.model.player.JgHumanoidModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel.ArmPose;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.network.SetScopeMessage;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.InventoryUtils;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MeleeHelper;
import com.jg.jgpg.utils.NBTUtils;
import com.jg.jgpg.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.RenderTypeHelper;

public class PirateRifleModel extends AbstractGunModel {

	public static Animation LOOK = new Animation("Look");
	public static Animation RELOAD = new Animation("ReloadAnimation");
	public static Animation HIT = new Animation("Hit");
	public static Animation REMOVESCOPE = new Animation("RemoveScope");
	
	public PirateRifleModel(ClientHandler handler) {
		super(handler);
	}

	@Override
	public void initParts() {
		
		addPart(new JgModelPart("gun", 0.59f, -0.58f, -1.09f, 0.100000136f, 0, 0));
		addPart(new JgModelPart("hammer", 0.59f, -0.58f, -1.09f, 0.100000136f, 0, 0));
		addPart(new JgModelPart("leftarm", -0.04f, -0.12f, 1.13f, -0.14f, -0.44f, -0.64f));
		addPart(new JgModelPart("rightarm", 0.28f, -0.16f, 1.93f, -0.14f, 0.2f, 0.6f));
		addPart(new JgModelPart("rightarmgun", 0, 0, 0, 0, 0, 0f));
		addPart(new JgModelPart("leftarmgun", 0, 0, 0, 0, 0, 0f));
		addPart(new JgModelPart("gunwithhammer", 0, 0, 0, 0, 0, 0f));
		addPart(new JgModelPart("all", 0.34f, -0.21f, 0.06f, 0f, 0.09f, 0f));
		addPart(new JgModelPart("aim", -0.8779923f, 0.7059952f, 0.0f, -0.1392767f, -0.09250209f, 0f));
		addPart(new JgModelPart("sprint", 0.09999999f, -1.379999f, 0.0f, 0.97738487f, -0.069813184f, 
				0.0f));
		addPart(new JgModelPart("recoil", -0.02f, 0.0f, 0.24000004f, 0.0034906575f, -0.0034906585f, -9.313226E-10f));
		addPart(new JgModelPart("leftarm_model", 0, 0, 0, 0, 0, 0));
		
	}
	
	@Override
	public void initAnimations() {
		
		LOOK = new Animation("Look")
				.addKeyframe(5)
				.traslate(getPart("leftarm"), -0.8399995f, -0.16f, 0.24000004f, "easeInSine")
				.traslate(getPart("rightarmgun"), 0.87999946f, 0.31999996f, 0.0f, "easeInSine")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.rotate(getPart("rightarmgun"), 0.02f, 0.0f, -0.57999974f, "easeInSine")
				.addKeyframe(40)
				.traslate(getPart("leftarm"), -0.8399995f, -0.16f, 0.24000004f, "empty")
				.traslate(getPart("rightarmgun"), 0.87999946f, 0.31999996f, 0.0f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarmgun"), 0.02f, 0.0f, -0.57999974f, "empty")
				.addKeyframe(50)
				.traslate(getPart("leftarm"), -1.3399991f, 0.17999999f, -0.06000001f, "empty")
				.traslate(getPart("rightarmgun"), -0.97999936f, -0.35999995f, 0.0f, "easeOutSine")
				.rotate(getPart("leftarm"), 0.0f, -0.2f, 0.0f, "empty")
				.rotate(getPart("rightarmgun"), 0.07999999f, -0.13999997f, 0.57999974f, "easeOutSine")
				.addKeyframe(70)
				.traslate(getPart("leftarm"), -1.3399991f, 0.17999999f, -0.06000001f, "empty")
				.traslate(getPart("rightarmgun"), -0.97999936f, -0.35999995f, 0.0f, "empty")
				.rotate(getPart("leftarm"), 0.0f, -0.2f, 0.0f, "empty")
				.rotate(getPart("rightarmgun"), 0.07999999f, -0.13999997f, 0.57999974f, "empty")
				.addKeyframe(80)
				.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.traslate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.rotate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "easeInSine")
				.end();
		
		RELOAD = new Animation("ReloadAnimation")
				.addKeyframe(5)
				.traslate(getPart("leftarm"), -0.9399994f, -0.32f, 0.7599996f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(10)
				.traslate(getPart("rightarmgun"), -0.79999954f, -0.57999974f, -0.28f, "empty")
				.rotate(getPart("rightarmgun"), 0.02f, -0.02f, 0.7399996f, "empty")
				.addKeyframe(15)
				.traslate(getPart("leftarm"), -0.69999963f, 0.17999999f, 0.6399997f, "empty")
				.addKeyframe(18)
				.traslate(getPart("leftarm"), -0.69999963f, 0.17999999f, 0.8599995f, "easeOutBack")
				.addKeyframe(19)
				.traslate(getPart("leftarm"), -0.69999963f, 0.17999999f, 0.8599995f, "easeOutBack")
				.addKeyframe(23)
				.traslate(getPart("leftarm"), -0.9399994f, -0.32f, 0.7599996f, "empty")
				.addKeyframe(33)
				.traslate(getPart("leftarm"), -0.69999963f, 0.17999999f, 0.6399997f, "empty")
				.addKeyframe(43)
				.traslate(getPart("leftarm"), -0.70053077f, 0.12013654f, 0.6404853f, "empty")
				.addKeyframe(53)
				.traslate(getPart("leftarm"), -0.69999963f, 0.17999999f, 0.6399997f, "empty")
				.addKeyframe(63)
				.traslate(getPart("leftarm"), -0.70053077f, 0.12013654f, 0.6404853f, "empty")
				.addKeyframe(64)
				.traslate(getPart("rightarmgun"), -0.79999954f, -0.57999974f, -0.28f, "empty")
				.rotate(getPart("rightarmgun"), 0.02f, -0.02f, 0.7399996f, "empty")
				.addKeyframe(73)
				.traslate(getPart("rightarmgun"), 1.4787066f, -2.0009353f, -0.28045234f, "easeOutCirc")
				.rotate(getPart("rightarmgun"), 1.1800315f, 0.6199674f, 0.0010889322f, "easeOutCirc")
				.addKeyframe(79)
				.traslate(getPart("leftarm"), -0.70053077f, 0.12013654f, 0.6404853f, "empty")
				.addKeyframe(83)
				.traslate(getPart("leftarm"), -0.12115758f, -0.47975585f, 0.641058f, "empty")
				.traslate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(92)
				.traslate(getPart("leftarmgun"), 0.28f, -0.4799998f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(100)
				.traslate(getPart("leftarm"), -1.1211568f, -1.0797553f, 0.641058f, "empty")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "empty")
				.addKeyframe(110)
				.traslate(getPart("leftarm"), -0.54115736f, 0.14024395f, 0.52105814f, "empty")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "empty")
				.addKeyframe(120)
				.traslate(getPart("leftarm"), -0.44115746f, 0.060243957f, 0.52105814f, "easeOutBack")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "easeOutBack")
				.addKeyframe(130)
				.traslate(getPart("leftarm"), -1.1211568f, -1.0797553f, 0.641058f, "empty")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "empty")
				.addKeyframe(135)
				.traslate(getPart("leftarm"), -0.54115736f, 0.14024395f, 0.52105814f, "empty")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "empty")
				.addKeyframe(145)
				.traslate(getPart("leftarm"), -0.44115746f, 0.060243957f, 0.52105814f, "easeOutBack")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "easeOutBack")
				.addKeyframe(155)
				.traslate(getPart("leftarm"), -0.54115736f, 0.14024395f, 0.52105814f, "empty")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "empty")
				.addKeyframe(165)
				.traslate(getPart("leftarm"), -0.44115746f, 0.060243957f, 0.52105814f, "easeOutBack")
				.rotate(getPart("leftarm"), 0.29999998f, 0.0f, 0.0f, "easeOutBack")
				.addKeyframe(175)
				.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.end();
		
		HIT = new Animation("HitAnimation")
				.addKeyframe(4)
				.traslate(getPart("rightarm"), 0.30360854f, 0.0f, 0.08144749f, "empty")
				.traslate(getPart("gun"), 0.12629102f, 0.07769316f, -0.36888438f, "empty")
				.traslate(getPart("hammer"), 0.12629102f, 0.07769316f, -0.36888438f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gun"), 0.0f, 0.23863375f, 0.0f, "empty")
				.rotate(getPart("hammer"), 0.0f, 0.23863375f, 0.0f, "empty")
				.addKeyframe(9)
				.traslate(getPart("rightarm"), 0.8399995f, -0.07999999f, -0.43999985f, "empty")
				.traslate(getPart("gun"), 0.5278247f, 0.0856373f, -0.90998614f, "empty")
				.traslate(getPart("hammer"), 0.5278247f, 0.0856373f, -0.90998614f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gun"), 0.0f, 1.3741149f, 0.0f, "empty")
				.rotate(getPart("hammer"), 0.0f, 1.3741149f, 0.0f, "empty")
				.addKeyframe(15)
				.traslate(getPart("rightarm"), 0.100160874f, 0.098079905f, -1.1705596f, "empty")
				.traslate(getPart("gun"), 0.24000005f, 0.13999999f, -1.8799986f, "empty")
				.traslate(getPart("hammer"), 0.24000005f, 0.13999999f, -1.8799986f, "empty")
				.traslate(getPart("leftarmgun"), -0.31999996f, 0.0f, 0.3799999f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gun"), 0.0f, 2.7599978f, 0.0f, "empty")
				.rotate(getPart("hammer"), 0.0f, 2.7599978f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(20)
				.traslate(getPart("all"), -0.97999936f, 0.5999997f, 1.0399994f, "empty")
				.rotate(getPart("all"), -0.22000003f, 0.0f, 0.0f, "empty")
				.addKeyframe(21)
				.traslate(getPart("all"), -0.97999936f, 0.5999997f, 1.0399994f, "empty")
				.rotate(getPart("all"), -0.22000003f, 0.0f, 0.0f, "empty")
				.addKeyframe(24)
				.traslate(getPart("all"), -0.49168116f, -0.021279234f, 0.059440784f, "easeOutBack")
				.rotate(getPart("all"), 0.13215037f, 0.11999998f, 0.0f, "easeOutBack")
				.addKeyframe(33)
				.traslate(getPart("all"), -0.49168116f, -0.021279234f, 0.059440784f, "empty")
				.rotate(getPart("all"), 0.13215037f, 0.11999998f, 0.0f, "empty")
				.addKeyframe(36)
				.traslate(getPart("rightarm"), 0.6801605f, -0.021920083f, -0.9905598f, "empty")
				.traslate(getPart("gun"), 0.4199999f, 0.17999999f, -1.2399992f, "empty")
				.traslate(getPart("hammer"), 0.4199999f, 0.17999999f, -1.2399992f, "empty")
				.traslate(getPart("all"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gun"), 0.0f, 1.9399986f, 0.0f, "empty")
				.rotate(getPart("hammer"), 0.0f, 1.9399986f, 0.0f, "empty")
				.rotate(getPart("all"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(39)
				.traslate(getPart("rightarm"), 0.6001606f, -0.061920084f, -0.2705605f, "empty")
				.traslate(getPart("gun"), 0.4199999f, 0.11999997f, -0.6999997f, "empty")
				.traslate(getPart("hammer"), 0.4199999f, 0.11999997f, -0.6999997f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gun"), 0.02f, 0.9399996f, 0.0f, "empty")
				.rotate(getPart("hammer"), 0.02f, 0.9399996f, 0.0f, "empty")
				.addKeyframe(45)
				.traslate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("gun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("hammer"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("all"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("hammer"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("all"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.end();
		
		REMOVESCOPE = new Animation("Remove Scope")
				.addKeyframe(14)
				.traslate(getPart("rightarmgun"), -0.3999999f, -0.69999963f, 0.0f, "empty")
				.traslate(getPart("leftarm"), -0.45999983f, 0.22000003f, 0.5999997f, "empty")
				.rotate(getPart("rightarmgun"), 0.0f, 0.0f, 0.8028517f, "empty")
				.addKeyframe(20)
				.traslate(getPart("leftarm"), -0.45999983f, 0.22000003f, 0.5399998f, "empty")
				.addKeyframe(22)
				.traslate(getPart("leftarm"), -0.45999983f, 0.22000003f, 0.7199996f, "empty")
				.addKeyframe(23)
				.traslate(getPart("rightarmgun"), -0.3999999f, -0.69999963f, 0.02f, "empty")
				.traslate(getPart("leftarm"), -0.45999983f, 0.22000003f, 0.7199996f, "empty")
				.addKeyframe(35)
				.traslate(getPart("rightarmgun"), -0.3999999f, -0.69999963f, 0.02f, "empty")
				.traslate(getPart("leftarm"), -0.45999983f, 0.22000003f, 0.7199996f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(50)
				.traslate(getPart("leftarm"), -1.005724f, -1.9172606f, 1.0289595f, "empty")
				.rotate(getPart("leftarm"), 0.7679451f, 0.0f, 0.0f, "empty")
				.addKeyframe(80)
				.traslate(getPart("leftarm"), -1.005724f, -1.9172606f, 1.0289595f, "empty")
				.rotate(getPart("leftarm"), 0.7679451f, 0.0f, 0.0f, "empty")
				.addKeyframe(90)
				.traslate(getPart("leftarm"), -0.28572464f, -1.557261f, -0.071039826f, "easeOutExpo")
				.rotate(getPart("leftarm"), 0.66322523f, 0.3141593f, 0.0f, "easeOutExpo")
				.addKeyframe(100)
				.traslate(getPart("leftarm"), -0.28572464f, -1.557261f, -0.071039826f, "empty")
				.rotate(getPart("leftarm"), 0.66322523f, 0.3141593f, 0.0f, "empty")
				.addKeyframe(120)
				.traslate(getPart("leftarm"), 1.9493756f, -1.5039574f, -0.17225769f, "easeInOutCubic")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "easeInOutCubic")
				.addKeyframe(130)
				.traslate(getPart("leftarm"), 1.9493756f, -1.5039574f, -0.17225769f, "empty")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "empty")
				.addKeyframe(140)
				.traslate(getPart("leftarm"), 1.549376f, -1.5039574f, -0.17225769f, "easeInOutCubic")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "easeInOutCubic")
				.addKeyframe(150)
				.traslate(getPart("leftarm"), 1.549376f, -1.5039574f, -0.17225769f, "empty")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "empty")
				.addKeyframe(160)
				.traslate(getPart("leftarm"), 2.669375f, -1.5039574f, -0.17225769f, "easeInOutCubic")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "easeInOutCubic")
				.addKeyframe(170)
				.traslate(getPart("leftarm"), 2.669375f, -1.5039574f, -0.17225769f, "empty")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "empty")
				.addKeyframe(180)
				.traslate(getPart("leftarm"), 1.9493756f, -1.5039574f, -0.17225769f, "easeInBack")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "easeInBack")
				.addKeyframe(190)
				.traslate(getPart("leftarm"), 1.9493756f, -1.5039574f, -0.17225769f, "empty")
				.rotate(getPart("leftarm"), 0.673741f, 0.8081448f, 0.0f, "empty")
				.addKeyframe(215)
				.traslate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.end();
	}

	@Override
	public boolean canReload(LocalPlayer player) {
		int gunPowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
		int bullets = InventoryUtils.getCountForItem(player, ItemRegistries.MUSKET_BULLET.get());
		return gunPowder >= Config.SERVER.prGunpowderToReload.get() && bullets >= 1;
	}
	
	@Override
	public void reload(LocalPlayer player) {
		animator.setCurrent(this, RELOAD);
		animator.play();
	}
	
	@Override
	public void tick() {
		animator.tick();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		if(NBTUtils.isLoaded(stack)) {
			getPart("hammer").getDtransform().set(0.59f, -0.58f, -1.09f, 0.100000136f, 0, 0);
		} else {
			getPart("hammer").getDtransform().set(0.59f, -0.38000017f, -0.97000015f, -1.4358906f, 
					0.0f, 0.0f);
		}
		
		if(NBTUtils.hasScope(stack)) {
			if(client.getAimHandler().getProgress(stack.getItem()) > 0.5f) {
				return;
			}
		}
		
		if(client.shouldRenderDefault()) {
			renderDefault(player, stack, buffer, matrix, light);
		}
		
		// float p = getShootProgress(stack);
		float p = 0;
		//p = EasingHandler.INSTANCE.getEasing("easeOutCirc").get(p);
		
		if(player.getCooldowns().isOnCooldown(stack.getItem())) {
			p = getShootProgress(stack, 0.2f, "easeOutExpo", "easeInOutQuint");
		}
		
		matrix.pushPose();
		traslateRotate("all", matrix);
		if(client.shouldRenderDefault()) {
			traslateRotate("recoil", matrix);
		} else {
			lerpToTransform("recoil", matrix, p);
		}
		lerpToTransform("aim", matrix, client.getAimHandler().getProgress(stack.getItem()));
		lerpToTransform("sprint", matrix, client.getSprintHandler().getProgress(stack.getItem()));
		//LogUtils.log("PirateRifleModel", client.getAimHandler().getProgress() + "");
		matrix.pushPose();
		traslateRotate("rightarmgun", matrix);
		traslateRotate("rightarm", matrix);
		matrix.scale(1f, 1f, 3f);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT);
		matrix.popPose();
		matrix.pushPose();
		traslateRotate("leftarmgun", matrix);
		traslateRotate("leftarm", matrix);
		matrix.scale(1f, 1f, 3f);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT);
		matrix.popPose();
		matrix.pushPose();
		traslateRotate("leftarmgun", matrix);
		traslateRotate("rightarmgun", matrix);
		// Gun
		traslateRotate("gun", matrix);
		
		BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, player.level(), 
				player, 0);

		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, model, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		
		Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext
				.NONE, false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, model);
		
		matrix.popPose();
		
		matrix.pushPose();
		traslateRotate("rightarmgun", matrix);
		traslateRotate("gunwithhammer", matrix);
		traslateRotate("hammer", matrix);
		ModelManager man = Minecraft.getInstance().getModelManager();
		
		// Hammer
		BakedModel hammer = man.getModel(new ModelResourceLocation(new ResourceLocation(Constants
				.PRHAMMER), "inventory"));
		hammer = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, hammer, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext
				.NONE, false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, hammer);
		matrix.popPose();
		
		matrix.popPose();
	}
	
	@Override
	public void renderDefault(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light) {
		matrix.pushPose();
		traslateRotateD("all", matrix);
		matrix.pushPose();
		traslateRotateD("rightarmgun", matrix);
		traslateRotateD("rightarm", matrix);
		matrix.scale(1f, 1f, 3f);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.RIGHT);
		matrix.popPose();
		matrix.pushPose();
		traslateRotateD("leftarmgun", matrix);
		traslateRotateD("leftarm", matrix);
		matrix.scale(1f, 1f, 3f);
		RenderHelper.renderPlayerArm(matrix, buffer, light, 0, 0, HumanoidArm.LEFT);
		matrix.popPose();
		matrix.pushPose();
		traslateRotateD("leftarmgun", matrix);
		traslateRotateD("rightarmgun", matrix);
		// Gun
		traslateRotateD("gun", matrix);
		BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, player.level(), 
				player, 0);
		LogUtils.log("PirateRifleModel", "Overrides: " + model.getOverrides().getOverrides().size());
		
				//Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
		/*model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, model, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);*/
		
		RenderType type = RenderTypeHelper.getFallbackItemRenderType(stack, model, false);
		VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(buffer, type, true, false);
        matrix.translate(-0.5F, -0.5F, -0.5F);
		Minecraft.getInstance().getItemRenderer().renderModelLists(model, stack, light, 
				OverlayTexture.NO_OVERLAY, matrix, vertexConsumer);
		//ItemRenderer
		/*Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, 
				false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, Minecraft.getInstance()
				.getItemRenderer().getItemModelShaper().getItemModel(stack));*/
		matrix.popPose();
		
		matrix.pushPose();
		traslateRotateD("rightarmgun", matrix);
		traslateRotateD("gunwithhammer", matrix);
		traslateRotateD("hammer", matrix);
		ModelManager man = Minecraft.getInstance().getModelManager();
		
		// Hammer
		BakedModel hammer = man.getModel(new ModelResourceLocation(new ResourceLocation(Constants
				.PRHAMMER), "inventory"));
		hammer = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, hammer, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext
				.NONE, false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, hammer);
		matrix.popPose();
		
		matrix.popPose();
	}

	@Override
	public ArmPose getArmPose(ItemStack stack) {
		return ArmPose.SPYGLASS;
	}
	
	@Override
	public <T extends LivingEntity> void handleArmPose(ArmPose pose, ItemStack stack, JgHumanoidModel<T> model,
			HumanoidArm arm) {
		if(pose == ArmPose.SPYGLASS) {
			model.rightArm.yRot = -0.1F + model.head.yRot;
			model.leftArm.yRot = 0.1F + model.head.yRot + 0.5235987f;
			model.rightArm.x = -4f;
			model.rightArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot;
            model.leftArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot + -0.034906566f;
	    }
	}
	
	public void removeScope() {
		// Start removing scope animation
		animator.play();
		if(animator.getCurrent() == null) {
			animator.setCurrent(this, REMOVESCOPE);
			animator.play();
			LogUtils.log("PirateRifleModel", "Starting animation");
		} else {
			LogUtils.log("PirateRifleModel", "Current animation: " + animator.getCurrent().getName());
		}
	}
	
	@Override
	public void onAnimationEnd(Animation anim) {
		LogUtils.log("PirateRifleModel", "Animation ended: " + anim.getName());
	}

	@Override
	public void onAnimationStart(Animation anim) {
		
	}

	@Override
	public void onAnimationTick(Animation anim, float prevTick, float tick) {
		Animation current = animator.getCurrent();
		
		prevTick = (float) Math.floor(prevTick);
		tick = (float) Math.floor(tick);
		
		if(Math.abs(tick - prevTick) > 0) {
			if(current == HIT) {
				if(tick == 15) {
					Utils.playSoundOnServer(SoundRegistries.GUN_MOVING.get());
				} else if(tick == 22) {
					Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
				} else if(tick == 24) {
					MeleeHelper.hit(client, Config.SERVER.prMeleeHitDamage.get());
				}
			} else if(current == RELOAD) {
				if(tick == 16) {
					Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
					
					consumeItems(Config.SERVER.prGunpowderToReload.get(), 1, false);
					
				} else if(tick == 36) {
					Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
				} else if(tick == 55) {
					Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
				} else if(tick == 111) {
					Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
				} else if(tick == 137) {
					Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
				} else if(tick == 158) {
					Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
				} else if(tick == 161) {
					Utils.playSoundOnServer(SoundRegistries.SMALL_BULLET_HITTING_METAL.get());
				}
			} else if(current == REMOVESCOPE) {
				if(tick == 182) {
					boolean hasScope = NBTUtils.hasScope(Minecraft.getInstance()
							.player.getMainHandItem());
					PirateGuns.channel.sendToServer(new SetScopeMessage(!hasScope));
				}
			}
		}
	}

	@Override
	public Animation getLookAnimAnimation(LocalPlayer player) {
		return LOOK;
	}
	
	@Override
	public Animation getKickbackAnimAnimation(LocalPlayer player) {
		return HIT;
	}
	
	@Override
	public List<Animation> getAnimations() {
		return List.of(LOOK, RELOAD, HIT, REMOVESCOPE);
	}

}
