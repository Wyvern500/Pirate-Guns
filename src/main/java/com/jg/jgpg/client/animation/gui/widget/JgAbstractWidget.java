package com.jg.jgpg.client.animation.gui.widget;

import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class JgAbstractWidget extends AbstractWidget {
	
	protected NewAnimationGui parent;
	
	public JgAbstractWidget(int x, int y, int w, int h, NewAnimationGui parent) {
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
	
}
