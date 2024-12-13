package com.jg.jgpg.common.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GunDataComponent(String id, int bullets) {
    public static final Codec<GunDataComponent> CODEC = RecordCodecBuilder
            .create(builder -> builder.group(
            Codec.STRING.fieldOf("id").forGetter(GunDataComponent::id),
            Codec.INT.fieldOf("bullets").forGetter(GunDataComponent::bullets)
    ).apply(builder, GunDataComponent::new));
    public static final StreamCodec<FriendlyByteBuf, GunDataComponent> STREAM_CODEC =
            StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, GunDataComponent::id,
            ByteBufCodecs.INT, GunDataComponent::bullets,
            GunDataComponent::new);

    public GunDataComponent withId(String id) {
        return new GunDataComponent(id, bullets());
    }

    public GunDataComponent withBullets(int bullets) {
        return new GunDataComponent(id(), bullets);
    }

}

/*public class GunDataComponent {
    public static final Codec<GunDataComponent> CODEC;

    public static final StreamCodec<FriendlyByteBuf, GunDataComponent> STREAM_CODEC;

    String id;
    int bullets;

    public GunDataComponent(String id, int bullets) {
        this.id = id;
        this.bullets = bullets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBullets() {
        return bullets;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    static {
        CODEC = RecordCodecBuilder
                .create(builder -> builder.group(
                    Codec.STRING.fieldOf("id")
                        .forGetter(GunDataComponent::getId),
                    Codec.INT.fieldOf("bullets")
                        .forGetter(GunDataComponent::getBullets)
                ).apply(builder, GunDataComponent::new));
        STREAM_CODEC = StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, GunDataComponent::getId,
                        ByteBufCodecs.INT, GunDataComponent::getBullets,
                        GunDataComponent::new);
    }

}*/
