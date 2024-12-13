package com.jg.jgpg.client.jgmodel;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BaseJgModModel implements BakedModel {

	BakedModel origin;
	Item item;
	
	public BaseJgModModel(BakedModel origin, Item item) {
		this.origin = origin;
		this.item = item;
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState p_235039_, Direction p_235040_, RandomSource p_235041_) {
		return origin.getQuads(p_235039_, p_235040_, p_235041_);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return origin.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return origin.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return origin.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return origin.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return origin.getParticleIcon();
	}
	
	@Override
	public ItemTransforms getTransforms() {
		return origin.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return new JgModModelItemOverrides(item);
	}
	
}
