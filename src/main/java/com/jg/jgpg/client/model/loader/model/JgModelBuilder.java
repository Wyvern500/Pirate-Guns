package com.jg.jgpg.client.model.loader.model;

import java.util.function.BiFunction;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class JgModelBuilder extends ModelBuilder<JgModelBuilder> {

	public JgModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
		super(outputLocation, existingFileHelper);
	}

	@Override
	public ResourceLocation getLocation() {
		return super.getLocation();
	}
	
	@Override
	public ResourceLocation getUncheckedLocation() {
		return super.getUncheckedLocation();
	}
	
	@Override
	public <L extends CustomLoaderBuilder<JgModelBuilder>> L customLoader(
			BiFunction<JgModelBuilder, ExistingFileHelper, L> customLoaderFactory) {
		return super.customLoader(customLoaderFactory);
	}
	
}
