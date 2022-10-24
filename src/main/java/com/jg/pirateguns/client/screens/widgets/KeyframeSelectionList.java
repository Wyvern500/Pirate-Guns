package com.jg.pirateguns.client.screens.widgets;

import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.screens.widgets.CustomSelectionList.Entry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KeyframeSelectionList extends CustomSelectionList<KeyframeSelectionList.Entry> {
	
	private Font font;
	private int width;
	private int x;
	
	public KeyframeSelectionList(Minecraft p_94442_, GunModel model, Font font, int x, int width) {
		super(p_94442_, 100, 140, 2, 60, 16);
		this.font = font;
		this.width =  width;
		this.setRenderTopAndBottom(false);
		// LogUtils.getLogger().info("Screen width: " + AnimationScreen.this.width +
		// " height: " + AnimationScreen.this.height);
		this.x = x;
		setLeftPos(100);
		
		/*for (int i = 0; i < model.getGunParts().size(); i++) {
			addEntry(new Entry(model.getGunParts().get(i), this));
			LogUtils.getLogger().info("Adding entry for part: " + model.getGunParts().get(i).getName());
		}
		for (int i = 0; i < model.getGunParts().size(); i++) {
			addEntry(new Entry(model.getGunParts().get(i), this));
			LogUtils.getLogger().info("Adding entry for part: " + model.getGunParts().get(i).getName());
		}
		for (int i = 0; i < model.getGunParts().size(); i++) {
			addEntry(new Entry(model.getGunParts().get(i), this));
			LogUtils.getLogger().info("Adding entry for part: " + model.getGunParts().get(i).getName());
		}*/
		//setSelected(getEntry(0));
		LogUtils.getLogger().info("GunParts Size: " + model.getGunParts().size());
	}
	
	@Override
	public void render(PoseStack p_93447_, int p_93448_, int p_93449_, float p_93450_) {
		if(this.visible) {
			this.renderBackground(p_93447_);
			int i = this.getScrollbarPosition();
			int j = i + 6;
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder bufferbuilder = tesselator.getBuilder();
			RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
	
			RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			float f = 32.0F;
			bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
			bufferbuilder.vertex((double) this.x0, (double) this.y1, 0.0D)
					.uv((float) this.x0 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F)
					.color(32, 32, 32, 255).endVertex();
			bufferbuilder.vertex((double) this.x1, (double) this.y1, 0.0D)
					.uv((float) this.x1 / 32.0F, (float) (this.y1 + (int) this.getScrollAmount()) / 32.0F)
					.color(32, 32, 32, 255).endVertex();
			bufferbuilder.vertex((double) this.x1, (double) this.y0, 0.0D)
					.uv((float) this.x1 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F)
					.color(32, 32, 32, 255).endVertex();
			bufferbuilder.vertex((double) this.x0, (double) this.y0, 0.0D)
					.uv((float) this.x0 / 32.0F, (float) (this.y0 + (int) this.getScrollAmount()) / 32.0F)
					.color(32, 32, 32, 255).endVertex();
			tesselator.end();
	
			int j1 = this.getRowLeft();
			int k = this.y0 + 4 - (int) this.getScrollAmount();
	
			this.renderList(p_93447_, j1, k, p_93448_, p_93449_, p_93450_);
	
			int k1 = this.getMaxScroll();
			if (k1 > 0) {
				RenderSystem.disableTexture();
				RenderSystem.setShader(GameRenderer::getPositionColorShader);
				int l1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
				l1 = Mth.clamp(l1, 32, this.y1 - this.y0 - 8);
				int i2 = (int) this.getScrollAmount() * (this.y1 - this.y0 - l1) / k1 + this.y0;
				if (i2 < this.y0) {
					i2 = this.y0;
				}
	
				bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
				bufferbuilder.vertex((double) i, (double) this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.vertex((double) j, (double) this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.vertex((double) j, (double) this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.vertex((double) i, (double) this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.vertex((double) i, (double) (i2 + l1), 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.vertex((double) j, (double) (i2 + l1), 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.vertex((double) j, (double) i2, 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.vertex((double) i, (double) i2, 0.0D).color(128, 128, 128, 255).endVertex();
				bufferbuilder.vertex((double) i, (double) (i2 + l1 - 1), 0.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.vertex((double) (j - 1), (double) (i2 + l1 - 1), 0.0D).color(192, 192, 192, 255)
						.endVertex();
				bufferbuilder.vertex((double) (j - 1), (double) i2, 0.0D).color(192, 192, 192, 255).endVertex();
				bufferbuilder.vertex((double) i, (double) i2, 0.0D).color(192, 192, 192, 255).endVertex();
				tesselator.end();
			}
	
			this.renderDecorations(p_93447_, p_93448_, p_93449_);
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
		}
	}

	@Override
	protected void renderList(PoseStack p_93452_, int p_93453_, int p_93454_, int p_93455_, int p_93456_,
			float p_93457_) {
		int i = this.getItemCount();
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();

		for (int j = 0; j < i; ++j) {
			int k = this.getRowTop(j);
			int l = this.getRowTop(j) + this.itemHeight;
			if (l >= this.y0 + 12 && k <= this.y1 - 11) {
				int i1 = p_93454_ + j * this.itemHeight + this.headerHeight;
				int j1 = this.itemHeight - 4;
				Entry e = this.getEntry(j);
				int v = 120;
				int k1 = this.getRowWidth()-v;
				// 160 -> 20 , 140 -> 10
				if (this.isSelectedItem(j)) {
					Entry entry = children.get(j);
					int l1 = 0;
					int i2 = 0;
					if(!entry.getRot()) {
						l1 = this.x0 + 50 + this.width / 2 - (k1) / 2;
						i2 = this.x0 + 50 + this.width / 2 + (k1) / 2;
					} else {
						l1 = this.x0 + this.width / 2 - (k1) / 2;
						i2 = this.x0 + this.width / 2 + (k1) / 2;
					}
					RenderSystem.disableTexture();
					RenderSystem.setShader(GameRenderer::getPositionShader);
					float f = this.isFocused() ? 1.0F : 0.5F;
					RenderSystem.setShaderColor(f, f, f, 1.0F);
					bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
					bufferbuilder.vertex((double) l1, (double) (i1 + j1 + 2), 0.0D).endVertex();
					bufferbuilder.vertex((double) i2, (double) (i1 + j1 + 2), 0.0D).endVertex();
					bufferbuilder.vertex((double) i2, (double) (i1 - 2), 0.0D).endVertex();
					bufferbuilder.vertex((double) l1, (double) (i1 - 2), 0.0D).endVertex();
					tesselator.end();
					RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
					bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
					bufferbuilder.vertex((double) (l1 + 1), (double) (i1 + j1 + 1), 0.0D).endVertex();
					bufferbuilder.vertex((double) (i2 - 1), (double) (i1 + j1 + 1), 0.0D).endVertex();
					bufferbuilder.vertex((double) (i2 - 1), (double) (i1 - 1), 0.0D).endVertex();
					bufferbuilder.vertex((double) (l1 + 1), (double) (i1 - 1), 0.0D).endVertex();
					tesselator.end();
					RenderSystem.enableTexture();
				}

				int j2 = this.getRowLeft();
				e.render(p_93452_, j, k, j2, k1, j1, p_93455_, p_93456_, false, p_93457_);
			}
		}
	}

	@Override
	protected void renderHeader(PoseStack p_93458_, int p_93459_, int p_93460_, Tesselator p_93461_) {
		super.renderHeader(p_93458_, p_93459_, p_93460_, p_93461_);
	}

	protected int getScrollbarPosition() {
		return x1 - 6;
	}

	protected void renderBackground(PoseStack p_96105_) {

	}
	
	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
		
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Entry extends CustomSelectionList.Entry<KeyframeSelectionList.Entry> {
		
		private String key;
		private boolean rot;
		public Keyframe keyframe;
		private KeyframeSelectionList list;
		
		public Entry(String key, boolean rot, Keyframe keyframe, KeyframeSelectionList list) {
			this.key = key;
			this.rot = rot;
			this.keyframe = keyframe;
			this.list = list;
		}

		@Override
		public boolean mouseClicked(double p_94737_, double p_94738_, int button) {
			if (button == 0) {
				list.setSelected(this);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void render(PoseStack matrix, int p_93524_, int p_93525_, int p_93526_, int p_93527_, int p_93528_,
				int p_93529_, int p_93530_, boolean p_93531_, float p_93532_) {
			if(!rot) {
				list.font.drawShadow(matrix, key,
						(float) 130,
						(float) (p_93525_ + 1), 16777215, true);
			} else {
				list.font.drawShadow(matrix, key,
						(float) 240,
						(float) (p_93525_ + 1), 16777215, true);
			}
		}
		
		public boolean getRot() {
			return rot;
		}
		
	}
}
