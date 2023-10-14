package com.jg.jgpg.client.model.loader.model;

import java.util.function.Function;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public class JgSimpleModel implements IUnbakedGeometry<JgSimpleModel> {

	public JgSimpleModel() {
		
	}
	
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
			Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, 
			ItemOverrides overrides, ResourceLocation modelLocation) {
		return null;
	}

}
