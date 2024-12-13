package com.jg.jgpg.common.damagetypes;

import com.jg.jgpg.PirateGuns;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.LevelAccessor;

import java.util.HashMap;
import java.util.Map;

public class DamageType {

    private static final ResourceLocation MELEE = PirateGuns.prefix("melee");

    private static final Map<ResourceLocation, DamageSource> cache = new HashMap<>();

    public static DamageSource melee(LevelAccessor world) {
        if(!cache.containsKey(MELEE)) {
            DamageSource source = new DamageSource(world.holderOrThrow(ResourceKey
                    .create(Registries.DAMAGE_TYPE, MELEE)));
            cache.put(MELEE, source);
            return source;
        } else {
            return cache.get(MELEE);
        }
    }

}
