package com.jg.pirateguns.client.screens.widgets;

import org.lwjgl.glfw.GLFW;

import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.client.handlers.GunModelsHandler;
import com.jg.pirateguns.registries.ItemRegistries;
import com.jg.pirateguns.utils.FileUtils;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class JgEditBox extends EditBox {

	public JgEditBox(Font p_94114_, int p_94115_, int p_94116_, int p_94117_, int p_94118_, Component p_94119_) {
		super(p_94114_, p_94115_, p_94116_, p_94117_, p_94118_, p_94119_);
	}
	
	public void onKeyRelease(int key) {
		if(getValue() != null && !getValue().equals("")) {
			if(this.isFocused()) {
				if(key == GLFW.GLFW_KEY_ENTER) {
					GunModel model = GunModelsHandler.get(Minecraft.getInstance().player
							.getMainHandItem().getItem().getRegistryName().toString());
					if(model != null) {
						if(model.getAnimation() != null) {
							LogUtils.getLogger().info("Trying to create animation");
							FileUtils.writeFile(getValue(), model.getAnimation());
						}
					}
				}
			}
		}
	}

}
