package com.jg.pirateguns.client.screens.widgets;

import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.screens.widgets.JGSelectionList.Key;

import net.minecraft.client.gui.Font;

public class GunPartKey extends Key {

	protected GunModelPart part;
	
	public GunPartKey(Font font, GunModelPart part) {
		super(font, part.getName());
		this.part = part;
	}
	
	public GunModelPart getPart() {
		return part;
	}
	
}
