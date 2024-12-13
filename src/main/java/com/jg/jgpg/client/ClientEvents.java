package com.jg.jgpg.client;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = PirateGuns.MODID, bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event) {
        ClientEventHandler.setup();
    }

}
