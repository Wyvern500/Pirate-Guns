package com.jg.jgpg.client.gui.widget.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.animations.Keyframe;
import com.jg.jgpg.client.animations.KeyframeInfoWrapper;
import com.jg.jgpg.client.animations.KeyframePartData;
import com.jg.jgpg.client.animations.KeyframePartData.TransformData;
import com.jg.jgpg.client.animations.KeyframeTransformData;
import com.jg.jgpg.client.animations.PreviewTransformData;
import com.jg.jgpg.client.animations.Transform;
import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.gui.widget.JgAbstractWidget;
import com.jg.jgpg.client.gui.widget.widgets.JgList.AbstractJgListItem;
import com.jg.jgpg.client.handlers.ClientEventHandler;
import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class KeyframeManagerWidget extends JgAbstractWidget {
	
	List<JgAbstractWidget> widgets;
	List<PreviewTransformData> previewTransforms;
	List<TransformData> copiedTransformDatas;
	List<TransformData> selectedTransformDatas;
	
	ScrollbarWidget verticalScrollbar;
	ScrollbarWidget horizontalScrollbar;
	
	AnimationTickMarkerWidget tickManager;
	
	KeyframesList items;
	
	ClickType type;
	
	int offset;
	int startingKeyframeStuff;
	int keyframeMarkerIndex;
	int tileSize;
	int maxTicksVisible;
	boolean keys[];
	
	boolean ctrl;
	
	long clickStartTimeTracker;
	
	long outOfRangeCounterStart;
	boolean canOutRangeCounter;
	
	float outOfRangeMovingStart;
	float offsetIncreaseSpeed;
	float offsetIncreaseCounter;
	
	boolean keyPressed;
	
	boolean adjustRemaining;
	int beforeX;
	
	// This variable is for tracking the firstTransformTick event if the transform data changes
	int lastFirstTick;
	
	boolean transformTickTextFieldFocus;
	
	public KeyframeManagerWidget(int x, int y, int w, int h, AnimationGui parent) {
		super(x, y, w, h, parent);
		widgets = new ArrayList<>();
		
		tileSize = 10;
		startingKeyframeStuff = 100;
		maxTicksVisible = (w - startingKeyframeStuff) / tileSize;
		verticalScrollbar = new ScrollbarWidget(x + w, y, 10, height, 10, true, parent);
		horizontalScrollbar = new ScrollbarWidget(x, y - 10, w, 10, 10, false, parent);
		items = new KeyframesList(x, y, w, h, 3, parent);
		tickManager = new AnimationTickMarkerWidget(x + startingKeyframeStuff, y, 
				5, 10, w - startingKeyframeStuff, tileSize, parent);
		widgets.add(verticalScrollbar);
		widgets.add(horizontalScrollbar);
		widgets.add(items);
		widgets.add(tickManager);
		selectedTransformDatas = new ArrayList<>();
		copiedTransformDatas = new ArrayList<>();
		
		keys = new boolean[400];
		
		outOfRangeMovingStart = 0.5f;
		offsetIncreaseSpeed = 0.1f;
		
		previewTransforms = new ArrayList<>();
		
		lastFirstTick = -1;
	}

	@Override
	public void tick() {
		for(JgAbstractWidget widget : widgets) {
			widget.tick();
		}
		handleOutOfRange();
		// Checking if the focus of transformTickTextField changed so i can reset the lastFirstTick value
		if(parent.getTransformTickEditBox().isFocused() && !transformTickTextFieldFocus) {
			focusChanged(true);
			transformTickTextFieldFocus = true;
		} else if(transformTickTextFieldFocus && !parent.getTransformTickEditBox().isFocused()) {
			focusChanged(false);
			transformTickTextFieldFocus = false;
		}
		if(selectedTransformDatas.isEmpty()) {
			if(parent.getTransformTickEditBox().visible) {
				parent.getTransformTickEditBox().setVisible(false);
				beforeX = parent.getAdjustRemaining().getX();
				parent.getAdjustRemaining().setX(-1000);
			}
		} else {
			if(!parent.getTransformTickEditBox().visible) {
				parent.getTransformTickEditBox().setVisible(true);
				parent.getAdjustRemaining().setX(beforeX);
			}
		}
	}

	@Override
	protected void renderWidget(GuiGraphics gg, int mx, int my, float deltaTime) {
		if(visible && parent.getModel() != null) {
			if(parent.getModel().getAnimator().getCurrent() == null) return;
			if(items.items.isEmpty()) {
				for(JgModelPart part : parent.getModel().getParts().values()) {
					items.addItem(new JgItem(items, part));
				}
			}
			
			RenderHelper.rect(gg, getX(), getY(), width, height, Color.rgba(0, 0, 0, 255));
			
			for(JgAbstractWidget widget : widgets) {
				widget.render(gg, mx, my, deltaTime);
			}
			
			if(parent.getModel().getAnimator().getCurrent() != null) {
				int animDur = parent.getModel().getAnimator().getCurrent().getDuration();
				int maxTicks = ((width - startingKeyframeStuff) / tileSize);
				
				if(!items.getItems().isEmpty()) {
					int scaleOffset = 50;
					float scaleMultiplier = 2.5f;
					for(int i = offset; i < Math.min(maxTicks + offset, animDur); i++) {
						int ti = i - offset;
						gg.pose().pushPose();
						gg.pose().scale(0.8f, 0.8f, 0.8f);
						gg.drawString(Minecraft.getInstance().font, Component.translatable(i + ""), 
								(int) (scaleOffset + (ti * scaleMultiplier) + getX() + startingKeyframeStuff 
								+ (ti * tileSize)), getY() - 10, Color.rgba(255, 255, 255, 255));
						gg.pose().popPose();
					}
				}
				
				gg.drawString(Minecraft.getInstance().font, "Tick: " + 
						Math.floor(parent.getModel().getAnimator().getTick()), 
						100, 180, Color.rgba(255, 255, 255, 255));
			}
		}
	}
	
	// Mouse

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		// Set click type
		if (btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			clickStartTimeTracker = System.currentTimeMillis();
		}
		
		for(JgAbstractWidget widget : widgets) {
			widget.mouseClicked(mx, my, btn);
		}
		return super.mouseClicked(mx, my, btn);
	}

	@Override
	public boolean mouseReleased(double mx, double my, int btn) {
		
		if (btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			long time = (System.currentTimeMillis() - clickStartTimeTracker) / 1000;
			if (time <= ClickType.NORMAL.getMax()) {
				type = ClickType.NORMAL;
			} else if (time <= ClickType.PRESSED.getMax() || time >= ClickType.PRESSED.getMax()) {
				type = ClickType.PRESSED;
			}
		}
		
		for(JgAbstractWidget widget : widgets) {
			widget.mouseReleased(mx, my, btn);
		}
		return super.mouseReleased(mx, my, btn);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int btn, double f1, double f2) {
		for(JgAbstractWidget widget : widgets) {
			widget.mouseDragged(mx, my, btn, f1, f2);
		}
		// Update some childs stuff
		if (btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(parent.getModel().getAnimator().getCurrent() == null) return super.mouseDragged(mx, my, btn, 
					f1, f2);
			items.update(verticalScrollbar.getProgress());
			int visibleKeyframes = (width - startingKeyframeStuff) / tileSize;
			if (parent.getModel().getAnimator().getCurrent() != null) {
				this.offset = (int) MathUtils.lerp(0,
						(parent.getModel().getAnimator().getCurrent().getDuration() + 1) - visibleKeyframes,
						horizontalScrollbar.getProgress());
			}
			items.updateHorizontalStuff(offset);
			keyframeMarkerIndex = tickManager.getTick();
		}
		return super.mouseDragged(mx, my, btn, f1, f2);
	}

	@Override
	public void mouseMoved(double mx, double my) {
		for(JgAbstractWidget widget : widgets) {
			widget.mouseMoved(mx, my);
		}
		super.mouseMoved(mx, my);
	}

	@Override
	public boolean mouseScrolled(double f1, double f2, double f3) {
		for(JgAbstractWidget widget : widgets) {
			widget.mouseScrolled(f1, f2, f3);
		}
		return super.mouseScrolled(f1, f2, f3);
	}

	// Keyboard

	@Override
	public boolean keyPressed(int keyCode, int f2, int f3) {
		for(JgAbstractWidget widget : widgets) {
			widget.keyPressed(keyCode, f2, f3);
		}
		if(keyCode == GLFW.GLFW_KEY_LEFT_CONTROL) {
			ctrl = true;
		}
		keyPressed = true;
		keys[keyCode] = true;
		// LogUtils.log("KfMW", "f1: " + keyCode + " f2: " + f2 + " f3: " + f3);
		
		// Hotkeys
		
		LogUtils.log("KeyframeManagerWidget", "Tick: " + tickManager.getTick());
		
		// 17 Ctrl | 68 d | 259 Del
		if (keys[GLFW.GLFW_KEY_LEFT_CONTROL] && keys[GLFW.GLFW_KEY_D]) { // Ctrl + d
			LogUtils.log("KeyframeManagerWidget", "Ctrl + d: Tick: " + tickManager.getTick());
			if (!selectedTransformDatas.isEmpty()) {
				LogUtils.log("KeyframeManagerWidget", "Ctrl + d: Tick: " + tickManager.getTick());
				if(parent.getModel().getAnimator().getCurrent() == null) return super.keyPressed(keyCode, f2, f3);
				for (TransformData data : selectedTransformDatas) {
					Keyframe selected = data.getKf();
					Keyframe newKf = getKeyframeOnTick(selected.getTick() + 1);
					if (newKf != null) {
						if (data.isRot()) {
							newKf.getRotations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						} else {
							newKf.getTraslations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						}
					} else {
						newKf = new Keyframe(selected.getTick() + 1);
						if (data.isRot()) {
							newKf.getRotations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						} else {
							newKf.getTraslations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						}
						data.getAnimation().addKeyframe(newKf);
					}
					updateStuff();
				}
			}
		} else if(keys[GLFW.GLFW_KEY_LEFT_CONTROL] && keys[GLFW.GLFW_KEY_C]) {
			// Te quedaste haciendo que un copy paste funcionara
			copiedTransformDatas.clear();
			copiedTransformDatas.addAll(selectedTransformDatas);
		} else if(keys[GLFW.GLFW_KEY_LEFT_CONTROL] && keys[GLFW.GLFW_KEY_V]) {
			if(!copiedTransformDatas.isEmpty()) {
				// Check if it is empty
				int targetTick = tickManager.getTick();
				int anchorTick = copiedTransformDatas.get(0).getKf().getTick();
				for (TransformData data : copiedTransformDatas) {
					int offset = data.getKf().getTick() - anchorTick;
					Keyframe kf = getKeyframeOnTick(targetTick + offset);
					if (kf != null) {
						if (data.isRot()) {
							kf.getRotations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						} else {
							kf.getTraslations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						}
					} else {
						kf = new Keyframe(targetTick + offset);
						if (data.isRot()) {
							kf.getRotations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						} else {
							kf.getTraslations().put(data.getParent().getPart(),
									new KeyframeTransformData(data.getValue(), data.getEasing()));
						}
						data.getAnimation().addKeyframe(kf);
					}
					updateStuff();
				}
			}
		} else if (keys[259]) { // Delete key
			if (!selectedTransformDatas.isEmpty()) {
				for (TransformData data : selectedTransformDatas) {
					data.remove();
				}
				updateStuff();
			}
		}
		
		return super.keyPressed(keyCode, f2, f3);
	}

	@Override
	public boolean keyReleased(int keyCode, int f2, int f3) {
		for(JgAbstractWidget widget : widgets) {
			widget.keyPressed(keyCode, f2, f3);
		}
		if(keyCode == GLFW.GLFW_KEY_LEFT_CONTROL) {
			ctrl = false;
		}
		if(keyCode == GLFW.GLFW_KEY_ENTER && parent.getTransformTickEditBox().isFocused()) {
			//parent.getTransformTickEditBox().setFocused(false);
		}
		keyPressed = false;
		keys[keyCode] = false;
		return super.keyReleased(keyCode, f2, f3);
	}
	
	public void handleValueChange(int type, JgEditBox field, String text) {
		switch(type) {
		case 0:
			if(!selectedTransformDatas.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll(
				          "[^a-zA-Z0-9.]", "");
				if(finalText.length() == 0) {
					finalText = "0.0";
				}
				for(TransformData data : selectedTransformDatas) {
					if(data.isRot()) {
						float[] val = data.getKf().getRotations().get(data.getParent().getPart()).getVal();
						data.setValue(new float[] { Float.valueOf(finalText), val[1], val[2] });
					} else {
						float[] val = data.getKf().getTraslations().get(data.getParent().getPart()).getVal();
						data.setValue(new float[] { Float.valueOf(finalText), val[1], val[2] });
					}
				}
			}
			updateStuff();
			break;
		case 1:
			if(!selectedTransformDatas.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll(
				          "[^a-zA-Z0-9.]", "");
				if(finalText.length() == 0) {
					finalText = "0.0";
				}
				for(TransformData data : selectedTransformDatas) {
					if(data.isRot()) {
						float[] val = data.getKf().getRotations().get(data.getParent().getPart()).getVal();
						data.setValue(new float[] { val[0], Float.valueOf(finalText), val[2] });
					} else {
						float[] val = data.getKf().getTraslations().get(data.getParent().getPart()).getVal();
						data.setValue(new float[] { val[0], Float.valueOf(finalText), val[2] });
					}
				}
			}
			updateStuff();
			break;
		case 2:
			if(!selectedTransformDatas.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll(
				          "[^a-zA-Z0-9.]", "");
				if(finalText.length() == 0) {
					finalText = "0.0";
				}
				for(TransformData data : selectedTransformDatas) {
					if(data.isRot()) {
						float[] val = data.getKf().getRotations().get(data.getParent().getPart()).getVal();
						data.setValue(new float[] { val[0], val[1], Float.valueOf(finalText) });
					} else {
						float[] val = data.getKf().getTraslations().get(data.getParent().getPart()).getVal();
						data.setValue(new float[] { val[0], val[1], Float.valueOf(finalText) });
					}
				}
			}
			updateStuff();
			break;
		case 3:
			if(!selectedTransformDatas.isEmpty()) {
				String finalText = "";
				finalText = field.getValue().replaceAll(
				          "[^a-zA-Z0-9.]", "");
				if(finalText.length() == 0) {
					finalText = "empty";
				}
				if(!EasingHandler.INSTANCE.has(finalText)) {
					finalText = "empty";
				}
				for(TransformData data : selectedTransformDatas) {
					if(data.isRot()) {
						data.setEasing(finalText);
					} else {
						data.setEasing(finalText);
					}
				}
			}
			updateStuff();
			break;
		}
	}
	
	public void onEditBoxEnter(JgEditBox box) {
		handleChangeTransformTick(box);
	}

	private void focusChanged(boolean isFocused) {
		LogUtils.log("KfMW", "isFocused changed to " + isFocused);
		// If the user finished using the tick manager then reset lastFirstTick
		if(!isFocused) {
			lastFirstTick = -1;
		}
	}
	
	private void handleChangeTransformTick(JgEditBox box) {
		if(!selectedTransformDatas.isEmpty()) {
			String finalText = "";
			if(!box.getValue().contains("/")) {
				finalText = box.getValue().replaceAll(
				          "[^0-9]", "");
				if(finalText.length() == 0) {
					finalText = "0";
				}
				
				if(!adjustRemaining) {
	
					List<TransformData> newSelectedTransforms = new ArrayList<>();
					
					int tick = Math.max(0, Integer.parseInt(finalText));
					
					// Getting the tick of the first transform data so i will calculate their ticks based on first
					// tick
					int firstTick = selectedTransformDatas.get(0).getKf().getTick();
					if(lastFirstTick == -1) {
						lastFirstTick = firstTick;
					}
					
					// If the targetTick and the lastTick are the same then return
					if(tick == lastFirstTick) {
						return;
					}
					
					boolean canClean = true;
					
					int newLastTick = -1;
					
					// I do this because i dont want a transform data to have a negative tick
					Collections.sort(selectedTransformDatas, (i1, i2) -> Integer.compare(i1.getKf().getTick(), 
							i2.getKf().getTick()));
					for(TransformData data : selectedTransformDatas) {
						int dif = data.getKf().getTick() - lastFirstTick;
						int newTick = tick + dif;
						
						// Getting the firstTick so even if the transformData is destroyed i still having access to the
						// "firstTick"
						if(firstTick == data.getKf().getTick()) {
							newLastTick = newTick;
						}
						
						if(newTick < 0) {
							LogUtils.log("KeyframeManagerWidget", "Tick cannot be less than 0. lowest tick: " 
									+ data.getKf().getTick());
							canClean = false;
							newLastTick = 0;
							break;
						}
						
						Keyframe kf = getKeyframeOnTick(newTick);
						
						String easing = data.getEasing();
						
						if(easing == null) {
							easing = "empty";
						} else if(easing.equals("null")) {
							easing = "empty";
						}
						
						LogUtils.log("KfMW", data.toString());
						
						if(kf != null) { // The keyframe exists
							if(data.isRot()) {
								kf.getRotations().put(data.getParent().getPart(), 
										new KeyframeTransformData(data.getValue(), easing));
							} else {
								kf.getTraslations().put(data.getParent().getPart(), 
										new KeyframeTransformData(data.getValue(), easing));
							}
						} else {
							kf = new Keyframe(newTick);
							if(data.isRot()) {
								kf.getRotations().put(data.getParent().getPart(), 
										new KeyframeTransformData(data.getValue(), easing));
							} else {
								kf.getTraslations().put(data.getParent().getPart(), 
										new KeyframeTransformData(data.getValue(), easing));
							}
							data.getAnimation().addKeyframe(kf);
						}
						
						newSelectedTransforms.add(new TransformData(data.getParent(), data.getAnimation(), kf, 
								data.isRot(), data.getValue(), easing));
						
						data.remove();
					}
					
					if(canClean) {
					
						// Cleaning selectedTransforms
						selectedTransformDatas.clear();
						
						// Adding the newSelectedTransforms so they still selected after changing their ticks
						for(TransformData data : newSelectedTransforms) {
							selectedTransformDatas.add(data);
						}
				
						updateStuff();
						
						// Setting the lastFirstTick
						lastFirstTick = newLastTick;
					}
					
					LogUtils.log("KeyframeManagerWidget", " Keyframes: " + parent.getModel().getAnimator()
							.getCurrent().getKeyframes().toString());
				} else {
					parent.getModel().getAnimator().getCurrent().sort();
					
					int tick = Math.max(0, Integer.parseInt(finalText)); 
					
					TransformData first = selectedTransformDatas.get(0);
					
					int dif = tick - first.getKf().getTick();
					
					List<Keyframe> kfsToDelete = new ArrayList<>();
					
					int anchor = -1;
					
					List<Keyframe> keyframes = parent.getModel().getAnimator().getCurrent().getKeyframes();
					
					for(int i = 0; i < keyframes.size(); i++) {
						Keyframe kf = keyframes.get(i);
						// This anchor is important because we want to define the index of the source kf, so we can
						// start working
						if(kf.getTick() >= first.getKf().getTick() && kf.getTick() != first.getKf().getTick()) {
							anchor = i; 
							break;
						}
					}
					
					if(anchor != -1) {
						
						if(dif < 0 && anchor - 2 > 0) {
							
							Keyframe anchorKf = keyframes.get(anchor - 1);
							int newTick = anchorKf.getTick() + dif;
							for(int i = anchor - 2; i > -1; i--) {
								Keyframe kf = keyframes.get(i);
								LogUtils.log("KeyframeManagerWidget", "Tick: " + kf.getTick() + " newTick: " + 
										newTick + " anchor: " + anchor + " anchorTick: " + anchorKf.getTick() 
										+ " i: " + i + " startingIndex: " + (anchor - 2));
								if(newTick < kf.getTick()) {
									LogUtils.log("KeyframeManagerWidget", "Returning on Tick: " + kf.getTick() 
										+ " newTick: " + newTick + " anchor: " + anchor + " anchorTick: " 
										+ anchorKf.getTick() + " i: " + i + " startingIndex: " 
										+ (anchor - 2));
									return;
								}
							}
						}
						
						for (int i = anchor; i < keyframes.size(); i++) {
							Keyframe kf = keyframes.get(i);
							int newTick = kf.getTick() + dif;
							if (newTick < 0) { // The first keyframe tick is lower than 0
								LogUtils.log("KfMW", "First tick is lower than 0, changes werent done");
								return;
							}
							
							if(dif < 0) {
							
								// Deleting repeated keyframes becuase of overlaping
	
								for (Keyframe kfToCompare : keyframes) {
									// Finding the overlapped keyframe
									if (kfToCompare.getTick() == newTick) {
										// Setting the old missing keyfames data to the new keyframe
										for (Entry<JgModelPart, KeyframeTransformData> entry : kfToCompare.getTraslations()
												.entrySet()) {
											if (!kf.getTraslations().containsKey(entry.getKey())) {
												kf.getTraslations().put(entry.getKey(), entry.getValue());
											}
										}
										for (Entry<JgModelPart, KeyframeTransformData> entry : kfToCompare.getRotations()
												.entrySet()) {
											if (!kf.getRotations().containsKey(entry.getKey())) {
												kf.getRotations().put(entry.getKey(), entry.getValue());
											}
										}
										// We need to delete the replaced keyframe
										kfsToDelete.add(kfToCompare);
										break;
									}
								}
							
							}

							kf.setTick(newTick);

						}
					}
					
					/*if(dif > 0) {
						// Adjusting to forward
						
					} else {
						// Adjusting to backward
						for(Keyframe kf : keyframes) {
							int newTick = kf.getTick() + dif;
							if(newTick < 0) { // The first keyframe tick is lower than 0
								LogUtils.log("KfMW", "First tick is lower than 0, changes werent done");
								return;
							}
							kf.setTick(newTick);
						}
					}*/
					
					// Deleting keyframes
					
					/*for(int i = 0; i < keyframes.size(); i++) {
						Keyframe kf = keyframes.get(i);
						for(Keyframe kfToCompare : parent.getModel().getAnimator().getCurrent().getKeyframes()) {
							if(kfToCompare.getTick() == kf.getTick() + dif) {
								kfsToDelete.add(kfToCompare);
								LogUtils.log("KeyframeManagerWidget", "Adding kf " + kfToCompare.getTick());
								break;
							}
						}
					}*/
					
					Keyframe firstKf = first.getKf();
					
					if(dif < 0) {
					
						for(Keyframe kfToCompare : keyframes) {
							// Finding the overlapped keyframe
							if(kfToCompare.getTick() == tick) {
								// Setting the old missing keyfames data to the new keyframe
								for(Entry<JgModelPart, KeyframeTransformData> entry : kfToCompare
										.getTraslations().entrySet()) {
									if(!firstKf.getTraslations().containsKey(entry.getKey())) {
										firstKf.getTraslations().put(entry.getKey(), entry.getValue());
									}
								}
								for(Entry<JgModelPart, KeyframeTransformData> entry : kfToCompare
										.getRotations().entrySet()) {
									if(!firstKf.getRotations().containsKey(entry.getKey())) {
										firstKf.getRotations().put(entry.getKey(), entry.getValue());
									}
								}
								// We need to delete the replaced keyframe
								kfsToDelete.add(kfToCompare);
								break;
							}
						}
					
					}
					
					// Deleting keyframes
					
					for(Keyframe kf : kfsToDelete) {
						LogUtils.log("KeyframeManagerWidget", "Kf Tick: " + kf.getTick());
						parent.getModel().getAnimator().getCurrent().removeKeyframe(kf);
					}
					
					firstKf.setTick(tick);
					
					LogUtils.log("KeyframeManagerWidget", "Anchor: " + anchor + " Keyframes: " + keyframes
							.toString());
					
					updateStuff();
				}
			} else {
				String data = box.getValue();
				String[] spData = data.split(" ");
				LogUtils.log("KfMW", "Data: " + Arrays.toString(spData));
				if(spData.length == 2) {
					if(spData[0].equals("/scale")) {
						String val = spData[1].replaceAll(
						          "[^0-9.]", "");
						float scale = Float.valueOf(val);
						if(scale != 0) {
							for(Keyframe kf : parent.getModel().getAnimator().getCurrent().getKeyframes()) {
								int kfTick = kf.getTick();
								if(kfTick != 0) {
									kf.setTick((int) (kfTick * scale));
								}
							}
						}
						
						updateStuff();
					}
				}
			}
			
			/*for(Keyframe kf : parent.getModel().getAnimator().getCurrent().getKeyframes()) {
				LogUtils.log("KeyframeManagerWidget", "KfTick: " + kf.getTick());
			}*/
		}
	}
	
	public void handleOutOfRange() {
		if((System.currentTimeMillis() - outOfRangeCounterStart) / 1000 > outOfRangeMovingStart && 
				canOutRangeCounter) {
			// Init previewTransforms
			if(previewTransforms.isEmpty() && !selectedTransformDatas.isEmpty()) {
				for(TransformData data : selectedTransformDatas) {
					Minecraft mc = Minecraft.getInstance();
					// En vez de usar mouseX crudo, tuve que tratarlo de esta forma por que si no daba
					// un valor raro que no me servia, esto lo saque de GameRenderer en el metodo render
					int mouseX = (int)(mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() 
							/ (double)mc.getWindow().getScreenWidth());
					int tickIndex = (int) Math.floor((((mouseX - (getX() + startingKeyframeStuff)) / (tileSize))));
					previewTransforms.add(new PreviewTransformData(data.getAnimation(), data.getParent()
							.getPart(), data.getKf().getTick(), (tickIndex + offset) - data.getKf()
							.getTick(), data.isRot(), data.getValue(), data.getEasing()));
				}
			}
			
			// Do normal stuff
			if (canOutRangeCounter && !previewTransforms.isEmpty()) {
				Minecraft mc = Minecraft.getInstance();
				// En vez de usar mouseX crudo, tuve que tratarlo de esta forma por que si no daba
				// un valor raro que no me servia, esto lo saque de GameRenderer en el metodo render
				int mouseX = (int)(mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() 
						/ (double)mc.getWindow().getScreenWidth());
				int tickIndex = (int) Math.floor((((mouseX - (getX() + startingKeyframeStuff)) / (tileSize))));
				// Preparing previewTransforms and removing transform datas from keyframes
				if (tickIndex >= maxTicksVisible - 1) {
					if (offsetIncreaseCounter < 1) {
						offsetIncreaseCounter += offsetIncreaseSpeed;
					} else {
						addOffset(1);
						offsetIncreaseCounter = 0;
					}
				} else if (tickIndex <= 0) {
					if (offset != 0) {
						if (offsetIncreaseCounter < 1) {
							offsetIncreaseCounter += offsetIncreaseSpeed;
						} else {
							addOffset(-1);
							offsetIncreaseCounter = 0;
						}
					}
				}
				int newKfTick = tickIndex + offset;
				if (newKfTick < 0) {
					newKfTick = 0;
				}
				// List<JgModelPart> parts = new
				// ArrayList<>(parent.getModel().getParts().values());
				/*LogUtils.log("keyframeManagerWidget", "tickIndex: " + tickIndex + " offset: " + 
						offset + " newTickIndex: " + (tickIndex + offset));*/
				for (PreviewTransformData data : previewTransforms) {
					data.addOffset(newKfTick);
				}
				if (!selectedTransformDatas.isEmpty()) {
					for (TransformData data : selectedTransformDatas) {
						data.remove();
					}
					clearTransformsData();
				}
			}
		}
	}
	
	public void createTransformsFromPreviewTransforms() {
		for(PreviewTransformData data : previewTransforms) {
			Keyframe kf = getKeyframeOnTick(data.getTick());
			LogUtils.log("KeyframeManagerWidget", "tick: " + data.getTick() + " part: " + data.getPart()
				.toString());
			if(kf != null) {
				if(data.isRot()) {
					kf.getRotations().put(data.getPart(), new KeyframeTransformData(data.getVal(), 
							data.getEasing()));
				} else {
					kf.getTraslations().put(data.getPart(), new KeyframeTransformData(data.getVal(), 
							data.getEasing()));
				}
				LogUtils.log("KeyframeManagerWidget", "Keyframe updated on " + kf.getTick());
			} else {
				kf = new Keyframe(data.getTick());
				if(data.isRot()) {
					kf.getRotations().put(data.getPart(), new KeyframeTransformData(data.getVal(), 
							data.getEasing()));
				} else {
					kf.getTraslations().put(data.getPart(), new KeyframeTransformData(data.getVal(), 
							data.getEasing()));
				}
				data.getAnim().addKeyframe(kf);
				LogUtils.log("KeyframeManagerWidget", "Keyframe added on " + kf.getTick());
			}
		}
		previewTransforms.clear();
		updateStuff();
	}
	
	public void updateStuff() {
		parent.getClient().getAnimationDataHandler().setup();
		parent.getModel().getAnimator().update();
	}
	
	public void setupForModel(AbstractJgModel model) {
		items.clear();
		for(JgModelPart part : model.getParts().values()) {
			items.addItem(new JgItem(items, part));
		}
		setupForAnimation(model.getAnimator().getCurrent());
	}
	
	public void setupForAnimation(Animation animation) {
		if(animation != null) {
			for(AbstractJgListItem item : items.getItems()) {
				item.setAnimation(animation);
			}
		} else {
			items.clear();
		}
	}
	
	public void addOffset(int offset) {
		this.offset += offset;
		int visibleKeyframes = (width - startingKeyframeStuff) / tileSize;
		float nuevoProgreso = (((this.offset + 0.0f) / (parent.getModel().getAnimator().getCurrent()
				.getDuration() - visibleKeyframes)) * (horizontalScrollbar.getWidth() - 
						horizontalScrollbar.sw)) + horizontalScrollbar.getX();
		if (parent.getModel().getAnimator().getCurrent() != null) {
			horizontalScrollbar.setScrollbarX((int) Math.ceil(nuevoProgreso));
		}
		items.updateHorizontalStuff(this.offset);
		keyframeMarkerIndex = tickManager.getTick();
	}
	
	public void setOffset(int offset) {
		int visibleKeyframes = (width - startingKeyframeStuff) / tileSize;
		float nuevoProgreso = (((offset + 0.0f) / (parent.getModel().getAnimator().getCurrent()
				.getDuration() - visibleKeyframes)) * (horizontalScrollbar.getWidth() - 
						horizontalScrollbar.sw)) + horizontalScrollbar.getX();
		this.offset = offset;
		if (parent.getModel().getAnimator().getCurrent() != null) {
			horizontalScrollbar.setScrollbarX((int) Math.ceil(nuevoProgreso));
		}
		items.updateHorizontalStuff(offset);
		keyframeMarkerIndex = tickManager.getTick();
	}
	
	public void addTransformData(TransformData data) {
		selectedTransformDatas.add(data);
		fillTextFieldsData(data);
	}
	
	public void removeTransformData(TransformData data) {
		selectedTransformDatas.remove(data);
		parent.getXEditBox().setValue("0.0");
		parent.getYEditBox().setValue("0.0");
		parent.getZEditBox().setValue("0.0");
		parent.getEasingEditBox().setValue("");
		parent.getTransformTickEditBox().setValue("0");
	}
	
	private void fillTextFieldsData(TransformData data) {
		parent.getXEditBox().setValue(String.valueOf(data.getValue()[0]));
		parent.getYEditBox().setValue(String.valueOf(data.getValue()[1]));
		parent.getZEditBox().setValue(String.valueOf(data.getValue()[2]));
		parent.getEasingEditBox().setValue(data.getEasing());
		parent.getTransformTickEditBox().setValue(String.valueOf(data.getKf().getTick()));
	}
	
	private void clearTransformsData() {
		selectedTransformDatas.clear();
		parent.getXEditBox().setValue("");
		parent.getYEditBox().setValue("");
		parent.getZEditBox().setValue("");
		parent.getEasingEditBox().setValue("");
		parent.getTransformTickEditBox().setValue("");
	}
	
	public void setAdjustRemaining(boolean adjustRemaining) {
		this.adjustRemaining = adjustRemaining;
	}
	
	public boolean shouldAdjustRemaining() {
		return adjustRemaining;
	}
	
	public int getOffset() {
		return offset;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getMaxTicksVisible() {
		return maxTicksVisible;
	}

	public AnimationGui getParent() {
		return parent;
	}

	public List<JgAbstractWidget> getChilds() {
		return widgets;
	}

	public JgList getItems() {
		return items;
	}

	public List<TransformData> getSelectedTransformDatas() {
		return selectedTransformDatas;
	}

	public AnimationTickMarkerWidget getTickManager() {
		return tickManager;
	}

	public int getKeyframeMarkerIndex() {
		return keyframeMarkerIndex;
	}

	public boolean[] getKeys() {
		return keys;
	}
	
	public List<Keyframe> getKeyframes() {
		return parent.getModel().getAnimator().getCurrent().getKeyframes();
	}
	
	public Keyframe getKeyframeOnTick(int tick) {
		for(Keyframe kf : getKeyframes()) {
			if(kf.getTick() == tick) {
				return kf;
			}
		}
		return null;
	}
	
	public static enum ClickType {
		
		NORMAL(0.3f), PRESSED(0.5f);
		
		float max;
		
		private ClickType(float max) {
			this.max = max;
		}
		
		public float getMax() {
			return max;
		}
		
	}
	
	public class JgItem extends AbstractJgListItem {

		JgModelPart part;
		TreeMap<Keyframe, KeyframeInfoWrapper> keyframes; 
		TransformData selected;
		List<KeyframePartData> data;
		int offset;
		
		public JgItem(JgList parent, JgModelPart part) {
			super(parent);
			this.part = part;
			this.keyframes = new TreeMap<Keyframe, KeyframeInfoWrapper>();
			this.data = KeyframeManagerWidget.this.parent.getClient()
					.getAnimationDataHandler().getKeyframesPartData();
		}
		
		@Override
		public void render(GuiGraphics gg, int x, int y, int w, int h, int i) {
			if(animation == null) return;
			Minecraft mc = Minecraft.getInstance();
			RenderHelper.rect(gg, x, y, 100, h - 1, Color.rgba(100, 100, 100, 255));
			RenderHelper.rect(gg, x, y + h - 1, 100, 1, Color.rgba(0, 0, 0, 255));
			gg.drawString(mc.font, Component.translatable(part.getName()), x, y, 
					Color.rgba(255, 255, 255, 255));
			gg.drawString(mc.font, Component.translatable("Tr"), x + 10, y + 10, 
					Color.rgba(255, 255, 255, 255));
			gg.drawString(mc.font, Component.translatable("Rt"), x + 10, y + 20, 
					Color.rgba(255, 255, 255, 255));
			RenderHelper.rect(gg, x + startingKeyframeStuff - tileSize, y + 10, tileSize, tileSize, 
					Color.rgba(255, 255, 255, 255));
			RenderHelper.rect(gg, x + startingKeyframeStuff - tileSize, y + 20, tileSize, tileSize, 
					Color.rgba(0, 0, 0, 255));
			
			Iterator<KeyframePartData> iterator = data.iterator();
			
			while(iterator.hasNext()) {
				KeyframePartData partData = iterator.next();
				
				if(partData.getPart() == part) {
					int kfx = (int) Math.floor((x + startingKeyframeStuff) + MathUtils.lerp(0, 
							(animation.getDuration()) * tileSize, (partData.getKeyframe().getTick() - 
									offset + 0.0f) / animation.getDuration()));
					if(kfx >= x + startingKeyframeStuff && kfx <= x + w - tileSize) {
						if(!partData.getPosData().isEmpty()) {
							int color = Color.rgba(0, 90, 0, 255);
							if(selectedTransformDatas.contains(partData.getPosData())) {
								color = Color.rgba(255, 0, 0, 255);
							}
							RenderHelper.rect(gg, kfx, y + tileSize, tileSize, tileSize, color);
						}
						if(!partData.getRotData().isEmpty()) {
							int color = Color.rgba(90, 90, 90, 255);
							if(selectedTransformDatas.contains(partData.getRotData())) {
								color = Color.rgba(255, 0, 0, 255);
							}
							RenderHelper.rect(gg, kfx, y + (tileSize * 2), tileSize, tileSize, color);
						}
					}
				}
			}
			
			Iterator<PreviewTransformData> previewDataIterator = previewTransforms.iterator();
			
			while(previewDataIterator.hasNext()) {
				PreviewTransformData transformData = previewDataIterator.next();
				int kfx = (int) Math
						.floor((x + startingKeyframeStuff) + MathUtils.lerp(0, (animation.getDuration()) * 
								tileSize, (transformData.getTick() - offset + 0.0f) / animation
								.getDuration()));
				if (kfx >= x + startingKeyframeStuff && kfx <= x + w - tileSize) {
					if(transformData.getPart() == part) {
						RenderHelper.rect(gg, kfx, y + (transformData.isRot() ? tileSize * 2 : tileSize), 
								tileSize, tileSize, Color.rgba(255, 0, 0, 255));
					}
				}
			}
		}
		
		@Override
		public void updateHorinzontalStuff(int offset) {
			this.offset = offset;
		}
		
		@Override
		public void handleMouse(double mx, double my, int btn, double x, double y, double w, double h) {
			if(btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				Iterator<KeyframePartData> iterator = data.iterator();
				
				while(iterator.hasNext()) {
					KeyframePartData partData = iterator.next();
					
					if(partData.getPart() == part) {
						int kfx = (int) Math.floor((x + startingKeyframeStuff) + MathUtils.lerp(0, 
								(animation.getDuration()) * tileSize, (partData.getKeyframe().getTick() - 
										offset + 0.0f) 
								/ animation.getDuration()));
						if(kfx >= x + startingKeyframeStuff && kfx <= x + w - tileSize) {
							TransformData posData = partData.getPosData();
							if(!posData.isEmpty()) {
								if (Utils.collides(kfx, y + tileSize, tileSize, tileSize, mx, my)) {
									if(!ClientEventHandler.keys[GLFW.GLFW_KEY_X]) {
										LogUtils.log("KfMW", "Clicking: " + posData.getParent().getPart().getName());
										selected = posData;
										
										canOutRangeCounter = true;
										outOfRangeCounterStart = System.currentTimeMillis();
									} else {
										// This is important because without this it would be choosing and unchoosing
										selected = null;
										// End
										selectedTransformDatas.clear();
										addTransformData(posData);
										for(KeyframePartData dta : data) {
											if(dta.getKeyframe().getTick() == posData.getKf().getTick()
													&& dta.getPosData() != posData) {
												addTransformData(dta.getPosData());
												addTransformData(dta.getRotData());
											}
										}
									}
 								}
							}
							TransformData rotData = partData.getRotData();
							if(!rotData.isEmpty()) {
								if (Utils.collides(kfx, y + (tileSize * 2), tileSize, tileSize, mx, my)) {
									if(!ClientEventHandler.keys[GLFW.GLFW_KEY_X]) {
										selected = rotData;
										
										canOutRangeCounter = true;
										outOfRangeCounterStart = System.currentTimeMillis();
									} else {
										// This is important because without this it would be choosing and unchoosing
										selected = null;
										// End
										selectedTransformDatas.clear();
										addTransformData(rotData);
										for(KeyframePartData dta : data) {
											if(dta.getKeyframe().getTick() == rotData.getKf().getTick()
													&& dta.getPosData() != rotData) {
												addTransformData(dta.getPosData());
												addTransformData(dta.getRotData());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		@Override
		public boolean mouseReleased(double mx, double my, int btn, double x, double y, double w, double h) {
			if(btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				// Add Pos
				if (Utils.collides(x + startingKeyframeStuff - tileSize, y + 20, tileSize, 
						tileSize, mx, my)) {
					//addPosToKeyframe(keyframeMarkerIndex, part, part.getTransform().pos);
					addPosToKeyframe(KeyframeManagerWidget.this.parent.getModel().getAnimator().getTick(), 
							part, part.getTransform().pos);
					LogUtils.log("KfMW", "1st");
				}
				// Add Rot
				if (Utils.collides(x + startingKeyframeStuff - tileSize, y + 30, tileSize, 
						tileSize, mx, my)) {
					//addRotToKeyframe(keyframeMarkerIndex, part, part.getTransform().rot);
					LogUtils.log("KfMW", "2nd");
					addRotToKeyframe(KeyframeManagerWidget.this.parent.getModel().getAnimator().getTick(), 
							part, part.getTransform().rot);
				}
				if(type == ClickType.NORMAL) {
					
					for(int i = 0; i < data.size(); i++) {
						
						KeyframePartData partData = data.get(i);
						
						if(partData.getPart() == part) {
							int kfx = (int) Math.floor((x + startingKeyframeStuff) + MathUtils.lerp(0, 
									(animation.getDuration()) * tileSize, (partData.getKeyframe().getTick() - 
											offset + 0.0f) 
									/ animation.getDuration()));
							//LogUtils.log("KFMW", "kfx: " + kfx + " x: " + getX() + " startingKfStuff: " + 
							//		startingKeyframeStuff + " ");
							if(kfx >= x + startingKeyframeStuff && kfx <= x + width - tileSize) {
								TransformData posData = partData.getPosData();
								if(!posData.isEmpty()) {
									// Le sumo 10 por que por que agarra la Y del JgItem y es como un offset
									if (Utils.collides(kfx, y + (tileSize * 2), tileSize, tileSize, mx, my)) {
										if(selected == posData) {
											if(!selectedTransformDatas.contains(posData)) {
												if(ctrl) {
													addTransformData(posData);
													// Init the press counter
													/*if(e.getAction() == 1) { // Press
														canOutRangeCounter = true;
														outOfRangeCounterStart = System.currentTimeMillis();
													}*/
												} else {
													clearTransformsData();
													addTransformData(posData);
													// Init the press counter
													/*if(e.getAction() == 1) { // Press
														canOutRangeCounter = true;
														outOfRangeCounterStart = System.currentTimeMillis();
													}*/
												}
											} else {
												if(ctrl) {
													canOutRangeCounter = false;
													removeTransformData(posData);
												} else {
													canOutRangeCounter = false;
													clearTransformsData();
												}
											}
										}
	 								}
								}
								TransformData rotData = partData.getRotData();
								if(!rotData.isEmpty()) {
									// Le sumo 20 por que por que agarra la Y del JgItem y es como un offset
									LogUtils.log("KFMW", "kfx: " + kfx + " x: " + getX() + " startingKfStuff: " + 
											startingKeyframeStuff + " tileSize: " + tileSize + " x: " + x + 
											" y: " + y + " mx: " + mx + " my: " + my);
									if (Utils.collides(kfx, y+ (tileSize * 3), tileSize, tileSize, 
											mx, my)) {
										if(selected == rotData) {
											if(!selectedTransformDatas.contains(rotData)) {
												if(ctrl) {
													addTransformData(rotData);
													// Init the press counter
													/*if(e.getAction() == 1) { // Press
														canOutRangeCounter = true;
														outOfRangeCounterStart = System.currentTimeMillis();
													}*/
												} else {
													clearTransformsData();
													addTransformData(rotData);
													// Init the press counter
													/*if(btn == GLFW) { // Press
														canOutRangeCounter = true;
														outOfRangeCounterStart = System.currentTimeMillis();
													}*/
												}
											} else {
												if(ctrl) {
													canOutRangeCounter = false;
													removeTransformData(rotData);
												} else {
													canOutRangeCounter = false;
													clearTransformsData();
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
				// On Release click
				boolean startedMoving = ((System.currentTimeMillis() - outOfRangeCounterStart)
						/ 1000 > outOfRangeMovingStart);
				if (offsetIncreaseCounter < 1 && !startedMoving) {
					canOutRangeCounter = false;
					previewTransforms.clear();
				} else if (startedMoving && !previewTransforms.isEmpty()) {
					createTransformsFromPreviewTransforms();
					canOutRangeCounter = false;
				} else {
					canOutRangeCounter = false;
				}
			}
			return false;
		}
		
		@Override
		public void onUnselect(double mx, double my, int btn, double x, double y, double w, double h) {
			
		}
		
		@Override
		public void setAnimation(Animation animation) {
			super.setAnimation(animation);
			if(animation != null) {
				for(Keyframe kf : animation.getKeyframes()) {
					for(Entry<JgModelPart, KeyframeTransformData> e : kf.getTraslations().entrySet()) {
						if(e.getKey() == part) {
							if(!keyframes.containsKey(kf)) {
								keyframes.put(kf, new KeyframeInfoWrapper(kf, kf.getTick()));
							}
							keyframes.get(kf).setPos(new float[] { 0, 0 });
							keyframes.get(kf).getPos()[0] = e.getValue().getVal()[0];
							keyframes.get(kf).getPos()[1] = e.getValue().getVal()[1];
						}
	 				}
					
					for(Entry<JgModelPart, KeyframeTransformData> e : kf.getRotations().entrySet()) {
						if(e.getKey() == part) {
							if(!keyframes.containsKey(kf)) {
								keyframes.put(kf, new KeyframeInfoWrapper(kf, kf.getTick()));
							}
							keyframes.get(kf).setRot(new float[] { 0, 0 });
							keyframes.get(kf).getRot()[0] = e.getValue().getVal()[0];
							keyframes.get(kf).getRot()[1] = e.getValue().getVal()[1];
						}
	 				}
				}
			}
		}
		
		public void addPosToKeyframe(float tick, JgModelPart part, float[] pos) {
			Keyframe kfToAdd = null;
			for(Keyframe kf : animation.getKeyframes()) {
				if(kf.getTick() == tick) {
					kf.traslate(part, pos[0], pos[1], pos[2]);
					break;
				}
				if(!(tick > kf.getTick())) {
					kfToAdd = new Keyframe((int) tick);
					kfToAdd.traslate(part, pos[0], pos[1], pos[2]);
					break;
				}
			}
			if(kfToAdd != null) {
				animation.addKeyframe(kfToAdd);
			}
			updateStuff();
			//updateForAnimation(animation);
			LogUtils.log("KeyframeManagerWidget", "AddPos");
		}
		
		public void addRotToKeyframe(float tick, JgModelPart part, float[] rot) {
			Keyframe kfToAdd = null;
			for(Keyframe kf : animation.getKeyframes()) {
				if(kf.getTick() == tick) {
					kf.rotate(part, rot[0], rot[1], rot[2]);
					break;
				}
				if(!(tick > kf.getTick())) {
					kfToAdd = new Keyframe((int) tick);
					kfToAdd.rotate(part, rot[0], rot[1], rot[2]);
					break;
				}
			}
			if(kfToAdd != null) {
				animation.addKeyframe(kfToAdd);
			}
			updateStuff();
			//updateForAnimation(animation);
			LogUtils.log("KeyframeManagerWidget", "AddRot");
		}
		
	}
	
}
