package com.jg.jgpg.client.handler.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;

public class BreathingHandler {

	private boolean down;
	private int MAX;
	private float p;
	private boolean startOffset;
	private int MAXOFFSET;
	private float op;
	
	public BreathingHandler() {
		
	}

	public void tick() {
		MAX = 40;
		MAXOFFSET = 20;

		float multiplier = 0.2f;

		if(!startOffset) {
			if(!down) {
				if(p < MAX) {
					p += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()
							* multiplier;
					if(p > MAX) {
						p = MAX;
					}
				} else {
					down = true;
				}
			} else {
				if(p > 0) {
					p -= Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()
							* multiplier;
					if(p < 0) {
						p = 0;
					}
				} else {
					down = false;
					startOffset = true;
				}
			}
		}
		
		if(startOffset) {
			if(op < MAXOFFSET) {
				op += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()
						* multiplier;
				if(op > MAXOFFSET) {
					op = MAXOFFSET;
				}
			} else {
				op = 0;
				startOffset = false;
			}
		}
	}

	public float getProgress(Item item) {
		return p / MAX;
	}
	
}
