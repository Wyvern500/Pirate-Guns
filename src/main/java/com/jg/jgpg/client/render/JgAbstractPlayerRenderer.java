package com.jg.jgpg.client.render;

import com.jg.jgpg.utils.Utils;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

public abstract class JgAbstractPlayerRenderer<T extends LivingEntity, M extends EntityModel<T>>
		extends LivingEntityRenderer<T, M> {

	protected Context context;

	public JgAbstractPlayerRenderer(M p_174290_, float p_174291_) {
		super(Utils.getEntityRendererContext(), p_174290_, p_174291_);
		this.context = Utils.getEntityRendererContext();
	}

}