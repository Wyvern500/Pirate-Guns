package com.jg.jgpg.containers;

import com.jg.jgpg.registries.BlockRegistries;
import com.jg.jgpg.registries.ContainerRegistries;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GunAndAmmoCraftingContainer extends AbstractContainerMenu {

	public GunAndAmmoCraftingContainer(int id, Inventory inv) {
		super(ContainerRegistries.CRAFTING_CONTAINER.get(), id);
		
		for (int y1 = 0; y1 < 3; ++y1) {
			for (int x1 = 0; x1 < 9; ++x1) {
				this.addSlot(new Slot(inv, x1 + y1 * 9 + 9, 8 + x1 * 18, 84 + y1 * 18));
			}
		}

		for (int x1 = 0; x1 < 9; ++x1) {
			this.addSlot(new Slot(inv, x1, 8 + x1 * 18, 142));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return super.stillValid(ContainerLevelAccess.NULL, player, BlockRegistries
				.GUN_AND_AMMO_CRAFTING_BLOCK.get());
	}

}
