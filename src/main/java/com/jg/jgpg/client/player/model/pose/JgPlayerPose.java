package com.jg.jgpg.client.player.model.pose;

import net.minecraft.resources.ResourceLocation;

public class JgPlayerPose {

    private final ResourceLocation loc;

    public JgPlayerPose(ResourceLocation loc) {
        this.loc = loc;
    }

    public ResourceLocation getLoc() {
        return loc;
    }

}
