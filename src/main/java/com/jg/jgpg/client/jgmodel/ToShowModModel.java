package com.jg.jgpg.client.jgmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jg.jgpg.client.animation.Transform;
import com.jg.jgpg.client.handlers.ClientEventHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ToShowModModel implements BakedModel {

	BakedModel origin;
	List<BakedQuad> quads;
	protected boolean shouldRebuild;
	protected boolean firstTime;
	
	public ToShowModModel(BakedModel origin) {
		this.origin = origin;
		shouldRebuild = true;
		quads = new ArrayList<>();
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction direction, RandomSource random) {
		if(shouldRebuild) {
			ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
			quads.clear();
			rebuild(stack, state, random);
			shouldRebuild = false;
		}
		
		return quads;
	}

	public void setBuildForGui() {
		firstTime = true;
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
	public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack,
			boolean applyLeftHandTransform) {
		getTransforms().getTransform(transformType).apply(applyLeftHandTransform, poseStack);
		return this;
	}
	
	@Override
	public ItemTransforms getTransforms() {
		return origin.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return null;
	}
	
	public void tryToRebuild() {
		shouldRebuild = true;
	}
	
	public abstract void rebuild(ItemStack stack, BlockState blockState, RandomSource random);
	
	protected void addItem(Item item, BlockState state, RandomSource source) {
		BakedModel model = Minecraft.getInstance().getItemRenderer()
				.getItemModelShaper().getItemModel(item);
		for(BakedQuad quad : model.getQuads(state, null, source)) {
			quads.add(copyQuad(quad));
		}
	}
	
	protected void addSpecial(ResourceLocation rl, BlockState state, RandomSource source) {
		BakedModel model = Minecraft.getInstance().getModelManager()
				.getModel(new ModelResourceLocation(rl, "inventory"));
		for(BakedQuad quad : model.getQuads(state, null, source)) {
			quads.add(copyQuad(quad));
		}
	}
	
	protected void addSpecial(String path, BlockState state, RandomSource source) {
		BakedModel model = Minecraft.getInstance().getModelManager()
				.getModel(new ModelResourceLocation(new ResourceLocation(path), "inventory"));
		model = model.applyTransform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, new PoseStack(), 
				false);
		for(BakedQuad quad : model.getQuads(state, null, source)) {
			quads.add(copyQuad(quad));
		}
	}
	
	protected void addAndModifyVertexData(ResourceLocation rl, BlockState state, RandomSource source, 
			float offsetX, float offsetY, float offsetZ) {
		BakedModel model = Minecraft.getInstance().getModelManager()
				.getModel(new ModelResourceLocation(rl, "inventory"));
		for(BakedQuad quad : model.getQuads(state, null, source)) {
			int[] vertices = quad.getVertices().clone();
			
			for (int i = 0; i < vertices.length; i += 8) {
	            float x = Float.intBitsToFloat(vertices[i]);
	            float y = Float.intBitsToFloat(vertices[i + 1]);
	            float z = Float.intBitsToFloat(vertices[i + 2]);

	            x += offsetX;
	            y += offsetY;
	            z += offsetZ;

	            vertices[i] = Float.floatToIntBits(x);
	            vertices[i + 1] = Float.floatToIntBits(y);
	            vertices[i + 2] = Float.floatToIntBits(z);
	        }
			
			BakedQuad newQuad = new BakedQuad(vertices, quad.getTintIndex(), 
					quad.getDirection(), quad.getSprite(), quad.isShade());
			quads.add(newQuad);
		}
	}
	
	
	protected void applyTransformToVertices(ResourceLocation rl, ItemTransform transform, 
			BlockState state, RandomSource source) {
		BakedModel model = Minecraft.getInstance().getModelManager()
				.getModel(new ModelResourceLocation(rl, "inventory"));
		
		if(model == null) return;
		
		//LogUtils.log("ToShowModModel", "Xd"); 

	    float[] posA = null;
	    
	    // Aplicar traslación
	    JgModel itemModel = ClientEventHandler.getClientHandler().getModel();
	    
	    if(itemModel != null) {
	    	TreeMap<String, JgModelPart> parts = itemModel.getParts();
	    	
	    	if(parts.containsKey("aim")) {
	    		JgModelPart part = parts.get("aim");
	    		
	    		Transform t = part.getTransform();
	    		posA = t.pos;
	    	}
	    }
	    
	    if(posA == null) {
	    	posA = new float[] { 0, 0, 0 };
	    }
	    // Aplicar rotación
	    //matrix.rotate(new Quaternionf(transform.rotation.x(), transform.rotation.y(), 
	    //		transform.rotation.z(), 1.0f));

	    // Aplicar escalado
	    //matrix.scale(new Vector3f(transform.scale));

	    // Aplicar la matriz de transformación a los vértices
	    List<BakedQuad> modelQuads = model.getQuads(state, null, source); 
	    
	    for (BakedQuad quad : modelQuads) {
	    	int[] vertexData = quad.getVertices().clone();
	    	for (int i = 0; i < 4; i++) {
	            
	            float x = Float.intBitsToFloat(vertexData[i * 8]);
	            float y = Float.intBitsToFloat(vertexData[i * 8 + 1]);
	            float z = Float.intBitsToFloat(vertexData[i * 8 + 2]);
	            
	            Vector3f toBlockbench = new Vector3f(x * 16.0f, y * 16.0f, z * 16.0f);
	            
	            System.out.println(String.format("X: %.4f Y: %.4f Z: %.4f BlockBenchStuff: "
	            		+ "(%.4f %.4f %.4f)", x * 16.0f, y * 16.0f, z * 16.0f, toBlockbench.x(), 
	            		toBlockbench.y(), toBlockbench.z())); 
	            
	            Matrix4f matrix = new Matrix4f();
	    	    matrix.identity();
	            
	            Vector4f pos = new Vector4f(x, y, z, 1.0F);
	            
	            Vector3f targetPosition = new Vector3f(8.888f, 6.36f, 1.52f).sub(toBlockbench)
	            		.add(toBlockbench).div(16);
	            
	            String target = String.format("TargetPosition: (X: %.4f Y: %.4f Z: %.4f)", 
	            		targetPosition.x, targetPosition.y, targetPosition.z);
	            
	            Vector3f test = new Vector3f(8.888f, 6.36f, 1.52f).sub(toBlockbench).div(16);
	            
	            String output = String.format(" Test: (X: %.4f Y: %.4f Z: %.4f)", test.x, test.y, 
	            		test.z);
	            
	            System.out.println(target + output);
	            
	            matrix.translate(test); // transform.translation
	            
	            pos.mul(matrix);

	            vertexData[i * 8] = Float.floatToRawIntBits(pos.x());
	            vertexData[i * 8 + 1] = Float.floatToRawIntBits(pos.y());
	            vertexData[i * 8 + 2] = Float.floatToRawIntBits(pos.z());
	            
	            /*vertexData[i * 8] = Float.floatToRawIntBits(vertexData[i * 8] - test.x);
	            vertexData[i * 8 + 1] = Float.floatToRawIntBits(vertexData[i * 8 + 1] - test.y);
	            vertexData[i * 8 + 2] = Float.floatToRawIntBits(vertexData[i * 8 + 2] - test.z)*/;
	        }

	        BakedQuad newQuad = new BakedQuad(vertexData, quad.getTintIndex(), 
					quad.getDirection(), quad.getSprite(), quad.isShade());
			quads.add(newQuad);
	    }
	    
	    //LogUtils.log("ToShowModModel", "ModelQuads: " + modelQuads.size());
	}
	
	protected void addModel(BakedModel model, BlockState state, RandomSource source) {
		for(BakedQuad quad : model.getQuads(state, null, source)) {
			quads.add(copyQuad(quad));
		}
	}
	
	protected BakedQuad copyQuad(BakedQuad quad) {
		return new BakedQuad(quad.getVertices().clone(), quad.getTintIndex(), quad.getDirection(), 
				quad.getSprite(), quad.isShade());
	}
	
}
