package com.jg.pirateguns.entities;

import java.util.ArrayList;

import com.jg.pirateguns.PirateGuns;
import com.jg.pirateguns.client.handlers.ClientEventHandler;
import com.jg.pirateguns.network.ShootCanonBallMessage;
import com.jg.pirateguns.registries.EntityRegistries;
import com.mojang.logging.LogUtils;

import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Canon extends Mob {

	public float xDir;
	public float yDir;
	public float xViewRot;
	public float yViewRot;
	private boolean switchV;
	
	public Canon(EntityType<? extends Mob> p_21368_, Level p_21369_) {
		super(p_21368_, p_21369_);
	}
	
	public Canon(Level p_21369_) {
		super(EntityRegistries.CANON.get(), p_21369_);
	}

	public static AttributeSupplier bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.ATTACK_SPEED, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0f)
        		.build();
    }
	
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		//if(level.isClientSide) {
			if(hand == InteractionHand.OFF_HAND) {
				ItemStack stack = player.getMainHandItem();
				if(stack.getItem() != Items.STICK) {
					//this.xDir = player.getXRot()-180;
					//lookAt(player, 90.0f, 90.0f);
					
					// Look at
					//if(!level.isClientSide) {
					LogUtils.getLogger().info("Player pos: " + player.position().toString() + 
							" canon pos: " + this.position().toString());
					double d0 = player.getX() - this.getX();
				    double d2 = player.getZ() - this.getZ();

				    float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
				    this.setYRot(Mth.wrapDegrees(f));
				    
					if(ClientEventHandler.SWITCH.isDown()) {
						switchV = !switchV;
						LogUtils.getLogger().info("Sitch Value: " + switchV);
					}
					float v = switchV ? 2f : -2f;
					if(player.isShiftKeyDown()) {
						this.setXRot(Mth.wrapDegrees(this.getXRot()+v));
						this.xDir = Mth.wrapDegrees(this.xDir+v);
						if(this.getXRot() <= -72) {
							this.setXRot(-72);
						}
					}
					LogUtils.getLogger().info("X Rot: " + this.xDir + 
							" Y Rot: " + this.getYRot() + " shiftDown? " 
							+ player.isShiftKeyDown());
					//}
				}else {
					if(level.isClientSide) {
						PirateGuns.channel.sendToServer(
								new ShootCanonBallMessage(getId(), xDir, getYRot(), 
										getX(), getEyeY(), getZ()));
					}
				}
			}
		//}
		return super.mobInteract(player, hand);
	}
	
	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return new ArrayList<>();
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {
		
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

}
