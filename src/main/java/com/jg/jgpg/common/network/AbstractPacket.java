package com.jg.jgpg.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Code from: Ars-Nouveau, of baileyholl Bailey Hollingsworth
public abstract class AbstractPacket implements CustomPacketPayload {

    public AbstractPacket(){}

    void toBytes(RegistryFriendlyByteBuf buf){}

    public void onClientReceived(Minecraft minecraft, IPayloadContext ctx,
                                 Player player){}

    public void onServerReceived(MinecraftServer minecraftServer,
                                 IPayloadContext ctx, ServerPlayer player){}

}