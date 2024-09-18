package com.jg.jgpg.client.animation.gui.widget;

import java.util.List;
import java.util.Map;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Keyframe;
import com.jg.jgpg.client.animation.KeyframeItem;
import com.jg.jgpg.client.animation.ModelPartKeyframesHandler;
import com.jg.jgpg.client.animation.RangeHandler;
import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import com.jg.jgpg.client.animation.gui.widget.GuiModelPartKeyframesHandler.ClickType;
import com.jg.jgpg.client.handlers.ClientEventHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ModelPartGuiRenderer {

	GuiModelPartKeyframesHandler parent;
	JgModel model;
	Vector2i pos;
	Vector2i size;
	JgModelPart part;
	
	public ModelPartGuiRenderer(GuiModelPartKeyframesHandler parent, JgModelPart part, int w, int h) {
		this.parent = parent;
		this.size = new Vector2i(w, h);
		this.pos = new Vector2i();
		this.part = part;
		this.model = parent.getParent().getModel();
	}
	
	public void render(int x, int y, GuiGraphics graphics) {
		this.pos.set(x, y);
		
		double mx = Minecraft.getInstance().mouseHandler.xpos() * Minecraft.getInstance().getWindow()
				.getGuiScaledWidth() / Minecraft.getInstance().getWindow().getWidth();
		double my = Minecraft.getInstance().mouseHandler.ypos() * Minecraft.getInstance().getWindow()
				.getGuiScaledHeight() / Minecraft.getInstance().getWindow().getHeight();
		
		int buttonsWidth = 10;
		int color = Color.rgba(100, 50, 20, 255);
		RenderHelper.rect(graphics, x, y, size.x, size.y, color);
		color = Color.rgba(0, 255, 0, 255);
		RenderHelper.rect(graphics, x + (size.x - buttonsWidth), y, buttonsWidth, size.y / 2, color);
		color = Color.rgba(255, 0, 0, 255);
		RenderHelper.rect(graphics, x + (size.x - buttonsWidth), y + (size.y / 2), buttonsWidth, size.y / 2, color);
		if (model.getAnimator().getAnimation() != null) {
			// Render keyframes
			Animation animation = model.getAnimator().getAnimation();
			if (animation.getPartHandlers().containsKey(part)) {
				ModelPartKeyframesHandler handler = animation.getPartHandlers().get(part);
				// Translations
				color = Color.rgba(0, 255, 0, 255);
				RangeHandler visibleTicksHandler = parent.getVisibleTicksHandler();
				for (int i = 0; i < handler.getTranslations().size(); i++) {
					color = Color.rgba(0, 255, 0, 255);
					Keyframe kf = handler.getTranslations().get(i);
					if (kf.getTick() >= visibleTicksHandler.getOffsetStart()
							&& kf.getTick() <= visibleTicksHandler.getOffsetEnd()) {
						int kfx = NewAnimationGui.KEYFRAMESSTARTX
								+ ((kf.getTick() - visibleTicksHandler.getOffsetStart()) * NewAnimationGui.TILESIZE);
						int kfy = y;
						if (kf.isRepetitive()) {
							color = Color.rgba(0, 0, 255, 255);
						}
						if (kf.isShadow()) {
							color = Color.rgba(255, 128, 128, 255);
						}
						if (NewAnimationGui.isKeyframeSelected(kf)) {
							color *= 1.2f;
						}
						
						RenderHelper.rect(graphics, kfx, kfy, NewAnimationGui.TILESIZE, NewAnimationGui.TILESIZE,
								color);
					}
				}
				// Rotations
				for (int i = 0; i < handler.getRotations().size(); i++) {
					color = Color.rgba(255, 0, 0, 255);
					Keyframe kf = handler.getRotations().get(i);
					if (kf.getTick() >= visibleTicksHandler.getOffsetStart()
							&& kf.getTick() <= visibleTicksHandler.getOffsetEnd()) {
						int kfx = NewAnimationGui.KEYFRAMESSTARTX
								+ ((kf.getTick() - visibleTicksHandler.getOffsetStart()) * NewAnimationGui.TILESIZE);
						int kfy = y + (size.y / 2);
						if (kf.isRepetitive()) {
							color = Color.rgba(0, 255, 255, 255);
						}
						if (kf.isShadow()) {
							color = Color.rgba(128, 255, 128, 255);
						}
						if (NewAnimationGui.isKeyframeSelected(kf)) {
							// color -= Color.rgba(28, 28, 28, 255);
							color *= 1.2f;
						}
						
						RenderHelper.rect(graphics, kfx, kfy, NewAnimationGui.TILESIZE, NewAnimationGui.TILESIZE,
								color);
					}
				}
				
				// Render keyframe data
				// Translations
				for (int i = 0; i < handler.getTranslations().size(); i++) {
					Keyframe kf = handler.getTranslations().get(i);
					if (kf.getTick() >= visibleTicksHandler.getOffsetStart()
							&& kf.getTick() <= visibleTicksHandler.getOffsetEnd()) {
						int kfx = NewAnimationGui.KEYFRAMESSTARTX
								+ ((kf.getTick() - visibleTicksHandler.getOffsetStart()) * NewAnimationGui.TILESIZE);
						int kfy = y;
						
						// Rendering keyframe data
						if(mx > kfx && mx < kfx + NewAnimationGui.TILESIZE && my > kfy && my < 
								kfy + NewAnimationGui.TILESIZE) {
							graphics.renderComponentTooltip(Minecraft.getInstance().font, 
									List.of(
											Component.translatable("Tick: " + kf.getTick()),
											Component.translatable("Tr: " + ("x: " + kf.getVec().x + 
											" y: " + kf.getVec().y + " z: " + kf.getVec().z)),
											Component.translatable("Easing: " + kf.getEasing())), 
									(int) mx, (int) my);
						}
					}
				}
				// Rotations
				for (int i = 0; i < handler.getRotations().size(); i++) {
					Keyframe kf = handler.getRotations().get(i);
					if (kf.getTick() >= visibleTicksHandler.getOffsetStart()
							&& kf.getTick() <= visibleTicksHandler.getOffsetEnd()) {
						int kfx = NewAnimationGui.KEYFRAMESSTARTX
								+ ((kf.getTick() - visibleTicksHandler.getOffsetStart()) * 
										NewAnimationGui.TILESIZE);
						int kfy = y + (size.y / 2);
						
						// Rendering keyframe data
						if(mx > kfx && mx < kfx + NewAnimationGui.TILESIZE && my > kfy && my < 
								kfy + NewAnimationGui.TILESIZE) {
							graphics.renderComponentTooltip(Minecraft.getInstance().font, 
									List.of(
											Component.translatable("Tick: " + kf.getTick()),
											Component.translatable("Rt: " + ("x: " + kf.getVec().x + 
											" y: " + kf.getVec().y + " z: " + kf.getVec().z)),
											Component.translatable("Easing: " + kf.getEasing())), 
									(int) mx, (int) my);
						}
					}
				}
			}
		}
		graphics.drawString(Minecraft.getInstance().font, part.getName(), x, y, Color.rgba(255, 255, 255, 255));
	}
	
	public void touchDown(int mx, int my, ClickType type) {
		if(type == ClickType.NORMAL) {
			// Normal Click
			Animation animation = model.getAnimator().getAnimation();
			if(animation != null) {
				if(animation.getPartHandlers().containsKey(part)) {
					ModelPartKeyframesHandler handler = animation.getPartHandlers().get(part);
					RangeHandler visibleTicksHandler = parent.getVisibleTicksHandler();
					for(int i = 0; i < handler.getTranslations().size(); i++) {
						Keyframe kf = handler.getTranslations().get(i);
						if(kf.getTick() >= visibleTicksHandler.getOffsetStart() && kf.getTick() <=
								visibleTicksHandler.getOffsetEnd()) {
							float kfx = NewAnimationGui.KEYFRAMESSTARTX +
									((kf.getTick() - visibleTicksHandler.getOffsetStart())
											* NewAnimationGui.TILESIZE);
							float kfy = pos.y;
							checkForCollision(i, handler, kf, mx, my, kfx, kfy, NewAnimationGui.TILESIZE, true);
						}
					}
					for(int i = 0; i < handler.getRotations().size(); i++) {
						Keyframe kf = handler.getRotations().get(i);
						if(kf.getTick() >= visibleTicksHandler.getOffsetStart() && kf.getTick() <=
								visibleTicksHandler.getOffsetEnd()) {
							float kfx = NewAnimationGui.KEYFRAMESSTARTX + 
									((kf.getTick() - visibleTicksHandler.getOffsetStart()) 
											* NewAnimationGui.TILESIZE);
							float kfy = pos.y + (size.y / 2);
							checkForCollision(i, handler, kf, mx, my, kfx, kfy, NewAnimationGui.TILESIZE, false);
						}
					}
				} else {
					animation.getPartHandlers().put(part, new ModelPartKeyframesHandler(part));
				}
			}
		} else {
			/*double my = Minecraft.getInstance().mouseHandler.ypos() * (Minecraft.getInstance().getWindow()
					.getGuiScaledHeight() / Minecraft.getInstance().getWindow().getHeight());*/
			if(my > 10) {
				// Long Click
				if(parent.getCurrentTick() != parent.getOldTick()) {
					List<KeyframeItem> keyframes = NewAnimationGui.getSelectedKeyframes();
					if(!keyframes.isEmpty()) {
						int anchorTick = keyframes.get(0).getKeyframe().getTick();
						for(int i = 0; i < keyframes.size(); i++) {
							Keyframe kf = keyframes.get(i).getKeyframe();
							int newTick = parent.getCurrentTick() + (kf.getTick() - anchorTick);
							kf.setTick(newTick);
						}
					}
				}
			}
		}
	}
	
	public void touchItemDown(int mx, int my) {
		if(model.getAnimator().getAnimation() != null) {
			Keyframe addedKf = null;
			int buttonsWidth = 20;
			if(mx > pos.x + (size.x - buttonsWidth) && mx < pos.x + size.x && 
					my > pos.y && my < pos.y + (size.y / 2)) {
				Map<JgModelPart, ModelPartKeyframesHandler> handlers = model.getAnimator().getAnimation()
						.getPartHandlers();
				if(!handlers.containsKey(part)) {
					handlers.put(part, new ModelPartKeyframesHandler(part));
				}
				Utils.tryToRemoveKeyframe(handlers.get(part), parent.getParent().getTickMarker()
						.getVisualTick(), true);
				addedKf = handlers.get(part).addKeyframe(parent.getParent().getTickMarker().getVisualTick(),
						"empty", true);
			}
			if(mx > pos.x + (size.x - buttonsWidth) && mx < pos.x + size.x && 
					my > pos.y + (size.y / 2) && my < pos.y + ((size.y / 2) * 2)) {
				Map<JgModelPart, ModelPartKeyframesHandler> handlers = model.getAnimator().getAnimation()
						.getPartHandlers();
				if(!handlers.containsKey(part)) {
					handlers.put(part, new ModelPartKeyframesHandler(part));
				}
				Utils.tryToRemoveKeyframe(handlers.get(part), parent.getParent().getTickMarker()
						.getVisualTick(), false);
				addedKf = handlers.get(part).addKeyframe(parent.getParent().getTickMarker().getVisualTick(),
						"empty", false);
			}
			
			model.getAnimator().getAnimation().end();
			
			if(parent.getParent().shouldAddOtherParts()) {
				Utils.tryToAddPrevKeyframes(addedKf, model);
			}
			
			model.getAnimator().update();
		}
	}
	
	public void checkForCollision(int index, ModelPartKeyframesHandler handler, Keyframe kf, int mx, int my, 
			float kfx, float kfy, int tileSize, boolean translate) {
		if(mx > kfx && mx < kfx + tileSize && my > kfy && my < kfy + tileSize) {
			if(!NewAnimationGui.isKeyframeSelected(kf)) {
				LogUtils.log("MPGR", "CanAddMultipleKeyframes: " + parent.canAddMultipleKeyframes());
				if(!ClientEventHandler.keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
					NewAnimationGui.getSelectedKeyframes().clear();
				}
				NewAnimationGui.select(handler, kf, index, translate);
				parent.getParent().getXEditBox().setValue(String.valueOf(kf.getVec().x));
				parent.getParent().getYEditBox().setValue(String.valueOf(kf.getVec().y));
				parent.getParent().getZEditBox().setValue(String.valueOf(kf.getVec().z));
				parent.getParent().getEasingEditBox().setValue(kf.getEasing());
				parent.getParent().getTransformTickEditBox().setValue(String.valueOf(kf.getTick()));
			} else {
				if(!ClientEventHandler.keys[GLFW.GLFW_KEY_LEFT_CONTROL]) {
					NewAnimationGui.getSelectedKeyframes().clear();
				}
				NewAnimationGui.unselect(kf);
				parent.getParent().getXEditBox().setValue("");
				parent.getParent().getYEditBox().setValue("");
				parent.getParent().getZEditBox().setValue("");
				parent.getParent().getEasingEditBox().setValue("");
				parent.getParent().getTransformTickEditBox().setValue("");
			}
		}
	}
	
	public Vector2i getSize() {
		return size;
	}

	public JgModelPart getPart() {
		return part;
	}
 	
}
