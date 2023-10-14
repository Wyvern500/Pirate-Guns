package com.jg.jgpg.client.gui.widget.widgets;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.utils.Utils;

public class KeyframesList extends JgList {

	public KeyframesList(int x, int y, int w, int h, int visibleItems, AnimationGui gui) {
		super(x, y, w, h, visibleItems, gui);
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
							AbstractJgListItem item = items.get(i);
							item.handleMouse(mx, my, btn, ix, getY() + iy, width, height / visibleItems);
						}
					}
				}
			}
		}
		return super.mouseClicked(mx, my, btn);
	}

}
