package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.utils.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlaySoundMessage extends AbstractPacket {

	public static final CustomPacketPayload.Type<PlaySoundMessage> TYPE =
			new CustomPacketPayload.Type<>(PirateGuns.prefix("playsound"));
	public static final StreamCodec<RegistryFriendlyByteBuf,
			PlaySoundMessage> CODEC = StreamCodec.ofMember(PlaySoundMessage::toBytes,
			PlaySoundMessage::new);

	private final String sound;
	private final float x, y, z;
	private final float volume, pitch;

	public PlaySoundMessage(SoundEvent sound, float x, float y, float z) {
		this(sound, x, y, z, 1.0F, 1.0F);
	}

	public PlaySoundMessage(SoundEvent sound, float x, float y, float z, float volume, float pitch) {
		this(BuiltInRegistries.SOUND_EVENT.getKey(sound).toString(), x, y, z,
				volume, pitch);
	}

	public PlaySoundMessage(String sound, float x, float y, float z, float volume, float pitch) {
		this.sound = sound;
		LogUtils.log("PlaySoundMessage", "Playing sound");
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
	}

	@Override
	void toBytes(RegistryFriendlyByteBuf buf) {
		buf.writeUtf(sound).writeFloat(x).writeFloat(y).writeFloat(z)
				.writeFloat(volume).writeFloat(pitch);
		LogUtils.log("PlaySoundMessage", "Playing sound 2");
	}

	public PlaySoundMessage(FriendlyByteBuf buf) {
		this(buf.readUtf(), buf.readFloat(), buf.readFloat(),
				buf.readFloat(), buf.readFloat(), buf.readFloat());
		LogUtils.log("PlaySoundMessage", "Playing sound 4");
	}

	@Override
	public void onServerReceived(MinecraftServer minecraftServer, IPayloadContext ctx, ServerPlayer player) {
		ctx.enqueueWork(() -> {
			SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT
					.get(ResourceLocation.parse(sound));

			if (player != null && sound != null) {
				player.level().playSound(null, x, y, z, soundEvent,
						SoundSource.NEUTRAL, volume, pitch);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
