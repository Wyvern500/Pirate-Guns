package com.jg.jgpg.client.jgmodel.itemmodel;

import net.minecraft.world.entity.player.Player;

public interface IAimableModel extends IAimable {

    boolean canAim(Player player);

}
