package com.jg.jgpg.common.entity;

import com.jg.jgpg.common.network.Networking;
import com.jg.jgpg.common.network.ShowHitmarkerMessage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public abstract class JgProjectile extends Projectile {

	protected float damage;
	protected int range;
	protected float rangeDamageReduction;
	protected float gravity;

	public JgProjectile(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
		super(p_37248_, p_37249_);
	}

	public JgProjectile(EntityType<? extends Projectile> p_37248_, Level p_37249_, LivingEntity owner) {
		super(p_37248_, p_37249_);
		this.setOwner(owner);
	}

	public void tick() {
		super.tick();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
			this.hitTargetOrDeflectSelf(hitresult);
		}

		this.checkInsideBlocks();
		Vec3 vec3 = this.getDeltaMovement();
		double d0 = this.getX() + vec3.x;
		double d1 = this.getY() + vec3.y;
		double d2 = this.getZ() + vec3.z;
		this.updateRotation();
		float f;
		if (this.isInWater()) {
			for(int i = 0; i < 4; ++i) {
				float f1 = 0.25F;
				this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
			}

			f = 0.8F;
		} else {
			f = 0.99F;
		}

		this.setDeltaMovement(vec3.scale(f));
		this.applyGravity();
		this.setPos(d0, d1, d2);

		if(level().isClientSide) {
			level().addParticle(ParticleTypes.ASH, getX(), getY(), getZ(),
					0, 0, 0);
		}

		if(tickCount > 200) {
			this.kill();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult p_37259_) {
		if(!level().isClientSide) {
			super.onHitEntity(p_37259_);
			Entity entity = p_37259_.getEntity();
			if (entity instanceof LivingEntity livingentity) {
				livingentity.hurt(this.damageSources().mobProjectile(this,
						(LivingEntity) getOwner()), damage);
				Networking.sendToPlayerClient(new ShowHitmarkerMessage(),
						(ServerPlayer) getOwner());
				this.kill();
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult p_37258_) {
		super.onHitBlock(p_37258_);
		this.kill();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public float getRangeDamageReduction() {
		return rangeDamageReduction;
	}

	public void setRangeDamageReduction(float rangeDamageReduction) {
		this.rangeDamageReduction = rangeDamageReduction;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

}
