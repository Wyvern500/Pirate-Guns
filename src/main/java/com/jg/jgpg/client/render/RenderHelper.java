package com.jg.jgpg.client.render;

import com.jg.jgpg.PirateGuns;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderHelper {

	private static GuiGraphics guiRender;

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

	public static void rect(GuiGraphics guiRender, int x, int y, int w, int h, int color){
		innerRect(RenderType.guiOverlay(), guiRender.pose(), x, y, x + w,
				y + h, -90, color);
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

		MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers()
				.bufferSource();

		float f3 = (float) FastColor.ARGB32.alpha(p_286599_) / 255.0F;
		float f = (float)FastColor.ARGB32.red(p_286599_) / 255.0F;
		float f1 = (float)FastColor.ARGB32.green(p_286599_) / 255.0F;
		float f2 = (float)FastColor.ARGB32.blue(p_286599_) / 255.0F;
		VertexConsumer vertexconsumer = source.getBuffer(p_286711_);
		vertexconsumer.vertex(matrix4f, (float)p_286234_, (float)p_286444_,
				(float)p_286671_).color(f, f1, f2, f3).endVertex();
		vertexconsumer.vertex(matrix4f, (float)p_286234_, (float)p_286411_,
				(float)p_286671_).color(f, f1, f2, f3).endVertex();
		vertexconsumer.vertex(matrix4f, (float)p_286244_, (float)p_286411_,
				(float)p_286671_).color(f, f1, f2, f3).endVertex();
		vertexconsumer.vertex(matrix4f, (float)p_286244_, (float)p_286444_,
				(float)p_286671_).color(f, f1, f2, f3).endVertex();

		RenderSystem.disableDepthTest();
		source.endBatch();
		RenderSystem.enableDepthTest();
	}

	public static void renderPlayerArm(PoseStack matrix, MultiBufferSource buffer, int light, float p_109350_,
			float p_109351_, HumanoidArm p_109352_) {
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
		RenderSystem.setShaderTexture(0, abstractclientplayer.getSkinTextureLocation());
		matrix.translate(f * -1.0F, 3.6F, 3.5F);
		matrix.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
		matrix.mulPose(Axis.XP.rotationDegrees(200.0F));
		matrix.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
		matrix.translate(f * 5.6F, 0.0F, 0.0F);
		PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
				.<AbstractClientPlayer>getRenderer(abstractclientplayer);
		if (flag) {
			playerrenderer.renderRightHand(matrix, buffer, light, abstractclientplayer);
		} else {
			playerrenderer.renderLeftHand(matrix, buffer, light, abstractclientplayer);
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
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93117_, (float) p_93118_).uv(p_93119_, p_93122_)
				.endVertex();
		bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93117_, (float) p_93118_).uv(p_93120_, p_93122_)
				.endVertex();
		bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93116_, (float) p_93118_).uv(p_93120_, p_93121_)
				.endVertex();
		bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93116_, (float) p_93118_).uv(p_93119_, p_93121_)
				.endVertex();
		BufferUploader.draw(bufferbuilder.end());
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
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(matrix4f, (float)p_281399_, (float)p_283615_, (float)p_281729_).uv(p_283247_, p_282883_).endVertex();
		bufferbuilder.vertex(matrix4f, (float)p_281399_, (float)p_283430_, (float)p_281729_).uv(p_283247_, p_283017_).endVertex();
		bufferbuilder.vertex(matrix4f, (float)p_283222_, (float)p_283430_, (float)p_281729_).uv(p_282598_, p_283017_).endVertex();
		bufferbuilder.vertex(matrix4f, (float)p_283222_, (float)p_283615_, (float)p_281729_).uv(p_282598_, p_282883_).endVertex();
		BufferUploader.drawWithShader(bufferbuilder.end());
	}

	public static void innerBlit(ResourceLocation p_283254_, PoseStack matrix, int p_283092_,
				   int p_281930_, int p_282113_, int p_281388_, int p_283583_,
				   float p_281327_, float p_281676_, float p_283166_,
				   float p_282630_, float p_282800_, float p_282850_,
				   float p_282375_, float p_282754_) {
		RenderSystem.setShaderTexture(0, p_283254_);
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = matrix.last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
		bufferbuilder.vertex(matrix4f, (float)p_283092_, (float)p_282113_, (float)p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281327_, p_283166_).endVertex();
		bufferbuilder.vertex(matrix4f, (float)p_283092_, (float)p_281388_, (float)p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281327_, p_282630_).endVertex();
		bufferbuilder.vertex(matrix4f, (float)p_281930_, (float)p_281388_, (float)p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281676_, p_282630_).endVertex();
		bufferbuilder.vertex(matrix4f, (float)p_281930_, (float)p_282113_, (float)p_283583_).color(p_282800_, p_282850_, p_282375_, p_282754_).uv(p_281676_, p_283166_).endVertex();
		BufferUploader.drawWithShader(bufferbuilder.end());
		RenderSystem.disableBlend();
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
		guiRender.blit(new ResourceLocation(PirateGuns.MODID,
				"textures/misc/scope.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
		guiRender.fill(RenderType.guiOverlay(), 0, j1, screenWidth, screenHeight, -90, -16777216);
		guiRender.fill(RenderType.guiOverlay(), 0, 0, screenWidth, l, -90, -16777216);
		guiRender.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
		guiRender.fill(RenderType.guiOverlay(), i1, l, screenWidth, j1, -90, -16777216);

		/*RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, new ResourceLocation("textures/misc/spyglass_scope.png"));
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		float f = (float) Math.min(screenWidth, screenHeight);
		float f1 = Math.min((float) screenWidth / f, (float) screenHeight
				/ f) * Mth.lerp((progress-0.5f)/0.5f,
						0.01f, 1.125f);
		float f2 = f * f1;
		float f3 = f * f1;
		float f4 = ((float) screenWidth - f2) / 2.0F;
		float f5 = ((float) screenHeight - f3) / 2.0F;
		float f6 = f4 + f2;
		float f7 = f5 + f3;
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex((double) f4, (double) f7, -90.0D).uv(0.0F, 1.0F).endVertex();
		bufferbuilder.vertex((double) f6, (double) f7, -90.0D).uv(1.0F, 1.0F).endVertex();
		bufferbuilder.vertex((double) f6, (double) f5, -90.0D).uv(1.0F, 0.0F).endVertex();
		bufferbuilder.vertex((double) f4, (double) f5, -90.0D).uv(0.0F, 0.0F).endVertex();
		tesselator.end();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.disableTexture();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		bufferbuilder.vertex(0.0D, (double) screenHeight, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) screenWidth, (double) screenHeight, -90.0D).color(0, 0, 0, 255)
				.endVertex();
		bufferbuilder.vertex((double) screenWidth, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex(0.0D, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex(0.0D, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) screenWidth, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) screenWidth, 0.0D, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex(0.0D, 0.0D, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex(0.0D, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) f4, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) f4, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex(0.0D, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) f6, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) screenWidth, (double) f7, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) screenWidth, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
		bufferbuilder.vertex((double) f6, (double) f5, -90.0D).color(0, 0, 0, 255).endVertex();
		tesselator.end();
		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		float f = (float)Math.min(this.screenWidth, this.screenHeight);
		float f1 = Math.min((float)this.screenWidth / f, (float)this.screenHeight / f) * p_283442_;
		int i = Mth.floor(f * f1);
		int j = Mth.floor(f * f1);
		int k = (this.screenWidth - i) / 2;
		int l = (this.screenHeight - j) / 2;
		int i1 = k + i;
		int j1 = l + j;
		p_282069_.blit(SPYGLASS_SCOPE_LOCATION, k, l, -90, 0.0F, 0.0F, i, j, i, j);
		p_282069_.fill(RenderType.guiOverlay(), 0, j1, this.screenWidth, this.screenHeight, -90, -16777216);
		p_282069_.fill(RenderType.guiOverlay(), 0, 0, this.screenWidth, l, -90, -16777216);
		p_282069_.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
		p_282069_.fill(RenderType.guiOverlay(), i1, l, this.screenWidth, j1, -90, -16777216);*/
	}

}
