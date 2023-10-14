package com.jg.jgpg.client.model.models;

import java.util.List;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractGunModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.model.player.JgHumanoidModel;
import com.jg.jgpg.client.model.player.JgHumanoidModel.ArmPose;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.registries.SoundRegistries;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.InventoryUtils;
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
import net.minecraftforge.client.RenderTypeHelper;

public class TrabucoModel extends AbstractGunModel {

	public static Animation LOOK = new Animation("Test");
	public static Animation RELOAD = new Animation("Reload");
	public static Animation HIT = new Animation("Hit");
	
	public TrabucoModel(ClientHandler handler) {
		super(handler);
	}

	@Override
	public void initParts() {
		
		addPart(new JgModelPart("gun", 0.59f, -0.70f, -1.03f, 0.100000136f, 0, 0));
		addPart(new JgModelPart("hammer", 0.6000002f, -0.2800004f, -0.92000043f, -1.5057038f, 0.0f, 
				0.0f));
		addPart(new JgModelPart("leftarm", -0.04f, -0.12f, 1.13f, -0.14f, -0.44f, -0.64f));
		addPart(new JgModelPart("rightarm", 0.28f, -0.16f, 1.93f, -0.14f, 0.2f, 0.6f));
		addPart(new JgModelPart("rightarmgun", 0, 0, 0, 0, 0, 0f));
		addPart(new JgModelPart("leftarmgun", 0, 0, 0, 0, 0, 0f));
		addPart(new JgModelPart("gunwithhammer", 0, 0, 0, 0, 0, 0f));
		addPart(new JgModelPart("all", 0.34f, -0.21f, 0.06f, 0f, 0.09f, 0f));
		addPart(new JgModelPart("aim", -0.8719993f, 0.68199974f, 0.20000002f, -0.10722665f, -0.08499042f, 0f));
		addPart(new JgModelPart("sprint", 0.09999999f, -1.379999f, 0.0f, 0.97738487f, -0.069813184f, 
				0.0f));
		addPart(new JgModelPart("recoil", -0.04f, 0.0f, 0.3999999f, 0.0034906575f, -0.0034906585f, 
				-9.313226E-10f));
		
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
				.traslate(getPart("leftarm"), -0.70053077f, 0.12013654f, 0.6404853f, "easeOutCirc")
				.addKeyframe(53)
				.traslate(getPart("leftarm"), -0.69999963f, 0.17999999f, 0.6399997f, "empty")
				.addKeyframe(63)
				.traslate(getPart("leftarm"), -0.70053077f, 0.12013654f, 0.6404853f, "easeOutCirc")
				.addKeyframe(64)
				.traslate(getPart("rightarmgun"), -0.79999954f, -0.57999974f, -0.28f, "empty")
				.rotate(getPart("rightarmgun"), 0.02f, -0.02f, 0.7399996f, "empty")
				.addKeyframe(73)
				.traslate(getPart("rightarmgun"), 1.8624916f, -1.7609389f, -0.060433216f, "empty")
				.traslate(getPart("leftarm"), -0.9605305f, -0.49986327f, 0.6404853f, "empty")
				.rotate(getPart("rightarmgun"), 1.0603341f, 0.91454566f, 0.009834299f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(83)
				.traslate(getPart("leftarm"), -0.70053077f, 0.12013654f, 0.6404853f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(92)
				.traslate(getPart("leftarm"), -2.0976338f, 1.7171371f, -0.6995113f, "empty")
				.rotate(getPart("leftarm"), -0.77852225f, -0.55999976f, 0.0f, "empty")
				.addKeyframe(99)
				.traslate(getPart("leftarm"), -2.012524f, 1.945215f, -1.0650382f, "empty")
				.rotate(getPart("leftarm"), -0.955553f, -0.54628736f, 0.0f, "empty")
				.addKeyframe(107)
				.traslate(getPart("leftarm"), -2.012524f, 1.945215f, -1.0650382f, "empty")
				.rotate(getPart("leftarm"), -0.955553f, -0.54628736f, 0.0f, "empty")
				.addKeyframe(112)
				.traslate(getPart("leftarm"), -2.012524f, 0.70521617f, -1.0650382f, "empty")
				.rotate(getPart("leftarm"), -0.55555344f, -0.54628736f, 0.0f, "empty")
				.addKeyframe(117)
				.traslate(getPart("leftarm"), -2.012524f, -1.3147825f, -1.0650382f, "empty")
				.rotate(getPart("leftarm"), -0.09555366f, -0.54628736f, 0.0f, "empty")
				.addKeyframe(121)
				.traslate(getPart("leftarm"), -0.51440495f, -0.5411578f, 0.52105814f, "empty")
				.rotate(getPart("leftarm"), -1.4901161E-8f, 0.0f, 0.0f, "empty")
				.addKeyframe(131)
				.traslate(getPart("leftarm"), -0.2544052f, 0.178842f, 0.8610578f, "empty")
				.rotate(getPart("leftarm"), -1.4901161E-8f, 0.0f, 0.0f, "empty")
				.addKeyframe(141)
				.traslate(getPart("leftarm"), -0.15440515f, 0.07884198f, 0.8610578f, "easeOutBack")
				.rotate(getPart("leftarm"), -1.4901161E-8f, 0.0f, 0.0f, "easeOutBack")
				.addKeyframe(142)
				.traslate(getPart("leftarm"), -0.15440515f, 0.07884198f, 0.8610578f, "easeOutBack")
				.rotate(getPart("leftarm"), -1.4901161E-8f, 0.0f, 0.0f, "easeOutBack")
				.addKeyframe(151)
				.traslate(getPart("leftarm"), -0.2544052f, 0.178842f, 0.8610578f, "empty")
				.rotate(getPart("leftarm"), -1.4901161E-8f, 0.0f, 0.0f, "empty")
				.addKeyframe(161)
				.traslate(getPart("leftarm"), -0.15440515f, 0.07884198f, 0.8610578f, "easeOutBack")
				.rotate(getPart("leftarm"), -1.4901161E-8f, 0.0f, 0.0f, "easeOutBack")
				.addKeyframe(162)
				.traslate(getPart("leftarm"), -0.15440515f, 0.07884198f, 0.8610578f, "easeOutBack")
				.rotate(getPart("leftarm"), -1.4901161E-8f, 0.0f, 0.0f, "easeOutBack")
				.addKeyframe(170)
				.traslate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarm"), 0.0f, 0.0f, 0.0f, "empty")
				.end();
		
		HIT = new Animation("HitAnimation")
				/*.addKeyframe(4)
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
				.rotate(getPart("hammer"), 0.02f, 0.9399996f, 0.0f, "empty")*/
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
		
		HIT = new Animation("HitAnimation")
				.addKeyframe(1)
				.traslate(getPart("all"), -0.3799999f, 0.24000004f, 0.0f, "easeOutExpo")
				.traslate(getPart("gunwithhammer"), 1.5399989f, 0.04f, -0.69999963f, "easeOutExpo")
				.traslate(getPart("leftarmgun"), -0.3799999f, 0.0f, 0.29999998f, "easeOutExpo")
				.traslate(getPart("rightarm"), 0.15999998f, -0.16f, 0.0f, "easeOutExpo")
				.rotate(getPart("gunwithhammer"), 0.0f, 0.94247824f, 0.0f, "easeOutExpo")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "easeOutExpo")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "easeOutExpo")
				.addKeyframe(3)
				.traslate(getPart("all"), -0.45999986f, 0.5399998f, 0.0f, "easeOutExpo")
				.traslate(getPart("gunwithhammer"), 2.0199986f, 0.09999999f, -2.3599982f, "easeOutExpo")
				.traslate(getPart("leftarmgun"), -0.5199998f, 0.0f, 0.65999967f, "easeOutExpo")
				.traslate(getPart("rightarm"), -0.12f, -0.11999998f, -0.35999992f, "easeOutExpo")
				.rotate(getPart("gunwithhammer"), 0.0f, 2.094396f, 0.0f, "easeOutExpo")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "easeOutExpo")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "easeOutExpo")
				.addKeyframe(4)
				.traslate(getPart("all"), -0.55999976f, 0.5399998f, -0.3999999f, "easeOutExpo")
				.traslate(getPart("gunwithhammer"), 1.6999989f, 0.09999999f, -3.0199976f, "easeOutExpo")
				.traslate(getPart("leftarmgun"), -0.5199998f, 0.0f, 0.79999954f, "easeOutExpo")
				.traslate(getPart("rightarm"), -0.26000005f, -0.11999998f, -0.49999982f, "easeOutExpo")
				.rotate(getPart("gunwithhammer"), 0.0f, 2.5132728f, 0.0f, "easeOutExpo")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "easeOutExpo")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "easeOutExpo")
				.addKeyframe(14)
				.traslate(getPart("all"), -0.55999976f, 0.5399998f, -0.3999999f, "empty")
				.traslate(getPart("gunwithhammer"), 1.6999989f, 0.09999999f, -3.0199976f, "empty")
				.traslate(getPart("leftarmgun"), -0.5199998f, 0.0f, 0.79999954f, "empty")
				.traslate(getPart("rightarm"), -0.26000005f, -0.11999998f, -0.49999982f, "empty")
				.rotate(getPart("gunwithhammer"), 0.0f, 2.5132728f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(19)
				.traslate(getPart("all"), -0.45999986f, 0.5399998f, 0.0f, "empty")
				.traslate(getPart("gunwithhammer"), 2.0199986f, 0.09999999f, -2.3599982f, "empty")
				.traslate(getPart("leftarmgun"), -0.5199998f, 0.0f, 0.65999967f, "empty")
				.traslate(getPart("rightarm"), -0.12f, -0.11999998f, -0.35999992f, "empty")
				.rotate(getPart("gunwithhammer"), 0.0f, 2.094396f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(24)
				.traslate(getPart("all"), -0.3799999f, 0.24000004f, 0.0f, "empty")
				.traslate(getPart("gunwithhammer"), 1.5399989f, 0.04f, -0.69999963f, "empty")
				.traslate(getPart("leftarmgun"), -0.3799999f, 0.0f, 0.29999998f, "empty")
				.traslate(getPart("rightarm"), 0.15999998f, -0.16f, 0.0f, "empty")
				.rotate(getPart("gunwithhammer"), 0.0f, 0.94247824f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.addKeyframe(34)
				.traslate(getPart("gun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("all"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("gunwithhammer"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("hammer"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.traslate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("all"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("gunwithhammer"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("hammer"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("leftarmgun"), 0.0f, 0.0f, 0.0f, "empty")
				.rotate(getPart("rightarm"), 0.0f, 0.0f, 0.0f, "empty")
				.end();
	}
	
	@Override
	public boolean canReload(LocalPlayer player) {
		int gunPowder = InventoryUtils.getCountForItem(player, Items.GUNPOWDER);
		int bullets = InventoryUtils.getCountForItem(player, ItemRegistries.MUSKET_BULLET.get());
		return gunPowder >= Config.SERVER.tGunpowderToReload.get() && bullets >= Config.SERVER
				.tBulletsPerShoot.get();
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
			getPart("hammer").getDtransform().set(0.59f, -0.70f, -1.03f, 0.100000136f, 0, 0);
		} else {
			getPart("hammer").getDtransform().set(0.6000002f, -0.2800004f, -0.92000043f, 
					-1.5057038f, 0.0f, 0.0f);
		}
		
		float p = 0f;
		if(player.getCooldowns().isOnCooldown(stack.getItem())) {
			p = getShootProgress(stack, 0.2f, "easeOutExpo", "easeInOutQuint");
			//LogUtils.log("PirateRifleModel", "ShootProgress: " + p);
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
		traslateRotate("gunwithhammer", matrix);
		// Gun
		traslateRotate("gun", matrix);
		
		BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, model, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		
		Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext
				.NONE, false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, model);

		matrix.popPose();
		
		matrix.pushPose();
		traslateRotate("leftarmgun", matrix);
		traslateRotate("rightarmgun", matrix);
		traslateRotate("gunwithhammer", matrix);
		traslateRotate("hammer", matrix);
		ModelManager man = Minecraft.getInstance().getModelManager();
		
		// Hammer
		BakedModel hammer = man.getModel(new ModelResourceLocation(new ResourceLocation(Constants
				.THAMMER), "inventory"));
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
		BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrix, model, 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
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
		matrix.popPose();
	}
	
	@Override
	public ArmPose getArmPose(ItemStack stack) {
		return ArmPose.SPYGLASS;
	}
	
	@Override
	public <T extends LivingEntity> void handleArmPose(ArmPose pose, ItemStack stack, 
			JgHumanoidModel<T> model, HumanoidArm arm) {
		if(pose == ArmPose.SPYGLASS) {
			model.rightArm.yRot = -0.1F + model.head.yRot;
			model.leftArm.yRot = 0.1F + model.head.yRot + 0.5235987f;
			model.rightArm.x = -4f;
			model.rightArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot;
            model.leftArm.xRot = (-(float)Math.PI / 2F) + model.head.xRot + -0.034906566f;
	    }
	}
	
	@Override
	public void onAnimationEnd(Animation anim) {
		
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
				if(tick == 0) {
					Utils.playSoundOnServer(SoundRegistries.GUN_MOVING.get());
				} else if(tick == 2) {
					Utils.playSoundOnServer(SoundRegistries.GUN_SWING.get());
				} else if(tick == 4) {
					MeleeHelper.hit(client, Config.SERVER.tMeleeHitDamage.get());
				}
			} else if(current == RELOAD) {
				if(tick == 17) {
					Utils.playSoundOnServer(SoundRegistries.FLINTLOCK_HAMMER_BACK.get());
					
					consumeItems(Config.SERVER.tGunpowderToReload.get(), Config.SERVER
							.tBulletsPerShoot.get(), true);
					shouldUpdate = true;
					
				} else if(tick == 36) {
					Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_1.get());
				} else if(tick == 56) {
					Utils.playSoundOnServer(SoundRegistries.GUNPOWDER_DUST_2.get());
				} else if(tick == 100) {
					Utils.playSoundOnServer(SoundRegistries.MULTIPLE_BULLETS_HITTING_METAL.get());
				} else if(tick == 133) {
					Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
				} else if(tick == 158) {
					Utils.playSoundOnServer(SoundRegistries.METAL_SLIDING.get());
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
		return List.of(LOOK, RELOAD, HIT);
	}

}
