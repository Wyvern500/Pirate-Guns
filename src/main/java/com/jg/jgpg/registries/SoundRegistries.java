package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SoundRegistries {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister
            .create(Registries.SOUND_EVENT, PirateGuns.MODID);

    public static final Supplier<SoundEvent> CRAFT_SOUND = register("craft_sound");

    public static final Supplier<SoundEvent> SWING = register("swing");

    public static final Supplier<SoundEvent> FLINTLOCK_PISTOL_SHOOT = register("flintlock_pistol_shoot");

    public static final Supplier<SoundEvent> FLINTLOCK_RIFLE_SHOOT = register("flintlock_rifle_shoot");

    public static final Supplier<SoundEvent> FLINTLOCK_HAMMER_BACK = register("hammer_back");

    public static final Supplier<SoundEvent> MULTIPLE_BULLETS_HITTING_METAL = register("multiple_bullets_hitting_metal");

    public static final Supplier<SoundEvent> SMALL_BULLET_HITTING_METAL = register("small_bullet_hitting_metal");

    public static final Supplier<SoundEvent> METALIC_DING = register("metalic_ding");

    public static final Supplier<SoundEvent> GUNPOWDER_DUST_1 = register("gunpowder_dust_1");

    public static final Supplier<SoundEvent> GUNPOWDER_DUST_2 = register("gunpowder_dust_2");

    public static final Supplier<SoundEvent> INSERTING_BULLET = register("inserting_bullet");

    public static final Supplier<SoundEvent> REVOLVER_SINGLE_SPIN = register("revolver_single_spin");

    public static final Supplier<SoundEvent> GUN_HIT = register("gun_hit_sound");

    public static final Supplier<SoundEvent> GUN_MOVING = register("gun_moving_sound");

    public static final Supplier<SoundEvent> GUN_SWING = register("gun_swing");

    public static final Supplier<SoundEvent> GUN_MELEE_HIT = register("meleehit");

    public static final Supplier<SoundEvent> METAL_SLIDING = register("metal_sliding");

    public static final Supplier<SoundEvent> REMOVE_SOMETHING_METALIC = register("remove_something_metalic");

    public static final Supplier<SoundEvent> REMOVE_SOMETHING_METALIC_2 = register("remove_something_metalic_2");

    public static final Supplier<SoundEvent> HITTING_GUN = register("hitting_gun");

    public static final Supplier<SoundEvent> SHORTROTATE = register("short_rotate_whoosh");

    public static final Supplier<SoundEvent> SWING_WHOOSH_3 = register("swing_whoosh_3");

    public static final Supplier<SoundEvent> CANNON_SHOOT = register("cannon_fire");

    public static final Supplier<SoundEvent> HITMARKER = register("hitmarker");

    public static Supplier<SoundEvent> register(String key) {
        return SOUNDS.register(key, () -> SoundEvent.createVariableRangeEvent(
                PirateGuns.prefix(key)));
    }

}
