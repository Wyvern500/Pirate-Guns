package com.jg.jgpg.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerCapability extends INBTSerializable<CompoundTag> {
	
	public void setAimProgress(float aimProgress);
	
	public float getAimProgress();
	
}
