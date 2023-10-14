package com.jg.jgpg.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.gui.widget.widgets.JgListWidget;
import com.jg.jgpg.client.gui.widget.widgets.OnlyViewWidget;
import com.jg.jgpg.client.gui.widget.widgets.SpecialButton;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.containers.GunAndAmmoCraftingContainer;
import com.jg.jgpg.network.CraftItemMessage;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.InventoryUtils;
import com.jg.jgpg.utils.InventoryUtils.InvData;
import com.jg.jgpg.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.RegistryObject;

public class GunAndAmmoCraftingGui extends AbstractContainerScreen<GunAndAmmoCraftingContainer> {

	public static final ResourceLocation GUI_RES = new ResourceLocation(PirateGuns.MODID, 
			"textures/gui/container/gun_crafting_table_gui.png");
	
	OnlyViewWidget<ItemStack> ing1;
	OnlyViewWidget<ItemStack> ing2;
	OnlyViewWidget<ItemStack> res;
	
	JgListWidget<JgListItem> items;
	
	List<Supplier<BasicRecipe>> recipes;
	BasicRecipe currentRecipe;
	
	public GunAndAmmoCraftingGui(GunAndAmmoCraftingContainer p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
		
		recipes = new ArrayList<>();
	}

	@Override
	protected void init() {
		super.init();
		
		int i = leftPos;
		int j = topPos;
		
		items = new JgListWidget<>(i + 8, j + 16, 96, 48, 16, GUI_RES);
		items.getSideBar().setXOffset(6);
		
		if(Config.SERVER.musketBulletCraft.get()) {
			addRecipe(ItemRegistries.MUSKET_BULLET, Config.SERVER.musketBulletCraftingResult.get(), 
					0, Config.SERVER.musketBulletMetal.get());
		}
		if(Config.SERVER.musketBulletCraft.get()) {
			addRecipe(ItemRegistries.PIRATEPISTOL, 1, 
					Config.SERVER.ppWood.get(), Config.SERVER.ppMetal.get());
		}
		if(Config.SERVER.prCraft.get()) {
			addRecipe(ItemRegistries.PIRATERIFLE, 1, 
					Config.SERVER.prWood.get(), Config.SERVER.prMetal.get());
		}
		if(Config.SERVER.tCraft.get()) {
			addRecipe(ItemRegistries.TRABUCO, 1, 
					Config.SERVER.tWood.get(), Config.SERVER.tMetal.get());
		}
		if(Config.SERVER.trabucoBulletCraft.get()) {
			addRecipe(ItemRegistries.TRABUCO_BULLET, Config.SERVER.trabucoBulletCraftingResult.get(), 
					0, Config.SERVER.trabucoBulletMetal.get());
		}
		
		for(Supplier<BasicRecipe> br : recipes) {
			items.addItem(new JgListItem(br.get().res));
		}
		
		ing1 = new OnlyViewWidget<ItemStack>(this, i + 126, j + 30, 16, 16) {
			
			@Override
			public void renderSlot(GuiGraphics gg, int x, int y) {
				if(item != null) {
					gg.renderItem(item, getX(), getY());
					gg.renderItemDecorations(font, item, getX(), getY());
				}
			}
			
			@Override
			public void renderHovered(GuiGraphics gg, int x, int y) {
				if(item != null) {
					highlight(gg, this.getX(), this.getY());
					gg.renderComponentTooltip(font, item.getTooltipLines((Player)null, 
							TooltipFlag.Default.NORMAL), x, y);
				}
			}
		};
		
		ing2 = new OnlyViewWidget<ItemStack>(this, i + 154, j + 30, 16, 16) {
			
			@Override
			public void renderSlot(GuiGraphics gg, int x, int y) {
				if(item != null) {
					gg.renderItem(item, getX(), getY());
					gg.renderItemDecorations(font, item, getX(), getY());
				}
			}
			
			@Override
			public void renderHovered(GuiGraphics gg, int x, int y) {
				if(item != null) {
					highlight(gg, getX(), getY());
					gg.renderComponentTooltip(font, item.getTooltipLines((Player)null, 
							TooltipFlag.Default.NORMAL), x, y);
				}
			}
		};
		
		res = new OnlyViewWidget<ItemStack>(this, i + 140, j + 59, 16, 16) {
			
			@Override
			public void renderSlot(GuiGraphics gg, int x, int y) {
				if(item != null) {
					gg.renderItem(item, this.getX(), this.getY());
					gg.renderItemDecorations(font, item, this.getX(), this.getY());
				}
			}
			
			@Override
			public void renderHovered(GuiGraphics gg, int x, int y) {
				if(item != null) {
					highlight(gg, this.getX(), this.getY());
					gg.renderComponentTooltip(font, item.getTooltipLines((Player)null, 
							TooltipFlag.Default.NORMAL), x, y);
				}
			}
		};
		
		SpecialButton button = new SpecialButton(i + 135, j + 11, 26, 18, (b) -> {
			tryToCraft();
		}) {
			@Override
			public void renderButton(GuiGraphics gg, int mx, int my, float partialTicks) {
				gg.blit(GUI_RES, getX(), getY(), 176, 20, width, height);
				
				if(isPressed) {
					shadow(gg, getX(), getY(), width, height);
				} else if(isHovered) {
					highlight(gg, getX(), getY(), width, height);
				}
			}
		};
		
		addRenderableWidget(items);
		addRenderableWidget(ing1);
		addRenderableWidget(ing2);
		addRenderableWidget(res);
		addRenderableWidget(button);

	}
	
	@Override
	public void render(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
		super.render(p_283479_, p_283661_, p_281248_, p_281886_);
	}
	
	@Override
	protected void renderBg(GuiGraphics gg, float partialTicks, int mx, int my) {
		int i = this.leftPos;
		int j = this.topPos;
		
		gg.blit(new ResourceLocation(PirateGuns.MODID, 
				"textures/gui/container/gun_crafting_table_gui.png"), i, j, 0, 0, imageWidth, imageHeight);
	}
	
	private void tryToCraft() {
		if(currentRecipe != null) {
			InvData wood = null;
			InvData metal = null;
			
			if(currentRecipe.wood != 0) {
				wood = InventoryUtils.getTotalCountAndIndexForItem(Minecraft.getInstance().player, 
						ItemTags.PLANKS, currentRecipe.wood);
			}
			metal = InventoryUtils.getTotalCountAndIndexForItem(Minecraft.getInstance().player, 
					Items.IRON_INGOT, currentRecipe.metal);
			if(metal != null) {
				if(wood != null) {
					if(metal.getTotal() >= currentRecipe.metal && wood.getTotal() >= currentRecipe
							.wood) {
						PirateGuns.channel.sendToServer(new CraftItemMessage(Utils.item(currentRecipe
								.res.getItem()), Utils.mixIntArrays(metal.getData(), wood.getData()), 
								InventoryUtils.distributeItem(currentRecipe.res.getItem(), 
										currentRecipe.res.getCount())));
					}
				} else {
					if(metal.getTotal() >= currentRecipe.metal) {
						PirateGuns.channel.sendToServer(new CraftItemMessage(Utils.item(currentRecipe
								.res.getItem()), metal.getData(), InventoryUtils
								.distributeItem(currentRecipe.res.getItem(), currentRecipe.res
										.getCount())));
					}
				}
			}
		}
	}
	
	private void setupForResult(ItemStack stack) {
		res.setItem(stack);
		for(Supplier<BasicRecipe> br : recipes) {
			if(br.get().getRes().getItem() == stack.getItem()) {
				currentRecipe = br.get();
				break;
			}
		}
		if(currentRecipe.wood != 0) {
			ing1.setItem(new ItemStack(Items.OAK_PLANKS, currentRecipe.wood));
		}
		ing2.setItem(new ItemStack(Items.IRON_INGOT, currentRecipe.metal));
	}
	
	private void addRecipe(RegistryObject<Item> item, int resAmount, int wood, int metal) {
		recipes.add(() -> new BasicRecipe(new ItemStack(item.get(), resAmount), wood, metal));
	}

	public class BasicRecipe {
		
		ItemStack res;
		int wood;
		int metal;
		
		public BasicRecipe(ItemStack res, int wood, int metal) {
			this.res = res;
			this.wood = wood;
			this.metal = metal;
		}

		public ItemStack getRes() {
			return res;
		}

		public int getWood() {
			return wood;
		}
		
		public int getMetal() {
			return metal;
		}
		
	}
	
	public class JgListItem extends JgListWidget.JgListKey {

		ItemStack stack;
		
		public JgListItem(ItemStack stack) {
			this.stack = stack;
		}
		
		@Override
		public void onClick(double mx, double my, int index) {
			setupForResult(stack);
		}

		@Override
		public void onUnselect(double mx, double my, int index) {
			res.setItem(null);
			ing1.setItem(null);
			ing2.setItem(null);
		}

		@Override
		public void render(GuiGraphics gg, int x, int y, int mouseX, int mouseY, int index, 
				boolean selected) {
			gg.blit(GUI_RES, x, y, 203, 0, 16, 16);
			gg.renderItem(stack, x, y);
			
			if(selected) {
				shadow(gg, x, y);
			}
		}

		@Override
		public void renderHovered(GuiGraphics gg, int x, int y, int mouseX, int mouseY, int index, 
				boolean selected) {
			if(isHovered(x, y, mouseX, mouseY)) {
				gg.renderComponentTooltip(font, stack.getTooltipLines((Player)null, 
						TooltipFlag.Default.NORMAL), mouseX, mouseY);
			}
		}
		
	}
	
}
