package com.jg.jgpg.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class JgPlayerData implements IJgPlayerData, INBTSerializable<CompoundTag> {

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
    public void copyFrom(IJgPlayerData data) {
        this.model = data.getModel();
        this.pose = data.getPose();
        this.animationIndex = data.getAnimationIndex();
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("model", model);
        nbt.putString("pose", pose);
        nbt.putInt("animation_index", animationIndex);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        model = nbt.getString("model");
        pose = nbt.getString("pose");
        animationIndex = nbt.getInt("animation_index");
    }
}
