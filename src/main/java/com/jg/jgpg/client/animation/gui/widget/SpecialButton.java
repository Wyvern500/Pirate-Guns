package com.jg.jgpg.client.animation.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public abstract class SpecialButton extends Button {

	protected boolean isPressed;
	
	public SpecialButton(int x, int y, int w, int h, OnPress onPress) {
		super(x, y, w, h, Component.translatable(""), onPress, DEFAULT_NARRATION);
	}
	
	public void shadow(GuiGraphics gg, int x, int y, int w, int h) {
		RenderSystem.disableDepthTest();
	    RenderSystem.colorMask(true, true, true, false);
	    // Llamada a fillGradient con los colores personalizados
	    gg.fillGradient(x, y, x + w, y + h, 0x90000000, 0x90000000);
	    RenderSystem.colorMask(true, true, true, true);
	    RenderSystem.enableDepthTest();
	}
	
	public void shadow(GuiGraphics gg, int x, int y) {
		RenderSystem.disableDepthTest();
	    RenderSystem.colorMask(true, true, true, false);
	    // Llamada a fillGradient con los colores personalizados
	    gg.fillGradient(x, y, x + 16, y + 16, 0x90000000, 0x90000000);
	    RenderSystem.colorMask(true, true, true, true);
	    RenderSystem.enableDepthTest();
	}
	
	public void highlight(GuiGraphics gg, int x, int y, int w, int h) {
		RenderSystem.disableDepthTest();
	    RenderSystem.colorMask(true, true, true, false);
	    gg.fillGradient(x, y, x + w, y + h, 
	    		-2130706433, -2130706433);
	    RenderSystem.colorMask(true, true, true, true);
	    RenderSystem.enableDepthTest();
	}
	
	public void highlight(GuiGraphics gg, int x, int y) {
		RenderSystem.disableDepthTest();
	    RenderSystem.colorMask(true, true, true, false);
	    gg.fillGradient(x, y, x + 16, y + 16, 
	    		-2130706433, -2130706433);
	    RenderSystem.colorMask(true, true, true, true);
	    RenderSystem.enableDepthTest();
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY,
								float partialTick) {
		renderButton(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void onPress() {
		super.onPress();
		isPressed = true;
	}
	
	@Override
	public void onRelease(double p_93669_, double p_93670_) {
		super.onRelease(p_93669_, p_93670_);
		isPressed = false;
	}
	
	public abstract void renderButton(GuiGraphics gg, int mx, int my, float partialTicks);

}
