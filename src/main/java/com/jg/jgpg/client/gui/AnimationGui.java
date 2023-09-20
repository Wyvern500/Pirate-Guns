package com.jg.jgpg.client.gui;

import java.util.function.Function;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.gui.widget.widgets.JgEditBox;
import com.jg.jgpg.client.gui.widget.widgets.JgList;
import com.jg.jgpg.client.gui.widget.widgets.JgList.AbstractJgListItem;
import com.jg.jgpg.client.gui.widget.widgets.KeyframeManagerWidget;
import com.jg.jgpg.client.gui.widget.widgets.SimpleButton;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AnimationGui extends Screen {

	ClientHandler client;
	KeyframeManagerWidget kfm;
	AbstractJgModel model;
	
	JgEditBox x;
	JgEditBox y;
	JgEditBox z;
	JgEditBox easing;
	
	JgList animations;
	
	public AnimationGui(ClientHandler client) {
		super(Component.translatable(""));
		this.client = client;
		
		kfm = new KeyframeManagerWidget(100, 10, 314, 100, this);
		animations = new JgList(0, 160, 100, 80, 4, this);
	}
	
	@Override
	protected void init() {
		super.init();
		
		Font font = Minecraft.getInstance().font;
		
		int windowWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		
		addRenderableWidget(x = new JgEditBox(font, 
				0, 0, 100, 20, Component.translatable("")));
		x.setResponder((s) -> {
			kfm.handleValueChange(0, x, s);
		});
		addRenderableWidget(y = new JgEditBox(font, 
				0, 20, 100, 20, Component.translatable("")));
		y.setResponder((s) -> {
			kfm.handleValueChange(1, y, s);
		});
		addRenderableWidget(z = new JgEditBox(font, 
				0, 40, 100, 20, Component.translatable("")));
		z.setResponder((s) -> {
			kfm.handleValueChange(2, z, s);
		});
		addRenderableWidget(easing = new JgEditBox(font, 
				0, 60, 100, 20, Component.translatable("")));
		easing.setResponder((s) -> {
			kfm.handleValueChange(3, easing, s);
		});
		addRenderableWidget(new SimpleButton(0, 82, 30, 20, Component.translatable("Stp"), 
				(b) -> {
					model.getAnimator().stop();
				}));
		addRenderableWidget(new SimpleButton(30, 82, 30, 20, Component.translatable("Ply"), 
				(b) -> {
					model.getAnimator().play();
				}));
		addRenderableWidget(new SimpleButton(60, 82, 30, 20, Component.translatable("Run"), 
				(b) -> {
					int selected = animations.getSelected();
					if(selected != -1) {
						model.getAnimator().setCurrent(model, animations.getSelectedItem().getAnimation());
					}
				}));
		addRenderableWidget(new SimpleButton(0, 102, 30, 20, Component.translatable("Save"), 
				(b) -> {
					model.getAnimator().save();
				}));
		addRenderableWidget(new SimpleButton(30, 102, 30, 20, Component.translatable("Nxt"), 
				(b) -> {
					model.getAnimator().nextTick(this);
				}));
		addRenderableWidget(new SimpleButton(60, 102, 30, 20, Component.translatable("Prv"), 
				(b) -> {
					model.getAnimator().prevTick(this);
				}));
		
		Function<String, Component> f = new Function<String, Component>() {
			
			@Override
			public Component apply(String t) {
				return Component.translatable(t);
			}
		};
		
		addRenderableWidget(CycleButton.builder(f).withValues("Normal", "Loop", "Debug")
				.withInitialValue("Normal").create(0, 122, 80, 20, Component.translatable("Type: "), 
						(cycleBtn, val) -> {
							switch(val) {
							case "Normal":
								model.getAnimator().setType(0);
								break;
							case "Loop":
								model.getAnimator().setType(1);
								break;
							case "Debug":
								model.getAnimator().setType(2);
								break;
							}
						}));
		
		addRenderableWidget(CycleButton.onOffBuilder(true).create(0, 142, 80, 20, 
				Component.translatable("Forward: "), (cycleBtn, val) -> {
					model.getAnimator().setMovementType(val);
				}));
		
		addRenderableWidget(kfm);
		addRenderableWidget(animations);
	}
	
	// Render
	
	@Override
	public void render(GuiGraphics gg, int x, int y, float p_282465_) {
		kfm.tick();
		super.render(gg, x, y, p_282465_);
		// RenderHelper.rect(gg, 200, 100, 10, 10, Color.rgba(255, 0, 0, 255));
		// LogUtils.log("AnimationGui", "x: " + x);
	}
	
	@Override
	public void renderBackground(GuiGraphics p_283688_) {
		super.renderBackground(p_283688_);
	}
	
	@Override
	public void renderDirtBackground(GuiGraphics p_282281_) {
		super.renderDirtBackground(p_282281_);
	}
	
	// Mouse
	
	@Override
	public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
		return super.mouseClicked(p_94695_, p_94696_, p_94697_);
	}
	
	@Override
	public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
		return super.mouseReleased(p_94722_, p_94723_, p_94724_);
	}
	
	@Override
	public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
		kfm.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
		return super.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
	}
	
	@Override
	public void mouseMoved(double p_94758_, double p_94759_) {
		super.mouseMoved(p_94758_, p_94759_);
	}
	
	@Override
	public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
		return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
	}
	
	// Keyboard
	
	@Override
	public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_) {
		if (p_96552_ == 256 && this.shouldCloseOnEsc()) {
	         this.onClose();
	         return true;
	      } else if (this.getFocused() != null && this.getFocused().keyPressed(p_96552_, p_96553_, p_96554_)) {
	         return true;
	      } else {
	         Object object;
	         switch (p_96552_) {
	            case 258:
	               break;
	            case 259:
	            case 260:
	            case 261:
	            default:
	               object = null;
	               break;
	            case 262:
	               break;
	            case 263:
	               break;
	            case 264:
	               break;
	            case 265:
	         }

	         return false;
	      }
	}
	
	@Override
	public boolean keyReleased(int p_94715_, int p_94716_, int p_94717_) {
		return super.keyReleased(p_94715_, p_94716_, p_94717_);
	}
	
	// Getters and setters
	
	public KeyframeManagerWidget getKeyframeManager() {
		return kfm;
	}

	public AbstractJgModel getModel() {
		return model;
	}

	public void setModel(AbstractJgModel model) {
		this.model = model;
		kfm.setupForModel(model);
		animations.clear();
		for(Animation animation : model.getAnimations()) {
			animations.addItem(new AnimationListItem(animations, animation));
		}
	}
	
	public ClientHandler getClient() {
		return client;
	}

	public JgEditBox getXEditBox() {
		return x;
	}

	public JgEditBox getYEditBox() {
		return y;
	}

	public JgEditBox getZEditBox() {
		return z;
	}

	public JgEditBox getEasingEditBox() {
		return easing;
	}
	
	public static class AnimationListItem extends AbstractJgListItem {

		Animation animation;
		
		public AnimationListItem(JgList parent, Animation animation) {
			super(parent);
			this.animation = animation;
		}

		@Override
		public void render(GuiGraphics gg, int x, int y, int w, int h, int i) {
			int color = Color.rgba(0, 0, 0, 0);
			if(i == parent.getSelected()) {
				color = Color.rgba(90, 90, 90, 255);
			}
			RenderHelper.rect(gg, x, y, w, h, color);
			gg.drawString(Minecraft.getInstance().font, animation.getName(), x, y + (h / 2), 
					Color.rgba(255, 255, 255, 255));
		}

		@Override
		public void updateHorinzontalStuff(int offset) {
			
		}

		@Override
		public void handleMouse(double mx, double my, int btn, double x, double y, double w, double h) {
			
		}

		@Override
		public boolean mouseReleased(double mx, double my, int btn, double x, double y, double w, double h) {
			return false;
		}

		@Override
		public void onUnselect(double mx, double my, int btn, double x, double y, double w, double h) {
			
		}
		
		public Animation getAnimation() {
			return animation;
		}
		
	}
	
}
