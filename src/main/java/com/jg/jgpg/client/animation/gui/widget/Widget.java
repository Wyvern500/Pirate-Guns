package com.jg.jgpg.client.animation.gui.widget;

import com.jg.jgpg.client.animation.gui.NewAnimationGui;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class Widget extends AbstractWidget {
	
	NewAnimationGui parent;
	
	public Widget(NewAnimationGui parent, int x, int y, int w, int h) {
		super(x, y, w, h, Component.translatable(""));
		this.parent = parent;
	}
	
	public abstract void tick();
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
		
	}

	public NewAnimationGui getParent() {
		return parent;
	}

	public void setParent(NewAnimationGui parent) {
		this.parent = parent;
	}
	
}
