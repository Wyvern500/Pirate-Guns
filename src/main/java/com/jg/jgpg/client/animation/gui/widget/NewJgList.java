package com.jg.jgpg.client.animation.gui.widget;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.RangeHandler;
import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.Utils;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class NewJgList extends JgAbstractWidget {

	List<AbstractJgListItem> items;
	RangeHandler range;
	int selected;
	
	public NewJgList(int x, int y, int w, int h, NewAnimationGui parent, RangeHandler handler) {
		super(x, y, w, h, parent);
		this.range = handler;
		this.items = new ArrayList<>();
		this.selected = -1;
	}

	@Override
	public void tick() {
		
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int p_268034_, int p_268009_, float p_268085_) {
		if(visible) {
			RenderHelper.rect(gui, getX(), getY(), width, height, Color.rgba(0, 0, 0, 255));
			for(int i = range.getStart(); i < range.getEnd(); i++) {
				int ix = getX();
				int iy = (i - range.getStart()) * (height / range.getVisibleRange());
				AbstractJgListItem item = items.get(i);
				item.render(gui, ix, getY() + iy, width, height / range.getVisibleRange(), i);
			}
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		for(int i = range.getStart(); i < range.getEnd(); i++) {
			int ix = getX();
			int iy = (i - range.getStart()) * (height / range.getVisibleRange());
			if(Utils.collides(ix, getY() + iy, width, height / range.getVisibleRange(), mx, my)) {
				AbstractJgListItem item = items.get(i);
				if(!item.isPressed()) {
					selected = i;
					item.setPressed(true);
					item.handleMouse(mx, my, btn, ix, iy, width, height / range.getVisibleRange());
				} else {
					selected = -1;
					item.setPressed(false);
					item.onUnselect(mx, my, btn, ix, iy, width, height / range.getVisibleRange());
				}
			}
		}
		return super.mouseClicked(mx, my, btn);
	}
	
	public void clear() {
		items.clear();
		range.clear();
		range.update(4, 0);
	}
	
	public void addItem(AbstractJgListItem item) {
		items.add(item);
		range.update(items.size(), 0);
	}
	
	public AbstractJgListItem getSelectedItem() {
		return items.get(selected);
	}
	
	public int getSelected() {
		return selected;
	}
	
	public void setSelected(int index) {
		this.selected = index;
	}
	
	public int size() {
		return items.size();
	}
	
	public static abstract class AbstractJgListItem {
		
		protected boolean pressed;
		protected NewJgList parent;
		protected Animation animation;
		
		public AbstractJgListItem(NewJgList parent) {
			this.parent = parent;
		}

		public abstract void render(GuiGraphics gg, int x, int y, int w, int h, int i);
		
		public abstract void handleMouse(double mx, double my, int btn, double x, double y, double w, 
				double h);
		
		public abstract void onUnselect(double mx, double my, int btn, double x, double y, double w, double h);
		
		public void setAnimation(Animation animation) {
			this.animation = animation;
		}
		
		public Animation getAnimation() {
			return animation;
		}
		
		public void setPressed(boolean pressed) {
			this.pressed = pressed;
		}
		
		public boolean isPressed() {
			return pressed;
		}
		
	}
	
}
