package com.jg.jgpg.registries;

import com.google.common.collect.Sets;
import com.jg.jgpg.PirateGuns;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ItemRegistries {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister
			.create(ForgeRegistries.ITEMS, PirateGuns.MODID);

	private static final ResourceKey<CreativeModeTab> MAIN =
			ResourceKey.create(Registries.CREATIVE_MODE_TAB,
					new ResourceLocation(PirateGuns.MODID, "pirateguns"));

	/*public static final RegistryObject<Item> PIRATEPISTOL = regAdd("pirate_pistol",
			PiratePistol::new);
	
	public static final RegistryObject<Item> PIRATERIFLE = regAdd("pirate_rifle",
			PirateRifle::new);
	
	public static final RegistryObject<Item> TRABUCO = regAdd("trabuco",
			Trabuco::new);*/
	
	public static final RegistryObject<Item> MUSKET_BULLET = regAdd("musket_bullet", 
			() -> new Item(new Item.Properties().stacksTo(64)));
	
	public static final RegistryObject<Item> TRABUCO_BULLET = regAdd("trabuco_bullet", 
			() -> new Item(new Item.Properties().stacksTo(64)));
	
	public static RegistryObject<Item> regAdd(String name, final Supplier<Item> sup) {
		RegistryObject<Item> obj = ITEMS.register(name, sup);
		return obj;
	}

	public static void creativeModeTabRegister(RegisterEvent event) {
		/*event.register(Registries.CREATIVE_MODE_TAB, helper -> {
			helper.register(MAIN, CreativeModeTab.builder().icon(() ->
							new ItemStack(Items.GUNPOWDER))//new ItemStack(ItemRegistries.PIRATERIFLE.get()))
					.title(Component.translatable("pirateguns"))
					.displayItems((params, output) -> {
						ITEMS.getEntries().forEach((reg) -> {
							output.accept(new ItemStack(reg.get()));
						});
					})
					.build());
		});*/
	}

	public static void buildCreativeModeTab(BuildCreativeModeTabContentsEvent event) {
		if(event.getTabKey() == MAIN){
			/*event.accept(PIRATEPISTOL);
			event.accept(PIRATERIFLE);
			event.accept(TRABUCO);*/
			event.accept(MUSKET_BULLET);
			event.accept(TRABUCO_BULLET);
		}
	}

}
