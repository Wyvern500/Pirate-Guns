package com.jg.jgpg.common.damagesource;

import com.jg.jgpg.PirateGuns;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class JgDamageTypes {

    public static final ResourceKey<DamageType> MELEE_DAMAGE_TYPE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    new ResourceLocation(PirateGuns.MODID, "melee"));

}
