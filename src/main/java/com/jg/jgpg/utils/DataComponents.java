package com.jg.jgpg.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.Nullable;

public class DataComponents {

    public class GunProperties implements MutableDataComponentHolder {

        @Override
        public <T> @Nullable T set(DataComponentType<? super T> dataComponentType,
                                   @Nullable T t) {
            return null;
        }

        @Override
        public <T> @Nullable T remove(DataComponentType<? extends T> dataComponentType) {
            return null;
        }

        @Override
        public void applyComponents(DataComponentPatch dataComponentPatch) {

        }

        @Override
        public void applyComponents(DataComponentMap dataComponentMap) {

        }

        @Override
        public DataComponentMap getComponents() {
            return null;
        }
    }

    public record GunData(String id, String bulletPath, int bullets) { }

    // Basic codec
    public static final Codec<GunData> BASIC_CODEC = RecordCodecBuilder
            .create(instance -> instance.group(
                    Codec.STRING.fieldOf("id")
                            .forGetter(GunData::id),
                    Codec.STRING.fieldOf("bullet_path")
                            .forGetter(GunData::bulletPath),
                    Codec.INT.fieldOf("bullets")
                            .forGetter(GunData::bullets)
            ).apply(instance, GunData::new)
    );
    public static final StreamCodec<ByteBuf, GunData> BASIC_STREAM_CODEC =
            StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, GunData::id,
            ByteBufCodecs.STRING_UTF8, GunData::bulletPath,
            ByteBufCodecs.INT, GunData::bullets,
            GunData::new
    );

    public static final StreamCodec<ByteBuf, GunData> UNIT_STREAM_CODEC =
            StreamCodec.unit(new GunData("", "", 0));

}
