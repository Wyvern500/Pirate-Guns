package com.jg.pirateguns.registries;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.entities.Canon;
import com.jg.pirateguns.entities.GunBullet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistries {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
			.create(ForgeRegistries.ENTITIES, PirateGuns.MODID);
	
	public static final RegistryObject<EntityType<Canon>> CANON = ENTITIES.register("canon", () -> EntityType.Builder
			.<Canon>of(Canon::new, MobCategory.MISC).sized(1F, 1F).build(new ResourceLocation(PirateGuns.MODID, "canon").toString()));
	
	public static final RegistryObject<EntityType<GunBullet>> BULLET = ENTITIES.register("bullet", () -> EntityType.Builder
			.<GunBullet>of(GunBullet::new, MobCategory.MISC).sized(0.375F, 0.375F).build(new ResourceLocation(PirateGuns.MODID, "bullet").toString()));
	
}
