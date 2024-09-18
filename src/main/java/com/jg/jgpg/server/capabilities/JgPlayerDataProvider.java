package com.jg.jgpg.server.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JgPlayerDataProvider implements ICapabilitySerializable<CompoundTag> {

    public static Capability<IJgPlayerData> PLAYER_DATA = CapabilityManager
            .get(new CapabilityToken<IJgPlayerData>() {});

    private JgPlayerData playerData = null;
    private final LazyOptional<IJgPlayerData> optional = LazyOptional
            .of(this::createPlayerData);

    public JgPlayerDataProvider() {

    }

    private IJgPlayerData createPlayerData() {
        if(playerData == null) {
            playerData = new JgPlayerData();
        }

        return playerData;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_DATA) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerData().saveNbt(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerData().loadNbt(nbt);
    }
}
