package com.jg.pirateguns.client.screens.widgets;

import java.util.List;

import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.client.screens.AnimationScreen;
import com.jg.pirateguns.client.screens.widgets.JGSelectionList.Key;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;

public class KeyframeLineWidget extends GuiComponent implements Widget {

	public JGSelectionList pos;
	public JGSelectionList rot;
	public AnimationScreen screen;
	private List<Keyframe> keyframes;
	
	protected int minX;
	protected int maxVX;
	protected int maxX;
	protected int deltaX;
	protected int animDur;
	protected int selected;
	protected float scale;
	
	protected int scrollX;
	protected int scrollWidth;
	protected int offset;
	protected int scrollY;
	
	private boolean visible;
	
	public KeyframeLineWidget(AnimationScreen screen, JGSelectionList pos, 
			JGSelectionList rot) {
		this.screen = screen;
		this.pos = pos;
		this.rot = rot;
		this.visible = true;
		this.minX = 100;
		this.maxVX = 400;
		this.deltaX = 300;
		this.scale = 1;
		if(this.keyframes != null) {
			int x = (int)(this.minX + (Mth.lerp(0, 
		            this.deltaX, this.keyframes.get(this.keyframes.size()-1)
		            .startTick / this.animDur)*this.scale) - 10);
			this.maxX = x;
			this.scrollY = 120;
			this.scrollX = this.minX;
		    this.scrollWidth = (int)Mth.lerp((this.maxVX-10)/x, 0, this.deltaX);
		    float part1 = this.minX - this.scrollX;
	        float part2 = ((this.deltaX)-this.scrollWidth);
	        if(part1 != 0 && part2 != 0){
	            this.offset = (int)Mth.abs(Mth.lerp(0, this.maxX-this.maxVX, 
	                part1/part2));
	        } else {
	            this.offset = 0;
	        }
		} else {
			this.maxX = this.maxVX;
			this.scrollY = 120;
			this.scrollWidth = deltaX;
			this.scrollX = this.minX;
		}
	}
	
	public void tick() {
		
	}
	
	@Override
	public void render(PoseStack matrix, int x, int y, float partialTicks) {
		if(this.visible) {
			if(keyframes != null) {
				for(int i = 0; i < keyframes.size(); i++) {
					int kfx = (int)(this.minX + (Mth.lerp(this.keyframes.get(i)
			                .startTick / (float)this.animDur, 0, this.deltaX)
							*this.scale) - 10 - this.offset);
					if(kfx > minX-20 && kfx < maxVX+10) {
						RenderSystem.setShader(GameRenderer::getPositionTexShader);
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); 
						RenderSystem.setShaderTexture(0, AnimationScreen.WIDGETS);
						blit(matrix, kfx, 92, 
								(this.selected == i || 
									(screen.getGunModel().getAnimation()
											.getCurrent()-1) == i) ? 208 : 
									224, 0, 15, 15);
					}
				}
				renderScrollbar();
			}
		}
	}

	public void renderScrollbar() {
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		
		float nw = this.deltaX;
		float nh = 7;
		float nx = this.minX;
		float ny = this.scrollY;
		RenderSystem.disableTexture();
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(0F, 0F, 0F, 0.1F);
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		bufferbuilder.vertex(nx, nh + ny, 0).endVertex();
		bufferbuilder.vertex(nw + nx, nh + ny, 0).endVertex();
		bufferbuilder.vertex(nw + nx, ny, 0).endVertex();
		bufferbuilder.vertex(nx, ny, 0).endVertex();
		tesselator.end();

		float nw2 = this.scrollWidth;
		float nh2 = 7;
		float nx2 = this.scrollX;
		float ny2 = this.scrollY;

		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(0.7706F, 0.7706F, 0.7706F, 0.1F);
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		bufferbuilder.vertex(nx2, nh2 + ny2, 0).endVertex();
		bufferbuilder.vertex(nw2 + nx2, nh2 + ny2, 0).endVertex();
		bufferbuilder.vertex(nw2 + nx2, ny2, 0).endVertex();
		bufferbuilder.vertex(nx2, ny2, 0).endVertex();
		tesselator.end();
		RenderSystem.enableTexture();
	}
	
	public void check(int mouseX, int mouseY) {
		if(mouseX > this.minX && mouseX < this.maxVX && 
	            mouseY > this.scrollY && mouseY < (this.scrollY)+16){
	            this.scrollX = mouseX-(this.scrollWidth/2);
	            this.wrapScrollbarX();
	            float part1 = this.minX - this.scrollX;
	            float part2 = ((this.deltaX)-this.scrollWidth);
	            if(part1 != 0 && part2 != 0){
	                this.offset = (int)Mth.abs(Mth.lerp(0, this.maxX-this.maxVX, 
	                    part1/part2));
	            }
	            LogUtils.getLogger().info("check");
	        }
	}
	
	public void wrapScrollbarX() {
		if(this.scrollX > this.maxVX-this.scrollWidth){
            this.scrollX = this.maxVX-this.scrollWidth;
        }
        if(this.scrollX < this.minX){
            this.scrollX = this.minX;
        }
	}
	
	public void onClick(int mouseX, int mouseY) {
		if(this.visible) {
			if(keyframes != null) {
				for(int i = 0; i < keyframes.size(); i++) {
					int kfx = (int) (this.minX + (Mth.lerp((float)this.keyframes.get(i)
			                .startTick / this.animDur, 0, this.deltaX)*this.scale) 
							- 10 - this.offset);
					if(mouseX > kfx && mouseX < kfx + 20 && mouseY > 92 
							&& mouseY < 112) {
						LogUtils.getLogger().info("collide");
						if(this.selected != i) {
							this.selected = i;
							LogUtils.getLogger().info("selected");
							processOnClick(i);
						} else {
							LogUtils.getLogger().info("else");
							this.selected = -1;
						}
					}
				}
			}
		}
	}

	public void processOnClick(int i) {
		try {
			screen.getEditBoxes().get(7).setValue(String.valueOf(
					this.keyframes.get(i).dur));
			screen.getEditBoxes().get(8).setValue(String.valueOf(
					this.keyframes.get(i).startTick));
			Key[] newPosKeys = new Key[this.keyframes.get(i).pos.size()];
			int j = 0;
			for(String s : this.keyframes.get(i).pos.keySet()) {
				newPosKeys[j] = new Key(screen.getFont(), s);
				j++;
			}
			pos.setKeys(newPosKeys);
			Key[] newRotKeys = new Key[this.keyframes.get(i).rot.size()];
			int l = 0;
			for(String s : this.keyframes.get(i).rot.keySet()) {
				newRotKeys[l] = new Key(screen.getFont(), s);
				l++;
			}
			rot.setKeys(newRotKeys);
			LogUtils.getLogger().info("Clicked at index: " + i);
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	public List<Keyframe> getKeyframes() {
		return keyframes;
	}

	public void setKeyframes(List<Keyframe> keyframes) {
		this.keyframes = keyframes;
		if(this.keyframes != null) {
			float x = this.minX + (Mth.lerp(0, 
		            this.deltaX, this.keyframes.get(this.keyframes.size()-1).startTick / this.animDur)*this.scale) - 10 -
		                this.offset;
		    this.maxX = (int)x;
		    this.scrollWidth = (int)Mth.lerp(0, this.deltaX, (this.maxVX-10)/x);
		}
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
	
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		if(keyframes != null) {
			float x = this.minX + (Mth.lerp(0, 
		            this.deltaX, this.keyframes.get(this.keyframes.size()-1).startTick / this.animDur)*this.scale) - 10 -
		                this.offset;
		    this.maxX = (int)x;
		    this.scrollWidth = (int)Mth.lerp(0, this.deltaX, (this.maxVX-10)/x);
		}
	}
	
	public float getScale() {
		return scale;
	}
	
	public int getSelected() {
		return selected;
	}
	
}
