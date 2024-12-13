package com.jg.jgpg.client.animation.gui.widget;

import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.MathUtils;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2i;

public class ScrollbarWidget extends Widget {

	Vector2i barPos;
	Vector2i barSize;
	boolean vertical;
	Callback callback;
	
	public ScrollbarWidget(NewAnimationGui parent, int x, int y, int w, int h, int bw, int bh) {
		super(parent, x, y, w, h);
		if(w < h) {
			vertical = true;
		}
		barPos = new Vector2i(x, y);
		if(vertical) {
			bw = w;
		} else {
			bh = h;
		}
		barSize = new Vector2i(bw, bh);
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mx, int my, float p_268085_) {
		int color = Color.rgba(103, 0, 98, 100);
		RenderHelper.rect(graphics, getX(), getY(), width, height, color);
		color = Color.rgba(249, 59, 242, 255);
		RenderHelper.rect(graphics, barPos.x, barPos.y, barSize.x, barSize.y, color);
	}
	
	public float getProgress() {
		if(vertical) {
			return (barPos.y - (getY() + 0.0f)) / (height - barSize.y);
		}
		return (barPos.x - (getX() + 0.0f)) / (width - barSize.x);
	}
	
	public void setProgress(float progress) {
		if(vertical) {
			barPos.y = (int) MathUtils.lerp(getY(), getY() + height - barSize.y, progress);
			//callback.call(getProgress());
		} else {
			barPos.x = (int) MathUtils.lerp(getX(), getX() + width - barSize.x, progress);
			//callback.call(getProgress());
		}
	}
	
	public void dragBar(float mx, float my) {
		int offset = 5;
		if(mx >getX() - offset && mx <getX() + width + offset && 
				my >getY() - offset && my <getY() + height + offset) {
			if(vertical) {
				barPos.y = (int) my;
				if(barPos.y + barSize.y >getY() + height) {
					barPos.y = getY() + (height - barSize.y);
				} else if(barPos.y <getY()) {
					barPos.y = getY();
				}
				if(callback != null) {
					callback.call(getProgress());
				}
			} else {
				barPos.x = (int) mx;
				if(barPos.x + barSize.x >getX() + width) {
					barPos.x = getX() + (width - barSize.x);
				} else if(barPos.x <getX()) {
					barPos.x =getX();
				}
				if(callback != null) {
					callback.call(getProgress());
				}
			}
		}
	}
	
	public void scrollbar(float amountX, float amountY) {
		if(vertical) {
			barPos.y += amountY * 4;
			if(barPos.y + barSize.y >getY() + height) {
				barPos.y = getY() + (height - barSize.y);
			} else if(barPos.y <getY()) {
				barPos.y = getY();
			}
			if(callback != null) {
				callback.call(getProgress());
			}
		} else {
			barPos.x += amountX * 4;
			if(barPos.x + barSize.x >getX() + width) {
				barPos.x = getX() + (width - barSize.x);
			} else if(barPos.x <getX()) {
				barPos.x = getX();
			}
			if(callback != null) {
				callback.call(getProgress());
			}
		}
	}
	
	@Override
	public boolean mouseDragged(double mx, double my, int p_93647_, double p_93648_, double p_93649_) {
		dragBar((float) mx, (float) my);
		return super.mouseDragged(mx, my, p_93647_, p_93648_, p_93649_);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX,
								 double scrollY) {
		scrollbar((float) scrollX, (float) scrollY);
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public interface Callback {
		public void call(float progress);
	}
	
}
