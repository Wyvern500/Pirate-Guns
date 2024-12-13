package com.jg.jgpg.client.jgmodel;

import com.google.common.collect.ImmutableList;
import com.jg.jgpg.utils.Constants;
import com.jg.jgpg.utils.NBTUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JgModModelItemOverrides extends ItemOverrides {

	Item gun;
	
	public JgModModelItemOverrides(Item gun) {
		this.gun = gun;
	}
	
	@Override
	public BakedModel resolve(BakedModel origin, ItemStack stack, ClientLevel world, 
			LivingEntity entity, int p_173469_) {
		String id = NBTUtils.getId(stack);
		
		if(!BakedModelHandler.INSTANCE.getDisplayModels().containsKey(id) && !id.equals(Constants.EMPTYID)) {
			BakedModelHandler.INSTANCE.setDisplayModel(id, new JgModModelHandler(origin, gun, id));
		} else if(BakedModelHandler.INSTANCE.getDisplayModels().containsKey(id)){
			return BakedModelHandler.INSTANCE.getModelHandlerForId(id);
		}
		
		if(!BakedModelHandler.INSTANCE.getToShowOnTabModels().containsKey(stack.getItem())) {
			ToShowModModel model = JgModModels.getModel(gun, origin);
			model.setBuildForGui();
			BakedModelHandler.INSTANCE.addToShowOnTab(stack.getItem(), model);
		}
		
		return BakedModelHandler.INSTANCE.getToShowOnTabModel(stack.getItem());
	}
	
	@Override
	public ImmutableList<BakedOverride> getOverrides() {
		return ImmutableList.of();
	}
	
}
