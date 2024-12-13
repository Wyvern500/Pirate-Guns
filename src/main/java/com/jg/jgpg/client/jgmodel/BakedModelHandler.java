package com.jg.jgpg.client.jgmodel;

import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum BakedModelHandler {

	INSTANCE;
	
	Map<String, JgModModelHandler> displayModels;
	Map<String, ToShowModModel> toShowModModels;
	Map<Item, ToShowModModel> toShowOnTabModels;
	
	private BakedModelHandler() {
		displayModels = new HashMap<>();
		toShowModModels = new HashMap<>();
		toShowOnTabModels = new HashMap<>();
	}
	
	public void update(ItemStack stack) {
		String id = NBTUtils.getId(stack);
		if(toShowModModels.containsKey(id)) {
			toShowModModels.get(id).tryToRebuild();
		} else {
			LogUtils.log("BakedModelHandler", "Holy shit, it seems like the itemStack id is not registered");
		}
	}
	
	public void update(String id) {
		if(toShowModModels.containsKey(id)) {
			toShowModModels.get(id).tryToRebuild();
		} else {
			LogUtils.log("BakedModelHandler", "Holy shit, it seems like the itemStack id is not registered");
		}
	}
	
	public void initToShowModel(ItemStack stack) {
		String id = NBTUtils.getId(stack);
		if(!BakedModelHandler.INSTANCE.getToShowModModels().containsKey(id)) {
			BakedModelHandler.INSTANCE.setToShowModel(id, JgModModels.getModel(stack.getItem(), 
					Minecraft.getInstance().getItemRenderer().getItemModelShaper()
					.getItemModel(stack)));
		}
	}
	
	public ToShowModModel getModelForStack(ItemStack stack) {
		String id = NBTUtils.getId(stack);
		return toShowModModels.get(id);
	}
	
	public ToShowModModel getModelForId(String id) {
		if(toShowModModels.containsKey(id)) {
			return toShowModModels.get(id);
		} else {
			return null;
		}
	}
	
	public JgModModelHandler getModelHandlerForId(String id) {
		return displayModels.get(id);
	}
	
	public ToShowModModel getToShowOnTabModel(Item item) {
		return toShowOnTabModels.get(item);
	}
	
	public void setDisplayModel(String id, JgModModelHandler model) {
		displayModels.put(id, model);
	}
	
	public void setToShowModel(String id, ToShowModModel model) {
		toShowModModels.put(id, model);
	}
	
	public void addToShowOnTab(Item item, ToShowModModel model) {
		toShowOnTabModels.put(item, model);
	}
	
	public Map<String, JgModModelHandler> getDisplayModels() {
		return displayModels;
	}
	
	public Map<String, ToShowModModel> getToShowModModels() {
		return toShowModModels;
	}
	
	public Map<Item, ToShowModModel> getToShowOnTabModels() {
		return toShowOnTabModels;
	}
	
}
