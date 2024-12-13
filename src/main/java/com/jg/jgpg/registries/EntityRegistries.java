package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.common.entity.GunBullet;
import com.jg.jgpg.common.entity.HandCannonBullet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EntityRegistries {

	public static final net.neoforged.neoforge.registries.DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
			.create(Registries.ENTITY_TYPE, PirateGuns.MODID);

	public static final Supplier<EntityType<GunBullet>> BULLET = ENTITIES.register("bullet",
			() -> EntityType.Builder.<GunBullet>of(GunBullet::new, MobCategory.MISC)
					.sized(0.375F, 0.375F).build(PirateGuns
							.prefix("bullet").toString()));

	public static final Supplier<EntityType<HandCannonBullet>> HANDCANNONBULLET = ENTITIES
			.register("handcannon_bullet", () -> EntityType.Builder
					.<HandCannonBullet>of(HandCannonBullet::new, MobCategory
							.MISC).sized(0.375F, 0.375F)
					.updateInterval(1).setTrackingRange(40)
					.build(PirateGuns.prefix("handcannon_bullet")
							.toString()));
	
}
