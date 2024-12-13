package com.jg.jgpg.utils;

public class Timer {

	long start;
	float max;
	boolean canTikTak;
	Callback callback;
	
	public Timer(float max, Callback callback) {
		this.max = max;
		this.callback = callback;
	}
	
	public void tick() {
		if(canTikTak) {
			long time = (System.currentTimeMillis() - start) / 100;
			if(time > max) {
				canTikTak = false;
				callback.call();
			}
			//LogUtils.log("Timer", "Time: " + time);
		}
	}
	
	public void tickWithCondition(boolean shouldContinue) {
		if(canTikTak) {
			long time = (System.currentTimeMillis() - start) / 100;
			if(!shouldContinue) {
				start = System.currentTimeMillis();
				canTikTak = false;
			}
			if(time > max) {
				canTikTak = false;
				callback.call();
			}
			//LogUtils.log("Timer", "Time: " + time);
		}
	}
	
	public void start() {
		this.start = System.currentTimeMillis();
		canTikTak = true;
	}
	
	public void stop() {
		canTikTak = false;
	}
	
	public boolean hasFinished() {
		return !canTikTak;
	}
	
	public interface Callback {
		public void call();
	}
	
}
