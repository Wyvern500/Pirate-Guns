package com.jg.jgpg.client.gui.widget.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.animations.Keyframe;
import com.jg.jgpg.client.animations.KeyframeInfoWrapper;
import com.jg.jgpg.client.animations.KeyframePartData;
import com.jg.jgpg.client.animations.KeyframePartData.TransformData;
import com.jg.jgpg.client.animations.KeyframeTransformData;
import com.jg.jgpg.client.animations.PreviewTransformData;
import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.gui.widget.JgAbstractWidget;
import com.jg.jgpg.client.gui.widget.widgets.JgList.AbstractJgListItem;
import com.jg.jgpg.client.model.AbstractJgModel;
import com.jg.jgpg.client.model.JgModelPart;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class KeyframeManagerWidget extends JgAbstractWidget {
	
	List<JgAbstractWidget> widgets;
	List<PreviewTransformData> previewTransforms;
	Vector<TransformData> selectedTransformDatas;
	
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
		selectedTransformDatas = new Vector<>();
		
		keys = new boolean[400];
		
		outOfRangeMovingStart = 0.5f;
		offsetIncreaseSpeed = 0.1f;
		
		previewTransforms = new ArrayList<>();
	}

	@Override
	public void tick() {
		for(JgAbstractWidget widget : widgets) {
			widget.tick();
		}
		handleOutOfRange();
		//LogUtils.log("KfMW", "MouseX: " + Minecraft.getInstance().mouseHandler.xpos());
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
		LogUtils.log("KfMW", "f1: " + keyCode + " f2: " + f2 + " f3: " + f3);
		
		// Hotkeys
		
		// 17 Ctrl | 68 d | 127 Supr
		if (keys[GLFW.GLFW_KEY_LEFT_CONTROL] && keys[GLFW.GLFW_KEY_D]) { // Ctrl + d
			if (!selectedTransformDatas.isEmpty()) {
				if(parent.getModel().getAnimator().getCurrent() == null) return super.keyPressed(keyCode, f2, 
						f3);
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
		} else if (keys[GLFW.GLFW_KEY_DELETE]) { // Supr
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
				if(!parent.getClient().getEasingHandler().has(finalText)) {
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
	}
	
	private void fillTextFieldsData(TransformData data) {
		parent.getXEditBox().setValue(String.valueOf(data.getValue()[0]));
		parent.getYEditBox().setValue(String.valueOf(data.getValue()[1]));
		parent.getZEditBox().setValue(String.valueOf(data.getValue()[2]));
		parent.getEasingEditBox().setValue(data.getEasing());
	}
	
	private void clearTransformsData() {
		selectedTransformDatas.clear();
		parent.getXEditBox().setValue("");
		parent.getYEditBox().setValue("");
		parent.getZEditBox().setValue("");
		parent.getEasingEditBox().setValue("");
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

	public Vector<TransformData> getSelectedTransformDatas() {
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
									LogUtils.log("KfMW", "Clicking: " + posData.getParent().getPart().getName());
									selected = posData;
									
									canOutRangeCounter = true;
									outOfRangeCounterStart = System.currentTimeMillis();
 								}
							}
							TransformData rotData = partData.getRotData();
							if(!rotData.isEmpty()) {
								if (Utils.collides(kfx, y + (tileSize * 2), tileSize, tileSize, mx, my)) {
									selected = rotData;
									
									canOutRangeCounter = true;
									outOfRangeCounterStart = System.currentTimeMillis();
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
