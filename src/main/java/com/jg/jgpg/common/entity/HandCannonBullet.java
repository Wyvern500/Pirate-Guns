package com.jg.jgpg.common.entity;

import com.jg.jgpg.registries.EntityRegistries;
import com.jg.jgpg.registries.ItemRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class HandCannonBullet extends JgProjectile implements ItemSupplier {

	public HandCannonBullet(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}
	
	public HandCannonBullet(Level p_37249_, LivingEntity owner) {
		super(EntityRegistries.HANDCANNONBULLET.get(), p_37249_, owner);
	}
	
	public HandCannonBullet(Level p_37249_) {
		super(EntityRegistries.HANDCANNONBULLET.get(), p_37249_);
	}

	public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
		Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().scale((double)pVelocity);
		this.setDeltaMovement(vec3);
		double d0 = vec3.horizontalDistance();
		this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
		this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}

	@Override
	protected void onHitEntity(EntityHitResult p_37259_) {
		super.onHitEntity(p_37259_);
	}

	@Override
	@NotNull
	public ItemStack getItem() {
		return new ItemStack(ItemRegistries.HEAVYHANDCANNONBULLET.get());
	}

}
