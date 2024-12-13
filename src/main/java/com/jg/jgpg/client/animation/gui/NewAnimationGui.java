package com.jg.jgpg.client.animation.gui;

import com.jg.jgpg.client.animation.*;
import com.jg.jgpg.client.animation.gui.widget.*;
import com.jg.jgpg.client.animation.gui.widget.NewJgList.AbstractJgListItem;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import com.jg.jgpg.client.handler.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.Pair;
import com.jg.jgpg.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class NewAnimationGui extends Screen {

	private static final List<KeyframeItem> selectedKeyframes = new ArrayList<>();
	private static final List<KeyframeItem> copiedKeyframes = new ArrayList<>();
	private static boolean[] keys = new boolean[350];
	
	public static int X = 182;
	public static int Y = 20;
	public static int W = 324;
	public static int H = 100;
	public static int KEYFRAMESSTARTX = 102;
	public static int VISIBLETICKS = 20;
	public static int TOTALTICKS = 170;
	public static int TILESIZE = (W - KEYFRAMESSTARTX) / VISIBLETICKS;
	
	private static int playMode = 0;
	
	ClientHandler client;
	
	JgEditBox x;
	JgEditBox y;
	JgEditBox z;
	JgEditBox easing;
	JgEditBox transformTick;
	JgEditBox cycles;
	
	NewJgList animations;
	
	CycleButton<Boolean> adjustRemaining;
	CycleButton<Boolean> shouldLerp;
	CycleButton<Boolean> shouldAddOtherPartsCycleBtn;
	CycleButton<String> keyframeType;
	
	SimpleButton build;
	
	GuiModelPartKeyframesHandler guiModelPartKeyframesHandler;
	TickMarkerWidget tickMarker;
	ScrollbarWidget keyframesScrollbar;
	ScrollbarWidget partsScrollbar;
	ScrollbarWidget animationsScrollbar;
	RangeHandler visibleTicksHandler;
	RangeHandler animationsHandler;
	
	JgModel model;
	
	boolean shouldAdjustRemaining;
	boolean down;
	boolean shouldAddOtherParts;
	
	public NewAnimationGui(ClientHandler client) {
		super(Component.translatable(""));
		this.client = client;
		
		animationsHandler = new RangeHandler(4);
		animationsHandler.update(4, 0);
		animations = new NewJgList(0, 160, 90, 80, this, animationsHandler);
	}
	
	@Override
	protected void init() {
		super.init();
		
		NewAnimationGui.X = 102;
		NewAnimationGui.Y = 10;
		NewAnimationGui.W = 245;
		NewAnimationGui.H = 100;
		NewAnimationGui.KEYFRAMESSTARTX = 182;
		NewAnimationGui.TILESIZE = W / VISIBLETICKS;
		
		Font font = Minecraft.getInstance().font;
		
		int windowWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		
		// Animation stuff
		
		animationsScrollbar = new ScrollbarWidget(this, 90, 160, 10, 80, 10, 10);
		animationsScrollbar.setCallback(this::updateAnimationsList);
		
		if(model.getAnimator().getAnimation() != null) {
			TOTALTICKS = model.getAnimator().getAnimation().getDuration();
		}
		visibleTicksHandler = new RangeHandler(VISIBLETICKS);
		visibleTicksHandler.update(TOTALTICKS, 0);
		
		guiModelPartKeyframesHandler = new GuiModelPartKeyframesHandler(this, visibleTicksHandler);
		guiModelPartKeyframesHandler.setModel(model);
		addRenderableWidget(guiModelPartKeyframesHandler);
		
		tickMarker = new TickMarkerWidget(this, KEYFRAMESSTARTX, Y, TILESIZE);
		addRenderableWidget(tickMarker);
		
		keyframesScrollbar = new ScrollbarWidget(this, KEYFRAMESSTARTX, Y - 10, W, 10, 
				10, 10);
		keyframesScrollbar.setCallback((p) -> {
			updateKeyframes(p);
		});
		addRenderableWidget(keyframesScrollbar);
		
		partsScrollbar = new ScrollbarWidget(this, guiModelPartKeyframesHandler.getX() + 
				guiModelPartKeyframesHandler.getWidth(), 
				guiModelPartKeyframesHandler.getY(), 10, guiModelPartKeyframesHandler.getHeight(), 10, 10);
		partsScrollbar.setCallback((p) -> {
			guiModelPartKeyframesHandler.update(p);
		});
		addRenderableWidget(partsScrollbar);
		
		// General stuff
		
		addRenderableWidget(x = new JgEditBox(font, 
				0, 0, 100, 20, Component.translatable("")));
		x.setResponder((s) -> {
			handleValueChange(0, x, s);
		});
		addRenderableWidget(y = new JgEditBox(font, 
				0, 20, 100, 20, Component.translatable("")));
		y.setResponder((s) -> {
			handleValueChange(1, y, s);
		});
		addRenderableWidget(z = new JgEditBox(font, 
				0, 40, 100, 20, Component.translatable("")));
		z.setResponder((s) -> {
			handleValueChange(2, z, s);
		});
		addRenderableWidget(easing = new JgEditBox(font, 
				0, 60, 100, 20, Component.translatable("")));
		easing.setResponder((s) -> {
			handleValueChange(3, easing, s);
		});
		addRenderableWidget(transformTick = new JgEditBox(font, 
				100, 120, 100, 20, (eb) -> {
					onEditBoxEnter(eb);
				}));
		transformTick.setResponder((s) -> {
			handleValueChange(4, transformTick, s);
		});
		addRenderableWidget(build = new SimpleButton(0, 0, 40, 20, Component.translatable("Build"), 
				(b) -> {
					Animation animation = model.getAnimator().getAnimation();
					if(animation instanceof RepetitiveAnimation) {
						String finalText = "";
						finalText = cycles.getValue().replaceAll(
						          "[^a-zA-Z0-9.]", "");
						if(finalText.length() == 0) {
							finalText = "0";
						}
						((RepetitiveAnimation) animation).setCycles(Integer.parseInt(finalText));
						LogUtils.log("NewAnimationGui", "Cycles: " + Integer.parseInt(finalText));
						((RepetitiveAnimation) animation).build();
						//((RepetitiveAnimation) animation).end();
						model.getAnimator().update();
					}
				}));
		addRenderableWidget(cycles = new JgEditBox(font, 
				0, 20, 100, 20, Component.translatable("")));
		cycles.setResponder((s) -> {
			Animation animation = model.getAnimator().getAnimation();
			if(animation instanceof RepetitiveAnimation) {
				String finalText = "";
				finalText = cycles.getValue().replaceAll(
				          "[^a-zA-Z0-9.]", "");
				if(finalText.length() == 0) {
					finalText = "0";
				}
				((RepetitiveAnimation) animation).setCycles(Integer.parseInt(finalText));
				LogUtils.log("NewAnimationGui", "Cycles: " + Integer.parseInt(finalText));
			}
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
						selectedKeyframes.clear();
						model.getAnimator().setAnimation(animations.getSelectedItem().getAnimation());
					}
				}));
		addRenderableWidget(new SimpleButton(0, 102, 30, 20, Component.translatable("Save"), 
				(b) -> {
					model.getAnimator().save();
				}));
		addRenderableWidget(new SimpleButton(30, 102, 30, 20, Component.translatable("Nxt"), 
				(b) -> {
					model.getAnimator().nextTick();
					if(model.getAnimator().getAnimation() != null) {
						int newOffset = (int) (Math.floor(model.getAnimator().getTick() / NewAnimationGui
								.VISIBLETICKS) * NewAnimationGui.VISIBLETICKS);
						visibleTicksHandler.setOffset(newOffset, model.getAnimator().getAnimation()
								.getDuration());
						keyframesScrollbar.setProgress((model.getAnimator().getTick() + 0.0f) / model.getAnimator()
								.getAnimation().getDuration());
						tickMarker.setVisualTick((int) model.getAnimator().getTick());
					}
				}));
		addRenderableWidget(new SimpleButton(60, 102, 30, 20, Component.translatable("Prv"), 
				(b) -> {
					model.getAnimator().prevTick();
					if(model.getAnimator().getAnimation() != null) {
						int newOffset = (int) (Math.floor(model.getAnimator().getTick() / NewAnimationGui
								.VISIBLETICKS) * NewAnimationGui.VISIBLETICKS);
						visibleTicksHandler.setOffset(newOffset, model.getAnimator().getAnimation().getDuration());
						keyframesScrollbar.setProgress((model.getAnimator().getTick() + 0.0f) / model.getAnimator()
								.getAnimation().getDuration());
						tickMarker.setVisualTick((int) model.getAnimator().getTick());
					}
				}));
		
		Function<String, Component> f = new Function<String, Component>() {
			
			@Override
			public Component apply(String t) {
				return Component.translatable(t);
			}
		};
		
		String playModeString = "";
		switch(playMode) {
		case 0:
			playModeString = "Normal";
			break;
		case 1:
			playModeString = "Loop";
			break;
		}
		LogUtils.log("NewAnimationGui", "PlayMode: " + playMode);
		addRenderableWidget(CycleButton.builder(f).withValues("Normal", "Loop", "Debug")
				.withInitialValue(playModeString).create(0, 122, 80, 20, Component.translatable("Type"), 
						(cycleBtn, val) -> {
							switch(val) {
							case "Normal":
								NewAnimationGui.playMode = 0;
								break;
							case "Loop":
								NewAnimationGui.playMode = 1;
								break;
							}
						}));
		addRenderableWidget(adjustRemaining = CycleButton.onOffBuilder(false).create(100, 140, 100, 20, 
				Component.translatable("AdjustRem"), (cycleBtn, val) -> {
					shouldAdjustRemaining = val.booleanValue();
				}));
		addRenderableWidget(shouldLerp = CycleButton.onOffBuilder(false).create(0, 140, 100, 20, 
				Component.translatable("ShouldLerp"), (cycleBtn, val) -> {
					tickMarker.setShouldLerp(val);
				}));
		addRenderableWidget(shouldAddOtherPartsCycleBtn = CycleButton.onOffBuilder(false).create(0, 40, 100, 
				20, 
				Component.translatable("ShouldAddKfs"), (cycleBtn, val) -> {
					shouldAddOtherParts = val;
				}));
		addRenderableWidget(keyframeType = CycleButton.builder(f).withValues("Normal", "Cycle")
				.withInitialValue("Normal").create(100, 160, 80, 20, Component.translatable("KfType"), 
						(cycleBtn, val) -> {
							switch(val) {
							case "Normal":
								for(KeyframeItem kfi : NewAnimationGui.selectedKeyframes) {
									kfi.getKeyframe().setRepetitive(false);
								}
								break;
							case "Cycle":
								for(KeyframeItem kfi : NewAnimationGui.selectedKeyframes) {
									kfi.getKeyframe().setRepetitive(true);
								}
								break;
							}
		}));
		addRenderableWidget(animations);
		addRenderableWidget(animationsScrollbar);
		
		Animation animation = getModel().getAnimator().getAnimation();
		if(animation != null && getSelectedKeyframes().isEmpty()) {
			if(animation instanceof RepetitiveAnimation) {
				cycles.setValue(String.valueOf(((RepetitiveAnimation) getModel().getAnimator()
						.getAnimation()).getCycles()));
			}
		}
		build.setActive(false);
		build.visible = false;
		shouldAddOtherPartsCycleBtn.visible = false;
		shouldAddOtherPartsCycleBtn.active = false;
	}
	
	public void updateAnimationsList(float progress) {
		animationsHandler.update(animations.size(), progress);
	}
	
	// Transform Keyframes
	
	public void handleValueChange(int type, JgEditBox field, String text) {
		switch(type) {
		case 0:
			if(!selectedKeyframes.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll("[^0-9.-]", "");
				if(finalText.length() == 0 || !finalText.matches(".*\\d.*")) {
					finalText = "0.0";
				}
				for(KeyframeItem data : selectedKeyframes) {
					data.getKeyframe().getVec().setX(Float.valueOf(finalText));
				}
			}
			//update();
			break;
		case 1:
			if(!selectedKeyframes.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll("[^0-9.-]", "");
				if(finalText.length() == 0 || !finalText.matches(".*\\d.*")) {
					finalText = "0.0";
				}
				for(KeyframeItem data : selectedKeyframes) {
					data.getKeyframe().getVec().setY(Float.valueOf(finalText));
				}
			}
			//update();
			break;
		case 2:
			if(!selectedKeyframes.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll("[^0-9.-]", "");
				if(finalText.length() == 0 || !finalText.matches(".*\\d.*")) {
					finalText = "0.0";
				}
				for(KeyframeItem data : selectedKeyframes) {
					data.getKeyframe().getVec().setZ(Float.valueOf(finalText));
				}
			}
			//update();
			break;
		case 3:
			if(!selectedKeyframes.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll(
				          "[^a-zA-Z0-9.]", "");
				if(finalText.length() == 0) {
					finalText = "empty";
				}
				if(!EasingHandler.INSTANCE.has(finalText)) {
					finalText = "empty";
				}
				for(KeyframeItem data : selectedKeyframes) {
					data.getKeyframe().setEasing(finalText);
				}
			}
			//update();
			break;
		}
	}
	
	public void onEditBoxEnter(JgEditBox box) {
		handleChangeTransformTick(box);
		setFocused(box);
	}
	
	private void handleChangeTransformTick(JgEditBox box) {
		if(!selectedKeyframes.isEmpty() && model.getAnimator().getAnimation() != null) {
			String finalText = "";
			if(!box.getValue().contains("/")) {
				finalText = box.getValue().replaceAll(
				          "[^0-9]", "");
				if(finalText.length() == 0) {
					finalText = "0";
				}
			}
			
			if(!adjustRemaining.getValue()) {
				int anchorTick = selectedKeyframes.get(0).getKeyframe().getTick();
				
				List<KeyframeItem> toDelete = new ArrayList<>();
				
				for(KeyframeItem kfi : selectedKeyframes) {
					int offset = (kfi.getKeyframe().getTick() - anchorTick);
					int newTick = Integer.parseInt(finalText) + offset;
					List<Keyframe> keyframes = kfi.isTranslate() ? kfi.getHandler().getTranslations() :
						kfi.getHandler().getRotations();
					
					for(int i = 0; i < keyframes.size(); i++) {
						Keyframe kf = keyframes.get(i);
						if(kf.getTick() == newTick) {
							toDelete.add(new KeyframeItem(kfi.getHandler(), kf, i, kfi.isTranslate()));
						} else if(kf.getTick() > newTick) {
							break;
						}
					}
					
					kfi.getKeyframe().setTick(newTick);
				}
				
				for(KeyframeItem kfi : toDelete) {
					List<Keyframe> keyframes = kfi.isTranslate() ? kfi.getHandler().getTranslations() :
						kfi.getHandler().getRotations();
					keyframes.remove(kfi.getKeyframe());
				}
				
				toDelete.clear();
				
				if(shouldAddOtherParts) {
					Utils.tryToAddPrevKeyframes(selectedKeyframes.get(0).getKeyframe(), model);
				}
				
				update();
			} else {
				int anchorTick = selectedKeyframes.get(0).getKeyframe().getTick();
				for(ModelPartKeyframesHandler handler : model.getAnimator().getAnimation().getPartHandlers()
						.values()) {
					List<Keyframe> toDelete = new ArrayList<>();
					for(int i = 0; i < handler.getTranslations().size(); i++) {
						Keyframe kf = handler.getTranslations().get(i);
						if(kf.getTick() >= anchorTick) {
							int offset = (kf.getTick() - anchorTick);
							int newTick = Integer.parseInt(finalText) + offset;
							if(newTick < kf.getTick()) {
								for(Keyframe kfToCompare : handler.getTranslations()) {
									if(kfToCompare.getTick() == newTick) {
										toDelete.add(kfToCompare);
									}
								}
							}
							kf.setTick(newTick);
						}
					}
					handler.getTranslations().removeAll(toDelete);
					toDelete.clear();
					for(int i = 0; i < handler.getRotations().size(); i++) {
						Keyframe kf = handler.getRotations().get(i);
						if(kf.getTick() >= anchorTick) {
							int offset = (kf.getTick() - anchorTick);
							int newTick = Integer.parseInt(finalText) + offset;
							if(newTick < kf.getTick()) {
								for(Keyframe kfToCompare : handler.getRotations()) {
									if(kfToCompare.getTick() == newTick) {
										toDelete.add(kfToCompare);
									}
								}
							}
							kf.setTick(newTick);
						}
					}
					handler.getRotations().removeAll(toDelete);
					toDelete.clear();
				}
				
				update();
			}
		}
	}
	
	public void update() {
		model.getAnimator().getAnimation().end();
		model.getAnimator().update();
		LogUtils.log("NewAnimationGui", "Animation Duration: " + model.getAnimator().getAnimation()
				.getDuration());
	}
	
	public void updateKeyframes(float progress) {
		if(model.getAnimator().getAnimation() != null) {
			TOTALTICKS = model.getAnimator().getAnimation().getDuration() + 30;
		}
		if(TOTALTICKS % VISIBLETICKS != 0) {
			TOTALTICKS = (int) ((Math.floor(TOTALTICKS / VISIBLETICKS) + 1) * VISIBLETICKS);
		}
		visibleTicksHandler.update(TOTALTICKS, progress);
	}
	
	public static boolean isKeyframeSelected(Keyframe keyframe) {
		for(KeyframeItem kfi : selectedKeyframes) {
			if(kfi.getKeyframe() == keyframe) {
				return true;
			}
		}
		return false;
	}

	public static void select(ModelPartKeyframesHandler handler, Keyframe kf, int index, boolean translate) {
		KeyframeItem kfi = new KeyframeItem(handler, kf, index, translate);
		selectedKeyframes.add(kfi);
	}
	
	public static void unselect(Keyframe kf) {
		for(int i = 0; i < selectedKeyframes.size(); i++) {
			if(selectedKeyframes.get(i).getKeyframe() == kf) {
				selectedKeyframes.remove(i);
			}
		}
	}
	
	public void tick() {
		if(model.getAnimator().getAnimation() != null) {
			if(model.getAnimator().canPlay()) {
				int newOffset = (int) (Math.floor(model.getAnimator().getTick() / NewAnimationGui
						.VISIBLETICKS) * NewAnimationGui.VISIBLETICKS);
				visibleTicksHandler.setOffset(newOffset, model.getAnimator().getAnimation().getDuration());
				keyframesScrollbar.setProgress((model.getAnimator().getTick() + 0.0f) / model.getAnimator()
						.getAnimation().getDuration());
				//LogUtils.log("NewAnimationGui", "ticking");
			}
		}
	}
	
	// Render
	
	@Override
	public void render(GuiGraphics gg, int x, int y, float p_282465_) {
		// 102 keyframesStartX
		// Maxy = 240 o 220 (y) + 80 (height)
		tick();
		
		int color = Color.rgba(0, 0, 0, 255);
		RenderHelper.rect(gg, NewAnimationGui.X + 80, NewAnimationGui.Y, NewAnimationGui.W, 
				NewAnimationGui.H, color);
		color = Color.rgba(0, 0, 255, 255);
		Animation animation = model.getAnimator().getAnimation();
		if(animation != null) {
			int animationDuration = animation.getDuration();
			if(animationDuration >= visibleTicksHandler.getOffsetStart() && animationDuration 
					<= visibleTicksHandler.getOffsetEnd()) {
				RenderHelper.rect(gg, KEYFRAMESSTARTX + ((animationDuration - visibleTicksHandler
						.getOffsetStart()) * TILESIZE), Y, 5, H, color);
			}
		}
		
		Animation current = getModel().getAnimator().getAnimation();
		if(current != null) {
			if(current instanceof RepetitiveAnimation) {
				if(selectedKeyframes.isEmpty()) {
					this.x.setVisible(false);
					this.y.setVisible(false);
					this.z.setVisible(false);
					this.easing.setVisible(false);
					this.transformTick.setVisible(false);
					this.adjustRemaining.active = false;
					this.adjustRemaining.visible = false;
					this.keyframeType.active = false;
					this.keyframeType.visible = false;
					this.shouldAddOtherPartsCycleBtn.visible = true;
					this.shouldAddOtherPartsCycleBtn.active = true;
					if(!cycles.isVisible()) {
						cycles.setValue(String.valueOf(((RepetitiveAnimation) getModel().getAnimator()
								.getAnimation()).getCycles()));
						LogUtils.log("AnimationGui", "sd");
					}
					cycles.setVisible(true);
					build.setActive(true);
				} else {
					this.x.setVisible(true);
					this.y.setVisible(true);
					this.z.setVisible(true);
					this.easing.setVisible(true);
					cycles.setVisible(false);
					build.setActive(false);
					build.visible = false;
					this.shouldAddOtherPartsCycleBtn.visible = false;
					this.shouldAddOtherPartsCycleBtn.active = false;
					this.transformTick.setVisible(true);
					this.adjustRemaining.active = true;
					this.adjustRemaining.visible = true;
					this.keyframeType.active = true;
					this.keyframeType.visible = true;
				}
			} else {
				// Normal Animation
				if(selectedKeyframes.isEmpty()) {
					this.x.setVisible(false);
					this.y.setVisible(false);
					this.z.setVisible(false);
					this.easing.setVisible(false);
					this.transformTick.setVisible(false);
					this.adjustRemaining.active = false;
					this.adjustRemaining.visible = false;
					this.keyframeType.active = false;
					this.keyframeType.visible = false;
					this.shouldAddOtherPartsCycleBtn.visible = true;
					this.shouldAddOtherPartsCycleBtn.active = true;
				} else {
					this.x.setVisible(true);
					this.y.setVisible(true);
					this.z.setVisible(true);
					this.easing.setVisible(true);
					this.transformTick.setVisible(true);
					this.adjustRemaining.active = true;
					this.adjustRemaining.visible = true;
					this.keyframeType.active = true;
					this.keyframeType.visible = true;
					cycles.setVisible(false);
					build.setActive(false);
					build.visible = false;
					this.shouldAddOtherPartsCycleBtn.visible = false;
					this.shouldAddOtherPartsCycleBtn.active = false;
				}
			}
		}
		super.render(gg, x, y, p_282465_);
		
		gg.drawString(font, "Tick: " + model.getAnimator().getTick(), 110, 190, 
				Color.rgba(255, 255, 255, 255));
		gg.drawString(font, "VisualTick: " + tickMarker.getVisualTick(), 110, 204, 
				Color.rgba(255, 255, 255, 255));
		
		color = 0xffffff;
		for(int i = visibleTicksHandler.getOffsetStart(); i <= visibleTicksHandler.getOffsetEnd(); i++) {
			// 150 = Parts Rendering width
			// 20 = Scrollbar width
			if(i % 10 == 0 || i == 0) {
				gg.drawString(font, String.valueOf(i), (KEYFRAMESSTARTX + 
						((i - visibleTicksHandler.getOffsetStart()) * TILESIZE)), Y, color);
			}
		}
	}
	
	// Mouse
	
	@Override
	public boolean mouseClicked(double mx, double my, int p_94697_) {
		/*LogUtils.log("NewAnimationGui", "MouseX: " + mx + " mouseY: " + my + " focused: " +
				getFocused());*/
		if(mx > KEYFRAMESSTARTX && mx < KEYFRAMESSTARTX + W && my > 10 && my < H + 10) {
			setFocused(null);
		}
		return super.mouseClicked(mx, my, p_94697_);
	}
	
	@Override
	public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
		guiModelPartKeyframesHandler.mouseReleased(p_94722_, p_94723_, p_94724_);
		return super.mouseReleased(p_94722_, p_94723_, p_94724_);
	}
	
	@Override
	public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
		tickMarker.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
		guiModelPartKeyframesHandler.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
		return super.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
	}
	
	@Override
	public void mouseMoved(double p_94758_, double p_94759_) {
		super.mouseMoved(p_94758_, p_94759_);
	}
	
	// Keyboard
	
	@Override
	public boolean keyPressed(int keycode, int p_96553_, int p_96554_) {
		boolean[] keys = ClientEventHandler.keys;
		keys[keycode] = true;
		
		/*LogUtils.log("NewAnimationGui", "First: " + keys[GLFW.GLFW_KEY_D] + " 2nd: " 
				+ keys[GLFW.GLFW_KEY_LEFT_CONTROL]);*/
		
		//LogUtils.log("NewAnimationGui", "Delete: " + keys[259]);
		
		//LogUtils.log("NewAnimationGui", "Focused: " + getFocused());
		if(getFocused() == null || !(getFocused() instanceof JgEditBox)) {
			if(keys[GLFW.GLFW_KEY_C] && keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
				// Copy
				// Adding keyframeItems to the copy list
				copiedKeyframes.clear();
				for(int i = 0; i < selectedKeyframes.size(); i++) {
					KeyframeItem kfi = selectedKeyframes.get(i);
					copiedKeyframes.add(kfi.copy());
				}
			} else if(keys[GLFW.GLFW_KEY_V] && keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
				// Paste
				if(!copiedKeyframes.isEmpty()) {
					int anchorTick = copiedKeyframes.get(0).getKeyframe().getTick();
					for(KeyframeItem kfi : copiedKeyframes) {
						int offset = kfi.getKeyframe().getTick() - anchorTick;
						int newTick = tickMarker.getVisualTick() + offset;
						List<Keyframe> keyframes = kfi.isTranslate() ? kfi.getHandler().getTranslations() : 
							kfi.getHandler().getRotations();
						Keyframe kf = kfi.getKeyframe().copy().setTick(newTick);
						Utils.tryToRemoveKeyframe(kfi.getHandler(), kf.getTick(), kfi.isTranslate());
						keyframes.add(kf);
					}
					model.getAnimator().getAnimation().end();
					
					if(shouldAddOtherParts) {
						Utils.tryToAddPrevKeyframes(copiedKeyframes.get(0).getKeyframe(), model);
					}
					
					model.getAnimator().update();
				}
			} else if(keys[GLFW.GLFW_KEY_LEFT_CONTROL] && keys[GLFW.GLFW_KEY_A]) {
				if(!selectedKeyframes.isEmpty()) {
					KeyframeItem first = selectedKeyframes.get(0);
					if(model.getAnimator().getAnimation() != null) {
						for(ModelPartKeyframesHandler handler : model.getAnimator().getAnimation()
								.getPartHandlers().values()) {
							for(int i = 0; i < handler.getTranslations().size(); i++) {
								Keyframe kf = handler.getTranslations().get(i);
								if(kf != first.getKeyframe()) { 
									if(kf.getTick() == first.getKeyframe().getTick()) {
										NewAnimationGui.select(handler, kf, i, true);
									}
								}
							}
							for(int i = 0; i < handler.getRotations().size(); i++) {
								Keyframe kf = handler.getRotations().get(i);
								if(kf != first.getKeyframe()) { 
									if(kf.getTick() == first.getKeyframe().getTick()) {
										NewAnimationGui.select(handler, kf, i, false);
									}
								}
							}
						}
					}
				}
			} else if(keys[259]) {
				// Delete
				for(KeyframeItem kfi : selectedKeyframes) {
					List<Keyframe> keyframes = kfi.isTranslate() ? kfi.getHandler().getTranslations() : 
						kfi.getHandler().getRotations();
					keyframes.remove(kfi.getKeyframe());
				}
				model.getAnimator().getAnimation().end();
				model.getAnimator().update();
				selectedKeyframes.clear();
			} else if(keys[GLFW.GLFW_KEY_D] && keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
				// Duplicate
				for(KeyframeItem kfi : selectedKeyframes) {
					List<Keyframe> keyframes = kfi.isTranslate() ? kfi.getHandler().getTranslations() : 
						kfi.getHandler().getRotations();
					Utils.tryToRemoveKeyframe(kfi.getHandler(), kfi.getKeyframe().getTick() + 1, 
							kfi.isTranslate());
					keyframes.add(kfi.getKeyframe().copy().setTick(kfi.getKeyframe().getTick() + 1));
				}
				model.getAnimator().getAnimation().end();
				model.getAnimator().update();
			} else if(keys[GLFW.GLFW_KEY_C] && keys[GLFW.GLFW_KEY_K]) {
				if(model.getAnimator().getAnimation() != null) {
					LogUtils.log("NewAnimationGui", "Checking for cloned keyframes");
					for(ModelPartKeyframesHandler handler : model.getAnimator().getAnimation()
							.getPartHandlers().values()) {
						for(int i = 0; i < handler.getTranslations().size() - 1; i++) {
							Keyframe current = handler.getTranslations().get(i);
							Keyframe next = handler.getTranslations().get(i + 1);
							if(next.getTick() == current.getTick()) {
								LogUtils.log("NewAnimationGui", "Translation: Same Tick " + 
										current.getTick());
							}
						}
						for(int i = 0; i < handler.getRotations().size() - 1; i++) {
							Keyframe current = handler.getRotations().get(i);
							Keyframe next = handler.getRotations().get(i + 1);
							if(next.getTick() == current.getTick()) {
								LogUtils.log("NewAnimationGui", "Rotation: Same Tick " + current.getTick());
							}
						}
					}
				}
			} else if(keys[GLFW.GLFW_KEY_LEFT_CONTROL] && keys[GLFW.GLFW_KEY_R]) {
				// Remove keyframes since the first selected keyframe
				if(!selectedKeyframes.isEmpty()) {
					int firstTick = selectedKeyframes.get(0).getKeyframe().getTick();
					for(ModelPartKeyframesHandler handler : model.getAnimator().getAnimation()
							.getPartHandlers().values()) {
						List<Keyframe> toDelete = new ArrayList<>();
						for(int i = handler.getTranslations().size() - 1; i > -1; i--) {
							Keyframe kf = handler.getTranslations().get(i);
							if(kf.getTick() > firstTick) {
								toDelete.add(kf);
							}
						}
						handler.getTranslations().removeAll(toDelete);
						toDelete.clear();
						for(int i = handler.getRotations().size() - 1; i > -1; i--) {
							Keyframe kf = handler.getRotations().get(i);
							if(kf.getTick() > firstTick) {
								toDelete.add(kf);
							}
						}
						handler.getRotations().removeAll(toDelete);
					}
					model.getAnimator().update();
					model.getAnimator().getAnimation().end();
				}
			} else if(keys[GLFW.GLFW_KEY_LEFT_CONTROL] && keys[GLFW.GLFW_KEY_Z]) {
				// Add zero kfs
				if(!selectedKeyframes.isEmpty()) {
					int firstTick = selectedKeyframes.get(0).getKeyframe().getTick();
					for(ModelPartKeyframesHandler handler : model.getAnimator().getAnimation()
							.getPartHandlers().values()) {
						boolean containsZeroTrKf = false;
						boolean containsZeroRtKf = false;
						boolean containsLastTrKf = false;
						boolean containsLastRtKf = false;
						for(int i = handler.getTranslations().size() - 1; i > -1; i--) {
							Keyframe kf = handler.getTranslations().get(i);
							if(kf.getTick() == firstTick + 10) {
								containsZeroTrKf = true;
							} else if(kf.getTick() < firstTick + 10 && !containsZeroTrKf) {
								handler.addKeyframe(kf.copy().toZero().setEasing("easeInOutSine")
										.setTick(firstTick + 10), true);
								containsZeroTrKf = true;
							}
							if(kf.getTick() == firstTick) {
								containsLastTrKf = true;
							} else if(kf.getTick() < firstTick && !containsLastTrKf) {
								handler.addKeyframe(kf.copy().setTick(firstTick), true);
								break;
							}
						}
						for(int i = handler.getRotations().size() - 1; i > -1; i--) {
							Keyframe kf = handler.getRotations().get(i);
							if(kf.getTick() == firstTick + 10) {
								containsZeroRtKf = true;
							} else if(kf.getTick() < firstTick + 10 && !containsZeroRtKf) {
								handler.addKeyframe(kf.copy().toZero().setEasing("easeInOutSine")
										.setTick(firstTick + 10), false);
								containsZeroRtKf = true;
							}
							if(kf.getTick() == firstTick) {
								containsLastRtKf = true;
							} else if(kf.getTick() < firstTick && !containsLastRtKf) {
								handler.addKeyframe(kf.copy().setTick(firstTick), false);
								break;
							}
						}
					}
					model.getAnimator().update();
					model.getAnimator().getAnimation().end();
				}
			} else if(keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
				guiModelPartKeyframesHandler.setCanAddMultipleKeyframes(true);
			}
			if(keys[GLFW.GLFW_KEY_I]) {
				if(model.getAnimator().getAnimation() != null) {
					if(model.getAnimator().getAnimation() instanceof RepetitiveAnimation) {
						LogUtils.log("NewAnimationGui", "Printing Keyframes");
						((RepetitiveAnimation) model.getAnimator().getAnimation()).printKeyframes();
					} else {
						List<Pair<JgModelPart, Keyframe>> data = new ArrayList<>();
						for(ModelPartKeyframesHandler handler : model.getAnimator().getAnimation()
								.getPartHandlers().values()) {
							for(Keyframe kf : handler.getTranslations()) {
								data.add(new Pair<JgModelPart, Keyframe>(handler.getPart(), kf));
							}
							for(Keyframe kf : handler.getRotations()) {
								data.add(new Pair<JgModelPart, Keyframe>(handler.getPart(), kf));
							}
						}
						Collections.sort(data, (i1, i2) -> Integer.compare(i1.getRight().getTick(), i2
								.getRight().getTick()));
						LogUtils.log("NewAnimationGui", "Printing Keyframes");
						for(Pair<JgModelPart, Keyframe> kf : data) {
							LogUtils.log("NewAnimationGui", "Part: " + kf.getLeft().getName() + " KfTick: " + kf
									.getRight().getTick() + " isShadow? " + kf.getRight().isShadow());
						}
					}
				}
			}
		}
		
		// LogUtils.log("sd", "Keycode: " + keycode);
		
		// Default stuff
		
		if (keycode == 256 && this.shouldCloseOnEsc()) {
	         this.onClose();
	         return true;
	      } else if (this.getFocused() != null && this.getFocused().keyPressed(keycode, p_96553_, p_96554_)) {
	         return true;
	      } else {
	         switch (keycode) {
	            case 258:
	               break;
	            case 259:
	            case 260:
	            case 261:
	            default:
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
	public boolean keyReleased(int keycode, int p_94716_, int p_94717_) {
		boolean[] keys = ClientEventHandler.keys;
		keys[keycode] = false;
		
		if(!keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
			guiModelPartKeyframesHandler.setCanAddMultipleKeyframes(false);
		}
		
		return super.keyReleased(keycode, p_94716_, p_94717_);
	}
	
	// Getters and setters

	public JgModel<?> getModel() {
		return model;
	}

	public void setModel(JgModel<?> model) {
		this.model = model;
		animationsHandler.setVisibleRange(Math.min(model.getAnimations().size(), 4));
		animationsHandler.update(model.getAnimations().size(), 0);
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
	
	public JgEditBox getCyclesEditBox() {
		return cycles;
	}
	
	public JgEditBox getTransformTickEditBox() {
		return transformTick;
	}
	
	public SimpleButton getBuildBtn() {
		return build;
	}
	
	public CycleButton<Boolean> getAdjustRemaining() {
		return adjustRemaining;
	}
	
	public CycleButton<String> getKeyframeType() {
		return keyframeType;
	}
	
	public static List<KeyframeItem> getSelectedKeyframes() {
		return selectedKeyframes;
	}
	
	public static List<KeyframeItem> getCopiedKeyframes() {
		return copiedKeyframes;
	}
	
	public GuiModelPartKeyframesHandler getGuiModelPartKeyframesHandler() {
		return guiModelPartKeyframesHandler;
	}

	public TickMarkerWidget getTickMarker() {
		return tickMarker;
	}

	public ScrollbarWidget getKeyframesScrollbar() {
		return keyframesScrollbar;
	}

	public ScrollbarWidget getPartsScrollbar() {
		return partsScrollbar;
	}

	public RangeHandler getVisibleTicksHandler() {
		return visibleTicksHandler;
	}

	public static int getPlayMode() {
		return playMode;
	}

	public static void setPlayMode(int playMode) {
		NewAnimationGui.playMode = playMode;
	}
	
	public boolean shouldAddOtherParts() {
		return shouldAddOtherParts;
	}

	public void setShouldAddOtherParts(boolean shouldAddOtherParts) {
		this.shouldAddOtherParts = shouldAddOtherParts;
	}

	public static class AnimationListItem extends AbstractJgListItem {

		Animation animation;
		
		public AnimationListItem(NewJgList parent, Animation animation) {
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
		public void handleMouse(double mx, double my, int btn, double x, double y, double w, double h) {
			
		}

		@Override
		public void onUnselect(double mx, double my, int btn, double x, double y, double w, double h) {
			
		}
		
		public Animation getAnimation() {
			return animation;
		}
		
	}
	
}
