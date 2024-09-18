package com.jg.jgpg.client.handler;

import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;

public class PlayerData {

    Animation animation;
    JgModel<?> model;
    JgPlayerPose pose;

    public PlayerData() {

    }

    public Animation getAnimation() {
        return animation;
    }

    public PlayerData setAnimation(Animation animation) {
        this.animation = animation;
        return this;
    }

    public JgModel<?> getModel() {
        return model;
    }

    public PlayerData setModel(JgModel<?> model) {
        this.model = model;
        return this;
    }

    public JgPlayerPose getPose() {
        return pose;
    }

    public PlayerData setPose(JgPlayerPose pose) {
        this.pose = pose;
        return this;
    }
    
}
