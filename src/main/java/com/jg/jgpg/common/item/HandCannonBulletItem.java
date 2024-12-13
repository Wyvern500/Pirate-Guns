package com.jg.jgpg.common.item;

import net.minecraft.world.item.Item;

public class HandCannonBulletItem extends Item {

    public HandCannonBulletItem() {
        super(new Properties().stacksTo(64));
    }

    /*@Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ClientEventHandler.getClientHandler().ISTER;
            }
        });
    }*/

}
