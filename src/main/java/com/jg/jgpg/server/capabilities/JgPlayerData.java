package com.jg.jgpg.server.capabilities;

import net.minecraft.nbt.CompoundTag;

public class JgPlayerData implements IJgPlayerData {

    String model = "empty";
    String pose = "empty";
    int animationIndex = -1;

    public JgPlayerData() {

    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setPose(String pose) {
        this.pose = pose;
    }

    @Override
    public String getPose() {
        return pose;
    }

    @Override
    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    @Override
    public int getAnimationIndex() {
        return animationIndex;
    }

    @Override
    public void saveNbt(CompoundTag nbt) {
        nbt.putString("model", model);
        nbt.putString("pose", pose);
        nbt.putInt("animation_index", animationIndex);
    }

    @Override
    public void loadNbt(CompoundTag nbt) {
        model = nbt.getString("model");
        pose = nbt.getString("pose");
        animationIndex = nbt.getInt("animation_index");
    }

    @Override
    public void copyFrom(IJgPlayerData data) {
        this.model = data.getModel();
        this.pose = data.getPose();
        this.animationIndex = data.getAnimationIndex();
    }
}
