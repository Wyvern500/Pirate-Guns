package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.common.item.data.GunDataComponent;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DataComponentRegistries {
    public static final DeferredRegister.DataComponents REGISTRAR =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,
                    PirateGuns.MODID);

    public static final DeferredHolder<DataComponentType<?>,
                DataComponentType<GunDataComponent>> GUN_DATA =
            register("gun_data", builder -> builder
                    .persistent(GunDataComponent.CODEC)
                    .networkSynchronized(GunDataComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>,
            DataComponentType<Boolean>> SCOPE =
            register("scope", (p_330231_) -> p_330231_.persistent(Codec.BOOL)
                .networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>,
            DataComponentType<Boolean>> LOADED =
            register("loaded", (p_330231_) -> p_330231_.persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL));

    private static <T> DeferredHolder<DataComponentType<?>,
            DataComponentType<T>> register(String id, UnaryOperator<DataComponentType
            .Builder<T>> builder) {
        return DataComponentRegistries.REGISTRAR.register(id, () -> builder
                .apply(DataComponentType.builder()).build());
    }
}
