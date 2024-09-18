package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.entity.entities.GunBullet;
import com.jg.jgpg.entity.entities.HandCannonBullet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistries {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
			.create(ForgeRegistries.ENTITY_TYPES, PirateGuns.MODID);

	public static final RegistryObject<EntityType<GunBullet>> BULLET = ENTITIES.register("bullet",
			() -> EntityType.Builder.<GunBullet>of(GunBullet::new, MobCategory.MISC)
					.sized(0.375F, 0.375F).build(new ResourceLocation(PirateGuns
							.MODID, "bullet").toString()));

	public static final RegistryObject<EntityType<HandCannonBullet>> HANDCANNONBULLET = ENTITIES
			.register("handcannon_bullet", () -> EntityType.Builder
					.<HandCannonBullet>of(HandCannonBullet::new, MobCategory
							.MISC).sized(0.375F, 0.375F)
					.updateInterval(1).setTrackingRange(40)
					.build(new ResourceLocation(PirateGuns.MODID, "handcannon_bullet")
							.toString()));
	
}
