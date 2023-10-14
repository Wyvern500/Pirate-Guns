package com.jg.jgpg.client.model.loader;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.model.loader.model.JgModelBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class JgModelLoaderBuilder extends CustomLoaderBuilder<JgModelBuilder>{

	public static JgModelLoaderBuilder begin(JgModelBuilder parent, 
			ExistingFileHelper existingFileHelper) {
	    return new JgModelLoaderBuilder(new ResourceLocation(PirateGuns.MODID, "jgloader"), parent, 
	    		existingFileHelper);
	}
	
	protected JgModelLoaderBuilder(ResourceLocation loaderId, JgModelBuilder parent,
			ExistingFileHelper existingFileHelper) {
		super(loaderId, parent, existingFileHelper);
	}
	
	@Override
	public CustomLoaderBuilder<JgModelBuilder> visibility(String partName, boolean show) {
		return super.visibility(partName, show);
	}

}
