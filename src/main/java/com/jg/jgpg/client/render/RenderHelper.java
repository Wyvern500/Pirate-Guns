package com.jg.jgpg.client.render;

import com.google.common.base.MoreObjects;
import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.player.render.JgPlayerRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

public class RenderHelper {

	public static ResourceLocation MUZZLE = PirateGuns.prefix("textures/effects/muzzle_flash.png");
	public static ResourceLocation HITMARKER = PirateGuns.prefix("textures/effects/hitmarker.png");
	
	private static GuiGraphics guiRender;
	public static RenderType itemRenderType = RenderType
			.entityTranslucentCull(ResourceLocation
					.withDefaultNamespace("textures/atlas/blocks.png"));

	private static float mainHandHeight;
	private static float oMainHandHeight;
	private static float offHandHeight;
	private static float oOffHandHeight;

	public static void applyHandTransformations(PoseStack matrix, float pSwingProgress,
												float pEquippedProgress) {
		float f5 = -0.4F * Mth.sin(Mth.sqrt(pSwingProgress) * (float)Math.PI);
		float f6 = 0.2F * Mth.sin(Mth.sqrt(pSwingProgress) * ((float)Math.PI * 2F));
		float f10 = -0.2F * Mth.sin(pSwingProgress * (float)Math.PI);
		int l = 1;
		matrix.translate((float)l * f5, f6, f10);
		applyItemArmTransform(matrix, HumanoidArm.RIGHT, pEquippedProgress);
		applyItemArmAttackTransform(matrix, HumanoidArm.RIGHT, pSwingProgress);
	}

	private static void applyItemArmAttackTransform(PoseStack pPoseStack, HumanoidArm pHand,
											 float pSwingProgress) {
		int i = pHand == HumanoidArm.RIGHT ? 1 : -1;
		float f = Mth.sin(pSwingProgress * pSwingProgress * (float)Math.PI);
		pPoseStack.mulPose(Axis.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
		float f1 = Mth.sin(Mth.sqrt(pSwingProgress) * (float)Math.PI);
		pPoseStack.mulPose(Axis.ZP.rotationDegrees((float)i * f1 * -20.0F));
		pPoseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
		pPoseStack.mulPose(Axis.YP.rotationDegrees((float)i * -45.0F));
	}

	private static void applyItemArmTransform(PoseStack pPoseStack, HumanoidArm pHand,
											  float pEquippedProg) {
		int i = pHand == HumanoidArm.RIGHT ? 1 : -1;
		pPoseStack.translate((float)i * 0.56F, -0.52F + pEquippedProg * -0.6F,
				-0.72F);
	}

	public static void renderScopeOverlay(float progress, GuiGraphics gg) {
		int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

		float f = (float)Math.min(screenWidth, screenHeight);
		float f1 = Math.min((float)screenWidth / f, (float)screenHeight / f) *
				Mth.lerp((progress - 0.5f) / 0.5f, 0.01f, 1.125f);
		int i = Mth.floor(f * f1);
		int j = Mth.floor(f * f1);
		int k = (screenWidth - i) / 2;
		int l = (screenHeight - j) / 2;
		int i1 = k + i;
		int j1 = l + j;
		gg.blit(PirateGuns.prefix("textures/misc/scope.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
		gg.fill(RenderType.guiOverlay(), 0, j1, screenWidth, screenHeight, -90, -16777216);
		gg.fill(RenderType.guiOverlay(), 0, 0, screenWidth, l, -90, -16777216);
		gg.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
		gg.fill(RenderType.guiOverlay(), i1, l, screenWidth, j1, -90, -16777216);
	}

	public static void renderScopeOverlay(float progress) {
		if(guiRender == null){
			guiRender = new GuiGraphics(Minecraft.getInstance(), Minecraft
					.getInstance().renderBuffers().bufferSource());
		}

		int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

		float f = (float)Math.min(screenWidth, screenHeight);
		float f1 = Math.min((float)screenWidth / f, (float)screenHeight / f) *
				Mth.lerp((progress - 0.5f) / 0.5f, 0.01f, 1.125f);
		int i = Mth.floor(f * f1);
		int j = Mth.floor(f * f1);
		int k = (screenWidth - i) / 2;
		int l = (screenHeight - j) / 2;
		int i1 = k + i;
		int j1 = l + j;
		guiRender.blit(PirateGuns.prefix("textures/misc/scope.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
		guiRender.fill(RenderType.guiOverlay(), 0, j1, screenWidth, screenHeight, -90, -16777216);
		guiRender.fill(RenderType.guiOverlay(), 0, 0, screenWidth, l, -90, -16777216);
		guiRender.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
		guiRender.fill(RenderType.guiOverlay(), i1, l, screenWidth, j1, -90, -16777216);
	}

	public static void renderItem(PoseStack matrix, ItemStack stack, MultiBufferSource buffer,
								  int light) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		BakedModel model = itemRenderer.getItemModelShaper().getItemModel(stack);
		model = ClientHooks.handleCameraTransforms(matrix, model,
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		matrix.translate(-0.5f, -0.5f, -0.5f);
		VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(buffer, itemRenderType, true, 
				stack.hasFoil());
		itemRenderer.renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, matrix, 
				vertexconsumer);
	}

	public static void renderItem(PoseStack matrix, ItemStack stack, VertexConsumer consumer,
								  int light) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		BakedModel model = itemRenderer.getItemModelShaper().getItemModel(stack);
		model = ClientHooks.handleCameraTransforms(matrix, model,
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		matrix.translate(-0.5f, -0.5f, -0.5f);
		itemRenderer.renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, matrix,
				consumer);
	}
	
	public static void renderMag(PoseStack matrix, ItemStack stack, MultiBufferSource buffer, int light) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		BakedModel model = itemRenderer.getItemModelShaper().getItemModel(stack);
		model = ClientHooks.handleCameraTransforms(matrix, model,
				ItemDisplayContext.HEAD, false);
		matrix.translate(-0.5f, -0.5f, -0.5f);
		VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(buffer, itemRenderType, true, 
				stack.hasFoil());
		itemRenderer.renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, matrix, 
				vertexconsumer);
	}
	
	public static void renderSpecial(PoseStack matrix, ItemStack stack, MultiBufferSource buffer, int light, 
			String path) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(
				ResourceLocation.parse(path), "standalone"));
		model = ClientHooks.handleCameraTransforms(matrix, model,
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		matrix.translate(-0.5f, -0.5f, -0.5f);
		VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(buffer, itemRenderType, true, 
				stack.hasFoil());
		itemRenderer.renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, matrix, 
				vertexconsumer);
	}

	public static void renderSpecial(PoseStack matrix, ItemStack stack, VertexConsumer consumer,
									 int light, String path) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		BakedModel model = Minecraft.getInstance().getModelManager()
				.getModel(new ModelResourceLocation(ResourceLocation.parse(path), "inventory"));
		model = ClientHooks.handleCameraTransforms(matrix, model,
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false);
		matrix.translate(-0.5f, -0.5f, -0.5f);
		itemRenderer.renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, matrix,
				consumer);
	}
	
	public static void renderModelLists(BakedModel model, ItemStack stack, int p_115192_, int p_115193_,
			PoseStack p_115194_, VertexConsumer p_115195_) {
		RandomSource randomsource = RandomSource.create();

		for (Direction direction : Direction.values()) {
			randomsource.setSeed(42L);
			renderQuadList(p_115194_, p_115195_, model.getQuads((BlockState) null, direction, 
					randomsource), stack, p_115192_, p_115193_);
		}

		randomsource.setSeed(42L);
		renderQuadList(p_115194_, p_115195_, model.getQuads((BlockState) null, (Direction) null, 
				randomsource), stack, p_115192_, p_115193_);
	}
	
	public static void renderQuadList(PoseStack p_115163_, VertexConsumer p_115164_, 
			List<BakedQuad> p_115165_, ItemStack p_115166_, int p_115167_, int p_115168_) {
		boolean flag = !p_115166_.isEmpty();

		for (BakedQuad bakedquad : p_115165_) {
			// Create a new PoseStack for each quad's transformation.
			PoseStack quadTransformStack = new PoseStack();
			float x = 0;
			float y = 0;
			float z = 0;
			float rx = 0;
			float ry = 0;
			float rz = 0;
			rx = (float) Math.sin(Minecraft.getInstance().getTimer()
					.getGameTimeDeltaTicks() * 40);
			quadTransformStack.translate(x, y, z);
			quadTransformStack.rotateAround(new Quaternionf(Math.toRadians(rx), ry, rz, 1), x, y, z);
			// Apply the current transformation to the new stack.
	        quadTransformStack.last().pose().mul(p_115163_.last().pose());
			
			int i = -1;
			if (flag && bakedquad.isTinted()) {
				i = Minecraft.getInstance().getItemColors().getColor(p_115166_, bakedquad.getTintIndex());
			}

			float f = (float) (i >> 16 & 255) / 255.0F;
			float f1 = (float) (i >> 8 & 255) / 255.0F;
			float f2 = (float) (i & 255) / 255.0F;
			
			quadTransformStack.translate(0.1f, 0.1f, 0.1f);
			p_115164_.putBulkData(quadTransformStack.last(), bakedquad, f, f1, f2, 1.0F, p_115167_, p_115168_, true);
		}

	}

	public static GuiGraphics getGuiGraphics(){
		if(guiRender == null){
			guiRender = new GuiGraphics(Minecraft.getInstance(), Minecraft
					.getInstance().renderBuffers().bufferSource());
		}
		return guiRender;
	}

	public static void fill(PoseStack matrix, int p_281437_, int p_283660_, int p_282606_,
					 int p_283413_, int p_283428_, int p_283253_) {
		innerRect(RenderType.gui(), matrix, p_281437_, p_283660_, p_282606_,
				p_283413_, p_283428_, p_283253_);
	}

	public static void rect(GuiGraphics guiRender, int x, int y, int w, int h, int setColor){
		innerRect(RenderType.guiOverlay(), guiRender.pose(), x, y, x + w,
				y + h, -90, setColor);
	}

	public static void innerRect(RenderType p_286711_, PoseStack matrix, int p_286234_,
					 int p_286444_, int p_286244_, int p_286411_, int p_286671_,
					 int p_286599_) {
		Matrix4f matrix4f = matrix.last().pose();
		if (p_286234_ < p_286244_) {
			int i = p_286234_;
			p_286234_ = p_286244_;
			p_286244_ = i;
		}

		if (p_286444_ < p_286411_) {
			int j = p_286444_;
			p_286444_ = p_286411_;
			p_286411_ = j;
		}

		BufferSource source = Minecraft.getInstance().renderBuffers()
				.bufferSource();

		float f3 = (float) FastColor.ARGB32.alpha(p_286599_) / 255.0F;
		float f = (float)FastColor.ARGB32.red(p_286599_) / 255.0F;
		float f1 = (float)FastColor.ARGB32.green(p_286599_) / 255.0F;
		float f2 = (float)FastColor.ARGB32.blue(p_286599_) / 255.0F;
		VertexConsumer vertexconsumer = source.getBuffer(p_286711_);
		vertexconsumer.addVertex(matrix4f, (float)p_286234_, (float)p_286444_,
				(float)p_286671_).setColor(f, f1, f2, f3);
		vertexconsumer.addVertex(matrix4f, (float)p_286234_, (float)p_286411_,
				(float)p_286671_).setColor(f, f1, f2, f3);
		vertexconsumer.addVertex(matrix4f, (float)p_286244_, (float)p_286411_,
				(float)p_286671_).setColor(f, f1, f2, f3);
		vertexconsumer.addVertex(matrix4f, (float)p_286244_, (float)p_286444_,
				(float)p_286671_).setColor(f, f1, f2, f3);

		RenderSystem.disableDepthTest();
		source.endBatch();
		RenderSystem.enableDepthTest();
	}

	public static void renderPlayerArm(PoseStack matrix, MultiBufferSource buffer, int light, float p_109350_,
			float p_109351_, HumanoidArm p_109352_, JgPlayerRenderer playerRenderer) {
		boolean flag = p_109352_ != HumanoidArm.LEFT;
		float f = flag ? 1.0F : -1.0F;
		float f1 = Mth.sqrt(p_109351_);
		float f2 = -0.3F * Mth.sin(f1 * (float)Math.PI);
		float f3 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
		float f4 = -0.4F * Mth.sin(p_109351_ * (float)Math.PI);
		matrix.translate(f * (f2 + 0.64000005F), f3 + -0.6F +
				p_109350_ * -0.6F, f4 + -0.71999997F);
		matrix.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
		float f5 = Mth.sin(p_109351_ * p_109351_ * (float)Math.PI);
		float f6 = Mth.sin(f1 * (float)Math.PI);
		matrix.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
		matrix.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
		AbstractClientPlayer abstractclientplayer = Minecraft.getInstance().player;
		RenderSystem.setShaderTexture(0, abstractclientplayer.getSkin()
				.texture());
		matrix.translate(f * -1.0F, 3.6F, 3.5F);
		matrix.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
		matrix.mulPose(Axis.XP.rotationDegrees(200.0F));
		matrix.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
		matrix.translate(f * 5.6F, 0.0F, 0.0F);
		/*PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
				.<AbstractClientPlayer>getRenderer(abstractclientplayer);*/
		if (flag) {
			playerRenderer.renderRightHand(matrix, buffer, light, abstractclientplayer);
		} else {
			playerRenderer.renderLeftHand(matrix, buffer, light, abstractclientplayer);
		}
	}

	// GUI

	public static void blit(PoseStack p_93201_, int p_93202_, int p_93203_, int p_93204_, int p_93205_, int p_93206_,
			TextureAtlasSprite p_93207_) {
		innerBlit(p_93201_.last().pose(), p_93202_, p_93202_ + p_93205_, p_93203_, p_93203_ + p_93206_, p_93204_,
				p_93207_.getU0(), p_93207_.getU1(), p_93207_.getV0(), p_93207_.getV1());
	}

	public void blit(PoseStack p_93229_, int p_93230_, int p_93231_, int p_93232_, int p_93233_, int p_93234_,
			int p_93235_) {
		blit(p_93229_, p_93230_, p_93231_, -90, (float) p_93232_, (float) p_93233_, p_93234_, p_93235_, 256, 256);
	}

	public static void blit(PoseStack p_93144_, int p_93145_, int p_93146_, int p_93147_, float p_93148_,
			float p_93149_, int p_93150_, int p_93151_, int p_93152_, int p_93153_) {
		innerBlit(p_93144_, p_93145_, p_93145_ + p_93150_, p_93146_, p_93146_ + p_93151_, p_93147_, p_93150_, p_93151_,
				p_93148_, p_93149_, p_93152_, p_93153_);
	}

	public static void blit(PoseStack p_93161_, int p_93162_, int p_93163_, int p_93164_, int p_93165_, float p_93166_,
			float p_93167_, int p_93168_, int p_93169_, int p_93170_, int p_93171_) {
		innerBlit(p_93161_, p_93162_, p_93162_ + p_93164_, p_93163_, p_93163_ + p_93165_, 0, p_93168_, p_93169_,
				p_93166_, p_93167_, p_93170_, p_93171_);
	}

	public static void blit(PoseStack p_93134_, int p_93135_, int p_93136_, float p_93137_, float p_93138_,
			int p_93139_, int p_93140_, int p_93141_, int p_93142_) {
		blit(p_93134_, p_93135_, p_93136_, p_93139_, p_93140_, p_93137_, p_93138_, p_93139_, p_93140_, p_93141_,
				p_93142_);
	}

	private static void innerBlit(PoseStack p_93188_, int p_93189_, int p_93190_, int p_93191_, int p_93192_,
			int p_93193_, int p_93194_, int p_93195_, float p_93196_, float p_93197_, int p_93198_, int p_93199_) {
		innerBlit(p_93188_.last().pose(), p_93189_, p_93190_, p_93191_, p_93192_, p_93193_,
				(p_93196_ + 0.0F) / (float) p_93198_, (p_93196_ + (float) p_93194_) / (float) p_93198_,
				(p_93197_ + 0.0F) / (float) p_93199_, (p_93197_ + (float) p_93195_) / (float) p_93199_);
	}

	private static void innerBlit(Matrix4f p_93113_, int p_93114_, int p_93115_, int p_93116_, int p_93117_,
								  int p_93118_, float p_93119_, float p_93120_, float p_93121_, float p_93122_) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance()
				.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.addVertex(p_93113_, (float) p_93114_, (float) p_93117_,
						(float) p_93118_).setUv(p_93119_, p_93122_);
		bufferbuilder.addVertex(p_93113_, (float) p_93115_, (float) p_93117_,
						(float) p_93118_).setUv(p_93120_, p_93122_);
		bufferbuilder.addVertex(p_93113_, (float) p_93115_, (float) p_93116_,
						(float) p_93118_).setUv(p_93120_, p_93121_);
		bufferbuilder.addVertex(p_93113_, (float) p_93114_, (float) p_93116_,
						(float) p_93118_).setUv(p_93119_, p_93121_);

		BufferUploader.draw(bufferbuilder.buildOrThrow());
	}

	// new methods

	public static void blit(PoseStack matrix, int p_282416_, int p_282989_, int p_282618_,
					 int p_282755_, int p_281717_, TextureAtlasSprite p_281874_,
					 float p_283559_, float p_282730_, float p_283530_,
					 float p_282246_) {
		innerBlit(p_281874_.atlasLocation(), matrix, p_282416_,
				p_282416_ + p_282755_, p_282989_,
				p_282989_ + p_281717_, p_282618_, p_281874_.getU0(),
				p_281874_.getU1(), p_281874_.getV0(), p_281874_.getV1(), p_283559_,
				p_282730_, p_283530_, p_282246_);
	}

	public static void blit(ResourceLocation p_283377_, PoseStack matrix, int p_281970_,
					 int p_282111_, int p_283134_, int p_282778_, int p_281478_,
					 int p_281821_) {
		blit(p_283377_, matrix, p_281970_, p_282111_, 0, (float)p_283134_, (float)p_282778_, p_281478_, p_281821_, 256, 256);
	}

	public static void blit(ResourceLocation p_283573_, PoseStack matrix, int p_283574_,
					 int p_283670_, int p_283545_, float p_283029_, float p_283061_,
					 int p_282845_, int p_282558_, int p_282832_, int p_281851_) {
		blit(p_283573_, matrix, p_283574_, p_283574_ + p_282845_,
				p_283670_, p_283670_ + p_282558_, p_283545_, p_282845_,
				p_282558_, p_283029_, p_283061_, p_282832_, p_281851_);
	}

	public static void blit(ResourceLocation p_282034_, PoseStack matrix, int p_283671_,
					 int p_282377_, int p_282058_, int p_281939_, float p_282285_,
					 float p_283199_, int p_282186_, int p_282322_, int p_282481_,
					 int p_281887_) {
		blit(p_282034_, matrix, p_283671_, p_283671_ + p_282058_, p_282377_,
				p_282377_ + p_281939_, 0, p_282186_, p_282322_,
				p_282285_, p_283199_, p_282481_, p_281887_);
	}

	public static void blit(ResourceLocation p_283272_, PoseStack matrix, int p_283605_,
					 int p_281879_, float p_282809_, float p_282942_, int p_281922_,
					 int p_282385_, int p_282596_, int p_281699_) {
		blit(p_283272_, matrix, p_283605_, p_281879_, p_281922_, p_282385_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
	}

	public static void blit(ResourceLocation p_282639_, PoseStack matrix, int p_282732_,
			  int p_283541_, int p_281760_, int p_283298_, int p_283429_,
			  int p_282193_, int p_281980_, float p_282660_, float p_281522_,
			  int p_282315_, int p_281436_) {
		innerBlit(p_282639_, matrix, p_282732_, p_283541_, p_281760_,
				p_283298_, p_283429_, (p_282660_ + 0.0F) / (float)p_282315_,
				(p_282660_ + (float)p_282193_) / (float)p_282315_,
				(p_281522_ + 0.0F) / (float)p_281436_,
				(p_281522_ + (float)p_281980_) / (float)p_281436_);
	}

	public static void innerBlit(ResourceLocation p_283461_, PoseStack matrix,
								 int p_281399_, int p_283222_, int p_283615_,
								 int p_283430_, int p_281729_, float p_283247_,
								 float p_282598_, float p_282883_, float p_283017_) {
		RenderSystem.setShaderTexture(0, p_283461_);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix4f = matrix.last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance()
				.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.addVertex(matrix4f, (float)p_281399_, (float)p_283615_, 
				(float)p_281729_).setUv(p_283247_, p_282883_);
		bufferbuilder.addVertex(matrix4f, (float)p_281399_, (float)p_283430_, 
				(float)p_281729_).setUv(p_283247_, p_283017_);
		bufferbuilder.addVertex(matrix4f, (float)p_283222_, (float)p_283430_, 
				(float)p_281729_).setUv(p_282598_, p_283017_);
		bufferbuilder.addVertex(matrix4f, (float)p_283222_, (float)p_283615_, 
				(float)p_281729_).setUv(p_282598_, p_282883_);
		BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
	}

	public static void innerBlit(ResourceLocation p_283254_, PoseStack matrix, int p_283092_,
				   int p_281930_, int p_282113_, int p_281388_, int p_283583_,
				   float p_281327_, float p_281676_, float p_283166_,
				   float p_282630_, float p_282800_, float p_282850_,
				   float p_282375_, float p_282754_) {
		RenderSystem.setShaderTexture(0, p_283254_);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = matrix.last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance()
				.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferbuilder.addVertex(matrix4f, (float)p_283092_, (float)p_282113_,
				(float)p_283583_).setUv(p_281327_, p_283166_)
				.setColor(p_282800_, p_282850_, p_282375_, p_282754_);
		bufferbuilder.addVertex(matrix4f, (float)p_283092_, (float)p_281388_,
				(float)p_283583_).setUv(p_281327_, p_282630_)
				.setColor(p_282800_, p_282850_, p_282375_, p_282754_);
		bufferbuilder.addVertex(matrix4f, (float)p_281930_, (float)p_281388_,
				(float)p_283583_).setColor(p_282800_, p_282850_, p_282375_, p_282754_)
				.setUv(p_281676_, p_282630_);
		bufferbuilder.addVertex(matrix4f, (float)p_281930_, (float)p_282113_,
				(float)p_283583_)
				.setUv(p_281676_, p_283166_)
				.setColor(p_282800_, p_282850_, p_282375_, p_282754_);
		BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
		RenderSystem.disableBlend();
	}
	
	public static void renderHitmarker(PoseStack stack, BufferSource impl, float partialTicks, 
			int size) {
		int window_width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int window_height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		
		stack.pushPose();
		
		stack.translate((window_width / 2) - (size / 2), (window_height / 2) - (size / 2), 0);
		
		Matrix4f matrix = stack.last().pose();
		
		VertexConsumer builder = impl.getBuffer(RenderTypes.HITMARKER);
		builder.addVertex(matrix, 0, size, 0).setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(0, 1.0F).setUv2(15728880, 15728880);
		builder.addVertex(matrix, size, size, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(1, 1.0F).setUv2(15728880, 15728880);
		builder.addVertex(matrix, size, 0, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(1, 0).setUv2(15728880, 15728880);
		builder.addVertex(matrix, 0, 0, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(0, 0).setUv2(15728880, 15728880);

		stack.popPose();
	}
	
	public static void renderMuzzleFlash(PoseStack stack, BufferSource impl, float partialTicks) {
		stack.pushPose();

		stack.scale(0.03F, 0.03F, 0.0F);

		// float scale = 0.5F + 0.5F * (1.0F - partialTicks);
		// stack.scale(scale, scale, 1.0F);
		float r = (float) (360f * Math.random());
		// System.out.println(r);
		stack.mulPose(Axis.ZP.rotationDegrees(r));
		stack.translate(-8 / 2, -8 / 2, 0);

		float minU = 0.0f;
		float maxU = 0.5f;
		Matrix4f matrix = stack.last().pose();
		VertexConsumer builder = impl.getBuffer(RenderTypes.MUZZLE_FLASH);
		builder.addVertex(matrix, 0, 0, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(maxU, 1.0F).setUv2(15728880, 15728880);
		builder.addVertex(matrix, 8, 0, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(minU, 1.0F).setUv2(15728880, 15728880);
		builder.addVertex(matrix, 8, 8, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(minU, 0).setUv2(15728880, 15728880);
		builder.addVertex(matrix, 0, 8, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(maxU, 0).setUv2(15728880, 15728880);

		stack.popPose();
	}
	
	public static void renderMuzzleFlash(PoseStack stack, BufferSource impl, float partialTicks, float xo, 
			float yo, float zo) {
		stack.pushPose();
		stack.translate(xo, yo, zo);

		stack.scale(0.03F, 0.03F, 0.0F);

		// float scale = 0.5F + 0.5F * (1.0F - partialTicks);
		// stack.scale(scale, scale, 1.0F);
		float r = (float) (360f * Math.random());
		// System.out.println(r);
		stack.mulPose(Axis.ZP.rotationDegrees(r));
		stack.translate(-8 / 2, -8 / 2, 0);

		float minU = 0.0f;
		float maxU = 0.5f;
		Matrix4f matrix = stack.last().pose();
		VertexConsumer builder = impl.getBuffer(RenderTypes.MUZZLE_FLASH);
		builder.addVertex(matrix, 0, 0, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(maxU, 1.0F).setUv2(15728880, 15728880);
		builder.addVertex(matrix, 8, 0, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(minU, 1.0F).setUv2(15728880, 15728880);
		builder.addVertex(matrix, 8, 8, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(minU, 0).setUv2(15728880, 15728880);
		builder.addVertex(matrix, 0, 8, 0)
				.setColor(1.0F, 1.0F, 1.0F, 1.0F)
				.setUv(maxU, 0).setUv2(15728880, 15728880);

		stack.popPose();
	}

	private static void quad(Matrix4f pMatrix, VertexConsumer pConsumer, float pX1, float pZ1,
							 int pIndex, float pX2, float pZ2, float pRed, float pGreen,
							 float pBlue, float p_115283_, float p_115284_, boolean p_115285_,
							 boolean p_115286_, boolean p_115287_, boolean p_115288_) {
		pConsumer.addVertex(pMatrix, pX1 + (p_115285_ ? p_115284_ : -p_115284_),
						(float)(pIndex * 16), pZ1 + (p_115286_ ? p_115284_ : -p_115284_))
				.setColor(pRed, pGreen, pBlue, 0.3F);
		pConsumer.addVertex(pMatrix, pX2 + (p_115285_ ? p_115283_ : -p_115283_),
						(float)((pIndex + 1) * 16), pZ2 + (p_115286_ ? p_115283_ : -p_115283_))
				.setColor(pRed, pGreen, pBlue, 0.3F);
		pConsumer.addVertex(pMatrix, pX2 + (p_115287_ ? p_115283_ : -p_115283_),
						(float)((pIndex + 1) * 16), pZ2 + (p_115288_ ? p_115283_ : -p_115283_))
				.setColor(pRed, pGreen, pBlue, 0.3F);
		pConsumer.addVertex(pMatrix, pX1 + (p_115287_ ? p_115284_ : -p_115284_),
						(float)(pIndex * 16), pZ1 + (p_115288_ ? p_115284_ : -p_115284_))
				.setColor(pRed, pGreen, pBlue, 0.3F);
	}

	public static void renderHandsWithItems(float p_109315_, PoseStack p_109316_,
											BufferSource p_109317_, LocalPlayer p_109318_, int p_109319_) {
		float f = p_109318_.getAttackAnim(p_109315_);
		InteractionHand interactionhand = MoreObjects.firstNonNull(p_109318_.swingingArm, InteractionHand.MAIN_HAND);
		float f1 = Mth.lerp(p_109315_, p_109318_.xRotO, p_109318_.getXRot());
		HandRenderSelection iteminhandrenderer$handrenderselection = evaluateWhichHandsToRender(p_109318_);
		float f2 = Mth.lerp(p_109315_, p_109318_.xBobO, p_109318_.xBob);
		float f3 = Mth.lerp(p_109315_, p_109318_.yBobO, p_109318_.yBob);
		p_109316_.mulPose(Axis.XP.rotationDegrees((p_109318_.getViewXRot(p_109315_) - f2) * 0.1F));
		p_109316_.mulPose(Axis.YP.rotationDegrees((p_109318_.getViewYRot(p_109315_) - f3) * 0.1F));
		if (iteminhandrenderer$handrenderselection.renderMainHand) {
			float f4 = interactionhand == InteractionHand.MAIN_HAND ? f : 0.0F;
			float f5 = 1.0F - Mth.lerp(p_109315_, oMainHandHeight, mainHandHeight);
			renderArmWithItem(p_109318_, p_109315_, f1, InteractionHand.MAIN_HAND, f4, f5,
					p_109316_, p_109317_, p_109319_);
		}

		if (iteminhandrenderer$handrenderselection.renderOffHand) {
			float f6 = interactionhand == InteractionHand.OFF_HAND ? f : 0.0F;
			float f7 = 1.0F - Mth.lerp(p_109315_, oOffHandHeight, offHandHeight);
			renderArmWithItem(p_109318_, p_109315_, f1, InteractionHand.OFF_HAND, f6, f7,
					p_109316_, p_109317_, p_109319_);
		}

		p_109317_.endBatch();
	}

	private static void renderArmWithItem(AbstractClientPlayer p_109372_, float p_109373_, float p_109374_,
										  InteractionHand p_109375_, float p_109376_, float p_109378_,
										  PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_) {
		if (!p_109372_.isScoping()) {
			boolean flag = p_109375_ == InteractionHand.MAIN_HAND;
			HumanoidArm humanoidarm = flag ? p_109372_.getMainArm() : p_109372_.getMainArm()
					.getOpposite();
			p_109379_.pushPose();
			if (flag) {
				renderPlayerArm(p_109379_, p_109380_, p_109381_, p_109378_, p_109376_,
						humanoidarm);
			}

			p_109379_.popPose();
		}
	}

	private static void renderPlayerArm(PoseStack p_109347_, MultiBufferSource p_109348_, int p_109349_, float p_109350_, float p_109351_, HumanoidArm p_109352_) {
		boolean flag = p_109352_ != HumanoidArm.LEFT;
		float f = flag ? 1.0F : -1.0F;
		float f1 = Mth.sqrt(p_109351_);
		float f2 = -0.3F * Mth.sin(f1 * (float)Math.PI);
		float f3 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
		float f4 = -0.4F * Mth.sin(p_109351_ * (float)Math.PI);
		p_109347_.translate(f * (f2 + 0.64000005F), f3 + -0.6F + p_109350_ * -0.6F, f4 + -0.71999997F);
		p_109347_.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
		float f5 = Mth.sin(p_109351_ * p_109351_ * (float)Math.PI);
		float f6 = Mth.sin(f1 * (float)Math.PI);
		p_109347_.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
		p_109347_.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
		AbstractClientPlayer abstractclientplayer = Minecraft.getInstance().player;
		RenderSystem.setShaderTexture(0, abstractclientplayer
				.getSkin().texture());
		p_109347_.translate(f * -1.0F, 3.6F, 3.5F);
		p_109347_.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
		p_109347_.mulPose(Axis.XP.rotationDegrees(200.0F));
		p_109347_.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
		p_109347_.translate(f * 5.6F, 0.0F, 0.0F);
		PlayerRenderer playerrenderer = (PlayerRenderer)Minecraft.getInstance()
				.getEntityRenderDispatcher()
				.<AbstractClientPlayer>getRenderer(abstractclientplayer);
		if (flag) {
			playerrenderer.renderRightHand(p_109347_, p_109348_, p_109349_, abstractclientplayer);
		} else {
			playerrenderer.renderLeftHand(p_109347_, p_109348_, p_109349_, abstractclientplayer);
		}

	}

	public static HandRenderSelection evaluateWhichHandsToRender(LocalPlayer p_172915_) {
		ItemStack itemstack = p_172915_.getMainHandItem();
		ItemStack itemstack1 = p_172915_.getOffhandItem();
		boolean flag = itemstack.is(Items.BOW) || itemstack1.is(Items.BOW);
		boolean flag1 = itemstack.is(Items.CROSSBOW) || itemstack1.is(Items.CROSSBOW);
		if (!flag && !flag1) {
			return HandRenderSelection.RENDER_BOTH_HANDS;
		} else {
			return HandRenderSelection.RENDER_MAIN_HAND_ONLY;
		}
	}

	@OnlyIn(Dist.CLIENT)
	static enum HandRenderSelection {
		RENDER_BOTH_HANDS(true, true),
		RENDER_MAIN_HAND_ONLY(true, false),
		RENDER_OFF_HAND_ONLY(false, true);

		final boolean renderMainHand;
		final boolean renderOffHand;

		private HandRenderSelection(boolean p_172928_, boolean p_172929_) {
			renderMainHand = p_172928_;
			renderOffHand = p_172929_;
		}

		public static HandRenderSelection onlyForHand(InteractionHand p_172932_) {
			return p_172932_ == InteractionHand.MAIN_HAND ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
		}
	}

}
