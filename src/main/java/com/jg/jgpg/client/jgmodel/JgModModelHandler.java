package com.jg.jgpg.client.jgmodel;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

public class JgModModelHandler implements BakedModel {

	BakedModel origin;
	Item item;
	String id;
	
	public JgModModelHandler(BakedModel origin, Item item, String id) {
		this.origin = origin;
		this.item = item;
		this.id = id;
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
	public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack,
			boolean applyLeftHandTransform) {
		if(transformType != ItemDisplayContext.FIRST_PERSON_LEFT_HAND && transformType != 
				ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
			if(!BakedModelHandler.INSTANCE.getToShowModModels().containsKey(id)) {
				BakedModelHandler.INSTANCE.setToShowModel(id, JgModModels.getModel(item, origin));
			}
			return BakedModelHandler.INSTANCE.getModelForId(id)
					.applyTransform(transformType, poseStack, applyLeftHandTransform);
		}
		getTransforms().getTransform(transformType).apply(applyLeftHandTransform, poseStack);
		return this;
	}
	
	@Override
	public ItemOverrides getOverrides() {
		return null;
	}
	
}
