package com.jg.jgpg.registries;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.item.HandCannonBulletItem;
import com.jg.jgpg.item.items.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemRegistries {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister
			.create(ForgeRegistries.ITEMS, PirateGuns.MODID);

	private static final ResourceKey<CreativeModeTab> MAIN =
			ResourceKey.create(Registries.CREATIVE_MODE_TAB,
					new ResourceLocation(PirateGuns.MODID, "tabs.jgpg.jgpg_tab"));

	public static final RegistryObject<Item> SMALLHAMMER = ITEMS.register("small_hammer",
			() -> new SmallHammer(new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Item> PIRATEPISTOL = regAdd("pirate_pistol",
			PiratePistol::new);

	public static final RegistryObject<Item> PRIMITIVEREVOLVER = regAdd("primitive_revolver",
			PrimitiveRevolver::new);

	public static final RegistryObject<Item> PIRATERIFLE = regAdd("pirate_rifle",
			PirateRifle::new);

	public static final RegistryObject<Item> TRABUCO = regAdd("trabuco",
			Trabuco::new);

	public static final RegistryObject<Item> HEAVYHANDCANNON =
			regAdd("heavy_hand_cannon", HeavyHandCannon::new);

	public static final RegistryObject<Item> HEAVYHANDCANNONBULLET =
			regAdd("handcannon_bullet", HandCannonBulletItem::new);

	public static final RegistryObject<Item> MUSKET_BULLET = regAdd("musket_bullet",
			() -> new Item(new Item.Properties().stacksTo(64)));

	public static final RegistryObject<Item> TRABUCO_BULLET = regAdd("trabuco_bullet",
			() -> new Item(new Item.Properties().stacksTo(64)));

	public static final RegistryObject<Item> GUN_AND_AMMO_CRAFTING_BLOCK =
			regAdd("gun_crafting_station_block", () -> new BlockItem(BlockRegistries
					.GUN_CRAFTING_STATION_BLOCK.get(), new Item.Properties().stacksTo(64)));

	public static RegistryObject<Item> regAdd(String name, final Supplier<Item> sup) {
		RegistryObject<Item> obj = ITEMS.register(name, sup);
		return obj;
	}

	public static void creativeModeTabRegister(RegisterEvent event) {
		event.register(Registries.CREATIVE_MODE_TAB, helper -> {
			helper.register(MAIN, CreativeModeTab.builder().icon(() ->
							new ItemStack(ItemRegistries.PIRATEPISTOL.get()))
					.title(Component.translatable("tabs.jgpg.jgpg_tab"))
					.displayItems((params, output) -> {
						ITEMS.getEntries().forEach((reg) -> {
							ItemStack stack = new ItemStack(reg.get());
							output.accept(stack);
						});
					})
					.build());
		});
	}

	public static void buildCreativeModeTab(BuildCreativeModeTabContentsEvent event) {
		if(event.getTabKey() == MAIN){

		}
	}
	
}
