package com.jg.jgpg.client.handlers;

import com.jg.jgpg.client.handler.AbstractHandler;
import com.jg.jgpg.client.handler.ClientHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;

public class BreathingHandler extends AbstractHandler {

	private boolean down;
	private int MAX;
	private float p;
	private int MAXOFFSET;
	private float op;
	
	public BreathingHandler(ClientHandler client) {
		super(client);
	}

	@Override
	public void tick(LocalPlayer player) {
		if(!down) {
			if(p < MAX) {
				p += Minecraft.getInstance().getDeltaFrameTime();
				if(p > MAX) {
					p = MAX;
				}
			} else {
				down = true;
			}
		} else {
			if(p > 0) {
				p -= Minecraft.getInstance().getDeltaFrameTime();
				if(p < 0) {
					p = 0;
				}
			} else {
				down = false;
			}
		}
	}

	@Override
	public float getProgress(Item item) {
		return p / MAX;
	}

}
