package com.jg.jgpg.client.animation;

import com.jg.jgpg.client.animation.gui.NewAnimationGui;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Animator {

	JgModel<?> model;
	Animation current;

	float tick;
	float oldTick;
	
	boolean play;
	
	public Animator(JgModel<?> model) {
		this.model = model;
	}
	
	public void tick() {

		if(current == null) return;
		//  || Minecraft.getInstance().screen instanceof PauseScreen
		//LogUtils.log("Animator", "Ticking: " + tick);

		if(tick < current.duration && play) {
			oldTick = tick;
			float multiplier = 0.2f;
			tick += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()
					* multiplier;
			//LogUtils.log("Animator", "Ticking: " + tick);
			if(tick > current.duration) {
				tick = current.duration;
			}
			if(!model.isEmulatedModel()) {
				float flooredTick = (float) Math.floor(tick);
				float flooredOldTick = (float) Math.floor(oldTick);
				if (flooredTick != flooredOldTick) {
					model.onAnimationTick(current, flooredOldTick, flooredTick);
				}
				for (ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
					handler.tick(tick, oldTick);
				}
			}
			
			if(model.shouldQuitAnimation()) {
				model.onAnimationEnd(current);
				setAnimation(null);
				model.resetQuitAnimation();
			}
			
			//LogUtils.log("Animator", "Tick: " + tick);
		} else if(tick >= current.duration && play) {
			
			model.onAnimationEnd(current);
			
			// Normal
			if(NewAnimationGui.getPlayMode() == 0) {
				// We have to reset the data values because if not, it doesnt work
				for(ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
					handler.resetData();
				}
				setAnimation(null);
				//current = null;
			} else if(NewAnimationGui.getPlayMode() == 1) {
				// Loop
				this.tick = 0;
				this.oldTick = 0;
				for(ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
					handler.resetData();
				}
			}

		}
	}
	
	public void update() {
		for(ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
			handler.update(tick, oldTick);
		}
	}
	
	public void save() {
		String all = "";
		if(current != null) {
				all += current instanceof RepetitiveAnimation ? " = new RepetitiveAnimation(\"" 
						+ current.getName() + "\")\n" : " = new Animation(\"" + current.getName() + "\")\n";
				List<KeyframeItem> keyframeItems = new ArrayList<>();
				for (ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
					for (int i = 0; i < handler.getTranslations().size(); i++) {
						Keyframe kf = handler.getTranslations().get(i);
						keyframeItems.add(new KeyframeItem(handler, kf, i, true));
					}
					for (int i = 0; i < handler.getRotations().size(); i++) {
						Keyframe kf = handler.getRotations().get(i);
						keyframeItems.add(new KeyframeItem(handler, kf, i, false));
					}
				}
				Collections.sort(keyframeItems,
						(i1, i2) -> Integer.compare(i1.getKeyframe().getTick(), i2.getKeyframe().getTick()));
				if (!(current instanceof RepetitiveAnimation)) {
					for (KeyframeItem kfi : keyframeItems) {
						if (kfi.isTranslate()) {
							all += ".translate(getPart(\"" + kfi.getHandler().getPart().getName() + "\"), "
									+ kfi.getKeyframe().getTick() + ", \"" + kfi.getKeyframe().getEasing() + "\", "
									+ kfi.getKeyframe().getVec().x + "f, " + kfi.getKeyframe().getVec().y + "f, "
									+ kfi.getKeyframe().getVec().z + "f)\n";
						} else {
							all += ".rotate(getPart(\"" + kfi.getHandler().getPart().getName() + "\"), "
									+ kfi.getKeyframe().getTick() + ", \"" + kfi.getKeyframe().getEasing() + "\", "
									+ kfi.getKeyframe().getVec().x + "f, " + kfi.getKeyframe().getVec().y + "f, "
									+ kfi.getKeyframe().getVec().z + "f)\n";
						}
					}
				} else {
					for (KeyframeItem kfi : keyframeItems) {
						if (kfi.isTranslate()) {
							if (!kfi.getKeyframe().isRepetitive()) {
								all += ".translate(getPart(\"" + kfi.getHandler().getPart().getName() + "\"), "
										+ kfi.getKeyframe().getTick() + ", \"" + kfi.getKeyframe().getEasing() + "\", "
										+ kfi.getKeyframe().getVec().x + "f, " + kfi.getKeyframe().getVec().y + "f, "
										+ kfi.getKeyframe().getVec().z + "f)\n";
							} else {
								all += ".translate(getPart(\"" + kfi.getHandler().getPart().getName() + "\"), "
										+ kfi.getKeyframe().getTick() + ", \"" + kfi.getKeyframe().getEasing() + "\", "
										+ kfi.getKeyframe().getVec().x + "f, " + kfi.getKeyframe().getVec().y + "f, "
										+ kfi.getKeyframe().getVec().z + "f, " + kfi.getKeyframe().isRepetitive()
										+ ")\n";
							}
						} else {
							if (!kfi.getKeyframe().isRepetitive()) {
								all += ".rotate(getPart(\"" + kfi.getHandler().getPart().getName() + "\"), "
										+ kfi.getKeyframe().getTick() + ", \"" + kfi.getKeyframe().getEasing() + "\", "
										+ kfi.getKeyframe().getVec().x + "f, " + kfi.getKeyframe().getVec().y + "f, "
										+ kfi.getKeyframe().getVec().z + "f)\n";
							} else {
								all += ".rotate(getPart(\"" + kfi.getHandler().getPart().getName() + "\"), "
										+ kfi.getKeyframe().getTick() + ", \"" + kfi.getKeyframe().getEasing() + "\", "
										+ kfi.getKeyframe().getVec().x + "f, " + kfi.getKeyframe().getVec().y + "f, "
										+ kfi.getKeyframe().getVec().z + "f, " + kfi.getKeyframe().isRepetitive()
										+ ")\n";
							}
						}
					}
				}
			}
		all += ".end();";
		Minecraft.getInstance().keyboardHandler.setClipboard(all);
		LogUtils.log("Animator", all);
	}
	
	public void setAnimation(Animation anim) {
		if(current == null && anim != null) {
			this.current = anim;
			this.tick = 0;
			this.oldTick = 0;
			model.onAnimationStart(anim);
			LogUtils.log("Animator", "Animation: " + anim.getName());
		}
		if(anim == null) {
			current = null;
			this.tick = 0;
			this.oldTick = 0;
		}
	}
	
	public Animation getAnimation() {
		return current;
	}

	public JgModel<?> getModel() {
		return model;
	}

	public void setTick(int tick) {
		oldTick = this.tick;
		this.tick = tick;
		if(current != null) {
			//LogUtils.log("Animator", "OldTick: " + oldTick + " Tick: " + tick);
			if(tick <= current.duration) {
				update();
				for(ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
					handler.tick(tick, oldTick);
				}
				// LogUtils.log("Animator", "Tick: " + tick);
			}
		}
	}
	
	public void nextTick() {
		if(current != null) {
			if(tick < current.duration) {
				oldTick = tick;
				tick++;
				tick = (float) Math.floor(tick);
				if(tick > current.duration) {
					tick = current.duration;
				}
				update();
				for(ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
					handler.tick(tick, oldTick);
				}
				// LogUtils.log("Animator", "Tick: " + tick);
			} else {
				// Loop mode
				if(NewAnimationGui.getPlayMode() == 1) {
					this.tick = 0;
					this.oldTick = 0;
					for(ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
						handler.resetData();
					}
				}
			}
		}
	}
	
	public void prevTick() {
		if(current != null) {
			if(tick > 0) {
				oldTick = tick;
				tick--;
				tick = (float) Math.floor(tick);
				if(tick < 0) {
					tick = 0;
				}
				update();
				for(ModelPartKeyframesHandler handler : current.getPartHandlers().values()) {
					handler.tick(tick, oldTick);
				}
				// LogUtils.log("Animator", "Tick: " + tick);
			}
		}
	}
	
	public float getTick() {
		return tick;
	}
	
	public float getOldTick() {
		return oldTick;
	}
	
	public void play() {
		this.play = true;
	}
	
	public void stop() {
		this.play = false;
	}
	
	public boolean canPlay() {
		return play;
	}
	
}
