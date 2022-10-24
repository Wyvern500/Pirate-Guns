package com.jg.pirateguns.client.screens.widgets;

import java.util.Set;

import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.client.screens.AnimationScreen;
import com.jg.pirateguns.client.screens.widgets.JGSelectionList.Key;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

public class KeyframeLineWidget extends GuiComponent implements Widget {

	public JGSelectionList pos;
	public JGSelectionList rot;
	public AnimationScreen screen;
	private Keyframe[] keyframes;
	
	private int minX;
	private int maxX;
	private int deltaX;
	private int animDur;
	private int selected;
	
	private boolean visible;
	
	public KeyframeLineWidget(AnimationScreen screen, JGSelectionList pos, 
			JGSelectionList rot) {
		this.screen = screen;
		this.pos = pos;
		this.rot = rot;
		this.visible = true;
		this.minX = 100;
		this.maxX = 400;
		this.deltaX = 300;
	}
	
	public void tick() {
		
	}
	
	@Override
	public void render(PoseStack matrix, int x, int y, float partialTicks) {
		if(this.visible) {
			if(keyframes != null) {
				for(int i = 0; i < keyframes.length; i++) {
					RenderSystem.setShader(GameRenderer::getPositionTexShader);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); 
					RenderSystem.setShaderTexture(0, AnimationScreen.WIDGETS); 
					blit(matrix, minX+(int)Mth.lerp((float)keyframes[i].startTick
							/animDur, 0, deltaX)-(10), 92, (this.selected == i || 
								(screen.getGunModel().getAnimation().getCurrent()-1) == i) ? 208 : 
								224, 0, 15, 15);
				}
			}
		}
	}
	
	public void onClick(int mouseX, int mouseY) {
		if(this.visible) {
			if(keyframes != null) {
				for(int i = 0; i < keyframes.length; i++) {
					int keyframeX = minX+(int)Mth.lerp((float)keyframes[i].startTick
							/animDur, 0, deltaX)-(10);
					if(mouseX > keyframeX && mouseX < keyframeX + 20 && mouseY > 92 
							&& mouseY < 112) {
						this.selected = i;
						Key[] newPosKeys = new Key[this.keyframes[i].pos.size()];
						int j = 0;
						for(String s : this.keyframes[i].pos.keySet()) {
							newPosKeys[j] = new Key(screen.getFont(), s);
							j++;
						}
						pos.setKeys(newPosKeys);
						Key[] newRotKeys = new Key[this.keyframes[i].rot.size()];
						int l = 0;
						for(String s : this.keyframes[i].rot.keySet()) {
							newRotKeys[l] = new Key(screen.getFont(), s);
							l++;
						}
						rot.setKeys(newRotKeys);
						LogUtils.getLogger().info("Clicked at index: " + i);
					}
				}
			}
		}
	}

	public Keyframe[] getKeyframes() {
		return keyframes;
	}

	public void setKeyframes(Keyframe[] keyframes) {
		this.keyframes = keyframes;
	}

	public int getAnimDur() {
		return animDur;
	}

	public void setAnimDur(int animDur) {
		this.animDur = animDur;
		this.selected = -1;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public int getSelected() {
		return selected;
	}
	
}
