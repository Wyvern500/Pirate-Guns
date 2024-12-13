package com.jg.jgpg.client.animation.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class OnlyViewWidget<T> extends AbstractWidget {

	protected T item;
	protected Screen s;
	protected List<Component> components;

	public OnlyViewWidget(Screen s, int x, int y, int w, int h) {
		this(s, null, x, y, w, h);
	}

	public OnlyViewWidget(Screen s, T item, int x, int y, int w, int h) {
		super(x, y, w, h, Component.translatable(""));
		this.item = item;
		this.s = s;
	}

	public void renderButton(GuiGraphics gg, int p_93677_, int p_93678_, float p_93679_) {
		renderSlot(gg, p_93677_, p_93678_);
	}

	public void shadow(GuiGraphics gg, int x, int y) {
		RenderSystem.disableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		// Llamada a fillGradient con los colores personalizados
		gg.fillGradient(x, y, x + 16, y + 16, 0x90000000, 0x90000000);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableDepthTest();
	}

	public void highlight(GuiGraphics gg, int x, int y) {
		RenderSystem.disableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		gg.fillGradient(x, y, x + 16, y + 16, -2130706433, -2130706433, 0);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableDepthTest();
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
		
	}
	
	@Override
	protected void renderWidget(GuiGraphics gg, int mx, int my, float partialTicks) {
		this.renderButton(gg, mx, my, partialTicks);
		if (isHovered) {
			renderHovered(gg, mx, my);
		}
	}
	
	public void setItem(T item) {
		this.item = item;
	}

	public T getItem() {
		return item;
	}

	public abstract void renderSlot(GuiGraphics gg, int x, int y);

	public abstract void renderHovered(GuiGraphics gg, int x, int y);

}
