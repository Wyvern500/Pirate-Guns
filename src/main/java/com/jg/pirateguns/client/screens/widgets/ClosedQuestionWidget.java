package com.jg.pirateguns.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;

public class ClosedQuestionWidget extends Button {

	private boolean yes;
	
	public ClosedQuestionWidget(int p_93721_, int p_93722_, int p_93723_, int p_93724_, String text,
			OnPress p_93726_) {
		super(p_93721_, p_93722_, p_93723_, p_93724_, new TranslatableComponent(""), p_93726_);
	}
	
	@Override
	public void renderButton(PoseStack p_93746_, int p_93747_, int p_93748_, float p_93749_) {
		Minecraft minecraft = Minecraft.getInstance();
	      Font font = minecraft.font;
	      RenderSystem.setShader(GameRenderer::getPositionTexShader);
	      RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
	      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
	      int i = this.getYImage(this.isHoveredOrFocused());
	      RenderSystem.enableBlend();
	      RenderSystem.defaultBlendFunc();
	      RenderSystem.enableDepthTest();
	      this.blit(p_93746_, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
	      this.blit(p_93746_, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
	      this.renderBg(p_93746_, minecraft, p_93747_, p_93748_);
	      int j = getFGColor();
	      drawCenteredString(p_93746_, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
	}
	
	@Override
	public Component getMessage() {
		return super.getMessage();
	}
	
	@Override
	public void onClick(double p_93371_, double p_93372_) {
		super.onClick(p_93371_, p_93372_);
	}

}
