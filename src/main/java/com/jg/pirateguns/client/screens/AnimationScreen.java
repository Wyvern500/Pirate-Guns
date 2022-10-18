package com.jg.pirateguns.client.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.parts.GunModelPart;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.rendering.RenderHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.ObjectSelectionList.Entry;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AnimationScreen extends Screen {

	private static ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
	
	private final List<Button> buttons;
	private final List<EditBox> edits;
	private final List<OptionsList> options;
	private AnimationSelectionList list;
	private final GunModel model;

	private int i;
	private int j;
	
	private boolean start;
	private float prog;
	private float prev;
	private float MAX = 4f;
	
	public AnimationScreen(GunModel model) {
		super(new TranslatableComponent("Animation Screen"));
		this.buttons = new ArrayList<>();
		this.edits = new ArrayList<>();
		this.options = new ArrayList<>();
		this.model = model;
		i = width/2;
		j = height/2;
	}

	@Override
	protected void init() {
		super.init();

		ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
		list = new AnimationSelectionList(minecraft, model);

		this.addWidget(this.list);

		// Pos
		// x
		edits.add(new EditBox(font, 16, 0, 80, 20, new TranslatableComponent("animationScreen.pos.x")));
		edits.get(0).setValue(String.valueOf(list.getSelected().part.getTransform().pos[0]));
		edits.get(0).setResponder((s) -> {
			try {
				float val = Float.parseFloat(s);
				list.getSelected().part.getTransform().pos[0] = val;
				LogUtils.getLogger().info("Val: " + val);
			} catch (Exception e) {

			}
		});
		// y
		edits.add(new EditBox(font, 16, 20, 80, 20, new TranslatableComponent("animationScreen.pos.y")));
		edits.get(1).setValue(String.valueOf(list.getSelected().part.getTransform().pos[1]));
		edits.get(1).setResponder((s) -> {
			try {
				float val = Float.parseFloat(s);
				list.getSelected().part.getTransform().pos[1] = val;
				LogUtils.getLogger().info("Val: " + val);
			} catch (Exception e) {

			}
		});
		// z
		edits.add(new EditBox(font, 16, 40, 80, 20, new TranslatableComponent("animationScreen.pos.z")));
		edits.get(2).setValue(String.valueOf(list.getSelected().part.getTransform().pos[2]));
		edits.get(2).setResponder((s) -> {
			try {
				float val = Float.parseFloat(s);
				list.getSelected().part.getTransform().pos[2] = val;
				LogUtils.getLogger().info("Val: " + val);
			} catch (Exception e) {

			}
		});

		// Rot
		// rx
		edits.add(new EditBox(font, 16, 60, 80, 20, new TranslatableComponent("animationScreen.rot.x")));
		edits.get(3).setValue(String.valueOf(list.getSelected().part.getTransform().rot[0]));
		edits.get(3).setResponder((s) -> {
			try {
				float val = Float.parseFloat(s);
				list.getSelected().part.getTransform().rot[0] = val;
				LogUtils.getLogger().info("Val: " + val);
			} catch (Exception e) {

			}
		});
		// ry
		edits.add(new EditBox(font, 16, 80, 80, 20, new TranslatableComponent("animationScreen.rot.y")));
		edits.get(4).setValue(String.valueOf(list.getSelected().part.getTransform().rot[1]));
		edits.get(4).setResponder((s) -> {
			try {
				float val = Float.parseFloat(s);
				list.getSelected().part.getTransform().rot[1] = val;
				LogUtils.getLogger().info("Val: " + val);
			} catch (Exception e) {

			}
		});
		// rz
		edits.add(new EditBox(font, 16, 100, 80, 20, new TranslatableComponent("animationScreen.rot.z")));
		edits.get(5).setValue(String.valueOf(list.getSelected().part.getTransform().rot[2]));
		edits.get(5).setResponder((s) -> {
			try {
				float val = Float.parseFloat(s);
				list.getSelected().part.getTransform().rot[2] = val;
				LogUtils.getLogger().info("Val: " + val);
			} catch (Exception e) {

			}
		});

		//Buttons
		buttons.add(new AButton(100, 49, 20, 20, (b) -> {
			start = true;
		}));
		
		// Initializing widgets
		for (Button b : buttons) {
			addRenderableWidget(b);
		}

		for (EditBox e : edits) {
			addRenderableWidget(e);
		}

		for (OptionsList b : options) {
			addRenderableWidget(b);
		}

	}

	@Override
	public void render(PoseStack matrix, int x, int y, float p_96565_) {
		super.render(matrix, x, y, p_96565_);
		
		// Gun Parts Selection Rendering
		list.render(matrix, x, y, p_96565_);
		
		// Field Indicators
		this.font.drawShadow(matrix, "x: ", (float) 10 + (-AnimationScreen.this.font.width("x: ") / 2), (float) (4),
				16777215, true);
		this.font.drawShadow(matrix, "y: ", (float) 10 + (-AnimationScreen.this.font.width("y: ") / 2), (float) (24),
				16777215, true);
		this.font.drawShadow(matrix, "z: ", (float) 10 + (-AnimationScreen.this.font.width("z: ") / 2), (float) (44),
				16777215, true);
		this.font.drawShadow(matrix, "rx: ", (float) 10 + (-AnimationScreen.this.font.width("rx: ") / 2), (float) (64),
				16777215, true);
		this.font.drawShadow(matrix, "ry: ", (float) 10 + (-AnimationScreen.this.font.width("ry: ") / 2), (float) (84),
				16777215, true);
		this.font.drawShadow(matrix, "rz: ", (float) 10 + (-AnimationScreen.this.font.width("rz: ") / 2), (float) (104),
				16777215, true);
		
		// Animation line rendering
		
		/*RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); 
		RenderSystem.setShaderTexture(0, WIDGETS); 
		this.blit(matrix, i, j, 0, 106, 20, 20);*/
		
		/*
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); 
		RenderSystem.setShaderTexture(0, WIDGETS); 
	    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
	    Matrix4f last = matrix.last().pose();
	    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
	    float v = 256;
	    float uv = 1f;
	    float xt = 0;
	    float yt = 0;
	    bufferbuilder.vertex(last, 0, 125, 0).uv(0, 126f/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.vertex(last, 90, 125, 0).uv(20f/256f, 126f/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.vertex(last, 90, 106, 0).uv(20f/256f, 106f/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.vertex(last, 0, 106, 0).uv(0, 106f/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.end();
	    BufferUploader.end(bufferbuilder);*/
		prev = prog;
		if(start) {
			if (prog < MAX) {
				prog += ClientHandler.partialTicks;
				if (prog > MAX) {
					prog = MAX;
				}
			} else {
				start = false;
			}
		}else {
			if (prog > 0) {
				prog -= ClientHandler.partialTicks;
				if (prog < 0) {
					prog = 0;
				}
			}
		}
		renderWidget(matrix, 100, 49, 0, 66, 200, 20, 100, -14);
		float v = (prev + (prog - prev)
				* (prev == 0 || 
				prev == MAX ? 0 : 
				ClientHandler.partialTicks)) / MAX;
		AButton button = (AButton)buttons.get(0);
		MAX = 40f;
		button.y = 108;
		button.x = (int)Mth.lerp(v, 100, 200);
	}
	
	public void renderWidget(PoseStack matrix, int x, int y, int i, int j, int w, 
			int h, int w2, int h2) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); 
		RenderSystem.setShaderTexture(0, WIDGETS); 
	    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
	    Matrix4f last = matrix.last().pose();
	    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
	    bufferbuilder.vertex(last, (i)+x, ((j+h)+y)+h2, 0).uv(0, (j+h)/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.vertex(last, ((i+w)+x)+w2, ((j+h)+y)+h2, 0).uv((i+w)/256f, (j+h)/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.vertex(last, ((i+w)+x)+w2, (j)+y, 0).uv((i+w)/256f, (j)/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.vertex(last, (i)+x, (j)+y, 0).uv(0, (j)/256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
	    bufferbuilder.end();
	    BufferUploader.end(bufferbuilder);
	}

	public class AButton extends Button {

		public AButton(int x, int y, int w, int h, OnPress press) {
			super(x, y, w, h, new TranslatableComponent(""), press);
		}
		
		@Override
		public void render(PoseStack matrix, int x, int y, float p_93660_) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); 
			RenderSystem.setShaderTexture(0, WIDGETS);
			blit(matrix, this.x, this.y, 224, 0, 15, 15);
			//renderWidget(matrix, this.x, this.y, 208, 0, 15, 15, 0, 0);
			//super.render(p_93657_, p_93658_, p_93659_, p_93660_);
		}
		
		@Override
		protected void renderBg(PoseStack p_93661_, Minecraft p_93662_, int p_93663_, int p_93664_) {
			super.renderBg(p_93661_, p_93662_, p_93663_, p_93664_);
		}
		
		@Override
		public void renderButton(PoseStack p_93746_, int p_93747_, int p_93748_, float p_93749_) {
			super.renderButton(p_93746_, p_93747_, p_93748_, p_93749_);
		}
		
		@Override
		public void renderToolTip(PoseStack p_93736_, int p_93737_, int p_93738_) {
			super.renderToolTip(p_93736_, p_93737_, p_93738_);
		}
		
	}
	
	@OnlyIn(Dist.CLIENT)
	public class AnimationSelectionList extends ObjectSelectionList<AnimationSelectionList.Entry> {

		public AnimationSelectionList(Minecraft p_94442_, GunModel model) {
			super(p_94442_, 100, 140, 2, 60, 16);
			this.setRenderTopAndBottom(false);
			// LogUtils.getLogger().info("Screen width: " + AnimationScreen.this.width +
			// " height: " + AnimationScreen.this.height);
			setLeftPos(100);
			for (int i = 0; i < model.getGunParts().size(); i++) {
				addEntry(new Entry(model.getGunParts().get(i), this.x0, 100 + (i * 18)));
				LogUtils.getLogger().info("Adding entry for part: " + model.getGunParts().get(i).getName());
			}
			for (int i = 0; i < model.getGunParts().size(); i++) {
				addEntry(new Entry(model.getGunParts().get(i), this.x0, 100 + (i * 18)));
				LogUtils.getLogger().info("Adding entry for part: " + model.getGunParts().get(i).getName());
			}
			for (int i = 0; i < model.getGunParts().size(); i++) {
				addEntry(new Entry(model.getGunParts().get(i), this.x0, 100 + (i * 18)));
				LogUtils.getLogger().info("Adding entry for part: " + model.getGunParts().get(i).getName());
			}
			setSelected(getEntry(0));
			LogUtils.getLogger().info("GunParts Size: " + model.getGunParts().size());
		}

		@Override
		public void render(PoseStack p_93447_, int p_93448_, int p_93449_, float p_93450_) {
			// super.render(p_93447_, p_93448_, p_93449_, p_93450_);
			this.renderBackground(p_93447_);
			int i = this.getScrollbarPosition();
			int j = i + 6;
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder bufferbuilder = tesselator.getBuilder();
			RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
			// hovered = this.isMouseOver((double)p_93448_, (double)p_93449_) ?
			// this.getEntryAtPosition((double)p_93448_, (double)p_93449_) : null;

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
						int l1 = this.x0 + this.width / 2 - (k1) / 2;
						int i2 = this.x0 + this.width / 2 + (k1) / 2;
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

					int j2 = this.getRowLeft();// Objects.equals(this.hovered, e)
					e.render(p_93452_, j, k, j2, k1, j1, p_93455_, p_93456_, false, p_93457_);
				}
			}
		}

		@Override
		protected void renderHeader(PoseStack p_93458_, int p_93459_, int p_93460_, Tesselator p_93461_) {
			super.renderHeader(p_93458_, p_93459_, p_93460_, p_93461_);
		}

		protected int getScrollbarPosition() {
			return x1 - 6;// super.getScrollbarPosition() + 110;
		}

		protected void renderBackground(PoseStack p_96105_) {

		}

		@OnlyIn(Dist.CLIENT)
		public class Entry extends ObjectSelectionList.Entry<AnimationSelectionList.Entry> {

			public GunModelPart part;
			int x, y;

			public Entry(GunModelPart part, int x, int y) {
				this.part = part;
				this.x = x;
				this.y = y;
			}

			@Override
			public Component getNarration() {
				return new TranslatableComponent("");
			}

			@Override
			public boolean mouseClicked(double p_94737_, double p_94738_, int button) {
				if (button == 0) {
					AnimationSelectionList.this.setSelected(this);
					return true;
				} else {
					return false;
				}
			}

			@Override
			public void render(PoseStack matrix, int p_93524_, int p_93525_, int p_93526_, int p_93527_, int p_93528_,
					int p_93529_, int p_93530_, boolean p_93531_, float p_93532_) {
				AnimationScreen.this.font.drawShadow(matrix, part.getName(),
						(float) (((AnimationScreen.this.width / 2) - 80)
								- (AnimationScreen.this.font.width(part.getName()) / 2)),
						(float) (p_93525_ + 1), 16777215, true);
			}

		}

	}

}
