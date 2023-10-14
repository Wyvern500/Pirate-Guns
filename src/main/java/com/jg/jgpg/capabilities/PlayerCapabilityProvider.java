package com.jg.jgpg.capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.jg.jgpg.PirateGuns;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	private final IPlayerCapability backend = new PlayerCapabilityImplementation();
	private final LazyOptional<IPlayerCapability> optional = LazyOptional.of(() -> backend);
	public static final ResourceLocation ID = new ResourceLocation(PirateGuns.MODID, "player_data");
	
	public PlayerCapabilityProvider() {
		
	}

	public void invalidate() {
		optional.invalidate();
	}
	
	@Override
	public CompoundTag serializeNBT() {
		return backend.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		backend.deserializeNBT(nbt);
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return PlayerCapability.INSTANCE.orEmpty(cap, optional);
	}

}
