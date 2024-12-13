package com.jg.jgpg.client.animation.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ItemSlotWidget extends AbstractWidget {

	protected Component hoverText;
	protected ItemStack stack;
	protected Screen screen;
	protected boolean pressed;
	
	public ItemSlotWidget(Screen screen, int x, int y, int w, int h) {
		this(screen, null, x, y, w, h, Component.empty());
	}

	public ItemSlotWidget(Screen screen, Item item, int x, int y, int w, int h, Component hoverText) {
		super(x, y, w, h, Component.translatable(""));
		this.stack = new ItemStack(item);
		this.screen = screen;
		this.hoverText = hoverText;
	}

	@Override
	protected void renderWidget(GuiGraphics gg, int mx, int my, float partialTick) {
		this.renderButton(gg, mx, my, partialTick);
		if (isHovered) {
			renderHovered(gg, mx, my);
		}
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
		// -2130706433, -2130706433
		gg.fillGradient(x, y, x + 16, y + 16, -2130706433, -2130706433);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableDepthTest();
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
		
	}
	
	@Override
	public void onClick(double p_93634_, double p_93635_) {
		this.pressed = true;
		this.onPressItemSlot(p_93634_,p_93635_);
		super.onClick(p_93634_, p_93635_);
	}
	
	@Override
	public void onRelease(double p_93669_, double p_93670_) {
		this.pressed = false;
		super.onRelease(p_93669_, p_93670_);
	}
	
	public void setItemStack(ItemStack stack) {
		this.stack = stack;
	}

	public ItemStack getItemStack() {
		return stack;
	}
	
	public abstract void renderSlot(GuiGraphics gg, int x, int y);

	public abstract void renderHovered(GuiGraphics gg, int x, int y);
	
	public abstract void onPressItemSlot(double x, double y);
	
}
