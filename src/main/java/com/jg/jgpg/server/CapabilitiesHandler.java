package com.jg.jgpg.server;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.registries.AttachmentRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;

public class CapabilitiesHandler {

    public static final EntityCapability<JgPlayerData, Void> PLAYER_DATA_CAPABILITY =
            EntityCapability.createVoid(PirateGuns.prefix("player_data"), JgPlayerData
                    .class);

    public static Lazy<JgPlayerData> getPlayerData(ServerPlayer player) {
        return Lazy.of(() -> player.getData(AttachmentRegistries
                .PLAYER_DATA_ATTACHMENT.get()));
    }

    public static void attachCapabilities(RegisterCapabilitiesEvent e) {
        e.registerEntity(CapabilitiesHandler.PLAYER_DATA_CAPABILITY,
                EntityType.PLAYER, (player, ctx) -> new JgPlayerData());
    }

}
