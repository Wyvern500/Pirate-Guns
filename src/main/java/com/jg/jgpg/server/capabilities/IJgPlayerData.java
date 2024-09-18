package com.jg.jgpg.server.capabilities;

import net.minecraft.nbt.CompoundTag;

public interface IJgPlayerData {

    void setModel(String model);

    String getModel();

    void setPose(String pose);

    String getPose();

    void setAnimationIndex(int animationIndex);

    int getAnimationIndex();

    void saveNbt(CompoundTag nbt);

    void loadNbt(CompoundTag nbt);

    void copyFrom(IJgPlayerData data);

}
