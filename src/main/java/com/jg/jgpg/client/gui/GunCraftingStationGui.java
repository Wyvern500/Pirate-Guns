package com.jg.jgpg.client.gui;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.gui.widget.ItemSlotWidget;
import com.jg.jgpg.client.animation.gui.widget.JgListWidget;
import com.jg.jgpg.client.animation.gui.widget.OnlyViewWidget;
import com.jg.jgpg.client.animation.gui.widget.SpecialButton;
import com.jg.jgpg.common.container.containers.GunCraftingStationContainer;
import com.jg.jgpg.config.Config;
import com.jg.jgpg.network.CraftItemMessage;
import com.jg.jgpg.registries.ItemRegistries;
import com.jg.jgpg.utils.InventoryUtils;
import com.jg.jgpg.utils.InventoryUtils.InvData;
import com.jg.jgpg.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GunCraftingStationGui extends AbstractContainerScreen<GunCraftingStationContainer> {

	public static final ResourceLocation GUI_RES = new ResourceLocation(PirateGuns.MODID, 
			"textures/gui/container/gun_crafting_station_gui.png");
	
	OnlyViewWidget<ItemStack> ing1;
	OnlyViewWidget<ItemStack> ing2;
	OnlyViewWidget<ItemStack> res;
	
	JgListWidget<JgListItem> items;
	
	List<Supplier<BasicRecipe>> recipes;
	List<Supplier<BasicRecipe>> gunRecipes;
	List<Supplier<BasicRecipe>> meleeRecipes;
	List<Supplier<BasicRecipe>> ammoRecipes;
	BasicRecipe currentRecipe;
	
	List<JgListItem> guns;
	List<JgListItem> melee;
	List<JgListItem> ammo;
	
	public GunCraftingStationGui(GunCraftingStationContainer container, Inventory inv, Component comp) {
		super(container, inv, comp);
		
		recipes = new ArrayList<>();
		gunRecipes = new ArrayList<>();
		meleeRecipes = new ArrayList<>();
		ammoRecipes = new ArrayList<>();
		guns = new ArrayList<>();
		melee = new ArrayList<>();
		ammo = new ArrayList<>();
	}
	
	@Override
	protected void init() {
		super.init();
		
		int i = leftPos;
		int j = topPos;
		
		items = new JgListWidget<>(i + 8, j + 16, 96, 48, 16, GUI_RES);
		items.getSideBar().setXOffset(6);
		
		// Initializing recipes
		
		// Guns
		
		if(!Config.SERVER.piratePistol.cancelCraft.get()) {
			gunRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.PIRATEPISTOL.get()), Config.SERVER
                    .piratePistol.wood.get(), Config.SERVER.piratePistol.metal.get()));
		}
		if(!Config.SERVER.pirateRifle.cancelCraft.get()) {
			gunRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.PIRATERIFLE.get()), Config.SERVER
                    .pirateRifle.wood.get(), Config.SERVER.pirateRifle.metal.get()));
		}
		if(!Config.SERVER.trabuco.cancelCraft.get()) {
			gunRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.TRABUCO.get()), Config.SERVER
                    .trabuco.wood.get(), Config.SERVER.trabuco.metal.get()));
		}
		if(!Config.SERVER.primitiveRevolver.cancelCraft.get()) {
			gunRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.PRIMITIVEREVOLVER.get()), Config.SERVER
                    .primitiveRevolver.wood.get(), Config.SERVER.primitiveRevolver.metal
					.get()));
		}
		if(!Config.SERVER.heavyHandCannon.cancelCraft.get()) {
			gunRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.HEAVYHANDCANNON
                    .get()), Config.SERVER.heavyHandCannon.wood.get(), Config.SERVER
                    .heavyHandCannon.metal.get()));
		}

		if(!Config.SERVER.smallHammer.cancelCraft.get()) {
			meleeRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.SMALLHAMMER
					.get()), Config.SERVER.smallHammer.wood.get(), Config.SERVER
					.smallHammer.metal.get()));
		}

		// Ammo
		
		ammoRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.MUSKET_BULLET.get(), 4), 0,
                Config.SERVER.musketBulletMetal.get(), RecipeType.AMMO));
		ammoRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries.TRABUCO_BULLET.get(), 16),
                0, Config.SERVER.trabucoBulletMetal.get(), RecipeType.AMMO));

		ammoRecipes.add(() -> new BasicRecipe(new ItemStack(ItemRegistries
                .HEAVYHANDCANNONBULLET.get(), Config.SERVER
                .heavyHandCannonBulletCraftingResult.get()), 0, Config.SERVER
                .heavyHandCannonBulletMetal.get(), RecipeType.AMMO));
		
		for(Supplier<BasicRecipe> br : gunRecipes) {
			items.addItem(new JgListItem(br.get()));
		}
		
		ing1 = new OnlyViewWidget<>(this, i + 126, j + 30, 16, 16) {
			
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
		
		ing2 = new OnlyViewWidget<>(this, i + 154, j + 30, 16, 16) {
			
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
		
		res = new OnlyViewWidget<>(this, i + 140, j + 59, 16, 16) {
			
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
		
		JgItemSlot gunTab = new JgItemSlot(this, ItemRegistries.PIRATEPISTOL.get(),
				i - 18, j + 16, 16, 16,
				Component.translatable("crafting_station.com.jgpg.gun_tab"),
				RecipeType.GUN);

		JgItemSlot meleeTab = new JgItemSlot(this, ItemRegistries.SMALLHAMMER.get(),
				i - 18, j + 34, 16, 16, Component
				.translatable("crafting_station.com.jgpg.melee_tab"),
				RecipeType.MELEE);
		
		JgItemSlot ammoTab = new JgItemSlot(this, ItemRegistries.MUSKET_BULLET
				.get(), i - 18, j + 52, 16, 16,
				Component.translatable("crafting_station.com.jgpg.ammo_tab"),
				RecipeType.AMMO);
		
		addRenderableWidget(gunTab);
		addRenderableWidget(meleeTab);
		addRenderableWidget(ammoTab);
		
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
	protected void renderBg(GuiGraphics gg, float p_97788_, int p_97789_, int p_97790_) {
		int i = this.leftPos;
		int j = this.topPos;

		gg.blit(GUI_RES, i, j, 0, 0, imageWidth, imageHeight);
		gg.blit(GUI_RES, i - 22, j + 12, 186, 44, 22, 60);
	}
	
	
	private void tryToCraft() {
		if(currentRecipe != null) {
			InvData wood = null;
			InvData metal;
			
			if(currentRecipe.wood != 0) {
				wood = InventoryUtils.getTotalCountAndIndexForItem(Minecraft.getInstance()
						.player, ItemTags.PLANKS, currentRecipe.wood);
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
	
	
	private void setupForRecipe(BasicRecipe recipe) {
		res.setItem(recipe.getRes());
		currentRecipe = recipe;
		
		if(currentRecipe.getRecipeType() == RecipeType.GUN) {
			if(currentRecipe.wood != 0) {
				ing1.setItem(new ItemStack(Items.OAK_PLANKS, currentRecipe.wood));
			} else {
				ing1.setItem(new ItemStack(Items.AIR));
			}
			ing2.setItem(new ItemStack(Items.IRON_INGOT, currentRecipe.metal));
		} else if(currentRecipe.getRecipeType() == RecipeType.MELEE) {
			if(currentRecipe.wood != 0) {
				ing1.setItem(new ItemStack(Items.OAK_PLANKS, currentRecipe.wood));
			} else {
				ing1.setItem(new ItemStack(Items.AIR));
			}
			ing2.setItem(new ItemStack(Items.IRON_INGOT, currentRecipe.metal));
		} else if(currentRecipe.getRecipeType() == RecipeType.AMMO) {
			ing1.setItem(new ItemStack(Items.AIR));
			ing2.setItem(new ItemStack(Items.IRON_INGOT, currentRecipe.metal));
		}
	}
	
	@Override
	public boolean mouseDragged(double p_97752_, double p_97753_, int p_97754_, double p_97755_, double p_97756_) {
		items.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
		return super.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
	}
	
	@Override
	public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
		return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
	}
	
	@Override
	public void mouseMoved(double p_94758_, double p_94759_) {
		super.mouseMoved(p_94758_, p_94759_);
	}
	

	public static class BasicRecipe {
		
		ItemStack res;
		int wood;
		int metal;
		boolean usesIron;
		RecipeType recipeType;
		
		public BasicRecipe(ItemStack res, int wood, int metal) {
			this(res, wood, metal, RecipeType.GUN);
		}
		
		public BasicRecipe(ItemStack res, int wood, int metal, RecipeType recipeType) {
			this.res = res;
			this.wood = wood;
			this.metal = metal;
			this.usesIron = usesIron;
			this.recipeType = recipeType;
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
		
		public RecipeType getRecipeType() {
			return recipeType;
		}
		
	}
	
	public static enum RecipeType {
		GUN, MELEE, AMMO;
	}
	
	public class JgListItem extends JgListWidget.JgListKey {

		BasicRecipe recipe;
		
		public JgListItem(BasicRecipe recipe) {
			this.recipe = recipe;
		}
		
		@Override
		public void onClick(double mx, double my, int index) {
			setupForRecipe(recipe);
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
			gg.renderItem(recipe.getRes(), x, y);
			
			if(selected) {
				shadow(gg, x, y);
			}
		}

		@Override
		public void renderHovered(GuiGraphics gg, int x, int y, int mouseX, int mouseY, int index, 
				boolean selected) {
			if(isHovered(x, y, mouseX, mouseY)) {
				gg.renderComponentTooltip(font, recipe.getRes().getTooltipLines((Player)null, 
						TooltipFlag.Default.NORMAL), mouseX, mouseY);
				if(!selected) {
					highlight(gg, x, y);
				}
			}
		}
		
	}

	public class JgItemSlot extends ItemSlotWidget {

		RecipeType type;

		public JgItemSlot(Screen screen, Item item, int x, int y, int w, int h,
						  Component hoverText, RecipeType type) {
			super(screen, item, x, y, w, h, hoverText);
			this.type = type;
		}

		@Override
		public void renderSlot(GuiGraphics gg, int x, int y) {
			gg.blit(GUI_RES, this.getX(), this.getY(), 203, 0, 16, 16);
			gg.renderItem(getItemStack(), this.getX(), this.getY());

			if(pressed) {
				shadow(gg, this.getX(), this.getY());
			} else if(isHovered) {
				highlight(gg, this.getX(), this.getY());
			}
		}

		@Override
		public void renderHovered(GuiGraphics gg, int mouseX, int mouseY) {
			if(Utils.collides(this.getX(), this.getY(), this.getWidth(), this.getHeight(), mouseX, mouseY)) {
				gg.renderComponentTooltip(font, List.of(hoverText), mouseX, mouseY);
			}
		}

		@Override
		public void onPressItemSlot(double x, double y) {
			if(type == RecipeType.GUN) {
				items.clearItems();
				for(Supplier<BasicRecipe> br : gunRecipes) {
					items.addItem(new JgListItem(br.get()));
				}
			} else if(type == RecipeType.MELEE) {
				items.clearItems();
				for(Supplier<BasicRecipe> br : meleeRecipes) {
					items.addItem(new JgListItem(br.get()));
				}
			} else if(type == RecipeType.AMMO) {
				items.clearItems();
				for(Supplier<BasicRecipe> br : ammoRecipes) {
					items.addItem(new JgListItem(br.get()));
				}
			}
		}
		
	}
	
}
