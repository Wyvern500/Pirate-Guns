package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.server.JgPlayerData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentRegistries {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES
            = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
            PirateGuns.MODID);

    public static final Supplier<AttachmentType<JgPlayerData>> PLAYER_DATA_ATTACHMENT =
            ATTACHMENT_TYPES.register("player_data_cap",
                    () -> AttachmentType.serializable(JgPlayerData::new).build());

}
