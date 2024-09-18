package com.jg.jgpg.entity.entities;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.entity.JgProjectile;
import com.jg.jgpg.network.ShowHitmarkerMessage;
import com.jg.jgpg.registries.EntityRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class GunBullet extends JgProjectile implements ItemSupplier {

    public static final ItemStack ITEM = new ItemStack(Items.AIR);
    public static final float BASE_SPEED = 20f;
    public static final int TICKS = 1;
    public int dist;

    public GunBullet(Level worldIn, LivingEntity owner) {
        super(EntityRegistries.BULLET.get(), worldIn, owner);
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

    public GunBullet(Level worldIn) {
        super(EntityRegistries.BULLET.get(), worldIn);
    }

    public GunBullet(EntityType<? extends GunBullet> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        if(!level().isClientSide) {
            Entity entity = p_37259_.getEntity();
            if (entity instanceof LivingEntity livingentity) {
                livingentity.invulnerableTime = 0;
                livingentity.hurt(this.damageSources().mobProjectile(this,
                        (LivingEntity) this.getOwner()), damage);
                PirateGuns.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) getOwner()),
                        new ShowHitmarkerMessage());
                this.kill();
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

}
