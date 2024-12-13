package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.common.entity.JgProjectile;
import com.jg.jgpg.common.item.JgGunItem;
import com.jg.jgpg.utils.NBTUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ShootMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<ShootMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("shoot"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			ShootMessage> CODEC = StreamCodec.ofMember(ShootMessage::toBytes,
			ShootMessage::new);

	private final String sound;
	private final String item;
	double f;
	double f1;
	double f2;
	int cooldown;
	
	public ShootMessage(String sound, String item, double f, double f1, double f2, int cooldown) {
		this.sound = sound;
		this.item = item;
		this.f = f;
		this.f1 = f1;
		this.f2 = f2;
		this.cooldown = cooldown;
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeUtf(sound);
		buf.writeUtf(item);
		buf.writeDouble(f);
		buf.writeDouble(f1);
		buf.writeDouble(f2);
		buf.writeInt(cooldown);
	}

	public ShootMessage(FriendlyByteBuf buf) {
		this(buf.readUtf(32767), buf.readUtf(32767), buf.readDouble(),
				buf.readDouble(), buf.readDouble(), buf.readInt());
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer,
								 IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			if (player != null) {
				ItemStack stack = player.getMainHandItem();
				SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT
						.get(ResourceLocation.parse(sound));
				if (sound != null) {
					player.level().playSound((Player) null, player.getX(),
							player.getY(), player.getZ(), soundEvent,
							SoundSource.NEUTRAL, 1.0f, 1.0f);
				}
				Vec3 view = player.getViewVector(0).scale(1);
				((ServerLevel) player.level()).sendParticles(ParticleTypes.SMOKE, player
								.getX() + view.x, player.getY() + player.getEyeHeight(),
						player.getZ() + view.z,
						player.level().random.nextInt(100), 0, 0, 0,
						0.1f);
				JgGunItem gun = (JgGunItem) player.getMainHandItem().getItem();
				for (int i = 0; i < gun.getBulletsPerShoot(stack); i++) {
					JgProjectile bullet = gun.getBullet().apply(player);
					bullet.setPos(player.position().add(0, player.getEyeHeight(), 0));
					bullet.setDamage(gun.getGunDamage(stack));
					bullet.setRange(gun.getRange(stack));
					bullet.setGravity(gun.getBulletGravity(stack));
					bullet.setRangeDamageReduction(gun.getRangeDamageReduction(stack));

					/*bullet.shoot(f, f1, f2, gun.getPower(stack) * 0.3f,
							gun.getInaccuracy(stack));*/

					Vec3 vec31 = player.getUpVector(1.0F);
					Quaternionf quaternionf = (new Quaternionf())
							.setAngleAxis(0 * ((float)Math.PI / 180F), vec31.x, vec31.y,
									vec31.z);
					Vec3 vec3 = player.getViewVector(1.0F);
					Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
					bullet.shoot(vector3f.x(), vector3f.y(), vector3f.z(),
							gun.getPower(stack) * 0.4f, gun.getInaccuracy(stack));

					Vec3 movement = player.getDeltaMovement();
					bullet.setDeltaMovement(
							bullet.getDeltaMovement().add(movement.x, player.onGround() ? 0.0D :
									movement.y, movement.z));
					player.level().addFreshEntity(bullet);
				}

				NBTUtils.setBullets(player.getMainHandItem(), Math.max(0, NBTUtils
						.getBullets(player.getMainHandItem()) - 1));
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
