package com.jg.jgpg.client.animation.gui.widget;

import java.util.Map.Entry;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.Animator;
import com.jg.jgpg.client.animation.ModelPartKeyframesHandler;
import com.jg.jgpg.client.animation.ModelPartKeyframesHandler.DataHolder;
import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Timer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class TickMarkerWidget extends Widget {

	float tick;
	float oldTick;
	int newTick;
	int tempOldNewTick;
	int oldNewTick;
	boolean mouseDown;
	boolean shouldLerp;
	Timer timer;
	
	public TickMarkerWidget(NewAnimationGui parent, int x, int y, int h) {
		super(parent, x, y, NewAnimationGui.TILESIZE / 2, h);
		timer = new Timer(15, this::onFinishTimer);
	}

	@Override
	public void tick() {
		handleTickMarkerTabUpdates();
		float x = (int) (getX() + ((tick - parent.getVisibleTicksHandler().getOffsetStart()) 
				* NewAnimationGui.TILESIZE));
		double mx = Minecraft.getInstance().mouseHandler.xpos();
		double my = Minecraft.getInstance().mouseHandler.ypos();
		boolean overTickMarker = mx > x && mx < x + NewAnimationGui.TILESIZE && my > getY() && my < getY() +
				height;
		timer.tickWithCondition(mouseDown && overTickMarker);
		if(newTick % NewAnimationGui.VISIBLETICKS == 0 && mouseDown && overTickMarker && newTick != 0) {
			/*LogUtils.log("TickMarkerWidget", "newTick % NewAnimationGui.VISIBLETICKS: " 
					+ (newTick % NewAnimationGui.VISIBLETICKS));*/
			if(timer.hasFinished()) {
				timer.start();
			}
		}
		/*for(Entry<ModelPart, ModelPartKeyframesHandler> entry : parent.getModel().getAnimator()
				.getAnimation().getPartHandlers().entrySet()) {
			DataHolder data = entry.getValue().getData();
			int prevTranslationTick = data.getPrevTranslation() != null ? data.getPrevTranslation().getTick()
					: -1;
			int currentTranslationTick = data.getCurrentTranslation() != null ? data.getCurrentTranslation()
					.getTick() : -1;
			int prevRotationTick = data.getPrevRotation() != null ? data.getPrevRotation().getTick()
					: -1;
			int currentRotationTick = data.getCurrentRotation() != null ? data.getCurrentRotation()
					.getTick() : -1;
			LogUtils.log("TickMarkerWidget", "Part: " + entry.getKey().getName() + " T Prev: " 
					+ prevTranslationTick + " T Current: " + currentTranslationTick + " R Prev: " 
					+ prevRotationTick + " R Current: " + currentRotationTick);
		}*/
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mx, int my, float p_268085_) {
		int start = parent.getVisibleTicksHandler().getOffsetStart();
		int end = parent.getVisibleTicksHandler().getOffsetEnd();
		float tick = parent.getModel().getAnimator().getTick();
		if(tick >= start && tick <= end) {
			int color = Color.rgba(0, 255, 240, 150);
			RenderHelper.rect(graphics, (int) (getX() + ((tick - start) * 
					NewAnimationGui.TILESIZE)), getY(), width, height, color);
		}
		if(newTick >= start && newTick <= end) {
			int color = Color.rgba(255, 0, 240, 150);
			RenderHelper.rect(graphics, (int) (getX() + ((newTick - start) * 
					NewAnimationGui.TILESIZE)), getY(), width, height, color);
		}
	}
	
	public void handleTickMarkerTabUpdates() {
		if(shouldLerp) {
			Animation animation = parent.getModel().getAnimator().getAnimation();
			oldTick = tick;
			tick = parent.getModel().getAnimator().getTick();
			oldNewTick = this.newTick;
			newTick = (int) tick;
			if(animation != null) {
				if(tick != oldTick) {
					updateKeyframesScrollbar(animation.getDuration());
				}
			}
		}
	}

	private void updateKeyframesScrollbar(int duration) {
		float progress = tick / duration;
		parent.getKeyframesScrollbar().setProgress(progress);
	}
	
	private void onFinishTimer() {
		//LogUtils.log("TickMarkerWiget", "Finished newTick - oldTick: " + (newTick - oldNewTick));
		Animator animator = parent.getModel().getAnimator();
		if(animator.getTick() != 0) {
			int newOffset = 0;
			//int maxVisibleTicks;
			if(newTick - oldNewTick > 0) {
				animator.nextTick();
				newOffset = (int) (Math.floor(animator.getTick() / NewAnimationGui.VISIBLETICKS) 
						* NewAnimationGui.VISIBLETICKS);
				updateVisibleTicksAndKeyframesScrollbar(newOffset, animator.getAnimation().getDuration());
			} else {
				animator.prevTick();
				newOffset = (int) (Math.floor(animator.getTick() / NewAnimationGui.VISIBLETICKS) 
						* NewAnimationGui.VISIBLETICKS);
				updateVisibleTicksAndKeyframesScrollbar(newOffset, animator.getAnimation().getDuration());
			}
			updateKeyframesScrollbar(animator.getAnimation().getDuration());
		}
	}
	
	private void updateVisibleTicksAndKeyframesScrollbar(int newOffset, int duration) {
		parent.getVisibleTicksHandler().setOffset(newOffset, duration);
		parent.getKeyframesScrollbar().setProgress(parent.getVisibleTicksHandler().getOffsetEnd()
				/ duration);
	}
	
	@Override
	public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_) {
		mouseDown = true;
		return super.mouseClicked(p_93641_, p_93642_, p_93643_);
	}
	
	@Override
	public boolean mouseReleased(double p_93684_, double p_93685_, int p_93686_) {
		mouseDown = false;
		return super.mouseReleased(p_93684_, p_93685_, p_93686_);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int p_93647_, double p_93648_, double p_93649_) {
		int endX = NewAnimationGui.KEYFRAMESSTARTX + NewAnimationGui.W;
		if(mx < endX && mx > NewAnimationGui.KEYFRAMESSTARTX && my < NewAnimationGui.Y + 
				NewAnimationGui.TILESIZE && my > NewAnimationGui.Y) {
			float progress = (float) (((mx - NewAnimationGui.KEYFRAMESSTARTX) + 0.0f) / (NewAnimationGui.W 
					- 10));
			tempOldNewTick = newTick;
			newTick = (int) MathUtils.lerp(parent
					.getVisibleTicksHandler().getOffsetStart(), parent.getVisibleTicksHandler()
					.getOffsetEnd(), progress);
			if(newTick != tempOldNewTick) {
				oldNewTick = tempOldNewTick;
				if(shouldLerp) {
					parent.getModel().getAnimator().setTick(newTick);
					parent.getModel().getAnimator().update();
				} else {
					oldTick = this.tick;
					tick = newTick;
				}
			}
		}
		return super.mouseDragged(mx, my, p_93647_, p_93648_, p_93649_);
	}
	
	@Override
	public void mouseMoved(double p_94758_, double p_94759_) {
		super.mouseMoved(p_94758_, p_94759_);
	}
	
	public void printCurrentAndPrevKeyframes() {
		for(Entry<JgModelPart, ModelPartKeyframesHandler> entry : parent.getModel().getAnimator()
				.getAnimation().getPartHandlers().entrySet()) {
			DataHolder data = entry.getValue().getData();
			int prevTranslationTick = data.getPrevTranslation() != null ? data.getPrevTranslation().getTick()
					: -1;
			int currentTranslationTick = data.getCurrentTranslation() != null ? data.getCurrentTranslation()
					.getTick() : -1;
			int prevRotationTick = data.getPrevRotation() != null ? data.getPrevRotation().getTick()
					: -1;
			int currentRotationTick = data.getCurrentRotation() != null ? data.getCurrentRotation()
					.getTick() : -1;
			LogUtils.log("TickMarkerWidget", "Part: " + entry.getKey().getName() + " T Prev: " 
					+ prevTranslationTick + " T Current: " + currentTranslationTick + " R Prev: " 
					+ prevRotationTick + " R Current: " + currentRotationTick);
		}
	}

	public boolean shouldLerp() {
		return shouldLerp;
	}

	public void setShouldLerp(boolean shouldLerp) {
		this.shouldLerp = shouldLerp;
	}

	public void setVisualTick(int visualTick) {
		this.oldNewTick = this.newTick;
		this.newTick = visualTick;
	}
	
	public int getVisualTick() {
		return newTick;
	}
	
}
