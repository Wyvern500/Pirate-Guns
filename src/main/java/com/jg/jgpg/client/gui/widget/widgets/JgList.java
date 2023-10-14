package com.jg.jgpg.client.gui.widget.widgets;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.gui.widget.JgAbstractWidget;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.gui.GuiGraphics;

public class JgList extends JgAbstractWidget {

	List<AbstractJgListItem> items;
	List<AbstractJgListItem> toDelete;
	int offset;
	int from;
	int to;
	int visibleItems;
	int selected;
	int cols;
	int rows;
	
	public JgList(int x, int y, int w, int h, int visibleItems, AnimationGui parent) {
		super(x, y, w, h, parent);
		this.items = new ArrayList<JgList.AbstractJgListItem>();
		this.toDelete = new ArrayList<JgList.AbstractJgListItem>();
		this.visibleItems = visibleItems;
		this.selected = -1;
	}

	@Override
	public void tick() {
		
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int mx, int my, float delta) {
		if(visible) {
			if(!items.isEmpty()) {
				if(!toDelete.isEmpty()) {
					for(AbstractJgListItem item : toDelete) {
						items.remove(item);
					}
					toDelete.clear();
				}
				
				RenderHelper.rect(gui, getX(), getY(), width, height, Color.rgba(0, 0, 0, 255));
				for(int i = from; i < to; i++) {
					int ix = getX();
					int iy = (i - from) * (height / visibleItems);
					AbstractJgListItem item = items.get(i);
					item.render(gui, ix, getY() + iy, width, height / visibleItems, i);
				}
			}
		}
	}
	
	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		if(visible) {
			if(btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if(!items.isEmpty()) {
					for(int i = from; i < to; i++) {
						int ix = getX();
						int iy = (i - from) * (height / visibleItems);
						if(Utils.collides(ix, getY() + iy, width, height / visibleItems, mx, my)) {
							if(selected != i) {
								selected = i;
								AbstractJgListItem item = items.get(i);
								item.setPressed(true);
								item.handleMouse(mx, my, btn, ix, getY() + iy, width, height / visibleItems);
							} else {
								selected = -1;
								AbstractJgListItem item = items.get(i);
								item.setPressed(false);
								item.onUnselect(mx, my, btn, ix, getY() + iy, width, height / visibleItems);
							}
						}
					}
				}
			}
		}
		return super.mouseClicked(mx, my, btn);
	}
	
	@Override
	public boolean mouseReleased(double mx, double my, int btn) {
		if(visible) {
			if(btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if(!items.isEmpty()) {
					for(int i = from; i < to; i++) {
						int ix = getX();
						int iy = (i - from) * (height / visibleItems);
						AbstractJgListItem item = items.get(i);
						item.mouseReleased(mx, my, btn, ix, iy, width, height / visibleItems);
					}
				}
			}
		}
		return super.mouseReleased(mx, my, btn);
	}
	
	public void addItem(AbstractJgListItem item) {
		items.add(item);
		to = Math.min(items.size(), visibleItems);
	}
	
	public void removeItem(AbstractJgListItem item) {
		toDelete.add(item);
	}
	
	public void removeItem(int index) {
		if(index >= items.size()) {
			index = items.size() - 1;
		} else if(index < 0) {
			index = 0;
		}
		AbstractJgListItem item = items.get(index);
		toDelete.add(item);
	}
	
	public void update(float prog) {
		from = (int) MathUtils.lerp(0, Math.max(items.size() - visibleItems, 0), prog);
		to = (int) MathUtils.lerp(Math.min(visibleItems, items.size()), items.size(), prog);
	}
	
	public void updateHorizontalStuff(int offset) {
		if(!items.isEmpty()) {
			for(int i = from; i < to; i++) {
				AbstractJgListItem item = items.get(i);
				item.updateHorinzontalStuff(offset);
			}
		}
	}
	
	public void clear() {
		items.clear();
		from = 0;
		to = 0;
	}
	
	public List<AbstractJgListItem> getItems() {
		return items;
	}
	
	public int getSelected() {
		return selected;
	}
	
	public AbstractJgListItem getSelectedItem() {
		return items.get(selected);
	}
	
	public static abstract class AbstractJgListItem {
		
		protected boolean pressed;
		protected JgList parent;
		protected Animation animation;
		
		public AbstractJgListItem(JgList parent) {
			this.parent = parent;
		}

		public abstract void render(GuiGraphics gg, int x, int y, int w, int h, int i);
		
		public abstract void updateHorinzontalStuff(int offset);
		
		public abstract void handleMouse(double mx, double my, int btn, double x, double y, double w, 
				double h);
		
		public abstract boolean mouseReleased(double mx, double my, int btn, double x, double y, double w, 
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
