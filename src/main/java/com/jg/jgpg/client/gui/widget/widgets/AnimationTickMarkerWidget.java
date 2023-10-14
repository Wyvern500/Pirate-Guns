package com.jg.jgpg.client.gui.widget.widgets;

import org.lwjgl.glfw.GLFW;

import com.jg.jgpg.client.animations.Animator;
import com.jg.jgpg.client.gui.AnimationGui;
import com.jg.jgpg.client.gui.widget.JgAbstractWidget;
import com.jg.jgpg.client.render.RenderHelper;
import com.jg.jgpg.utils.Color;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.gui.GuiGraphics;

public class AnimationTickMarkerWidget extends JgAbstractWidget {
	
	int tx;
	int tmw;
	int visibleInBoundTick;
	int prevVisibleInBoundTick;
	
	boolean pressed;
	
	long start;
	float maxTime;
	float maxCounter;
	float counter;
	float step;
	
	int tileSize;
	int prevVal;
	float prevTick;
	
	public AnimationTickMarkerWidget(int x, int y, int w, int h, int tmw, int tileSize, 
			AnimationGui gui) {
		super(x, y, w, h, gui);
		this.tx = x;
		this.tmw = tmw;
		this.tileSize = tileSize;
		maxTime = 200f; // 0.2 seconds
		maxCounter = 1f;
		step = 0.1f;
		start = System.currentTimeMillis();
	}

	@Override
	public void tick() {
		Animator animator = parent.getClient().getModel().getAnimator();
		if(animator.getCurrent() != null && //parent.getParent().getModel().getAnimator().getType() == 2 &&
				visibleInBoundTick - prevVisibleInBoundTick != 0) {
			/*if(visibleInBoundTick >= parent.getMaxTicksVisible() - 1) {
				int toWorkIndex = visibleInBoundTick + parent.getOffset();
				
				animator.setTick(toWorkIndex);
				
				if(animator.getCurrent() == null) return;
				
				int animDur = animator.getCurrent().getDuration();
				
				int newOffset = parent.getOffset();
				newOffset = Math.min(animDur, newOffset + 1);
				parent.setOffset(newOffset);
				
				this.tx = x + (toWorkIndex * (w * 2));
			} else if(visibleInBoundTick <= 0){
				int toWorkIndex = visibleInBoundTick + parent.getOffset();
				
				animator.setTick(toWorkIndex);
				
				if(animator.getCurrent() == null) return;
				
				int newOffset = parent.getOffset();
				newOffset = Math.max(0, newOffset - 1);
				parent.setOffset(newOffset);
				
				this.tx = x + (toWorkIndex * (w * 2));
			}*/
			
			/*int toWorkIndex = visibleInBoundTick + parent.getOffset();
			
			LogUtils.log("ATMW", "visibleInBoundTick: " + visibleInBoundTick + " offset: " + parent
					.getOffset());
			
			animator.setTick(toWorkIndex);
			
			this.tx = x + (toWorkIndex * (w * 2));*/
			
			start = System.currentTimeMillis();
		} else if(visibleInBoundTick - prevVisibleInBoundTick == 0 && animator.getCurrent() != null && 
				pressed) {
			
			int newOffset = parent.getKeyframeManager().getOffset();
			int animDur = animator.getCurrent().getDuration();
			int toWorkIndex = visibleInBoundTick + parent.getKeyframeManager().getOffset();
			
			if(visibleInBoundTick >= parent.getKeyframeManager().getMaxTicksVisible() - 1) {
				if((System.currentTimeMillis() - start) > maxTime) {
					LogUtils.log("ATMW", "visibleInBoundTick: " + visibleInBoundTick + " offset: " + parent
							.getKeyframeManager().getOffset());
					newOffset = Math.min(animDur, newOffset + 1);
					parent.getKeyframeManager().setOffset(newOffset);
					
					LogUtils.log("ATMW", "PressedAbove");
					animator.setTick(visibleInBoundTick + newOffset);
					
					start = System.currentTimeMillis();
				}
			} else if(visibleInBoundTick <= 0 && parent.getKeyframeManager().getOffset() > 0){
				if((System.currentTimeMillis() - start) > maxTime) {
					newOffset = Math.max(0, newOffset - 1);
					parent.getKeyframeManager().setOffset(newOffset);
					
					LogUtils.log("ATMW", "PressedFromDown");
					animator.setTick(visibleInBoundTick + newOffset);
					
					start = System.currentTimeMillis();
				}
			}
			
			setTx((int) (getX() + (Math.floor(toWorkIndex) * (width * 2))), "tick");
		}
	}

	@Override
	public void renderWidget(GuiGraphics gg, int mx, int my, float partialTicks) {
		float tick = parent.getClient().getModel().getAnimator().getTick();
		int offset = parent.getKeyframeManager().getOffset();
		if(tick >= offset && tick < offset + parent.getKeyframeManager().getMaxTicksVisible()) {
			RenderHelper.rect(gg, tx - (offset * tileSize), getY(), width, height, 
					Color.rgba(20, 255, 80, 255));
		}
	}
	
	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		if(btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(Utils.collides(getX(), getX(), tmw, height, mx, my)) {
				pressed = true;
			}
		}
		return super.mouseClicked(mx, my, btn);
	}
	
	@Override
	public boolean mouseReleased(double p_93684_, double p_93685_, int p_93686_) {
		pressed = false;
		return super.mouseReleased(p_93684_, p_93685_, p_93686_);
	}
	
	@Override
	public boolean mouseDragged(double mx, double my, int btn, double p_93648_, double p_93649_) {
		if(btn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(parent.getModel().getAnimator().getCurrent() != null) {
				if(Utils.collides(getX(), getY(), tmw, height, mx, my)) {
					int startOffset = (getX() / (width * 2));
					int tick = (int) Math.floor(MathUtils.clamp(mx, getX(), getX() + tmw - width) 
							/ (width * 2));
					
					int rawToWorkIndex = tick - startOffset;
					this.prevVisibleInBoundTick = visibleInBoundTick;
					this.visibleInBoundTick = rawToWorkIndex;
					int toWorkIndex = rawToWorkIndex + parent.getKeyframeManager().getOffset();
					
					setTx((int) (getX() + (Math.floor(toWorkIndex) * (width * 2))), "mouse");
					
					if(visibleInBoundTick - prevVisibleInBoundTick != 0) {
						parent.getKeyframeManager().getParent().getModel().getAnimator()
							.setTick(toWorkIndex);
					}
				}
			}
		}
		return super.mouseDragged(mx, my, btn, p_93648_, p_93649_);
	}
	
	public void update(float tick, int offset) {
		this.tx = (int) (getX() + (tick * tileSize));
		int maxTicks = parent.getKeyframeManager().getMaxTicksVisible();
		int animDur = parent.getModel().getAnimator().getCurrent().getDuration();
		int newOffset = offset;
		int min = Math.min(offset + maxTicks, animDur);
		if (tick >= min) {
			// I did this so tick marker moves in groups of maxTicks and if maxTicks >
			// remainingAnimDur
			// Then adjust it
			newOffset += Math.min(maxTicks, (animDur + 1) - (newOffset + maxTicks));
			parent.getKeyframeManager().setOffset(newOffset);
			setTx((int) (getX() + (Math.floor(tick) * tileSize)), "udpate 1st");
			//LogUtils.log("ATMW", "1st update: tick: " + tick + " offset: " + offset + " tx: " + tx);
		} else if (tick < offset) {
			// Same that above
			newOffset -= Math.min(maxTicks, offset);
			parent.getKeyframeManager().setOffset(newOffset);
			setTx((int) (getX() + (Math.floor(tick) * tileSize)), "udpate 2nd");
			//LogUtils.log("ATMW", "2nd update: tick: " + tick + " offset: " + offset + " tx: " + tx);
		}
	}

	public int getTx() {
		return tx;
	}

	public void setTx(int tx, String from) {
		this.tx = tx;
		//LogUtils.log("ATMW", from + " Tx: " + tx);
	}

	public int getTileSize() {
		return tileSize;
	}
	
	public int getTick() {
		return visibleInBoundTick + parent.getKeyframeManager().getOffset();
	}

}
