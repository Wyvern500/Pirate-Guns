package com.jg.jgpg.client.handler.handlers.handlerparts;

import com.jg.jgpg.config.Config;

public class HitmarkerHandler {

	public int hitmarkerTime;

	public HitmarkerHandler() {
		hitmarkerTime = 0;
	}

	public int getHitmarkerTime() {
		return hitmarkerTime;
	}

	public void reset() {
		this.hitmarkerTime = Config.CLIENT.hitmarkerTime.get();
	}

	public void resetTo0() {
		hitmarkerTime = 0;
	}

}