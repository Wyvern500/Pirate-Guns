package com.jg.jgpg.client.animation.gui.widget;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.animation.RangeHandler;
import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Timer;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public class GuiModelPartKeyframesHandler extends Widget {

	List<ModelPartGuiRenderer> modelPartGuiRenderers;
	RangeHandler handler;
	RangeHandler visibleTicksHandler;
	Timer timer;
	ClickType clickType;
	
	int currentTick;
	int oldTick;
	boolean multipleKeyframes;
	boolean down;
	
	public GuiModelPartKeyframesHandler(NewAnimationGui parent, RangeHandler visibleTicksHandler) {
		super(parent, NewAnimationGui.X, NewAnimationGui.Y, 70, NewAnimationGui.H);
		modelPartGuiRenderers = new ArrayList<>();
		handler = new RangeHandler(3);
		this.visibleTicksHandler = visibleTicksHandler;
		timer = new Timer(15, () -> { LogUtils.log("GMPKH", "Finished"); });
	}

	@Override
	protected void renderWidget(GuiGraphics p_282139_, int p_268034_, int p_268009_, float p_268085_) {
		int color = com.jg.jgpg.utils.Color.rgba(40, 40, 40, 255);
		RenderHelper.rect(p_282139_, getX(), getY(), width, height, color);
		
		for(int i = handler.getStart(); i < handler.getEnd(); i++) {
			ModelPartGuiRenderer renderer = modelPartGuiRenderers.get(i);
			int y = (int) (getY() + ((i - handler.getStart()) * (height / handler.getVisibleRange())));
			//LogUtils.log("GMPKH", "x: " + getX() + " y: " + y);
			renderer.render(getX(), y, p_282139_);
		}
		
		chooseClickType();
	}
	
	@Override
	public void tick() {
		
	}
	
	public void update(float progress) {
		handler.update(modelPartGuiRenderers.size(), progress);
		/*LogUtils.log("GMPKH", "Start: " + handler.getStart() + " End: " + handler.getEnd() + " offset: " + 
				handler.offset + " size " + modelPartGuiRenderers.size() + " parts: " 
				+ ((NewAnimationGui) parent).getModel().getParts().values().size());*/
	}
	
	public void setModel(JgModel model) {
		List<JgModelPart> parts = new ArrayList<>(model.getParts().values());
		for(int i = 0; i < parts.size(); i++) {
			modelPartGuiRenderers.add(new ModelPartGuiRenderer(this, parts.get(i), (int) width, 
					(int) (height / handler.getVisibleRange())));
		}
		handler.update(modelPartGuiRenderers.size(), 0);
	}
	
	public void update() {
		modelPartGuiRenderers.clear();
		List<JgModelPart> parts = new ArrayList<>(parent.getModel().getParts().values());
		for(int i = 0; i < parts.size(); i++) {
			modelPartGuiRenderers.add(new ModelPartGuiRenderer(this, parts.get(i), (int) width, 
					(int) (height / handler.getVisibleRange())));
		}
		handler.update(modelPartGuiRenderers.size(), 0);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		down = true;
		// Starting the timer to choose a click type
		timer.start();
		for (int i = handler.getStart(); i < handler.getEnd(); i++) {
			ModelPartGuiRenderer renderer = modelPartGuiRenderers.get(i);
			int x = getX();
			int y = (int) (getY() + ((i - handler.getStart()) * (height / handler.getVisibleRange())));
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if (my > y && my < y + renderer.getSize().y) {
					if (mx > x && mx < x + renderer.getSize().x) {
						// To check the collision with the model part item
						renderer.touchItemDown((int) mx, (int) my);
						break;
					}
				}
			}
		}
		return super.mouseClicked(mx, my, button);
	}

	public void chooseClickType() {
		Window window = Minecraft.getInstance().getWindow();
		double mx = Minecraft.getInstance().mouseHandler.xpos() * 
				((double)window.getGuiScaledWidth() / (double)window.getScreenWidth()); 
		double my = Minecraft.getInstance().mouseHandler.ypos() *
				((double)window.getGuiScaledHeight() / (double)window.getScreenHeight());
		if(down) {
			timer.tick();
			if(timer.hasFinished()) {
				//LogUtils.log("GMPKH", "long");
				clickType = ClickType.LONG;
				
				for(int i = handler.getStart(); i < handler.getEnd(); i++) {
					ModelPartGuiRenderer renderer = modelPartGuiRenderers.get(i);
					int y = (int) (getY() + ((i - handler.getStart()) * (height / handler.getVisibleRange())));
					if(my > y && my < y + renderer.getSize().y) {
						// This is for check the collisions of the keyframes
						renderer.touchDown((int) mx, (int) my, clickType);
					}
				}
			}
		}
	}
	
	@Override
	public boolean mouseReleased(double mx, double my, int button) {
		down = false;
		if (!timer.hasFinished()) {
			timer.stop();
			clickType = ClickType.NORMAL;

			for (int i = handler.getStart(); i < handler.getEnd(); i++) {
				ModelPartGuiRenderer renderer = modelPartGuiRenderers.get(i);
				int y = (int) (getY() + ((i - handler.getStart()) * (height / handler.getVisibleRange())));
				if (my > y && my < y + renderer.getSize().y) {
					// This is for check the collisions of the keyframes
					renderer.touchDown((int) mx, (int) my, clickType);
				}
			}
		}
		return super.mouseReleased(mx, my, button);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int p_93647_, double p_93648_, double p_93649_) {
		int endX = NewAnimationGui.KEYFRAMESSTARTX + NewAnimationGui.W;
		if (mx < endX && mx > NewAnimationGui.KEYFRAMESSTARTX) {
			float progress = (float) (((mx - NewAnimationGui.KEYFRAMESSTARTX) + 0.0f) / (NewAnimationGui.W - 10));
			oldTick = currentTick;
			currentTick = (int) MathUtils.lerp(parent.getVisibleTicksHandler().getOffsetStart(),
					parent.getVisibleTicksHandler().getOffsetEnd(), progress);
		}
		return super.mouseDragged(mx, my, p_93647_, p_93648_, p_93649_);
	}

	@Override
	public void mouseMoved(double p_94758_, double p_94759_) {
		timer.start();
		super.mouseMoved(p_94758_, p_94759_);
	}

	public List<ModelPartGuiRenderer> getModelPartGuiRenderers() {
		return modelPartGuiRenderers;
	}

	public RangeHandler getModelPartsRangeHandler() {
		return handler;
	}

	public RangeHandler getVisibleTicksHandler() {
		return visibleTicksHandler;
	}
	
	public int getCurrentTick() {
		return currentTick;
	}

	public int getOldTick() {
		return oldTick;
	}

	public boolean canAddMultipleKeyframes() {
		return multipleKeyframes;
	}
	
	public void setCanAddMultipleKeyframes(boolean multipleKeyframes) {
		this.multipleKeyframes = multipleKeyframes;
	}
	
	public ClickType getClickType() {
		return clickType;
	}

	public static enum ClickType {
		NORMAL, LONG;
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
		
	}

}
