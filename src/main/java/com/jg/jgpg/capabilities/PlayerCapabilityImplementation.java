package com.jg.jgpg.capabilities;

import net.minecraft.nbt.CompoundTag;

public class PlayerCapabilityImplementation implements IPlayerCapability {

	private static final String AIMPROGRESS = "aim_progress";
	private float aimProgress;
	
	public PlayerCapabilityImplementation() {
		
	}
	
	@Override
	public void setAimProgress(float aimProgress) {
		this.aimProgress = aimProgress;
	}

	@Override
	public float getAimProgress() {
		return aimProgress;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putFloat(AIMPROGRESS, aimProgress);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.aimProgress = nbt.getFloat(AIMPROGRESS);
	}

}
