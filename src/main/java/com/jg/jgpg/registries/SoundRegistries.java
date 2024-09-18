package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistries {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister
            .create(ForgeRegistries.SOUND_EVENTS, PirateGuns.MODID);

    public static final RegistryObject<SoundEvent> CRAFT_SOUND = register("craft_sound");

    public static final RegistryObject<SoundEvent> SWING = register("swing");

    public static final RegistryObject<SoundEvent> FLINTLOCK_PISTOL_SHOOT = register("flintlock_pistol_shoot");

    public static final RegistryObject<SoundEvent> FLINTLOCK_RIFLE_SHOOT = register("flintlock_rifle_shoot");

    public static final RegistryObject<SoundEvent> FLINTLOCK_HAMMER_BACK = register("hammer_back");

    public static final RegistryObject<SoundEvent> MULTIPLE_BULLETS_HITTING_METAL = register("multiple_bullets_hitting_metal");

    public static final RegistryObject<SoundEvent> SMALL_BULLET_HITTING_METAL = register("small_bullet_hitting_metal");

    public static final RegistryObject<SoundEvent> METALIC_DING = register("metalic_ding");

    public static final RegistryObject<SoundEvent> GUNPOWDER_DUST_1 = register("gunpowder_dust_1");

    public static final RegistryObject<SoundEvent> GUNPOWDER_DUST_2 = register("gunpowder_dust_2");

    public static final RegistryObject<SoundEvent> INSERTING_BULLET = register("inserting_bullet");

    public static final RegistryObject<SoundEvent> REVOLVER_SINGLE_SPIN = register("revolver_single_spin");

    public static final RegistryObject<SoundEvent> GUN_HIT = register("gun_hit_sound");

    public static final RegistryObject<SoundEvent> GUN_MOVING = register("gun_moving_sound");

    public static final RegistryObject<SoundEvent> GUN_SWING = register("gun_swing");

    public static final RegistryObject<SoundEvent> GUN_MELEE_HIT = register("meleehit");

    public static final RegistryObject<SoundEvent> METAL_SLIDING = register("metal_sliding");

    public static final RegistryObject<SoundEvent> REMOVE_SOMETHING_METALIC = register("remove_something_metalic");

    public static final RegistryObject<SoundEvent> REMOVE_SOMETHING_METALIC_2 = register("remove_something_metalic_2");

    public static final RegistryObject<SoundEvent> HITTING_GUN = register("hitting_gun");

    public static final RegistryObject<SoundEvent> SHORTROTATE = register("short_rotate_whoosh");

    public static final RegistryObject<SoundEvent> SWING_WHOOSH_3 = register("swing_whoosh_3");

    public static final RegistryObject<SoundEvent> CANNON_SHOOT = register("cannon_fire");

    public static final RegistryObject<SoundEvent> HITMARKER = register("hitmarker");

    public static RegistryObject<SoundEvent> register(String key) {
        return SOUNDS.register(key, () -> SoundEvent.createVariableRangeEvent(
                new ResourceLocation(PirateGuns.MODID, key)));
    }

}
