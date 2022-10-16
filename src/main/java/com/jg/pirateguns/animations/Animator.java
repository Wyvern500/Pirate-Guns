package com.jg.pirateguns.animations;

public class Animator {

	private Animation animation;
	
	public Animator() {
		animation = Animation.EMPTY;
	}
	
	public void tick() {
		if(animation != Animation.EMPTY) {
			animation.tick();
		}
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
}
