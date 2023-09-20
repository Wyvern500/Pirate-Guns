package com.jg.jgpg.client.gui.widget.widgets;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.gui.widget.JgAbstractWidget;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.gui.GuiGraphics;

public class ScrollbarWidget extends JgAbstractWidget {

	boolean vertical;
	int sx;
	int sy;
	int sw;
	int sh;
	
	public ScrollbarWidget(int x, int y, int w, int h, int s, boolean vertical, AnimationGui gui) {
		super(x, y, w, h, gui);
		this.sx = x;
		this.sy = y;
		if(vertical) {
			this.sh = s;
		} else {
			this.sw = s;
		}
		this.vertical = vertical;
	}

	@Override
	public void tick() {
		
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int mx, int my, float delta) {
		if(visible) {
			RenderHelper.rect(gui, getX(), getY(), width, height, Color.rgba(100, 100, 0, 255));
			if(vertical) {
				RenderHelper.rect(gui, getX(), sy, width, sh, Color.rgba(0, 0, 0, 255));
			} else {
				RenderHelper.rect(gui, sx, getY(), sw, height, Color.rgba(0, 0, 0, 255));
			}
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		
		return super.mouseClicked(mx, my, btn);
	}
	
	@Override
	public boolean mouseDragged(double mx, double my, int btn, double p_93648_, double p_93649_) {
		if(visible) {
			if(btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if(Utils.collides(getX() - 2, getY() - 2, width + 2, height + 2, mx, my)) {
					if(vertical) {
						sy = (int) MathUtils.clamp(my, getY(), getY() + height - sh);
					} else {
						sx = (int) MathUtils.clamp(mx, getX(), getX() + width - sw);
					}
				}
			}
		}
		return super.mouseDragged(mx, my, btn, p_93648_, p_93649_);
	}
	
	public void update(int visibleItems, int totalItems) {
		if(visible) {
			if(vertical) {
				sh = (int) MathUtils.lerp(0, height, (visibleItems + 0.0f) / totalItems);
			} else {
				sw = (int) MathUtils.lerp(0, width, (visibleItems + 0.0f) / totalItems);
			}
		}
	}
	
	public void add(int val) {
		if(vertical) {
			sy += val;
			sy = MathUtils.clamp(sy, getY(), getY() + height - sh);
		} else {
			sx += val;
			sx = MathUtils.clamp(sx, getX(), getX() + width - sw);
		}
	}
	
	public float getProgress() {
		if(vertical) {
			return (sy - getY() + 0.0f) / (height - sh);
		}
		return (sx - getX() + 0.0f) / (width - sw);
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	public int getScrollbarX() {
		return sx;
	}

	public void setScrollbarX(int sx) {
		this.sx = MathUtils.clamp(sx, 0, (getX() + width) - sw);
	}

	public int getScrollbarY() {
		return sy;
	}

	public void setScrollbarY(int sy) {
		this.sy = sy;
	}
	
}
