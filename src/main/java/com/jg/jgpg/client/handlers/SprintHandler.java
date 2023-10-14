package com.jg.jgpg.client.handlers;

import com.jg.jgpg.client.handler.AbstractHandler;
import com.jg.jgpg.client.handler.ClientHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;

public class SprintHandler extends AbstractHandler {

	private float prevProg;
	private float prog;
	private float maxSprintProg;
	
	public SprintHandler(ClientHandler client) {
		super(client);
		
		maxSprintProg = 5f;
	}

	@Override
	public void tick(LocalPlayer player) {
		this.prevProg = prog;
		
		if(player != null) {
			if(player.isSprinting()) {
				if(prog < maxSprintProg) {
					prog += Minecraft.getInstance().getDeltaFrameTime() * 2f;
					
					if(prog > maxSprintProg) {
						prog = maxSprintProg;
					}
				}
			} else {
				if(prog > 0) {
					prog -= Minecraft.getInstance().getDeltaFrameTime() * 2f;
					
					if(prog < 0) {
						prog = 0;
					}
				}
			}
		}
		
	}

	@Override
	public float getProgress(Item item) {
		float partialTick = Minecraft.getInstance().getPartialTick();
		float prog = ((prevProg + (this.prog - prevProg) * partialTick) / maxSprintProg);
		return prog;
	}
	
}
