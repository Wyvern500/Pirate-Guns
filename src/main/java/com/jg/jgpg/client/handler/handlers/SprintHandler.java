package com.jg.jgpg.client.handler.handlers;

import com.jg.jgpg.client.handler.AbstractClientHandlerPart;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.common.item.JgItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SprintHandler extends AbstractClientHandlerPart<ClientHandler> {

	private float prevProg;
	private float prog;
	private final float maxSprintProg;
	
	public SprintHandler(ClientHandler client) {
		super(client);
		
		maxSprintProg = 5f;
	}

	@Override
	public void onTickPre(LocalPlayer player, ItemStack stack, String stackId) {
		this.prevProg = prog;

		if(player != null) {
			float multiplier = 0.2f;
			if(player.isSprinting()) {
				if(prog < maxSprintProg) {
					prog += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()
							* multiplier;

					if(prog > maxSprintProg) {
						prog = maxSprintProg;
					}
				}
				if(parent.getModel().getAnimator().getAnimation() != null) {
					parent.getModel().getAnimator().setAnimation(null);
				}
			} else {
				if(prog > 0) {
					prog -= Minecraft.getInstance().getTimer()
							.getGameTimeDeltaTicks() * multiplier;

					if(prog < 0) {
						prog = 0;
					}
				}
			}
		}

		if(prog > 0) {
			parent.getModel().resetQuitAnimation();
		}
	}

	@Override
	public void onTickPost(LocalPlayer player, ItemStack stack, String stackId) {
		
	}

	@Override
	public float getProgress(Item item) {
		float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
        return ((prevProg + (this.prog - prevProg) * partialTick) / maxSprintProg);
	}

	@Override
	public boolean shouldWork(ItemStack stack, LocalPlayer player) {
		return stack.getItem() instanceof JgItem;
	}

}
