package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.entities.GunBullet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistries {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
			.create(ForgeRegistries.ENTITY_TYPES, PirateGuns.MODID);

	public static final RegistryObject<EntityType<GunBullet>> BULLET = ENTITIES.register("bullet", () -> EntityType.Builder
			.<GunBullet>of(GunBullet::new, MobCategory.MISC).sized(0.375F, 0.375F).build(new ResourceLocation(PirateGuns.MODID, "bullet").toString()));
	
}
